package sample.a.config.controller.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sample.a.config.controller.dto.LecturesGetOutDTO;
import sample.a.domain.SampleA;

import java.util.List;


@Mapper(componentModel = "spring")
public interface LectureGetOutMapper extends EntityMapper<LecturesGetOutDTO, SampleA> {


//    @Mapping(target = "status", ignore = true)
//    @Mapping(target = "version", ignore = true)
//    @Mapping(target = "categoryId", ignore = true)
//    @Mapping(target = "memberId", ignore = true)
//    @Mapping(source = "lectId", target = "id")
//    Lecture toEntity(LecturesGetOutDTO lectureDTO);

    @Mapping(source="id", target="lectureId")
    LecturesGetOutDTO toDto(SampleA entityList);

    @Mapping(source="id", target="lectureId")
    List <LecturesGetOutDTO> toDto(List<SampleA> entityList);
    
//    default Lecture fromId(Long id) {
//        if (id == null) {
//            return null;
//        }
//        Lecture lecture = new Lecture();
//        lecture.setId(id);
//        return lecture;
//    }
}
