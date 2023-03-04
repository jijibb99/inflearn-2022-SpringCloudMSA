package sample.a.config;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import sample.a.domain.SampleA;

@Component
public class HateoasProcessor
    implements RepresentationModelProcessor<EntityModel<SampleA>> {

    @Override
    public EntityModel<SampleA> process(EntityModel<SampleA> model) {
        return model;
    }
}
