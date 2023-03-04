package skmsa.apiutil.process.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import skmsa.apiutil.process.annotation.Retry;

@Aspect
@Component
@Slf4j    //Retry는 별도로 Logging하기 위하여 추가함
public class RetryAspect {

    public RetryAspect() {
        log.info("[RetryAspect  생성자");
    }

    @Around("@annotation(retry)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        System.out.println("RetryAspect.doRetry");
        log.info("[retry1] {} retry={}", joinPoint.getSignature(), retry);

        int maxRetry = retry.retryCount();

        for (int retryCount = 1; retryCount <= maxRetry; retryCount++) {
            try {
                log.info("[retry] try count={}/{}", retryCount, maxRetry);
                return joinPoint.proceed();
            } catch (Exception e) {
                // 일정 간격 Sleep
                if (retryCount < maxRetry) {
                    log.info("Retry: e.getMessage(): {}, Sleep: {}", e.getLocalizedMessage(), retry.sleepTime());
                    Thread.sleep(retry.sleepTime());
                } else {
                    log.error("== 재시도 실퍠: {}번 시도, {}Sleep ", retryCount,retry.sleepTime(), e );
                    throw e;
                }
            }
        }
        return null; //이문장은 실행되지 않음, 중간에 Return 하거나, throw 함
    }
}

