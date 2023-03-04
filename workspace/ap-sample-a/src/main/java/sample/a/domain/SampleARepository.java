package sample.a.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "entity/samplea",
    path = "entitySampleA"
)
public interface SampleARepository extends PagingAndSortingRepository<SampleA, Long> {


//	/**
//	 * 해당 강의분류 사용건수 조회
//	 * @param categoryId
//	 * @return
//	 */
//	@Query("SELECT COUNT(A) FROM Lecture A WHERE A.categoryId = :category_id")
//    long getCountOfCategoryId(@Param("category_id")long categoryId);	 
//
//
//	/**
//	 * 해당 강의분류 사용건수 조회
//	 * @param categoryId
//	 * @return
//	 */
//	@Query("SELECT COUNT(A) FROM Lecture A WHERE A.memberId = :member_id")
//    long getCountOfMemberId(@Param("member_id")String memberId);	
//	
//	/**
//	 * 특정 기간 강의 등록 건수 조회
//	 * @return
//	 */
//	@Query("SELECT COUNT(A) FROM Lecture A WHERE A.endterDt >= :fm_date and A.endterDt <= :to_date")
//    int getCountOfLecture(@Param("fm_date")Date fmEnterDate, @Param("to_date")Date toEnterDate);	
	
}
