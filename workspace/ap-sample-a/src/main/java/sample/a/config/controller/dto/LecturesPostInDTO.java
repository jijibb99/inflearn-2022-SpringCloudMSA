package sample.a.config.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@ToString

@Schema(description = "강의 등록요청 Input DTO")
public class LecturesPostInDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Size(min=3, max=10, message="강의명은 3~20자를 입력하셔야 합니다.")	// 길이(3~10) 체크
	@Schema(description = "강의명")
	private String  title;			

    @Min(value = 5, message = "최소수강인원은 5명 이상이어야 합니다.")
	@Schema(description = "최소수강인원", defaultValue = "")
    private Integer minEnrollment;  //최소수강인원

    @Max(value = 100, message = "최대 수강인원은 100명 이하이어야 합니다.")
    @Schema(description = "최대수강인원")
    private Integer maxEnrollment;  //

    @Schema(description = "강의상태", allowableValues = {"OPEN_REGISTER"})
    private String 	lectureStatus;  //강의상태

}
