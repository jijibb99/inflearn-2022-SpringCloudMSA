package sample.b.domain.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import sample.b.domain.entity.SampleBEntity;

@RepositoryRestResource(
        collectionResourceRel = "entity/sampleb",
        path = "entitySampleb"
)
public interface SampleARepository extends PagingAndSortingRepository<SampleBEntity, Long> {


}
