package sample.b;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;
import sample.b.config.kafka.KafkaProcessor;


@EnableEurekaClient   //텀포넌트 등록을 위하여 추가
@SpringBootApplication
@EnableAutoConfiguration
@EnableBinding(KafkaProcessor.class)  //kafka관련 설정 Binder
@ComponentScan({"sample.b", "skmsa"})
@EnableMongoRepositories("skmsa.apiutil.domain.mongorepository")

public class SampleBApplication {

    public static ApplicationContext applicationContext;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(SampleBApplication.class, args);
    }
}
