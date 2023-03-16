package sample.a.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sample.a.controller.vo.LecturesPostOutDTO;
import sample.a.domain.entity.SampleAEntity;


@Mapper(componentModel = "spring")
public interface LecturePostOutMapper extends EntityMapper<LecturesPostOutDTO, SampleAEntity> {


    @Mapping(target = "version", ignore = true)
    SampleAEntity toEntity(LecturesPostOutDTO lectureDTO);

    default SampleAEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleAEntity lecture = new SampleAEntity();
        lecture.setId(id);
        return lecture;
    }
}
