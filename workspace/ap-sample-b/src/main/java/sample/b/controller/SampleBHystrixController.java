package sample.b.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skmsa.apiutil.controller.SKMSAController;

@Tag(name = "sampleBHystrix", description = "MSA Hystrix")
@RestController
@RequestMapping("/msa")
public class SampleBHystrixController extends SKMSAController {


    @Tag(name = "sampleBHystrix")    //swagger용
    @Operation(summary = "고객 정보 조회", description = " 테스트 목적으로 무조건 오류 발생함",
            responses = {@ApiResponse(responseCode = "200", description = "고객 정보 조회")})
    @GetMapping(value = "/custinfoException/{customerId}")
    public String getCustomerDetailException(@PathVariable String customerId) {
        log().debug("getCustomerDetailException: {}", customerId);
        throw new RuntimeException("I/O Exception");
    }

    @Tag(name = "sampleBHystrix")    //swagger용
    @Operation(summary = "고객 정보 조회(5초Sleep)", description = " 테스트 목적으로 5초 지연 응답",
            responses = {@ApiResponse(responseCode = "200", description = "고객 정보 조회")})
    @GetMapping(value = "/custinfoSleep/{customerId}")
    public String getCustomerDetailSleep5(@PathVariable String customerId) {
        log().debug("getCustomerDetailSleep5: {}", customerId);
        try {
            Thread.sleep(5 * 1000l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "[Customer id = " + customerId + " at " + System.currentTimeMillis() + "]";
    }


}
