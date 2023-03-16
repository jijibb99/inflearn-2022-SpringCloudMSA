package sample.a.controller.mapper;


import org.mapstruct.Mapper;
import sample.a.controller.vo.LecturesGetDetailOutDTO;
import sample.a.domain.entity.SampleAEntity;


@Mapper(componentModel = "spring", uses = {})
public interface LectureGetDetailOutMapper extends EntityMapper<LecturesGetDetailOutDTO, SampleAEntity> {


    SampleAEntity toEntity(LecturesGetDetailOutDTO lectureDTO);

    default SampleAEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleAEntity lecture = new SampleAEntity();
        lecture.setId(id);
        return lecture;
    }
}
