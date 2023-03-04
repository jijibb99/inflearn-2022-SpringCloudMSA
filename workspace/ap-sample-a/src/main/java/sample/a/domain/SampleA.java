package sample.a.domain;

import lombok.Data;
import sample.a.SampleAApplication;
import sample.a.domain.vo.SampleAChanged;

import javax.persistence.*;

@Entity
@Table(name = "Samplea_table")
@Data
public class SampleA {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Version
    protected Long version;
    protected String title;
    protected Integer minEnrollment;
    protected Integer maxEnrollment;
    protected String lectureStatus;

    public static SampleARepository repository() {
        SampleARepository rectureRepository = SampleAApplication.applicationContext.getBean(SampleARepository.class);
        return rectureRepository;
    }
    
    /**
     * 강의 등록이력 Kafka 등록
     */
    @PostPersist
    public void onPostPersist(){
//        OnlineContext ctx = SampleAApplication.applicationContext.getBean(OnlineContext.class);

    	SampleAChanged sampleAChanged = new SampleAChanged("INSERT");
    	sampleAChanged.setId(id);
    	sampleAChanged.setVersion(version);
    	sampleAChanged.setTitle(title);
    	sampleAChanged.setMinEnrollment(minEnrollment);
    	sampleAChanged.setMaxEnrollment(maxEnrollment);
//        ctx.getLog().debug("onPostPersist: {}", sampleAChanged.toJson());

    	sampleAChanged.publishAfterCommit();
    }

    /**
     * 강의 수정이력 Kafka 등록
     */
    @PostUpdate
    public void onPostUpdate(){
//        OnlineContext ctx = SampleAApplication.applicationContext.getBean(OnlineContext.class);

    	SampleAChanged sampleAChanged = new SampleAChanged("UPDATE");
    	sampleAChanged.setId(id);
    	sampleAChanged.setVersion(version);
    	sampleAChanged.setTitle(title);
    	sampleAChanged.setMinEnrollment(minEnrollment);
    	sampleAChanged.setMaxEnrollment(maxEnrollment);

//        ctx.getLog().debug("onPostUpdate: {}", sampleAChanged.toJson());
    	sampleAChanged.publishAfterCommit();
    }

}