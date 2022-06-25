package individual.freshplace.util.lock;

import com.zaxxer.hikari.HikariDataSource;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.exception.StringLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLevelLock {

    private static final String GET_LOCK = "SELECT GET_LOCK(?, ?)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(?)";
    private static final String IS_USED_LOCK = "SELECT IS_USED_LOCK(?)";
    private static final long ACQUIRER_IN_SECONDS = 1L;
    private static final int SUCCESS_QUERY_VALUE = 1;
    private static final String SQLEXCEPTION_MESSAGE = "쿼리 실행 오류";

    private final HikariDataSource hikariDataSource;

    public <T> T LockProcess(String lockName, Supplier<T> supplier) {

        try (Connection connection = hikariDataSource.getConnection()) {
            try {
                getLock(connection, lockName);
                return supplier.get();
            } finally {
                releaseLock(connection, lockName);
            }
        } catch (SQLException e) {
            log.error(SQLEXCEPTION_MESSAGE);
            throw new RuntimeException(SQLEXCEPTION_MESSAGE);
        }
    }

    public void LockProcess(String lockName, Runnable runnable) {
        LockProcess(lockName, () -> {
            runnable.run();
            return null;
        });
    }

    private void getLock(Connection connection, String lockName) throws SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_LOCK)) {

            preparedStatement.setString(1, lockName);
            preparedStatement.setLong(2, ACQUIRER_IN_SECONDS);

            resultQuery(lockName, preparedStatement, "getLock()");
        }
    }

    private void releaseLock(Connection connection, String lockName) throws SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement(RELEASE_LOCK)){

            preparedStatement.setString(1, lockName);

            resultQuery(lockName, preparedStatement, "releaseLock()");
        }
    }

    private void resultQuery(String lockName, PreparedStatement preparedStatement, String methodType) throws SQLException {

        try (ResultSet resultSet = preparedStatement.executeQuery()){

            if (!resultSet.next()) {
                log.error(methodType + SQLEXCEPTION_MESSAGE);
                throw new RuntimeException(SQLEXCEPTION_MESSAGE);
            }

            int resultSetInt = resultSet.getInt(1);

            if (resultSetInt != SUCCESS_QUERY_VALUE) {
                log.error("lockName={} 에 대한 methodType={} 실패", lockName, methodType);
                throw new StringLockException(ErrorCode.UN_AVAILABLE_ID, lockName);
            }

            if (resultSetInt == SUCCESS_QUERY_VALUE) {
                log.info("lockName={} 에 대한 methodType={} 성공", lockName, methodType);
            }

        }

    }

}
