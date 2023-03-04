package sample.a.config.controller;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.micrometer.core.instrument.Metrics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import skmsa.apiutil.controller.SKMSAController;

/**
 * REST controller for managing
 */
@Tag(name = "sampleaHystrix", description = "MSA Hystrix")
@RestController
@RequestMapping("/msa")
@Slf4j
public class SampleAHystrixController extends SKMSAController {
    private RestTemplate restTemplate;    //타 마이크로 서비스 호출
    private LoadBalancerClient loadBalancer;

    public SampleAHystrixController(RestTemplate restTemplate, LoadBalancerClient loadBalancer) {
        this.restTemplate = restTemplate;
        this.loadBalancer = loadBalancer;
    }


    @Tag(name = "sampleaHystrix")    //swagger용
    @GetMapping(value = "/HystrixExceptionNo/{customerId}")
    @Operation(summary = "고객 정보 조회", description = " 아무런 처리를 하지 않는다")
    public String getCustNameException(@PathVariable String customerId) {

        //사용자 Metrics 추가: Prometheus에서 조회용추ㅊ

        log().info("getCustNameException:{}", customerId);

//        return restTemplate.getForObject("http://localhost:8092/sampleb/msa/custinfoException/" + customerId, String.class);
        return restTemplate.getForObject("http://sampleb/sampleb/msa/custinfoException/" + customerId, String.class);
    }

    @Tag(name = "sampleaHystrix")    //swagger용
    @GetMapping(value = "/HystrixSleepNo/{customerId}")
    @Operation(summary = "고객 정보 조회", description = " 아무런 처리를 하지 않는다")
    public String getCustNameSleepNo(@PathVariable String customerId) {
        //사용자 Metrics 추가: Prometheus에서 조회용
        Metrics.counter("skmsa_getCustomerInfo", "customerId", customerId).increment();


        log().info("getCustNameSleepNo:{}", customerId);
//        ServiceInstance instance = loadBalancer.choose("sampleb");
//        String url = String.format("http://%s:%s", instance.getHost(), instance.getPort());

        return restTemplate.getForObject("http://sampleb//sampleb/msa/custinfoSleep/" + customerId, String.class);
    }


    @Tag(name = "sampleaHystrix")    //swagger용
    @Operation(summary = "고객 정보 조회", description = " Exception 발생하는 경우 fallbackMethod 확인")
    @GetMapping(value = "/HystrixExceptionYes/{customerId}")
//    @HystrixCommand(fallbackMethod = "getCustNameHystrixFallback")
    public String getCustNameHystrix(String customerId) {
        //사용자 Metrics 추가: Prometheus에서 조회용추가

        System.out.println("SampleAHystrixController.getCustNameHystrix: ");
        log.info("getCustNameHystrix:{}", customerId);

        ServiceInstance instance = loadBalancer.choose("sampleb");
        String url = String.format("http://%s:%s", instance.getHost(), instance.getPort());
        String result = restTemplate.getForObject(url + "/sampleb/msa/custinfoException/" + customerId, String.class);
        return result;
    }

    @Tag(name = "sampleaHystrix")    //swagger용
    @Operation(summary = "고객 정보 조회", description = " 5초 Sleep 발생하는 경우 fallbackMethod 확인")
    @GetMapping(value = "/HystrixSleepYes/{customerId}")
//    @HystrixCommand(fallbackMethod = "getCustNameHystrixFallback")
    public String getCustNameSleepYes(@PathVariable String customerId) {
        //사용자 Metrics 추가: Prometheus에서 조회용추가


        log.info("HystrixSleepYes:{}", customerId);
        localMethod(customerId);
        String result = restTemplate.getForObject("http://sampleb/sampleb/msa/custinfoSleep/" + customerId, String.class);
        return result;
    }

    /**
     * CustNameHystrixFallback메소드
     */
    private String getCustNameHystrixFallback(String customerId, Throwable ex) {
        //사용자 Metrics 추가: Prometheus에서 조회용추가

        System.out.println("SampleAHystrixController.getCustNameHystrixFallback");
        System.out.println("customerId = " + customerId);
        System.out.println("ex = " + ex);
//        log().info("getCustNameHystrixFallback: {}", customerId, ex);
        log.info("-----------getCustNameHystrixFallback:{}", customerId, ex);
        return "고객정보 조회가 지연되고 있습니다.";
    }

    private void localMethod(String customerId) {
        System.out.println("SampleAHystrixController.localMethod");
        //log()가 가능한 상태여부 ..
        log().info("localMethod:{}", customerId);

    }
}
