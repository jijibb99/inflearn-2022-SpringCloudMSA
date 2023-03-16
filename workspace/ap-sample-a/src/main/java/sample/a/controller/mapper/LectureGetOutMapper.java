package sample.a.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sample.a.controller.vo.LecturesGetOutDTO;
import sample.a.domain.entity.SampleAEntity;

import java.util.List;


@Mapper(componentModel = "spring")
public interface LectureGetOutMapper extends EntityMapper<LecturesGetOutDTO, SampleAEntity> {


//    @Mapping(target = "status", ignore = true)
//    @Mapping(target = "version", ignore = true)
//    @Mapping(target = "categoryId", ignore = true)
//    @Mapping(target = "memberId", ignore = true)
//    @Mapping(source = "lectId", target = "id")
//    Lecture toEntity(LecturesGetOutDTO lectureDTO);

    @Mapping(source="id", target="lectureId")
    LecturesGetOutDTO toDto(SampleAEntity entityList);

    @Mapping(source="id", target="lectureId")
    List <LecturesGetOutDTO> toDto(List<SampleAEntity> entityList);
    
//    default Lecture fromId(Long id) {
//        if (id == null) {
//            return null;
//        }
//        Lecture lecture = new Lecture();
//        lecture.setId(id);
//        return lecture;
//    }
}
