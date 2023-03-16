package sample.b.adaptor;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import sample.b.domain.entity.SampleBEntity;

@Component
public class HateoasProcessor
        implements RepresentationModelProcessor<EntityModel<SampleBEntity>> {

    @Override
    public EntityModel<SampleBEntity> process(EntityModel<SampleBEntity> model) {
        return model;
    }
}
