// ObjectMapper objectMapper = new ObjectMapper(); --> 이 부분 해결을 못해서 일단 주석 처리 
//package sample.a.mapper;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import sample.a.controller.mapper.EntityMapper;
//
//import javax.persistence.*;
//import javax.validation.constraints.Max;
//import javax.validation.constraints.Min;
//import javax.validation.constraints.Size;
//import java.io.Serializable;
//
//@Slf4j
//public class MapperTest {
//
//
//    @Test
//    @DisplayName("Vo --> Entity Mapping")
//    void aopHelloTest() {
//        log.debug("Vo --> Entity 기본 변환 테스트 ");
//        ObjectVO objectVO = new ObjectVO();
//        ObjectMapper objectMapper = new ObjectMapper();
//        EntitySample entitySample = objectMapper.toEntity(objectVO);
//
////        assertEquals("Myinno", result);
//    }
//
//
//    private static ObjectVO  makeObjectVO() {
//        ObjectVO objectVO = new ObjectVO();
//        objectVO.setTitle("제목");
//        objectVO.setLectureStatus("OPEN");
//        objectVO.setMaxEnrollment(20);
//        objectVO.setMinEnrollment(10);
//        return objectVO;
//    }
//
//    private static EntitySample  makeEntityVO() {
//        EntitySample entitySample = new EntitySample();
//
//        entitySample.setId(100L);
//        entitySample.setVersion(10L);
//        entitySample.setTitle("Entite제목");
//        entitySample.setMinEnrollment(0);
//        entitySample.setMaxEnrollment(100);
//        entitySample.setLectureStatus("lectureStatus");
//
//        return entitySample;
//    }
//
//    @Mapper(componentModel = "spring")
//    public interface ObjectMapper extends EntityMapper<ObjectVO, EntitySample> {
//
//
////    @Mapping(target = "status", ignore = true)
////    @Mapping(target = "version", ignore = true)
////    @Mapping(target = "categoryId", ignore = true)
////    @Mapping(target = "memberId", ignore = true)
////    @Mapping(source = "lectId", target = "id")
////    Lecture toEntity(LecturesGetOutDTO lectureDTO);
//
//        @Mapping(source="id", target="Id")
//        ObjectVO toDto(EntitySample entityList);
//
////        @Mapping(source="id", target="lectureId")
////        List<LecturesGetOutDTO> toDto(List<SampleAEntity> entityList);
//
////    default Lecture fromId(Long id) {
////        if (id == null) {
////            return null;
////        }
////        Lecture lecture = new Lecture();
////        lecture.setId(id);
////        return lecture;
////    }
//    }
//
//// DTO 객체    
//    @Schema(description = "강의 등록요청 Input DTO")
//    @Data
//    public static class ObjectVO implements Serializable {
//
//        private static final long serialVersionUID = 1L;
//
//        @Size(min = 3, max = 10, message = "강의명은 3~20자를 입력하셔야 합니다.")    // 길이(3~10) 체크
//        @Schema(description = "강의명")
//        private String title;
//
//        @Min(value = 5, message = "최소수강인원은 5명 이상이어야 합니다.")
//        @Schema(description = "최소수강인원", defaultValue = "")
//        private Integer minEnrollment;  //최소수강인원
//
//        @Max(value = 100, message = "최대 수강인원은 100명 이하이어야 합니다.")
//        @Schema(description = "최대수강인원")
//        private Integer maxEnrollment;  //
//
//        @Schema(description = "강의상태", allowableValues = {"OPEN_REGISTER"})
//        private String lectureStatus;  //강의상태
//
//    }
//
//    @Entity
//    @Data
//    public static class EntitySample {
//        @Id
//        @GeneratedValue(strategy = GenerationType.AUTO)
//        protected Long id;
//
//        @Version
//        protected Long version;
//        protected String title;
//        protected Integer minEnrollment;
//        protected Integer maxEnrollment;
//        protected String lectureStatus;
//    }
//
//}