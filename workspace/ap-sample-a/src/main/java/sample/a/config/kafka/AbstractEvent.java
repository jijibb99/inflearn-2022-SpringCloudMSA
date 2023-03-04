package sample.a.config.kafka;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.MimeTypeUtils;
import sample.a.SampleAApplication;
import skmsa.apiutil.kafka.AbstractKafkaEvent;

// 메시지 전송시에만 사용
public class AbstractEvent extends AbstractKafkaEvent {

    public void publish(String json) {
        if (json != null) {
            /**
             * spring streams 방식
             */
            KafkaProcessor processor = SampleAApplication.applicationContext
            											.getBean(KafkaProcessor.class);
            MessageChannel outputChannel = processor.outboundTopic();

            outputChannel.send(
                MessageBuilder
                    .withPayload(json)
                    .setHeader(
                        MessageHeaders.CONTENT_TYPE,
                        MimeTypeUtils.APPLICATION_JSON
                    )
                    .build()
            );
        }
    }

    public void publish() {
        this.publish(this.toJson());
    }

    public void publishAfterCommit() {
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    AbstractEvent.this.publish();
                }
            }
        );
    }

}
