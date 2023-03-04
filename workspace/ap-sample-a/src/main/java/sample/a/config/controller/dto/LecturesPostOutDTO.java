package sample.a.config.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@ToString
@Schema(description = "강의 등록 처리결과 OutputDTO")
public class LecturesPostOutDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "강의ID")
    private long    id;			

	@Schema(description = "강의 Version")
    private long    version;			

	@Schema(description = "강의명")
    private String  title;			

    @Schema(description = "최소수강인원")
    private Integer minEnrollment;  
	@Schema(description = "최대수강인원")
    private Integer maxEnrollment;  

    private Date 	registerEndDt;  
	@Schema(description = "강의상태")
    private String 	lectureStatus;  

}
