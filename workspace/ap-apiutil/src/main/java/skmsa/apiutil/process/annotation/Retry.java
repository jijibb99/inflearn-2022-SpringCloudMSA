package skmsa.apiutil.process.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

/**
 * 특정 메소드 일정 주기 제처리 로직
 * retryCount: 오류 발생시 해당 횟수 만큼 반복
 * sleepTime: 반복 시점에 일정 시간 Sleep(단위 MS)
 */
public @interface Retry {
    //재시도 횟수
    int retryCount() default 3;
    // Sleep 간격(ms)
    int sleepTime() default 5;
}
