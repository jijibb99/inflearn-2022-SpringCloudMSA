package sample.a.config.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sample.a.config.controller.dto.LecturesGetDetailOutDTO;
import sample.a.config.controller.dto.LecturesGetOutDTO;
import sample.a.config.controller.dto.LecturesPostInDTO;
import sample.a.config.controller.dto.LecturesPostOutDTO;
import sample.a.config.controller.mapper.LectureGetDetailOutMapper;
import sample.a.config.controller.mapper.LectureGetOutMapper;
import sample.a.config.controller.mapper.LecturePostInMapper;
import sample.a.config.controller.mapper.LecturePostOutMapper;
import sample.a.domain.SampleA;
import sample.a.service.SampleAService;
import skmsa.apiutil.controller.SKMSAController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * REST controller for managing
 */
@Tag(name = "sampleas", description = "MSA 관련 기본 기능 검증모음 (SampleA)")
@RestController
@RequestMapping("/main")
public class SampleAController extends SKMSAController {

    private final SampleAService lectureService;
    private final LecturePostInMapper lecturePostInMapper;
    private final LecturePostOutMapper lecturesPostOutMapper;
    private final LectureGetDetailOutMapper lectureGetDetailOutMapper;
    private final LectureGetOutMapper lectureGetOutMapper;


    /**
     * 생성자를 통한  객체주입
     */
    public SampleAController(
            SampleAService lectureService,
            LecturePostInMapper lecturePOSTInMapper,
            LecturePostOutMapper lecturePOSTOutMapper,
            LectureGetDetailOutMapper lectureGetDetailOutMapper,
            LectureGetOutMapper lectureGetOutMapper) {
        this.lectureService = lectureService;
        this.lecturePostInMapper = lecturePOSTInMapper;
        this.lecturesPostOutMapper = lecturePOSTOutMapper;
        this.lectureGetDetailOutMapper = lectureGetDetailOutMapper;
        this.lectureGetOutMapper = lectureGetOutMapper;
    }


    @Tag(name = "sampleas")    //swagger용
    @GetMapping(value = "/sampleas/{id}")
    @Operation(summary = "신규 강의 세부내역 조회", description = "\"강의 내역1건에 대하여 전체 내역을 조회한다\"",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 조회 성공(Jenkins빌드 점검1)",
                            content = @Content(schema = @Schema(implementation = LecturesGetDetailOutDTO.class)))})
    public ResponseEntity<LecturesGetDetailOutDTO> getLeature(@PathVariable Long id) {
//        setCtx("sample.a.getLeature");
        log().debug("getLeature  id: {}", id);
        Optional<SampleA> lecture = lectureService.findOne(getCtx(), id);
        if (!lecture.isPresent()) {
            log().debug("해당 자료없음 id: {}", id);
            return ResponseEntity.ok().body(null);
        }
        LecturesGetDetailOutDTO lectureGetDetailDTO = lectureGetDetailOutMapper.toDto(lecture.get());
        return ResponseEntity.ok().body(lectureGetDetailDTO);
    }


    @DeleteMapping("/sampleas/{id}")
    @Tag(name = "sampleas")    //swagger용
    @Operation(summary = "강의 내역 조회", description = "\"수강신청 이전의 강의 내역은 삭제 가능\"",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 삭제 성공")})
    public ResponseEntity<Void> deleteLeature(@PathVariable Long id) {
//        setCtx("sample.a.deleteLeature");
        lectureService.delete(getCtx(), id);
        return ResponseEntity.ok().body(null);
    }


    @Tag(name = "sampleas")
    @GetMapping(value = "/sampleas/category")
    @Operation(summary = "강의분류 별 조회", description = "강의 분류 별 내역 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 조회 성공",
                            content = @Content(schema = @Schema(implementation = LecturesGetOutDTO.class)))})
    public ResponseEntity<List<LecturesGetOutDTO>> getLeaturesByCategoryId() {
//        setCtx("sample.a.getLeaturesByCategoryId");
        List<SampleA> lectureList = lectureService.findByCategoryId(getCtx());
        if (lectureList.isEmpty()) {
            log().debug("해당 자료없음 id: {}", "");
            return ResponseEntity.ok().body(null);
        }

        List<LecturesGetOutDTO> lectureGetDTOList = lectureGetOutMapper.toDto(lectureList);

       return ResponseEntity.ok().body(lectureGetDTOList);
    }


    /**
     * 강의등록하기
     */
    @Tag(name = "sampleas")
    @PostMapping(value = "/sampleas")
    @Operation(summary = "신규 강의 신청(등록)",
            description = "강의 분류, 강의명, 최소 필요 수강인원등을 등록한다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "신규 강의 신청(등록)",
                            content = @Content(schema = @Schema(implementation = LecturesPostOutDTO.class)))})

//    public ResponseEntity<LecturesPostOutDTO> registerLecture(@RequestBody   LecturesPostInDTO lecturesPostInDTO)
    public ResponseEntity<LecturesPostOutDTO> registerLecture(@Valid @RequestBody LecturesPostInDTO lecturesPostInDTO, BindingResult bindingResult)
            throws InterruptedException, ExecutionException, JsonProcessingException {
        if (getCtx() == null) {
            log().error(" 시작 시점11111ctx  Null=======================");
        } else {
            log().debug("registerLecture: 입력데이터 정상00000");
        }

        //입력자료 기초 검증(Java Validation 결과 확인)
        if (bindingResult.hasErrors()) {
            log().error("오류건수: {}", bindingResult.getErrorCount());
            log().error(Arrays.toString(bindingResult.getAllErrors().toArray()));
            return ResponseEntity.unprocessableEntity().body(null);
        }

        Thread.sleep(1000);   // 동시 처리 확인을 위하여

        log().debug("registerLecture: 입력데이터 정상11111");
        SampleA sampleA = lecturePostInMapper.toEntity(lecturesPostInDTO);
        if (getCtx() == null) {
            log().error(" 시작 시점22222ctx  Null----------------------");
        } else {
            log().debug(" 시작 시점333333ctx  log OK----------------------");
        }
        SampleA returnLecture = lectureService.registerLecture(getCtx(), sampleA);
        LecturesPostOutDTO returnDto = lecturesPostOutMapper.toDto(returnLecture);
        log().debug("registerLecture: 처리 완료");

        return ResponseEntity.ok().body(returnDto);
    }

}
