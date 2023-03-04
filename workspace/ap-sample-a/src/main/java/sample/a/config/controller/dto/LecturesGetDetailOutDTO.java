package sample.a.config.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Schema(description = "강의상세내역조회 OutputDTO")
public class LecturesGetDetailOutDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "강의ID")
    private long    id;			

    @Schema(description = "강의명")
    private String  title;			
	@Schema(description = "최소수강인원")
    private Integer minEnrollment;  
	@Schema(description = "최대수강인원")
    private Integer maxEnrollment;  


}
