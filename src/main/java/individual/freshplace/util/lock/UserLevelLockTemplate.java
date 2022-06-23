package individual.freshplace.util.lock;

import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.exception.StringLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLevelLockTemplate {

    private static final String GET_LOCK = "SELECT GET_LOCK(:lockName, :timeOutSeconds)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(:lockName)";
    private static final long ACQUIRER_IN_SECONDS = 1;
    private static final int SUCCESS_QUERY_VALUE = 1;
    private static final String LOCKNAME_OF_KEYVALUE = "lockName";
    private static final String TIMEOUTSECONDS_OF_KEYVALUE = "timeOutSeconds";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public <T> T LockProcess(String lockName, Supplier<T> supplier) {

        try {
            getLock(lockName);
            return supplier.get();

        } finally {
            releaseLock(lockName);
        }
    }

    private void getLock(String lockName) {

        Map<String, Object> map = new HashMap<>();
        map.put(LOCKNAME_OF_KEYVALUE, lockName);
        map.put(TIMEOUTSECONDS_OF_KEYVALUE, ACQUIRER_IN_SECONDS);

        Integer result = namedParameterJdbcTemplate.queryForObject(GET_LOCK, map, Integer.class);
        checkResult(result, lockName, "getLock");
    }

    private void releaseLock(String lockName) {

        Map<String, Object> map = new HashMap<>();
        map.put(LOCKNAME_OF_KEYVALUE, lockName);

        Integer result = namedParameterJdbcTemplate.queryForObject(RELEASE_LOCK, map, Integer.class);
        checkResult(result, lockName, "releaseLock");
    }

    private void checkResult(Integer result, String lockName, String methodType) {

        if (result == null) {
            log.error(methodType + " 쿼리 결과가 없습니다.");
        }

        if (result != SUCCESS_QUERY_VALUE) {
            log.error(lockName + " " + methodType + " 이 실패했습니다.");
            throw new StringLockException(ErrorCode.UN_AVAILABLE_ID, lockName);
        }

        if (result == SUCCESS_QUERY_VALUE) {
            log.info(lockName + " " + methodType + " 이 성공했습니다.");
       }

    }

}
