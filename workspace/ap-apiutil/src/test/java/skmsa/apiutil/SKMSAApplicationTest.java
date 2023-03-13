package skmsa.apiutil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

//@EnableWebMvc
@RunWith(SpringRunner.class)
@SpringBootTest
//@EnableMongoRepositories("skmsa.apiutil.domain.mongorepository.FWK_IO_LOGRepository")     //mongoDB는 별
@SpringBootApplication(scanBasePackages = {"skmsa"})
@EnableAutoConfiguration
@ActiveProfiles("local")
public class SKMSAApplicationTest {

    @Test
    public void contextLoads() {
        System.setProperty("spring.profiles.default", "local");
    }
}
