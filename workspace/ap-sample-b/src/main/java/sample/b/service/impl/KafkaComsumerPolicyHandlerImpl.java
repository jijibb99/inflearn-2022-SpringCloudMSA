package sample.b.service.impl;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import sample.b.adaptor.kafka.KafkaProcessor;
import sample.b.adaptor.kafka.dto.SampleAChanged;
import sample.b.domain.repository.SampleARepository;
import skmsa.apiutil.interceptor.OnlineContext;

@Service
//@Slf4j
public class KafkaComsumerPolicyHandlerImpl { //implements KafkaComsumerPolicyHandler {
    private final SampleARepository sampleARepository;
    private final SampleBAsyncProcessServiceImpl sampleBAsyncProcessServiceImpl;
    private OnlineContext ctx;
    private Logger log;

    @Autowired
    public KafkaComsumerPolicyHandlerImpl(SampleARepository sampleARepository, SampleBAsyncProcessServiceImpl sampleBAsyncProcessServiceImpl) {
        this.sampleARepository = sampleARepository;
        this.sampleBAsyncProcessServiceImpl = sampleBAsyncProcessServiceImpl;
    }

    //    @Override
    @StreamListener(KafkaProcessor.inboundTopicSampleAChanged)
    public void whateverCategoryChanged(@Payload String eventString) {
        //여기는 Kafka의 메시지를 Strig으로
        //{"eventType":"SampleAChanged","timestamp":"2022-10-10 17:41:46","jobType":"INSERT","id":32,"version":0,"title":"string","minEnrollment":10,"maxEnrollment":20}
    }

    /**
     * 강의분류 변경 비동기 처리
     * - (TODO):MultiThread, 유량처리를 고민
     */
//    @Override
    @StreamListener(KafkaProcessor.inboundTopicSampleAChanged)
    public void wheneverCategoryChanged(@Payload SampleAChanged sampleAChanged) {
        ctx = new OnlineContext(KafkaProcessor.inboundTopicSampleAChanged);
        log = ctx.getLog();


        //CTX의 기본 값 설정
        log.info("Kafka  데이터 수신.. sampleAChanged: {}", sampleAChanged.getId());

        sampleBAsyncProcessServiceImpl.saveSampleAChanged(ctx, sampleAChanged);

//        ctx.remove();  //마지막에 Thread Local 클리어
    }
}