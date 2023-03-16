package sample.a.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.a.domain.entity.SampleAEntity;
import sample.a.service.SampleAService;
import skmsa.apiutil.interceptor.OnlineContext;
import skmsa.apiutil.service.SKMSAService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class SampleAServiceImpl extends SKMSAService implements SampleAService {

	@Override
	public SampleAEntity save(OnlineContext ctx, SampleAEntity lecture) {
		setCtx(ctx);
		return null;
	}

	@Override
	public Page<SampleAEntity> findAll(OnlineContext ctx, Pageable pageable) {
		setCtx(ctx);
		return null;
	}

	@Override
    @Transactional(readOnly = true)
	public Optional<SampleAEntity> findOne(OnlineContext ctx, Long id) {
//		setCtx(ctx);
		log.debug("세부내역 조회-Service : id={}", id);
		Optional<SampleAEntity> lecture = SampleAEntity.repository().findById(id);
		return lecture;
	}
	// 강의 삭제
	@Override
	public void delete(OnlineContext ctx, Long id) {
//		setCtx(ctx);

		//강의상태가 등록 상태일떄만 삭제 가능(그 이후 상태에서는 삭제 불가능)
		Optional<SampleAEntity> lecture = SampleAEntity.repository().findById(id);
		if (!lecture.isPresent()) {
			log.debug("해당 자료는 이미삭제됨 id : {}", id);
		}

		
		SampleAEntity.repository().deleteById(id);
	}
	
	@Override
    @Transactional	
	public SampleAEntity registerLecture(OnlineContext ctx, SampleAEntity lecture)
			throws InterruptedException, ExecutionException, JsonProcessingException {
//		setCtx(ctx);
		log.debug("registerLecture: 로그 테스트");

        return SampleAEntity.repository().save(lecture);
	}

	@Override
	public List<SampleAEntity> findByCategoryId(OnlineContext ctx) {
//		setCtx(ctx);

		Iterable<SampleAEntity> lectureIterable = SampleAEntity.repository().findAll();
		
		List<SampleAEntity> lectureResult = StreamSupport
				  .stream(lectureIterable.spliterator(), false)
				  .collect(Collectors.toList());

		log.debug("조회 건수 : {}", lectureResult.size());
		return lectureResult;
	}

}
