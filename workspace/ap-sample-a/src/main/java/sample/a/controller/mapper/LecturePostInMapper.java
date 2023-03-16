package sample.a.controller.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sample.a.controller.vo.LecturesPostInDTO;
import sample.a.domain.entity.SampleAEntity;


@Mapper(componentModel = "spring")
public interface LecturePostInMapper extends EntityMapper<LecturesPostInDTO, SampleAEntity> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    SampleAEntity toEntity(LecturesPostInDTO lectureDTO);

    default SampleAEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleAEntity lecture = new SampleAEntity();
        lecture.setId(id);
        return lecture;
    }
}
