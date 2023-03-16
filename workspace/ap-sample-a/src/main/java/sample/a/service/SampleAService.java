package sample.a.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sample.a.domain.entity.SampleAEntity;
import skmsa.apiutil.interceptor.OnlineContext;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


/**
 * Service Interface for managing {@link lecturemgt.domain.Rental}.
 */

public interface SampleAService {

    /**
     * Save a rental.
     *
     * @param rentalDTO the entity to save.
     * @return the persisted entity.
     */
	SampleAEntity save(OnlineContext ctx, SampleAEntity lecture);

    /**
     * Get all the Lecture.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SampleAEntity> findAll(OnlineContext ctx, Pageable pageable);


    /**
     * 특정 강의분류 전체 조회.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    List<SampleAEntity> findByCategoryId(OnlineContext ctx);

    
    /**
     * Get the "id" rental.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SampleAEntity> findOne(OnlineContext ctx, Long id);

    /**
     * Delete the "id" rental.
     *
     * @param id the id of the entity.
     */
    void delete(OnlineContext ctx, Long id);

    /**
     * Business Logic
     * 강의 신규 등록
     **/
    SampleAEntity registerLecture(OnlineContext ctx, SampleAEntity lecture) throws InterruptedException, ExecutionException, JsonProcessingException;
}
