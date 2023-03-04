package skmsa.apiutil.domain.mongorepository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import skmsa.apiutil.domain.FWK_IO_LOG;

@Repository
//@RepositoryRestResource(
//        collectionResourceRel = "entity/FWK_IO_LOG",
//        path = "FWK_IO_LOG"
//)
public interface FWK_IO_LOGRepository extends MongoRepository<FWK_IO_LOG, String> {

}
