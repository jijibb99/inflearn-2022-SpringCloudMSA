# A3. Biz Sample


## 1. 네트워크 Mapping

### 1.1 Sample A

1. Dockefile

    ```shell
    FROM openjdk:15-jdk-alpine
    #FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.6_10-slim
    COPY target/sample.a*.jar app.jar
    EXPOSE 8071 8071
    
    ENV TZ=Asia/Seoul
    RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
    
    ENV JAVA_OPTS="-Xmx512m"
    
    ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,address=*:8071,server=y,suspend=n","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=docker"]
    
    ```
2. Application.yaml

    ```yaml
    spring:
      profiles:
        group:
          "local": "local_profile,default_profile"
          "docker": "docker_profile,default_profile"
    ---
    #spring.profiles: default
    spring.config.activate.on-profile: default_profile
    
    server.servlet.context-path: /samplea
    
    #The default binder to use, if multiple binders are configured. See Multiple Binders on the Classpath.
    spring.cloud.stream.default-binder: kafka
    
    spring:
      application:
        name: samplea
      mvc:
        pathmatch:
          matching-strategy: ant_path_matcher     #Swagger 관련 수정
      jpa:
        properties:
          hibernate:
            show_sql: true
            format_sql: true
        generate-ddl: true
        hibernate.ddl-auto: update
      cloud:
        stream:
          bindings:
            # 강의 변경 내역 공유(kafka 변경 내용 등록)
            out-sampleAChanged:   #출력(Producer) 채널 (consumer-MSA이름-토픽이름)
              destination: sampleAChanged   #토픽이름
              contentType: application/json    #데이터형식
    
    hibernate.dialect: org.hibernate.dialect.MySQLInnoDBDialect
    
    #Prometheus 관련 Application정보 노출
    management.endpoint.metrics.enabled: true
    management.endpoint.prometheus.enabled: true
    management.metrics.export.prometheus.enabled: true
    management.endpoints.web.exposure.include: "*"
    
    #management.endpoints.web.exposure.include: prometheus,health,info,metric
    
    # anagement.endpoints.web.base-path 와 management.endpoints.web.path-mapping.<id>값을 수정하여, 특정 id 의 endpoint 의 경로를 수정할 수 있다.
    #management.endpoints.web.base-path: /monitor
    #management.endpoints.web.path-mapping.health: healthcheck
    
    #외부 도메인에서 actuator 정보를 요청을 허용할 수 있다.
    #management.endpoints.web.cors.allowed-origins: http://other-domain.com
    #management.endpoints.web.cors.allowed-methods: GET,POST
    
    ---
    #Local Exec
    spring.config.activate.on-profile: local_profile
    server.port: 8091
    
    spring:
      cloud:
        config:
          enabled=false:   #No spring.config.import property has been defined
        stream:
          kafka:
            binder:
              brokers: inno3t2
              defaultBrokerPort: 4192
      datasource:
        #    url: jdbc:mysql://mysql/samplea?useUnicode=true&serverTimezone=Asia/Seoul
        url: jdbc:mariadb://inno3t2:4121/samplea?useUnicode=true&serverTimezone=Asia/Seoul
        #   url: jdbc:mysql://vm-mysql13.mysql.database.azure.com:3306/samplea?useUnicode=true&serverTimezone=Asia/Seoul
        username: user
        password: pwd
        hikari.initializationFailTimeout: 30000
    eureka:
      instance:
        prefer-ip-address: true
      client:
        service-url:
          defaultZone: http://inno3t2:4116/eureka  # default addre [8761 -> 4116]
    
    
    # MONGODB (MongoProperties)[27017 - 4197]
    spring.data.mongodb.uri: mongodb://inno3t2:4197/SKMSA
    
    #Zipkin
    #spring.zipkin.base-url: http://inno3t2:4126/    #[9411 -> 4126]
    #logging.pattern.console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%X{X-B3-TraceId},%X{X-B3-SpanId},%X{X-B3-ParentSpanId},%X{X-Span-Export}] [%thread] %-5level %logger{36} - %msg%n"
    
    ---
    #Docker
    spring.config.activate.on-profile: docker_profile
    server.port: 0
    #
    spring:
      cloud:
        stream:
          kafka:
            binder:
              brokers: kafka:9092
      datasource:
        url: jdbc:mariadb://mariadb:3306/samplea?useUnicode=true&serverTimezone=Asia/Seoul    #[ [3306 -> 4121]
        #   url: jdbc:mysql://vm-mysql13.mysql.database.azure.com:3306/samplea?useUnicode=true&serverTimezone=Asia/Seoul
        username: user
        password: pwd
        hikari.initializationFailTimeout: 30000
    eureka:
      instance:
        prefer-ip-address: true
      client:
        service-url:
          defaultZone: http://apigate-service:8761/eureka  # default addre [8761 -> 4116]
    
    
    # MONGODB (MongoProperties)
    spring.data.mongodb.uri: mongodb://mongo:27017/SKMSA      #[27017 - 4197]
    
    #Zipkin
    spring.zipkin.base-url: http://zipkin:9411/    #[9411 -> 4126]
    logging.pattern.console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%X{X-B3-TraceId},%X{X-B3-SpanId},%X{X-B3-ParentSpanId},%X{X-Span-Export}] [%thread] %-5level %logger{36} - %msg%n"
    
    ```

3. Local Run(Intellij에서 직접 수행)
   - local, debug 로 수행
   - SampleAApplication.java --> main
4. docker Build

   ```shell
   $ cd /home/msa2023/inflearn-2022-SpringCloudMSA/workspace/ap-sample-a
   $ mvn clean package -DskipTests
   $ docker build -t msa2023/ap-sample-a:1.0 . 
   $ docker push msa2023/ap-sample-a:1.0
   ```
5. run

```yaml
version: '2'
services:
  ap-sample-a:
    container_name: ap-sample-a
    image: msa2023/ap-sample-a:1.0
    environment:
      - spring.profiles.active=local
    networks:
      my-network:

```

   ```shell
   $ docker-compose -f docker-compose-ap-sample-a.yml up -d
   ```

### 1.2 Sample B
A 참조

### 1.3 Kafka 설정
Kafka를 Docker에서 수행하고, 개별서비스는 로컬 또는 docker로 수행하기 위해 변경한 내용
- EXTERNAL, INTERNAL로 분리
- EXTERNAL이 로컬 서버에서 docker의 kafka로 접속하는 환경

    ```yaml
      kafka_inno3t2:
        container_name: kafka
        image: confluentinc/cp-kafka:latest
        mem_limit: 500m
        depends_on:
          - zookeeper
        ports:
          - "4192:4192"
    #      - "4193:9092"
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
    ``` 

## 2. 테스트

### 2.1 Sample- 강의등록
SWAGGER URL: http://127.0.0.1:8091/samplea/swagger-ui/index.html
 
1. 강의 등록
   - Controller 메소드:  SampleAController.registerLecture()
   - URL:  (POST) /samplea/main/sampleas
   - 신규 강의 신청(등록)
   - 'Samplea_table'에 강의 등록하고, 강의 내용은 Kafka 을 통하여 SampleB 로 전달  

      ```java
      @Tag(name = "sampleas", description = "MSA 관련 기본 기능 검증모음 (SampleA)")
      @RestController
      @RequestMapping("/main")
      public class SampleAController extends SKMSAController {
          
          @Tag(name = "sampleas")
          @PostMapping(value = "/sampleas")
          @Operation(summary = "신규 강의 신청(등록)",
                  description = "강의 분류, 강의명, 최소 필요 수강인원등을 등록한다",
                  responses = {
                          @ApiResponse(responseCode = "200", description = "신규 강의 신청(등록)",
                                  content = @Content(schema = @Schema(implementation = LecturesPostOutDTO.class)))})
      
      //    public ResponseEntity<LecturesPostOutDTO> registerLecture(@RequestBody   LecturesPostInDTO lecturesPostInDTO)
          public ResponseEntity<LecturesPostOutDTO> registerLecture(@Valid @RequestBody LecturesPostInDTO lecturesPostInDTO, BindingResult bindingResult)
      ```

2. kafka 관련 설정
   - Kafka 관련 설정: Application.yaml 확인  (생성 관련-  ap-sample-a)
      ```yaml
      spring:
        cloud:
          stream:
            kafka:
              binder:
                brokers: inno3t2
                defaultBrokerPort: 4192
            bindings:
                 # 강의 변경 내역 공유(kafka 변경 내용 등록)
               out-sampleAChanged:   #출력(Producer) 채널 (consumer-MSA이름-토픽이름)
                  destination: sampleAChanged   #토픽이름
                  contentType: application/json    #데이터형식
      ```
   - Kafka 관련 설정: Application.yaml 확인  (consumer 관련-  ap-sample-b)
      ```yaml
      spring:
        cloud:
          stream:
            kafka:
              binder:
                brokers: inno3t2
                defaultBrokerPort: 4192
            bindings:
              in-sampleAChanged: #입력(Consumer) 채널 (in-토픽이름)
                destination: sampleAChanged   #토픽이름
                contentType: application/json    #데이터형식
                group: CGR-sampleb-sampleAChanged  #consumer 그룹: CGR-MSA이름-토픽이름
      ```
   - SampleA.java(): @Entity의   @PostPersist에서 Kafka 등록 로직 추가

### 2.1 Sample- 강의등록


3. 1


```shell

```

4. 1


```shell

```

5. 1


```shell

```


