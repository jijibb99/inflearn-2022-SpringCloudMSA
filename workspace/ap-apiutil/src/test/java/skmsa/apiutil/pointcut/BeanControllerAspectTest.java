package skmsa.apiutil.pointcut;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import skmsa.apiutil.SKMSAApplicationTest;
import skmsa.apiutil.controller.SKMSAController;
import skmsa.apiutil.interceptor.JournalAOP;
import skmsa.apiutil.interceptor.OnlineContext;
import skmsa.apiutil.process.annotation.Retry;
import skmsa.apiutil.service.SKMSAService;

//@Import(BeanControllerAspectTest.BeanAspect.class)
@Import(JournalAOP.class)


/**
 * Aspect 테스트
 * 2022-10-18 
 */
@SpringBootTest(classes = {SKMSAApplicationTest.class })
@SpringBootApplication(scanBasePackages={ "skmsa"})
@SpringBootConfiguration
public class BeanControllerAspectTest {

    @Autowired
    AOP1Controller aop1Controller;

    @Test
    void success() {
        aop1Controller.methodA("itemA", 10);
    }

    @Aspect
    static class BeanAspect {
        /**
         * Online Context 설정 --Controller 시작 시점에 설정한다
         * 여기서 테스트 하고 "JournalAOP"로 적용 했다
         */
        @Around("bean(*Controller)")
        public Object setOnlineContext(ProceedingJoinPoint joinPoint) throws Throwable {
            SKMSAController skmsaController = (SKMSAController)joinPoint.getTarget();

            //거래ID
            String	TRAN_ID	= joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
            skmsaController.setCtx(TRAN_ID);

            skmsaController.log().info("[bean Start] {}", TRAN_ID);
            return joinPoint.proceed();
        }
    }

    @Controller
    static class AOP1Controller extends SKMSAController {
        @Autowired
        private BeanControllerAspectTest.AOP1Service aOP1Service;

        public String methodA(String name, int cnt) {
            log().debug("Controller 시작");
            return aOP1Service.methodA(getCtx(), name, cnt);
        }
    }

    @Service
    static class AOP1Service extends SKMSAService {
        @Autowired
        private BeanControllerAspectTest.AOP2Service aOP2Service;

        @Retry(retryCount = 5, sleepTime = 10)
        public String methodA(OnlineContext ctx, String name, int cnt) {
            log.debug("AOP1Service-- Retry 적용(직전 로그에 Around 있어야 함");
            return aOP2Service.methodAB(ctx, name, cnt);
        }
    }

    @Service
    static class AOP2Service  extends SKMSAService{

        public String methodAB(OnlineContext ctx, String name, int cnt) {
//            setCtx((ctx));
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