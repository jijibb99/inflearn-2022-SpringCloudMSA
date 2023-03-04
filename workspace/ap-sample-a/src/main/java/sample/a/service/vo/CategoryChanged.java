package sample.a.service.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sample.a.config.kafka.AbstractEvent;

@Getter
@Setter
@ToString
//@AllArgsConstructor
public class CategoryChanged extends AbstractEvent {
//    String jobType;

    private Long categoryId;

    private String categoryName;
}
