package skmsa.apiutil.interceptor;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;


//@Component
@Data

//서비스 요청에서 응답까지 생명주기 유지 (종료 시점까지 동일 Logger을 생성하기 위하여)
//@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OnlineContext {
    //거래ID: 처음 호출되는 class명+메소드명
    private final String TRAN_ID;

    //로거를 거래 단위로 하기 위하여 생성
    private Logger log;

    //처음 생성 여부
    private boolean swFirst = true;

    //거래단위 GUID
    private String GUID;

    //=======향후를 위하여 정의한 필드
    private String GL_MAGAM_DT;
    private String GL_OP;

    /**
     * 처음 거래시 Online Context 생성
     */
    public OnlineContext(String TRAN_ID) {
//        this.log = LoggerFactory.getLogger("sample." + tranID);
        //로거이름은 TRAN ID로 생성(나중에 로그 모드 변경을 거래 단위로 변경하기 위하여)
        GUID = UUID.randomUUID().toString();
        MDC.put("GUID", GUID); //임시로 일부만 추가

        this.TRAN_ID = TRAN_ID;
        log = LoggerFactory.getLogger(TRAN_ID);
        MDC.put("TRAN_ID", TRAN_ID);
        log.debug("GUID: {}, TRAN_ID: {}", GUID, TRAN_ID);
        swFirst = true;
    }

//    public void setTRAN_ID(String TRAN_ID) {
//        if (swFirst) {
////            this.TRAN_ID = TRAN_ID;
////            log = LoggerFactory.getLogger(TRAN_ID);
////            MDC.put("TRAN_ID",TRAN_ID);
////            log.debug("GUID: {}, TRAN_ID: {}", GUID, TRAN_ID);
//            swFirst = false;
//        }
//    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        //거래ID: 처음 호출되는 class명+메소드명
        sb.append("TRAN_ID: " + TRAN_ID );

        //거래단위 GUID
        sb.append(", GUID: " + GUID );

        //거래단위 GUID
        sb.append(", GL_MAGAM_DT: " + GL_MAGAM_DT );
        //거래단위 GUID
        sb.append(", GL_OP: " + GL_OP );
        return  sb.toString();

    }
}
