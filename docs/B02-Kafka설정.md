clear<style>
.burk {
background-color: red;
color: yellow;
display:inline-block;
}
</style>


# B. Kafka 설정
Remote 서버에서 Docker로 서비스 하는 Kafka관련 접근하기
- To DO
  - kafka Connector 방식 정리(kafka로 하는 CDC)
  - 프로그램 로직으로 데이터 추가
## 1. kafka 명명 표준
1. 토픽
  - Format: MSA이름+'-'+[핵심엔티티]+행위(과거분사)
  - MSA이름, 핵심엔티티과 동일하면 생략
  - ex> category-CategoryChanged ==> categoryChanged
2. 컨슈머 그룹
  - Format: 'CGR-' + 소비하는 MSA + '-' + 토픽
  - ex> CGR-lecture-categoryChanged
4. channelName
  - spring.cloud.stream.bindings.<channelName>.<property>=<value>
  - Format: [out/in] + '-' + 토픽
  - out-categoryChanged

## 3. Producer(out) 구현하기
1. application.yaml 환경설정
  - spring.cloud.stream.binders 이하 부분
    - channel-name = out-categoryChanged
    - 토픽: categoryChanged
    ```yaml
    spring:
      profiles: docker
      cloud:
        stream:
          kafka:
            binder:
              brokers: kafka:9092
          bindings:
    #       event-in:
    #         group: lecturecategory
    #         destination: lecture
    #         contentType: application/json
            out-categoryChanged:  #producer의 이벤트 이름
              destination: categoryChanged  #토픽이름
              contentType: application/json

    ```
2. 설정값을 인스턴스에 바인딩
  - 바인딩 소스
     ```java
     public interface KafkaProcessor {
       //application.yaml에 정의한 이벤트 이름
         @Output("out-categoryChanged")
         MessageChannel outboundTopic();

     }
     ```
  
  - KafkaProcessor.java가 Binding을 하기 위해서 해당 소스를 Application.java에서 아래와 같이 추가 되어 있음
     ```java
     import com.everyoneslecture.lecturecategory.kafka.KafkaProcessor;

     @SpringBootApplication
     @EnableAspectJAutoProxy
     @EnableBinding(KafkaProcessor.class)   <== 이 부분
     public class LectureCategoryApplication {
     ````
3. Publising을 위한 기본 이벤트 처리 모듈
  - AbstractEvent.java
  - 별도 수정하지 않았으나, 시간 처리 부분의 Format만 변경
     ```java
     public class AbstractEvent {

       String eventType;
       String timestamp;   <== String으로 변경하고, 아래 Formatter적용

       public AbstractEvent(){
           this.setEventType(this.getClass().getSimpleName());
           SimpleDateFormat defaultSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
           this.timestamp = defaultSimpleDateFormat.format(new Date());
      //        this.timestamp = System.currentTimeMillis();
      }

     ```
4. 이벤트 Layout (DTO) 정의
  - getter/setter는 lombok 적용
    ```java
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.Setter;

    @Getter
    @Setter
    @AllArgsConstructor
    public class LectureCategoryChanged extends AbstractEvent {

      String	jobType; //INSERT, UPDATE, DELETE (작업구분)

      Long categoryId;
        String categoryName;

    }

    ```
5. 이벤트 생성
  - 관련 Entity 변경시에 이베트 등록
    ```java
    @Entity
    public class LectureCategory {

        @Id @GeneratedValue
        private Long categoryId;

        private String categoryName;


        @PostPersist
        public void onPostPersist(){
          LectureCategoryChanged lectureCategoryChanged = new LectureCategoryChanged
              ("INSERT", this.getCategoryId(), this.getCategoryName());
          lectureCategoryChanged.publishAfterCommit();
        }

        @PostUpdate
        public void onPostUpdate(){
          LectureCategoryChanged lectureCategoryChanged = new LectureCategoryChanged
              ("UPDATE", this.getCategoryId(), this.getCategoryName());
          lectureCategoryChanged.publishAfterCommit();
        }
    }
    ```

## 4. Consumer(IN) 구현하기
1. 환경설정(application.yaml)
  - application.yaml`
    ```yaml
    spring.profiles: docker

    server.port: 8080

    spring:
      cloud:
        stream:
          kafka:
            binder:
              brokers: kafka:9092
          bindings:
            in-categoryChanged:   #이벤트 이름 (consumer-MSA이름-토픽이름)
              destination: categoryChanged   #토픽이름
              contentType: application/json    #데이터형식
              group: CGR-lecture-categoryChanged  #consumer 그룹: CGR-MSA이름-토픽이름
    ```
2. binder 설정 인스턴스에 반영(producer와 동일 방법)
  - application.java(Main이 있는 클래스)에 Annotation추가
    ```java
    @SpringBootApplication(scanBasePackages={"everylecture"})
    @EnableBinding(KafkaProcessor.class)  //kafka관련 설정 Binder
    //@EnableFeignClients 이중으로 등록되어 있어 여기서는 제거
    @EnableAutoConfiguration
    ```
  - KafkaProcessor에서 Channel Name을 설정함
    ```java
    public interface KafkaProcessor {
        String EVENT_categoryChanged = "in-categoryChanged";

      //application.yaml에서 정의한 이벤트 그룹이름
      @Input(EVENT_categoryChanged)
        SubscribableChannel inboundTopic();
    }
    ```
3. 데이터 수신 처리 반영
-  여기 까지 했는데 어떻게 반영 프로그램이 호출될까?
```java
@Service
public class KafkaComsumerPolicyHandler {
    private final Logger log = LoggerFactory.getLogger(KafkaComsumerPolicyHandler.class);

	@Autowired
     CategoryRepository categoryRepository;

    @StreamListener(KafkaProcessor.IN_categoryChanged)
    public void whatever(@Payload String eventString){
    	log.debug(eventString);
    }

    @StreamListener(KafkaProcessor.IN_categoryChanged)
    public void wheneverPetReserved_displayOnTheStore(@Payload CategoryChanged categoryChanged){
    	log.debug(categoryChanged.toJson());

    	//임시로 누가 호출하는지 확인하기 위한 stacktrace 출력

    	try {
    		new Exception("테스트");
    	} catch (Exception e) {
    		log.debug("호출결로확인", e);
    	}
    	// if(!petReserved.validate())
        //     return;

        Category category = new Category();
        category.setCategoryId(categoryChanged.getCategoryId());
        category.setCategoryName(categoryChanged.getCategoryName());

        if ("INSERT".equals(categoryChanged.getJobType())) {
            categoryRepository.save(category);
        } else if ("UPDATE".equals(categoryChanged.getJobType())) {
            categoryRepository.save(category);
        } else if ("DELETE".equals(categoryChanged.getJobType())) {
            categoryRepository.delete(category);
        }
        log.debug(categoryChanged.toJson());
    }

```

## 5. Remote Docker설정 변경

1. Kafka Docker-Compose 설정
   - 외부에서 '4192'로 접근
   - KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1  <-- 이 부분이 없으면 '3'으로 기동되어 오류 발생했음

      ```yaml
      version: '2'
      
      services:
        zookeeper:
          container_name: zookeeper
          ...
      
        kafka_inno3t2:
          container_name: kafka
          image: confluentinc/cp-kafka:latest
          mem_limit: 500m
          depends_on:
            - zookeeper
          ports:
            - "4192:4192"
          environment:
            KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      
            KAFKA_LISTENERS: "EXTERNAL://:4192, INTERNAL://kafka:9092"
            KAFKA_ADVERTISED_LISTENERS: "EXTERNAL://10.250.141.146:4192, INTERNAL://kafka:9092"
            KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: "EXTERNAL:PLAINTEXT, INTERNAL:PLAINTEXT"
            KAFKA_INTER_BROKER_LISTENER_NAME: INTERNAL
      
            KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      
          volumes:
            - /var/run/docker.sock:/var/run/docker.sock
          networks:
            my-network:
              ipv4_address: 172.19.0.101      
      
      networks: 
        my-network:
          external: true
          name: ecommerce-network
      ```

2. 접근하는 Client(producer) 설정
   - Docker을 기동한 서버를 지정하고, 포트는 docker에서 포워딩한 포트로 설정
   - 테스트한 인스턴스 (SampleA)

      ```yaml
      spring:
        cloud:
          config:
            enabled=false:   #No spring.config.import property has been defined
          stream:
            kafka:
              binder:
                brokers: inno3t2
                defaultBrokerPort: 4192
      ```

## 6. 테스트한 내용

1. kafka 확인 내용
   - kafka docker의 terminal에서 확인
   - 'cd /usr/bin'
   - 포픽이름  확인

      ```shell
      Using username "msa2023".
      msa2023@10.250.141.146's password:
      Web console: https://master:9090/ or https://10.250.141.146:9090/
      
      Last login: Wed Mar  8 18:35:07 2023 from 10.250.141.157
      [msa2023@master ~]$ docker exec -it kafka bash
      [appuser@05a425d3d3d0 ~]$ kafka-topics --list --bootstrap-server 10.250.141.146:4192
      __consumer_offsets
      sampleAChanged
      [appuser@05a425d3d3d0 ~]$
      
      ```

   - 컨슈머 그룹 정보

      ```shell
      $ /usr/bin/kafka-consumer-groups --list --bootstrap-server 10.250.141.146:4192
      CGR-sampleb-sampleAChanged
      ```
   - 컨슈머 그룹 세부 정보

      ```shell
      $ /usr/bin/kafka-consumer-groups \
          --bootstrap-server 10.250.141.146:4192 \
          --group CGR-sampleb-sampleAChanged \
          --describe
      
      GROUP                      TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID                                                                HOST            CLIENT-ID
      CGR-sampleb-sampleAChanged sampleAChanged  0          1               1               0               consumer-CGR-sampleb-sampleAChanged-2-30d5fc48-7e38-40cd-8a38-04b26362c438 /10.250.141.157 consumer-CGR-sampleb-sampleAChanged-2
      ```
   - 특정 포픽 데이터 확인
      ```shell
      $ /bin/kafka-console-consumer \
          --bootstrap-server 10.250.141.146:4192  \
          --topic sampleAChanged \
          --from-beginning
      {"eventType":"SampleAChanged","timestamp":"2023-03-10 19:04:29","jobType":"INSERT","id":7,"version":0,"title":"강의01","minEnrollment":5,"maxEnrollment":10}
      {"eventType":"SampleAChanged","timestamp":"2023-03-10 19:13:38","jobType":"INSERT","id":8,"version":0,"title":"강의02","minEnrollment":6,"maxEnrollment":12}
      ```

2. 관련 프로그램
   - SampleA에서 강의를 등록하면 포픽 생성
   - SampleB에서  'CGR-sampleb-sampleAChanged'에서 작동

