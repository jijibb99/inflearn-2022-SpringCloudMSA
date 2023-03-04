package skmsa.apiutil.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletRequest;

//@RestControllerAdvice   2022-10-20 일단 보류함  (Aspect에서 처리 가능)
@Slf4j  // 개별로그.. (여기서는 별도 Exception 내역만 분리)
// 사용할까? ...aspect에서 더 편하게 사용 가능
public class ExceptionAdvice {
    @ExceptionHandler(BizException.class)
    protected ResponseEntity handleBizException(ServletRequest request, BizException ex) {
        //Biz Exception 발생
        log.error("ErrTestAdvice  Request:{} ", request.getAttribute("_IN"), ex);
        // 발생 내역 DB 로깅하자
        return new ResponseEntity(new ErrorDto(ex.getErrorCode().getStatus(), ex.getErrorCode().getMessage()), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    // NullPointerException도 여기에 설정됨
    @ExceptionHandler({NullPointerException.class})
    public String handleNLPException(Exception ex) {
        log.error("----------------------------: ex.getCause():{}", ex.getCause(), ex);

        return "NullPointerException";
    }
    // NullPointerException도 여기에 설정됨
    @ExceptionHandler({Exception.class, IllegalStateException.class})
    public String handleOtherException(Exception ex, ServletRequest request) {
        log.error("----------------------------: ex.getCause():{}", ex.getCause(), ex);
        log.error("handleOtherException  Request:{} ", request.getAttribute("_IN"), ex);
        log.error("handleOtherException  Request:{} ", request, ex);
        return "hello custom1111";
    }

    /**
     *  모든 나머지 Exception, Error 처리
     *  - 여기에 로깅되면 필수 원인 확인 필요
    */
    @ExceptionHandler({Throwable.class})
    public String handleThrowable(Exception ex, ServletRequest request) {
        log.error("----------------------------: ex.getCause():{}", ex.getCause(), ex);
        log.error("handleOtherException  Request:{} ", request.getAttribute("_IN"), ex);
        log.error("handleOtherException  Request:{} ", request, ex);
        return "Throwable :  IT 담당자 확인 필요";
    }


}