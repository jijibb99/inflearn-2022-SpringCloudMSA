clear<style>
.burk {
background-color: red;
color: yellow;
display:inline-block;
}
</style>


# B. Kafka Remote Docker
Remote 서버에서 Docker로 서비스 하는 Kafka관련 접근하기


## 1. 설정 변경

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

## 2. 테스트한 내용

1. kafka 확인 내용
   - kafka docker의 terminal에서 확인
   - 'cd /usr/bin'
   - 포픽이름  확인

      ```shell
      $ kafka-topics --list --bootstrap-server 10.250.141.146:4192
      __consumer_offsets
      sampleAChanged
      ```
   - 컨슈머 그룹 정보

      ```shell
      $ /usr/bin/kafka-consumer-groups --list --bootstrap-server 10.250.141.146:4192
      CGR-sampleb-sampleAChanged
      ```
   - 컨슈머 그룹 세부 정보

      ```shell
      $ /usr/bin/kafka-consumer-groups \
      >    --bootstrap-server 10.250.141.146:4192 \
      >    --group CGR-sampleb-sampleAChanged \
      >    --describe
      
      GROUP                      TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID                                                                HOST            CLIENT-ID
      CGR-sampleb-sampleAChanged sampleAChanged  0          1               1               0               consumer-CGR-sampleb-sampleAChanged-2-30d5fc48-7e38-40cd-8a38-04b26362c438 /10.250.141.157 consumer-CGR-sampleb-sampleAChanged-2
      ```
   - 특정 포픽 데이터 확인
      ```shell
      $ /bin/kafka-console-consumer \
      >    --bootstrap-server 10.250.141.146:4192  \
      >    --topic sampleAChanged \
      >    --from-beginning
      {"eventType":"SampleAChanged","timestamp":"2023-03-10 19:04:29","jobType":"INSERT","id":7,"version":0,"title":"강의01","minEnrollment":5,"maxEnrollment":10}
      {"eventType":"SampleAChanged","timestamp":"2023-03-10 19:13:38","jobType":"INSERT","id":8,"version":0,"title":"강의02","minEnrollment":6,"maxEnrollment":12}
      ```

2. 관련 프로그램
   - SampleA에서 강의를 등록하면 포픽 생성
   - SampleB에서  'CGR-sampleb-sampleAChanged'에서 작동

