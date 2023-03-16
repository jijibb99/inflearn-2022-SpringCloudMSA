package sample.b;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import sample.b.service.impl.SampleBAsyncProcessServiceImpl;
import skmsa.apiutil.controller.SKMSAController;
import skmsa.apiutil.interceptor.OnlineContext;
import skmsa.apiutil.process.annotation.Retry;
import skmsa.apiutil.service.SKMSAService;


@Slf4j
//@Import({RetryAspect.class, SampleBAsyncProcessServiceImpl.class, AopAspectANDRetryLogTest.AOP1Controller.class, AopAspectANDRetryLogTest.AOP1Service.class, AopAspectANDRetryLogTest.AOP2Service.class})
@Import({AopAspectANDRetryLogTest.AOP1Controller.class, AopAspectANDRetryLogTest.AOP1ServiceImpl.class, AopAspectANDRetryLogTest.AOP2ServiceImpl.class, AopAspectANDRetryLogTest.TestService.class})
@SpringBootTest(classes = {SampleBApplication.class})
@SpringBootConfiguration
@ActiveProfiles("local")
public class AopAspectANDRetryLogTest {
    @Autowired
    AOP1Controller aop1Controller;

    @Autowired
    TestService testService;

    @Test
    @DisplayName("AOP 기본 테스트: Controller --> Service")
    void aopTest() {
        log.debug("start:   Retry 테스트 ");
        aop1Controller.methodA("Myinno", 10);
//        assertEquals("Myinno", result);
    }


    @Test
    @DisplayName("Controller를 거치지 않고 서비스를 직접 호출하는 Case 테스트")
    void aopDirectServiceCallTest() {
        log.debug("start:   Retry 테스트 ");

        //Service를 직접 호출하는 경우는 CTX을 생성해서 호출하여야 함
        OnlineContext ctx = new OnlineContext("aopDirectServiceCallTest");

        testService.methodA(ctx, "Myinno", 10);
//        assertEquals("Myinno", result);
    }

    //    @Slf4j
    @Controller
    static class AOP1Controller extends SKMSAController {
        @Autowired
        private AOP1ServiceImpl aOP1Service;

        public String methodA(String name, int cnt) {
            log.debug("Controller 시작");

            return aOP1Service.methodA(getCtx(), name, cnt);
        }
    }

    //    @Slf4j
    @Service
    static class TestService extends SKMSAService {
        @Autowired
        private SampleBAsyncProcessServiceImpl sampleBAsyncProcessServiceImpl;

        public void methodA(OnlineContext ctx, String name, int cnt) {
            log.info("TestService 시작");
            sampleBAsyncProcessServiceImpl.testRespect(ctx, name);
        }
    }

    //    @Slf4j
    @Service
    static class AOP1ServiceImpl extends SKMSAService {
        @Autowired
        private AOP2ServiceImpl aOP2Service;

        @Retry(retryCount = 5, sleepTime = 10)
        public String methodA(OnlineContext ctx, String name, int cnt) {
            log.info("AOP1Service-- Retry 적용(직전 로그에 Around 있어야 함");
            return aOP2Service.methodAB(ctx, name, cnt);
        }
    }

    //    @Slf4j
    @Service
    static class AOP2ServiceImpl extends SKMSAService {

        public String methodAB(OnlineContext ctx, String name, int cnt) {
            log.debug("AOP2Service--Random 오류 발생- 70% 3 이상 오류 발생");

            int ranNumber = (int) (Math.random() * 10);
            if (ranNumber > 2) {
                throw new IllegalStateException("오류 발생:" + ranNumber);
            } else {
                log.debug("정상 처리: {}", ranNumber);
            }
            return name;
        }
    }
}
