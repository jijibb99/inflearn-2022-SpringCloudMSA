package sample.a.config.kafka;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface KafkaProcessor {
	// 강의 내역 공유
    String OUT_lectureChanged = "out-sampleAChanged";  //만약 application.yml에 없는 설정을 기록하면 이것이 바로 Topic 이름
	@Output(OUT_lectureChanged)
    MessageChannel outboundTopic();
}
