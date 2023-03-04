package sample.b.config.kafka.dto;

import lombok.Getter;
import lombok.Setter;
import skmsa.apiutil.kafka.AbstractKafkaEvent;


/**
 * 강의 변경 이력을 kafka에 등록
 *
 * @author myinno
 */
@Getter
@Setter
public class SampleAChanged extends AbstractKafkaEvent {

    String jobType; //INSERT, UPDATE, DELETE (작업구분)

    private Long id;
    private Long version;
    private String title;


}