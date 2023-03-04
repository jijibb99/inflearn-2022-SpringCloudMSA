package sample.a.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.a.domain.SampleA;
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
	public SampleA save(OnlineContext ctx, SampleA lecture) {
		setCtx(ctx);
		return null;
	}

	@Override
	public Page<SampleA> findAll(OnlineContext ctx, Pageable pageable) {
		setCtx(ctx);
		return null;
	}

	@Override
    @Transactional(readOnly = true)
	public Optional<SampleA> findOne(OnlineContext ctx, Long id) {
//		setCtx(ctx);
		log.debug("세부내역 조회-Service : id={}", id);
		Optional<SampleA> lecture = SampleA.repository().findById(id);
		return lecture;
	}
	// 강의 삭제
	@Override
	public void delete(OnlineContext ctx, Long id) {
//		setCtx(ctx);

		//강의상태가 등록 상태일떄만 삭제 가능(그 이후 상태에서는 삭제 불가능)
		Optional<SampleA> lecture = SampleA.repository().findById(id);
		if (!lecture.isPresent()) {
			log.debug("해당 자료는 이미삭제됨 id : {}", id);
		}

		
		SampleA.repository().deleteById(id);
	}
	
	@Override
    @Transactional	
	public SampleA registerLecture(OnlineContext ctx, SampleA lecture)
			throws InterruptedException, ExecutionException, JsonProcessingException {
//		setCtx(ctx);
		log.debug("registerLecture: 로그 테스트");

        return SampleA.repository().save(lecture);
	}

	@Override
	public List<SampleA> findByCategoryId(OnlineContext ctx) {
//		setCtx(ctx);

		Iterable<SampleA> lectureIterable =SampleA.repository().findAll();
		
		List<SampleA> lectureResult = StreamSupport
				  .stream(lectureIterable.spliterator(), false)
				  .collect(Collectors.toList());

		log.debug("조회 건수 : {}", lectureResult.size());
		return lectureResult;
	}

}
