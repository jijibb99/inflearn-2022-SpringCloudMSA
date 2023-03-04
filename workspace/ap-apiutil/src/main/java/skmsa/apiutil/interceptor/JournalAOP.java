package skmsa.apiutil.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import skmsa.apiutil.controller.SKMSAController;
import skmsa.apiutil.domain.FWK_IO_LOG;
import skmsa.apiutil.domain.mongorepository.FWK_IO_LOGRepository;
import skmsa.apiutil.service.SKMSAService;

import java.io.PrintWriter;
import java.io.StringWriter;

@Component
@Aspect
@Slf4j    //AOP내역은 별도 로깅을 하기 위하여 로거를 Slf4j 사용( GUID를 통해서 사용자 로깅 내욕과 연결)
public class JournalAOP {
    //저널 생성용 mongoDB (FWK_IO_LOG)
    @Autowired
    private FWK_IO_LOGRepository ioLogRepository;

    //호출 Depth을 관리하기위한 Thread로 관리 되는 변수
    private ThreadLocal<Integer> traceIdHolder;

    @Autowired
    public JournalAOP() {
        log.info("[SKMSA JournalAOP  생성자]");
        traceIdHolder = new ThreadLocal<>();
    }

    @Around("this(skmsa.apiutil.controller.SKMSAController)")
    public Object controllerAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Long startTime1 = System.currentTimeMillis();
        // 거래 ID
        String TRAN_ID = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
       SKMSAController skmsaController  = null;

        try {
            skmsaController = (SKMSAController) joinPoint.getTarget();
            skmsaController.setCtx(TRAN_ID);

        } catch (Exception e) {
            log.error("여기 확인 필요... Controller = {}", joinPoint.getSignature().toLongString(),e);
            return joinPoint.proceed();
        }

        syncTraceId();

        //입력 변수 출력
        StringBuffer argStr = new StringBuffer();
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                argStr.append(arg.toString() + ",");
            }
        }

        //Controller의 시작 메소드 로깅(+ 입력 값 추가)
        // 외부에서 호출한 메소드만 기록, 내부에서 private 메소드는 기록되지 않음
        log.info("{}: {}, args: {} ", getDepthString("-->"), TRAN_ID, argStr);
        Long startTime = System.currentTimeMillis();
        Object result;
        String resultStatus = "N";  //정상
        String sStackTrace = "";
        long execTime = 0;
        try {
            result = joinPoint.proceed();
            execTime = System.currentTimeMillis() - startTime;
            log.info("{}: {}, Exec: {}ms, Result: {} ", getDepthString("<--"), TRAN_ID, execTime, result.toString());
            return result;
        } catch (Exception e) {
            result = "Error";
            resultStatus = "E";  //오류
            sStackTrace = getsStackTrace(e);

            log.error("    ERR1 Occur OnlineContext: {}", skmsaController.getCtx()); //정상 모드(info Mode)일떄 입력값이 출력되지 않아, 오류 발생시 출력함
            log.error("    ERR2 Occur Args: {}", argStr); //정상 모드(info Mode)일떄 입력값이 출력되지 않아, 오류 발생시 출력함
            log.error("{}: {}, Exec: {}ms, Error: {} ", getDepthString("<--"), TRAN_ID, execTime, e);

            throw e;
        } finally {
            execTime = System.currentTimeMillis() - startTime;
            saveLog(TRAN_ID, skmsaController.getCtx(), argStr, resultStatus, execTime, sStackTrace);
            skmsaController.releaseThreadLocalVar();
            releaseTraceId();
        }

    }

    // stack trace as a string
    private static String getsStackTrace(Exception e) {
        //Exception 객체를 String으로 변환
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    // Controller 로그 저장
    private void saveLog(String TRAN_ID, OnlineContext ctx, StringBuffer argStr, String resultStatus, long execTime, String stack) {
        FWK_IO_LOG ioLog = new FWK_IO_LOG();
        ioLog.setGUID(ctx.getGUID());           //GUID
        ioLog.setParentGUID("");                //원인거래의GUID, 없으면 최초거래

        ioLog.setTRAN_ID(TRAN_ID);              // 거래ID
        ioLog.setCtx(ctx.toString());           // Online Ctx
        ioLog.setInArg(argStr.toString());      // 입력 파라미터
        ioLog.setResult(resultStatus);          // 입력 파라미터

        ioLog.setStackTrace(stack);      // 오류 StackTrace
        ioLog.setExecTime(execTime);           // 실행시간
        ioLogRepository.save(ioLog);
    }


    //    @Around("execution(* *..*ServiceImpl.*(..))")
//	@Around("bean(*ServiceImpl) " )        //org.springframework.context.support.ConversionServiceFactoryBean.isSingleton()이런 소스도 추출됨
    @Around("this(skmsa.apiutil.service.SKMSAService)")
    public Object logAction(ProceedingJoinPoint joinPoint) throws Throwable {
        Long startTime1 = System.currentTimeMillis();

        Object[] args = joinPoint.getArgs();
        OnlineContext ctx = null;
        try {
            SKMSAService skmsaService = (SKMSAService) joinPoint.getTarget();
            ctx = ((OnlineContext) args[0]);
            if (ctx == null) {
                log.error("여기 확인 필요 Service(CTX 확인) = {}", joinPoint.getSignature().toLongString());
            }
            skmsaService.setCtx(ctx);
        } catch (Exception e) {
            log.error("여기 확인 필요 Service = {}", joinPoint.getSignature().toLongString(),e);
            return joinPoint.proceed();
        }

        syncTraceId();

        //입력 변수 출력
        StringBuffer argStr = new StringBuffer();

        for (Object arg : args) {
            if (!Class.forName("skmsa.apiutil.interceptor.OnlineContext").isInstance(arg)) {
                argStr.append(arg.toString() + ",");
            }
        }
        String signature = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        log.info("{} {}, args: {} ", getDepthString("-->"), signature, argStr);
        Long startTime = System.currentTimeMillis();
        try {
            Object obj = joinPoint.proceed();
            long execTime = System.currentTimeMillis() - startTime;
            long execTime1 = System.currentTimeMillis() - startTime1;
            log.info("{}, Exec: {}ms{}, Result: {} ", getDepthString("<--"), execTime, execTime1, obj);
            return obj;
        } catch (Exception e) {
            long execTime = System.currentTimeMillis() - startTime;
            long execTime1 = System.currentTimeMillis() - startTime1;
            log.error("{}, Exec: {}ms{}, Error: {} ", getDepthString("<--"), execTime, execTime1, e.getMessage());
            log.error("    ERR Occur OnlineContext: {}", ctx); //정상 모드(info Mode)일떄 입력값이 출력되지 않아, 오류 발생시 출력함
            log.error("    ERR Occur Args: {}", argStr); //정상 모드(info Mode)일떄 입력값이 출력되지 않아, 오류 발생시 출력함
            throw e;
        } finally {
            releaseTraceId();
        }
    }


    //호출 흐름을 시각적으로 표현하기 위하여 'Depth'에 따른 표시
    public String getDepthString(String prefix) {
        return addSpace(prefix, traceIdHolder.get());
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == (level - 1)) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }


    private void syncTraceId() {
        Integer traceId = traceIdHolder.get();
        if (traceId == null) {
            traceId = 1;
            traceIdHolder.set(traceId);
        } else {
            traceId += 1;
            traceIdHolder.set(traceId);
        }
        MDC.put("DEPTH", "" + traceId);
    }

    /**
     * 해당 거래 종료 시점
     */
    private void releaseTraceId() {
        Integer traceId = traceIdHolder.get();
        if (traceId == 1) {
            //초기화시 3건의 로그에 미표시되는 문제가 있음(10.06)
            traceIdHolder.remove();//destroy
        } else {
            traceId -= 1;
            traceIdHolder.set(traceId);
        }
        MDC.put("DEPTH", "" + traceId);
    }
}
