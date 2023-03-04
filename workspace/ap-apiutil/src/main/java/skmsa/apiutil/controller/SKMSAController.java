package skmsa.apiutil.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skmsa.apiutil.interceptor.OnlineContext;

public class SKMSAController {
    //Controller 및 처음 거래 시점에서 생성: Thread별 별도 변수임
    //ThreadLocal 제외 ==> Hystrix는 별도의 Thread로 처리됨 ==> 이 부분에 대한 대안 필요
    protected static ThreadLocal<OnlineContext> ctx =  new ThreadLocal<OnlineContext>();
    private Logger log = null;

    public OnlineContext getCtx() {
        return ctx.get();
    }

    //로거를 거래 단위로 하기 위하여 생성
    public void setCtx(String TRAN_ID) {
        log = LoggerFactory.getLogger(TRAN_ID);
        ctx.set(new OnlineContext(TRAN_ID));
        log().info("releaseThreadLocalVar---생성");
    }

    public Logger log() {
        return ctx.get().getLog();
    }

    public void releaseThreadLocalVar() {
        log().info("releaseThreadLocalVar--Release");
        ctx.remove();
    }
}
