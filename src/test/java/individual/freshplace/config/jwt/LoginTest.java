package individual.freshplace.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import individual.freshplace.dto.LoginRequest;
import individual.freshplace.entity.DiscountByGrade;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.DiscountByGradeRepository;
import individual.freshplace.repository.MemberRepository;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.constant.GradeCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DiscountByGradeRepository discountByGradeRepository;

    @Autowired
    private MockMvc mockMvc;

    private static DiscountByGrade grade;

    @Before
    public void setup() {

        grade = discountByGradeRepository.findById(GradeCode.CODE_GRADE_GENERAL).orElseThrow();

        Member member = Member.builder().
                memberId("testId")
                .password(passwordEncoder.encode("testPw"))
                .memberName("testName")
                .phoneNumber("01012345678")
                .email("test@test.com")
                .memberBirth(LocalDate.of(2000, 1, 29))
                .gradeCode(grade)
                .build();

        memberRepository.save(member);

    }

    @Test
    public void 로그인_실패() throws Exception {

        String loginObject = objectMapper.writeValueAsString(new LoginRequest("testIdFail", "testPw"));

        ResultActions actions = mockMvc.perform(post("/login")
                .content(loginObject)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        actions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is(ErrorCode.FAILED_LOGIN.name())))
                .andExpect(jsonPath("$.message", is(ErrorCode.FAILED_LOGIN.getMessage())))
                .andDo(print());
    }

    @Test
    public void 로그인_성공() throws Exception {

        String loginObject = objectMapper.writeValueAsString(new LoginRequest("testId", "testPw"));

        mockMvc.perform(post("/login").content(loginObject))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andDo(print());
    }
}