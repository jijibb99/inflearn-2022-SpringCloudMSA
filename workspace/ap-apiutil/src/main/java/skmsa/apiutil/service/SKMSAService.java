package skmsa.apiutil.service;

import org.slf4j.Logger;
import skmsa.apiutil.interceptor.OnlineContext;

/**
 * Service의 기본 상속 인터페이스
 * - OnlineCtx관리 용
 */
public class SKMSAService {
    private OnlineContext ctx;

    //상속받는 곳에서 바로 사용하도록 Public로 선언 했음
    public Logger log;

    public OnlineContext getCtx() {
        return ctx;
    }

    public void setCtx(OnlineContext ctx) {
        this.ctx = ctx;
        this.log = ctx.getLog();
    }
}
