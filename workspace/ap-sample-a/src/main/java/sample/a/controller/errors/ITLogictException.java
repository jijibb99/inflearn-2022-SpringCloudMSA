package sample.a.controller.errors;

import lombok.Getter;

import java.util.Collection;
import java.util.Map;

/**
 * 프로그램 오류에 대한 Exception
 * - IT부서에서 수정이 필여한 오류 사항시 발생하는 Exception (프로그렘 수정이 필요한 Case)
 * - 예를 들어 비동기 in/out Format이 다르거나
 * - 발생이력을 별도의 NO-SQL로 관리해보자
 * @author myinno
 *
 */
@Getter
public class ITLogictException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String statusCode;
    private final String errorMessage;

    private final Map<String, Collection<String>> headers;

    public ITLogictException(ITLogicException_ERR_CODE statusCode, String errorMessage, Map<String, Collection<String>> headers) {
        super(errorMessage);
        this.statusCode = statusCode.name();
        this.errorMessage = errorMessage;
        this.headers = headers;

    }


}
