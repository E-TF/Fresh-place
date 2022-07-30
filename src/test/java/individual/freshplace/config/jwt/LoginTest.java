package individual.freshplace.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import individual.freshplace.dto.login.LoginRequest;
import individual.freshplace.entity.DiscountByGrade;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.DiscountByGradeRepository;
import individual.freshplace.repository.MemberRepository;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.constant.GradeCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    private static final String TEST_ID = "testId1";
    private static final String TEST_PW = "testPw";
    private static final String TEST_NAME = "testName";
    private static final String TEST_PHONE_NUMBER = "01012345678";
    private static final String TEST_EMAIL = "test@test.com";
    private static final LocalDate TEST_BIRTH = LocalDate.of(2000, 1, 29);

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

    @BeforeEach
    void setup() {

        grade = discountByGradeRepository.findById(GradeCode.GENERAL.getGradeCode()).orElseThrow();

        Member member = Member.builder().
                memberId(TEST_ID)
                .password(passwordEncoder.encode(TEST_PW))
                .memberName(TEST_NAME)
                .phoneNumber(TEST_PHONE_NUMBER)
                .email(TEST_EMAIL)
                .memberBirth(TEST_BIRTH)
                .gradeCode(grade)
                .build();

        memberRepository.save(member);
    }

    @Test
    @DisplayName("유효하지 않은 아이디 인증")
    void 로그인_실패_아이디() throws Exception {

        String loginObject = objectMapper.writeValueAsString(new LoginRequest("testIdFail", TEST_PW));

        ResultActions actions = mockMvc.perform(post("/login")
                .content(loginObject)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        actions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is(ErrorCode.FAILED_LOGIN.name())))
                .andExpect(jsonPath("$.message", is(ErrorCode.FAILED_LOGIN.getMessage())))
                .andDo(print());
    }

    @Test
    @DisplayName("유효하지 않은 비밀번호 인증")
    void 로그인_실패_비밀번호() throws Exception {

        String loginObject = objectMapper.writeValueAsString(new LoginRequest(TEST_ID, "testPwFail"));

        ResultActions actions = mockMvc.perform(post("/login")
                .content(loginObject)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        actions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is(ErrorCode.FAILED_LOGIN.name())))
                .andExpect(jsonPath("$.message", is(ErrorCode.FAILED_LOGIN.getMessage())))
                .andDo(print());
    }

    @Test
    @DisplayName("아이디, 비밀번호 인증 성공")
    void 로그인_성공() throws Exception {

        String loginObject = objectMapper.writeValueAsString(new LoginRequest(TEST_ID, TEST_PW));

        mockMvc.perform(post("/login").content(loginObject))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andDo(print());
    }
}
