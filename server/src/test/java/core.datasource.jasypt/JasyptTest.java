package core.datasource.jasypt;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.todo.core.jasypt.JasyptConfig;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource( properties = { "jasypt.encryptor.password=Happy1234@@" })
@Slf4j
@SpringBootTest(classes = JasyptConfig.class)
public class JasyptTest {
    @Resource(name = "jasyptStringEncryptor")
    private StringEncryptor encryptor;

    @Test
    public void jasyptTest() {
        Map<String, String> dbInfo = new HashMap<>();
        dbInfo.put("driverNm", "com.mysql.cj.jdbc.Driver");
        dbInfo.put("jdbcUrl", "jdbc:mysql://192.168.0.3:3306/happytodo");
        dbInfo.put("userNm", "happytodo");
        dbInfo.put("userPw", "Todo1234!");
        dbInfo.put("cryptoKey", "flskdlqjstm");

        dbInfo.forEach((key, value) -> {
            String encryptText = encryptor.encrypt(value);
            String decryptText = encryptor.decrypt(encryptText);

            log.info("key: " + key);
            log.info("encryptText: " + encryptText);
            log.info("decryptText: " + decryptText);
            assertEquals(value, decryptText);
        });
    }
}