package individual.freshplace.service;

import individual.freshplace.dto.signup.SignupRequest;
import individual.freshplace.entity.Member;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.constant.LockPrefix;
import individual.freshplace.util.exception.DuplicationException;
import individual.freshplace.util.lock.UserLevelLock;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FSignupService {

    private final UserLevelLock userLevelLock;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public void signupFacade(final SignupRequest signupRequest) {

        userLevelLock.lockProcess(LockPrefix.SIGNUP.getMethodName() + signupRequest.getMemberId(), () -> {
            signupInner(signupRequest);
        });
    }

    //전재 조건 : 회원가입 시 아이디는 중복 될 수 없다.
    //DB 격리 수준 : Repeatable Read -> 현재 트랜잭션 번호보다 작은 트랜잭션이 변경한 데이터를 레코드에서 읽지 않고 변경 전 내용을 언두로그에서 가져옴.
    //락을 건 이유 : 같은 아이디의 동시 요청에 대해 1트랜잭션이 member 를 save 하고 commit 전 2트랜잭션이 existsByMemberId() 사용 시 데이터가 삽입이 되어도
    //결과가 true 가 아니라 false 가 됨. 이유는 mvcc 때문에 언두로그에서 값을 가져옴. 그래서 데이터의 일관성을 해치게 되며 중복된 아이디가 삽입되는 이슈가 발생함.
    //read committed 같은 경우 다른 트랜잭션 commit 여부에 따라 결과가 달라지기 때문에 격리 수준 변화에도 의미가 없음.
    //해결 방법 : lock 을 잡고 트랜잭션을 순차적으로 실행.
    //select for update 같은 경우 쓰기락을 걸어야하는데 언두에 락을 걸 수 없으므로 이때는 현재 레코드 값을 가져옴. (phantom read 문제 발생)

    @Transactional
    protected void signupInner(final SignupRequest signupRequest) {

        if (memberService.existsByMemberId(signupRequest.getMemberId())) {
            throw new DuplicationException(ErrorCode.ID_DUPLICATE_PREVENTION, signupRequest.getMemberId());
        }

        Member member = signupRequest.toMember(passwordEncoder.encode(signupRequest.getPassword()));

        memberService.save(member);
    }

}
