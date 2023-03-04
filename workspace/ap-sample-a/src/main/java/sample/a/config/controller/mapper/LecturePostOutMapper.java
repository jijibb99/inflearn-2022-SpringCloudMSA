package sample.a.config.controller.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sample.a.config.controller.dto.LecturesPostOutDTO;
import sample.a.domain.SampleA;


@Mapper(componentModel = "spring")
public interface LecturePostOutMapper extends EntityMapper<LecturesPostOutDTO, SampleA> {


    @Mapping(target = "version", ignore = true)
    SampleA toEntity(LecturesPostOutDTO lectureDTO);

    default SampleA fromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleA lecture = new SampleA();
        lecture.setId(id);
        return lecture;
    }
}
