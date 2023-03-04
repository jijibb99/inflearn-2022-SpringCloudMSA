package sample.b;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import skmsa.apiutil.controller.SKMSAController;
import skmsa.apiutil.domain.FWK_IO_LOG;
import skmsa.apiutil.domain.mongorepository.FWK_IO_LOGRepository;

/**
 * SampleB 프로젝트에서 APIUTIL의 빈 호출 테스트
 */
@Slf4j
@Import({BeanCreationTest.AOP1Controller.class})
@SpringBootTest(classes = {SampleBApplicationTests.class})
@ComponentScan({"sample.b", "skmsa"})

public class BeanCreationTest {
    @Autowired
    AOP1Controller aop1Controller;

    @Autowired
    private FWK_IO_LOGRepository ioLogRepository;

    @Test
    @DisplayName("AOP 기본 테스트: Controller --> Service")
    void aopTest() {
        log.debug("start:   Retry 테스트 ");
        aop1Controller.methodA("Myinno", 10);
    }

    @Test
    @DisplayName("AOP 기본 테스트: Controller --> private Controller")
    void aopCtrlToCtrlTest() {
        log.debug("start:   Retry 테스트 ");
        aop1Controller.methodB1("public controller --> private controller", 10);
    }

    //    @Slf4j
    @Controller
    static class AOP1Controller extends SKMSAController {
        @Autowired
        private FWK_IO_LOGRepository ioLogRepository;

        public String methodA(String name, int cnt) {
            log.debug("Controller 시작");


            FWK_IO_LOG ioLog = new FWK_IO_LOG();
            ioLog.setGUID("111");           //GUID
            ioLog.setParentGUID("");                //원인거래의GUID, 없으면 최초거래

            ioLog.setTRAN_ID("BBB");              // 거래ID
            ioLog.setCtx(ctx.toString());           // Online Ctx
            ioLog.setInArg("argStr.toString()");      // 입력 파라미터
            ioLog.setResult("resultStatus");          // 입력 파라미터

            ioLog.setStackTrace("stack");      // 오류 StackTrace
            ioLog.setExecTime(10);           // 실행시간

            ioLogRepository.save(ioLog);
            return "OK";
        }

        // Controller --> Controller 호출 테스트
        public String methodB1(String name, int cnt) {
            log.debug("Controller 시작");
            return  methodB2Private(name, cnt);
        }

        private String methodB2Private(String name, int cnt) {
            log.debug("Controller 시작");
            return  "Private-" + name;
        }
    }


}
