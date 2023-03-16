package sample.b;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableMongoRepositories("skmsa.apiutil.domain.mongorepository.FWK_IO_LOGRepository")     //mongoDB는 별
@SpringBootApplication(scanBasePackages = {"sample.b", "skmsa"})
@ActiveProfiles("local") 
public class SampleBApplicationTests {

    @Test
    public void contextLoads() {
    }
}
