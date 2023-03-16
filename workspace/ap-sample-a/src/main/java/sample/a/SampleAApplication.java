package sample.a;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;
import sample.a.adaptor.kafka.KafkaProcessor;


@EnableEurekaClient   //컴포넌트 등록을 위하여 추가
@EnableAutoConfiguration
@EnableBinding(KafkaProcessor.class)  //kafka관련 설정 Binder
@ComponentScan(basePackages = {"sample.a.controller.mapper","sample.a", "skmsa"})
@SpringBootApplication
@EnableMongoRepositories("skmsa.apiutil.domain.mongorepository")
//2022-11-02  Hystrix 적용 관련 추가
// TODO: Deprecated 추후 변경 하자
//@EnableCircuitBreaker
public class SampleAApplication {

    public static ApplicationContext applicationContext;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(SampleAApplication.class, args);
    }
}
