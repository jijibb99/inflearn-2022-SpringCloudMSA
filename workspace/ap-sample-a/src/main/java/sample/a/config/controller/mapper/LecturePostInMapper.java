package sample.a.config.controller.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sample.a.config.controller.dto.LecturesPostInDTO;
import sample.a.domain.SampleA;


@Mapper(componentModel = "spring")
public interface LecturePostInMapper extends EntityMapper<LecturesPostInDTO, SampleA> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    SampleA toEntity(LecturesPostInDTO lectureDTO);

    default SampleA fromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleA lecture = new SampleA();
        lecture.setId(id);
        return lecture;
    }
}
