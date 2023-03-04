# A3. Biz Sample


## 1. 네트워크 Mapping

### 1.1 Sample A

1. Dockefile

    ```shell
    FROM openjdk:15-jdk-alpine
    #FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.6_10-slim
    COPY target/sample.a*.jar app.jar
    EXPOSE 8080 8071
    
    ENV TZ=Asia/Seoul
    RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
    
    ENV JAVA_OPTS="-Xmx512m"
    
    ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,address=*:8071,server=y,suspend=n","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=docker"]
    
    ```
5. Application.yaml

```yaml
spring:
  profiles:
    group:
      "docker": "docker_profile,default_profile"
      "local": "local_profile,default_profile"
---
#spring.profiles: default
spring.config.activate.on-profile: default_profile

server.servlet.context-path: /samplea
spring:
  application:
    name: samplea
  mvc:
    pathmatch:
      matching-strategy: ANT_PATH_MATCHER     #Swagger 관련 수정
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
server.port: 8080

spring:
  cloud:
    stream:
      kafka:
        binder:
#          brokers: localhost:9092
          brokers: inno3t2:4192
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
spring.zipkin.base-url: http://inno3t2:4126/    #[9411 -> 4126]
logging.pattern.console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%X{X-B3-TraceId},%X{X-B3-SpanId},%X{X-B3-ParentSpanId},%X{X-Span-Export}] [%thread] %-5level %logger{36} - %msg%n"

---
#Docker
spring.config.activate.on-profile: docker_profile
server.port: 0

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
spring.data.mongodb.uri: mongodb://Mongodb:27017/SKMSA      #[27017 - 4197]

#Zipkin
spring.zipkin.base-url: http://zipkin:9411/    #[9411 -> 4126]
logging.pattern.console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%X{X-B3-TraceId},%X{X-B3-SpanId},%X{X-B3-ParentSpanId},%X{X-Span-Export}] [%thread] %-5level %logger{36} - %msg%n"

```


3. docker Build

   ```shell
   $ cd /home/msa2023/inflearn-2022-SpringCloudMSA/workspace/ap-sample-a
   $ mvn clean package -DskipTests
   $ docker build -t msa2023/ap-sample-a:1.0 . 
   $ docker push msa2023/ap-sample-a:1.0
   ```
3. run

   ```shell
   $ docker-compose -f docker-compose-discovery.yml up -d
   ```

### 2.4 API Gateway

1. DockerFile

   #8051 port는 Remote Debug용으로 추가함 (추가시 space로 구분함)
   EXPOSE 8051
   - 일반용 DockerFile
      ```shell
      FROM openjdk:17-ea-11-jdk-slim
      VOLUME /tmp
      COPY target/apigateway-service-1.0.jar ApigatewayService.jar
      ENTRYPOINT ["java", "-jar", "ApigatewayService.jar"]
      ```

   - Debug용 DockerFileDebug
      ```shell
      FROM openjdk:17-ea-11-jdk-slim
      VOLUME /tmp
      COPY target/apigateway-service-1.0.jar ApigatewayService.jar
      ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,address=*:8051,server=y,suspend=n","-jar","ApigatewayService.jar"]
      EXPOSE 8051
      ```
2. application.yaml 
   ```yaml
   spring:
      profiles:
         group:
            "docker": "docker_profile, default_profie"
            "local": "local_profile, default_profie"
   ---
   spring.config.activate.on-profile: default_profie
   
   server:
      port: 8000
   spring:
      application:
         name: apigateway-service
      cloud:
         gateway:
            default-filters:
               - name: GlobalFilter
                 args:
                    baseMessage: Spring Cloud BaseMessage GlobalFilter
                    preLogger: true
                    postLogger: true
            routes:
               - id: user-service
                 uri: lb://USER-SERVICE
                 predicates:
                    - Path=/user-service/login
                    - Method=POST
                 filters:
                    - RemoveRequestHeader=Cookie
                    - RewritePath=/user-service/(?<segment>.*), /$\{segment}
               ....
   ---
   #Local Exec
   spring.config.activate.on-profile: local_profile
   spring:
      rabbitmq:
         host: 127.0.0.1
   eureka:
      client:
         service-url:
            defaultZone: http://localhost:8761/eureka
   ---
   #Docker
   spring.config.activate.on-profile: docker_profile
   
   spring:
      rabbitmq:
         host: rabbitmq
   eureka:
      client:
         service-url:
            defaultZone: http://discovery-service:8761/eureka/
   ```
3. docker build

   ```shell
   $ mvn clean package -DskipTests
   #일반용
   $ docker build -t msa2023/apigateway-service:1.0 .     
   $ docker push msa2023/apigateway-service:1.0
   

   # Debug용
   $ docker build -t msa2023/apigateway-service:1.0  -f DockerfileDebug .   
   $ docker push msa2023/apigateway-service:1.0
   ```
5. docker-compose
   - Run: docker-compose -f docker-compose-apigateway.yml up   
      ```yaml
      version: '2'
      services:
        apigateway-service:
          container_name: apigateway-service
          image: msa2023/apigateway-service:1.0
          ports:
            - "4112:8000"
          environment:
            - spring.profiles.active=docker
      ```
