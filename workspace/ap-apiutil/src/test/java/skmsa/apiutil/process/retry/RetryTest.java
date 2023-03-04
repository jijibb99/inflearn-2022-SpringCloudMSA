package skmsa.apiutil.process.retry;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import skmsa.apiutil.SKMSAApplicationTest;
import skmsa.apiutil.controller.SKMSAController;
import skmsa.apiutil.interceptor.OnlineContext;
import skmsa.apiutil.process.annotation.Retry;
import skmsa.apiutil.process.aop.RetryAspect;
import skmsa.apiutil.service.SKMSAService;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
@Import({RetryAspect.class, RetryTest.AOP1Controller.class, RetryTest.AOP1Service.class, RetryTest.AOP2Service.class})
@SpringBootTest(classes = {SKMSAApplicationTest.class})
//@SpringBootConfiguration

public class RetryTest {
    @Autowired
    AOP1Controller aop1Controller;

    @Test
    void aopTest() {
        log.debug("start:   Retry 테스트 ");
        String result = aop1Controller.methodA("Myinno", 100);
        assertEquals("Myinno", result);
    }


    @Controller
    static class AOP1Controller extends SKMSAController {
        @Autowired
        private AOP1Service aOP1Service;

        public String methodA(String name, int cnt) {
            log.debug("Controller 시작");
            return aOP1Service.methodA(getCtx(), name, cnt);
        }
    }

    @Service
    static class AOP1Service extends SKMSAService {
        @Autowired
        private AOP2Service aOP2Service;

        @Retry(retryCount = 5, sleepTime = 10)
        public String methodA(OnlineContext ctx, String name, int cnt) {
            log.debug("AOP1Service-- Retry 적용(직전 로그에 Around 있어야 함");
            return aOP2Service.methodAB(ctx, name, cnt);
        }
    }

    @Service
    static class AOP2Service extends SKMSAService {

        public String methodAB(OnlineContext ctx, String name, int cnt) {
            log.debug("AOP2Service--Random 오류 발생- 70% 3 이상 오류 발생");

            int ranNumber = (int) (Math.random() * 10);
            if (ranNumber > 6) {
                throw new IllegalStateException("오류 발생:" + ranNumber);
            } else {
                log.debug("정상 처리: {}", ranNumber);
            }
            return name;
        }
    }
}
