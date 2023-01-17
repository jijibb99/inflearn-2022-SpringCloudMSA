<style>
.burk {
    background-color: red;
    color: yellow;
    display:inline-block;
}
</style>

# 01. Service Discovery

- Spring Cloud Netflix Eureka
- Eureka Service Discovery – 프로젝트 생성
- User Service – 프로젝트 생성
- User Service – 등록
- User Service – Load Balancer

## 1. Spring Cloud Netflix Eureka
Service Discovery
- 전화 번호부 (서비스가 어디에 있나)
- Eureka
- ![](images/01-1-Concept.png)

## 2. Eureka Service Discovery – 프로젝트 생성
community version에서는  Spring Initializer 사용 불가
- Spring IO를 사용해서 생성하자 (https://start.spring.io/)
- Spring Boot 버전은 2.7.7 이상만 가능 (생성 이후 변경 예정)

1. 프로젝트 생성 정보
   - Spring Initializr
     - Group: com.example
     - Artifact: ecoomerce
     - Type : Maven
     - java: 11
     - Version: 0.0.1-SNAPSHOT
     - Package: com.example.ecoomerce
   - Dependencies
     - Spring Boot > 2.4.1
     - Spring Cloud Discovery > Eureka Server

2. application.yml
    ```yaml
    server:
      port: 8761
    
    spring:
      application:
        name: discoveryservice
    
    eureka:
      client:
        register-with-eureka: false
        fetch-registry: false
    ```

3. 소스 수정
   - main java 파일에 annotation 추가
     - @EnableEurekaServer

4. 실행
   - http://localhost:8761/


## 3. User Service – 프로젝트 생성
## 4. User Service – 등록
## 5. User Service – Load Balancer

```shell
User Service
§ Eureka Discovery Service에 등록
User Service
§ pom.xml
User Service
§ application.yml
User Service
§ application.yml
User Service
§ Scaling – 같은 서비스 추가 실행 #1
User Service
§ Scaling – 같은 서비스 추가 실행 #1
- Port 충돌
User Service
§ Scaling – 같은 서비스 추가 실행 #1
- VM Options à -Dserver.port=[다른포트]
User Service
§ Scaling - 같은 서비스 추가 실행 #2
$ mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dserver.port=9003'
§ Scaling - 같은 서비스 추가 실행 #3
$ mvn clean compile package
$ java -jar -Dserver.port=9004 ./target/user-service-0.0.1-SNAPSHOT.jar
§ Scaling – 같은 서비스 추가 실행 #1
User Service
§ Scaling – 같은 서비스 추가 실행
User Service
§ Scaling – Random 포트 사용으로 같은 서비스 추가 실행
- application.yml
$ mvn spring-boot:run
User Service
§ Scaling – Random 포트 사용으로 같은 서비스 추가 실행
- 같은 서비스 명으로 인해 Eureka에 하나의 앱만 등록
User Service
§ Scaling – Random 포트 사용으로 같은 서비스 추가 실행
- application.yml에 instance 정보 등록
- eureka.instance.instanceId
User Service
§ Scaling – Random 포트 사용으로 같은 서비스 추가 실행
- application.yml에 instance 정보 등록
- eureka.instance.instanceId
```