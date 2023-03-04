package sample.b.domain;

import lombok.Data;
import sample.b.SampleBApplication;

import javax.persistence.*;

@Entity
@Table(name = "Sampleb_table")
@Data
public class SampleB {
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