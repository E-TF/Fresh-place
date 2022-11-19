package individual.freshplace.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    private final static String ALGORITHM = "PBEWithMD5AndDES";
    private final static String KEY_OBTENTION_ITERATIONS = "1000";
    private final static String POOL_SIZE = "1";
    private final static String PROVIDER_NAME = "SunJCE";
    private final static String SALT_GENERATOR_CLASS_NAME = "org.jasypt.salt.RandomSaltGenerator";
    private final static String ENCODING_TYPE = "base64";

    @Value("${jasypt.encryptor.password}")
    private String PASSWORD;

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(PASSWORD);
        config.setAlgorithm(ALGORITHM);
        config.setKeyObtentionIterations(KEY_OBTENTION_ITERATIONS); // 반복할 해싱 회수
        config.setPoolSize(POOL_SIZE);
        config.setProviderName(PROVIDER_NAME);
        config.setSaltGeneratorClassName(SALT_GENERATOR_CLASS_NAME); // salt 생성 클래스
        config.setStringOutputType(ENCODING_TYPE);
        encryptor.setConfig(config);
        return encryptor;
    }
}
