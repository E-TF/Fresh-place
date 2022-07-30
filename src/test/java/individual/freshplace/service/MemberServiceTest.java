package individual.freshplace.service;

import individual.freshplace.dto.signup.SignupRequest;
import individual.freshplace.util.exception.DuplicationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MemberServiceTest {

    private static final String TEST_ID = "testId";
    private static final String TEST_PW = "testPw";
    private static final String TEST_NAME = "testName";
    private static final String TEST_PHONE_NUMBER = "01012345678";
    private static final String TEST_EMAIL = "test@test.com";
    private static final LocalDate TEST_BIRTH = LocalDate.of(2000, 1, 29);
    private static final SignupRequest SIGNUP_REQUEST = new SignupRequest(TEST_ID, TEST_PW, TEST_NAME, TEST_PHONE_NUMBER, TEST_EMAIL, TEST_BIRTH);

    @Autowired
    private FSignupService fSignupService;

    @Test
    @DisplayName("동일한 아이디로 회원가입 동시 요청 5번 일때 5번 성공, 0번 실패")
    void joinWithOutConcurrencyControl() throws InterruptedException {

        final int threadCount = 5;
        AtomicInteger throwCount = new AtomicInteger(0);

        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {

            service.execute(() -> {

                try {
                    fSignupService.signupInner(SIGNUP_REQUEST);
                } catch (DuplicationException e) {
                    throwCount.getAndIncrement();
                }

                countDownLatch.countDown();

            });
        }

        countDownLatch.await();
        Assertions.assertEquals(throwCount.get(), 0);
    }

    @Test
    @DisplayName("동일한 아이디로 회원가입 동시 요청 5번 일때 1번 성공, 4번 실패")
    void joinWithConcurrencyControl() throws InterruptedException {

        final int threadCount = 5;
        AtomicInteger throwCount = new AtomicInteger(0);

        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {

            service.execute(() -> {

                try {
                    fSignupService.signup(SIGNUP_REQUEST);
                } catch (DuplicationException e) {
                    throwCount.getAndIncrement();
                }

                countDownLatch.countDown();

            });
        }

        countDownLatch.await();
        Assertions.assertEquals(throwCount.get(), threadCount-1);
    }
}
