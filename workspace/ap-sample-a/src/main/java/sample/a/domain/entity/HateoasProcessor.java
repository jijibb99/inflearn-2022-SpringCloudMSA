package sample.a.domain.entity;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

/**
 * 테이블 CRUD용 - swagger
 * 계속 유지할까 여부 확인 필요
 */
@Component
public class HateoasProcessor
    implements RepresentationModelProcessor<EntityModel<SampleAEntity>> {

    @Override
    public EntityModel<SampleAEntity> process(EntityModel<SampleAEntity> model) {
        return model;
    }
}
