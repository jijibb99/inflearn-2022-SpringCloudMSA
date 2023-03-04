package sample.a.config.controller.mapper;


import org.mapstruct.Mapper;
import sample.a.config.controller.dto.LecturesGetDetailOutDTO;
import sample.a.domain.SampleA;


@Mapper(componentModel = "spring", uses = {})
public interface LectureGetDetailOutMapper extends EntityMapper<LecturesGetDetailOutDTO, SampleA> {


    SampleA toEntity(LecturesGetDetailOutDTO lectureDTO);

    default SampleA fromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleA lecture = new SampleA();
        lecture.setId(id);
        return lecture;
    }
}
