package sample.b.config;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import sample.b.domain.SampleB;

@Component
public class HateoasProcessor
        implements RepresentationModelProcessor<EntityModel<SampleB>> {

    @Override
    public EntityModel<SampleB> process(EntityModel<SampleB> model) {
        return model;
    }
}
