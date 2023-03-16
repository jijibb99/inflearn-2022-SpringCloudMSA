package sample.a.domain.vo;

import lombok.Getter;
import lombok.Setter;
import sample.a.adaptor.kafka.AbstractEvent;

/**
 * 강의 변경 이력을 kafka에 등록
 * @author myinno
 */
@Getter
@Setter
public class SampleAChanged extends AbstractEvent {

	String	jobType; //INSERT, UPDATE, DELETE (작업구분)
	
    private Long id;
    private Long version;
    private String title;
    private Integer minEnrollment;
    private Integer maxEnrollment;

    public SampleAChanged(String	jobType) {
    	this.jobType = jobType;
    }
}