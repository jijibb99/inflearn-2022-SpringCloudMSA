package sample.b.service.impl;

import org.springframework.stereotype.Service;
import sample.b.config.kafka.dto.SampleAChanged;
import sample.b.domain.SampleARepository;
import sample.b.domain.SampleB;
import sample.b.service.SampleBAsyncProcessService;
import skmsa.apiutil.interceptor.OnlineContext;
import skmsa.apiutil.process.annotation.Retry;
import skmsa.apiutil.service.SKMSAService;

@Service
public class SampleBAsyncProcessServiceImpl extends SKMSAService implements SampleBAsyncProcessService {
    private final SampleARepository sampleARepository;

    public SampleBAsyncProcessServiceImpl(SampleARepository sampleARepository) {
        this.sampleARepository = sampleARepository;
    }

    @Retry(retryCount = 5, sleepTime = 10)
    public void testRespect(OnlineContext ctx, String name) {
        log.debug("<Service 직접 호출 테스트 중...");
        //Retry 테스트를 위하여
        int ranNumber = (int) (Math.random() * 10);
        if (ranNumber > 2) {
            System.out.println("고의로 Exception 발생  ranNumber:" + ranNumber);
            throw new IllegalStateException("오류 발생:" + ranNumber);
        } else {
            System.out.println(" 정상 처리  ranNumber:" + ranNumber);
        }

    }

    @Override
    @Retry(retryCount = 5, sleepTime = 10)
    public void saveSampleAChanged(OnlineContext ctx, SampleAChanged sampleAChanged) {
//        setCtx(ctx);

        //Retry 테스트를 위하여
        int ranNumber = (int) (Math.random() * 10);
        if (ranNumber > 9) {
            log.error("고의로 Exception 발생");
            throw new IllegalStateException("오류 발생:" + ranNumber);
        } else {
            log.info("정상 처리: {}", ranNumber);
        }

        SampleB sampleb = new SampleB();
        sampleb.setVersion(sampleAChanged.getVersion());
        sampleb.setTitle(sampleAChanged.getTitle());
        sampleb.setId(sampleAChanged.getId());
        sampleb.setLectureStatus("기본등록");

        sampleARepository.save(sampleb);
    }
}
