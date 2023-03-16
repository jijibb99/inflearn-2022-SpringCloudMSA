package sample.b.domain.entity;

import lombok.Data;
import sample.b.SampleBApplication;
import sample.b.domain.repository.SampleARepository;

import javax.persistence.*;

@Entity
@Table(name = "Sampleb_table")
@Data
public class SampleBEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Version
    protected Long version;
    protected String title;
    protected String lectureStatus;

    public static SampleARepository repository() {
        SampleARepository rectureRepository = SampleBApplication.applicationContext.getBean(SampleARepository.class);
        return rectureRepository;
    }

}