package sample.b.adaptor.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface KafkaProcessor {
    String inboundTopicSampleAChanged = "in-sampleAChanged";

    // 강의 내역 공유
    //application.yaml에서 정의한 이벤트 그룹이름
    @Input(inboundTopicSampleAChanged)
    MessageChannel inboundTopicSampleAChanged();
}
