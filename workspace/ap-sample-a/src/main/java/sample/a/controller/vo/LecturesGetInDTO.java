package sample.a.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Schema(description = "강의분류별 내역조회 OutputDTO[To DO강의 분류 여러개를 선택하자]")
public class LecturesGetInDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "강의ID")
    @NotNull(message = "강의분류ID를 입력해 주세요")        // NULL 체크
    private long categoryId;        //강의분류ID
}
