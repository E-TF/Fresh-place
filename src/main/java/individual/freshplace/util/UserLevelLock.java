package individual.freshplace.util;

import com.zaxxer.hikari.HikariDataSource;
import individual.freshplace.util.exception.DuplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLevelLock {

    private static final String GET_LOCK = "SELECT GET_LOCK(?, ?)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(?)";
    private static final String IS_USED_LOCK = "SELECT IS_USED_LOCK(?)";
    private static final long ACQUIRER_IN_SECONDS = 1;

    private final HikariDataSource hikariDataSource;

    public void LockProcess(String lockName, Runnable runnable) {

        try (Connection connection = hikariDataSource.getConnection()) {
            try {
                getLock(connection, lockName);
                runnable.run();
            } finally {
                releaseLock(connection, lockName);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    private void getLock(Connection connection, String lockName) throws SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_LOCK)) {

            preparedStatement.setString(1, lockName);
            preparedStatement.setLong(2, ACQUIRER_IN_SECONDS);

            try (ResultSet resultSet = preparedStatement.executeQuery()){

                if (!resultSet.next()) {
                    log.error("쿼리 수행 실패");
                    throw new RuntimeException();
                }

                int resultSetInt = resultSet.getInt(1);

                if (resultSetInt != 1) {
                    log.error(lockName + " 획득 실패");
                    throw new DuplicationException(ErrorCode.UN_AVAILABLE_ID, lockName);
                } else {
                    log.info(lockName + " 획득 성공");
                }

            }
        }
    }

    private void releaseLock(Connection connection, String lockName) throws SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement(RELEASE_LOCK)){

            preparedStatement.setString(1, lockName);

            try (ResultSet resultSet = preparedStatement.executeQuery()){

                if (!resultSet.next()) {
                    log.error("쿼리 수행 실패");
                    throw new RuntimeException();
                }

                int resultSetInt = resultSet.getInt(1);

                if (resultSetInt != 1) {
                    log.error(lockName + " 반환 실패");
                    throw new DuplicationException(ErrorCode.UN_AVAILABLE_ID, lockName);
                } else {
                    log.info(lockName + " 반환 성공");
                }
            }
        }
    }
}
