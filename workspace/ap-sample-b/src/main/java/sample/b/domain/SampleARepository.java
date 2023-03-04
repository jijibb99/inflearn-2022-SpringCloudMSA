package sample.b.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        collectionResourceRel = "entity/sampleb",
        path = "entitySampleb"
)
public interface SampleARepository extends PagingAndSortingRepository<SampleB, Long> {


}
