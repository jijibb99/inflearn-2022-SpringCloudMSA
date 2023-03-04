package skmsa.apiutil.controller.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import skmsa.apiutil.controller.error.constants.BizErrCode;

/**
 * 기본 오류 처리용 Exception 객체
 */
@AllArgsConstructor
@Getter
public class BizException extends RuntimeException {
//    private static final long serialVersionUID = 1L;
    private final BizErrCode errorCode;

}
