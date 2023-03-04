# Lecture(강의)

## 1. Running in local development environment
1. run
    ```
    mvn spring-boot:run
    ```
2. Packaging and Running in docker environment
    ```
    mvn package -B
    docker build -t username/class:v1 .
    docker run username/class:v1
    ```
3. Push images and running in Kubernetes
    ```
    docker login 
    # in case of docker hub, enter your username and password

    docker push username/class:v1
    ```

4. Edit the deployment.yaml under the /kubernetes directory:
    ```
        spec:
        containers:
            - name: class
            image: username/class:latest   # change this image name
            ports:
                - containerPort: 8080

    ```

5. Apply the yaml to the Kubernetes:
    ```
    kubectl apply -f kubernetes/deployment.yml
    ```

6. See the pod status:
    ```
    kubectl get pods -l app=class
    ```

7. If you have no problem, you can connect to the service by opening a proxy between your local and the kubernetes by using this command:
    ```
    # new terminal
    kubectl port-forward deploy/lecture 8081:8081

    # another terminal
    http localhost:8080
    ```

8. If you have any problem on running the pod, you can find the reason by hitting this:
    ```
    kubectl logs -l app=lecture
    ```

Following problems may be occurred:

1. ImgPullBackOff:  Kubernetes failed to pull the image with the image name you've specified at the deployment.yaml. Please check your image name and ensure you have pushed the image properly.
2. CrashLoopBackOff: The spring application is not running properly. If you didn't provide the kafka installation on the kubernetes, the application may crash. Please install kafka firstly:

https://labs.msaez.io/#/courses/cna-full/full-course-cna/ops-utility

## 코드 테스트 방법

1. http로 테스트
   - pip install httpie
    ```
    http :8081/lectures title="MSA" minEnrollment=1 maxEnrollment=10 status="OPENED" categoryId[id]="1"
    -- 결과 조회
    http GET localhost:8081/lectures/1  

    http :8082/enrollments customerId="myinno" classId[id]="1" status="ENROLLED"

    http :8083/categories title="MSA_catalog"

    ```
    wsl 
    cd /tmp/docker-desktop-root/mnt/host/d/APP/git-amf3/backend#



## 2. 테이블 확인

### 처음 생성된 테이블
```bash
    create table class_table (
       id bigint not null,
        category_id_id bigint,
        max_enrollment integer,
        min_enrollment integer,
        status varchar(255),
        title varchar(255),
        primary key (id)
    )
```

  ```java
  @Entity
  @Table(name = "Class_table")
  @Data
  public class Clazz {

      @Id
      @GeneratedValue(strategy = GenerationType.AUTO)
      private Long id;

      private String title;

      private Integer minEnrollment;

      private Integer maxEnrollment;

      @Enumerated(EnumType.STRING)
      private Status status;

      @Embedded
      @AttributeOverride(
          name = "id",
          column = @Column(name = "categoryIdId", nullable = true)
      )
      private CategoryId categoryId;

  }
  ```
  ```java
    @Embeddable
    @Data
    public class CategoryId {
      private Long id;

    }
  ```
    
1. 변경 내용

	```java
	@Embeddable
	@Data
	public class CategoryId {

		private Long id;

		// 만약 필드가 2개 이상이면 테이블에 어찌 생성될까
		private String categoryName;
	}
	```
2. 변경후 테이블에
	```bash
	create table class_table (
	   id bigint not null,
		category_name varchar(255),
		category_id_id bigint,
		max_enrollment integer,
		min_enrollment integer,
		status varchar(255),
		title varchar(255),
		primary key (id)
	)
	```
3. 리스트로 변경후
	```java
	private CategoryId categoryId;
	<변경후>
	private List<CategoryId> categoryId;
	```
4. 결과(무시함)	
	```bash
	create table class_table (
	   id bigint not null,
		max_enrollment integer,
		min_enrollment integer,
		status varchar(255),
		title varchar(255),
		primary key (id)
	)
	```
5. String 필드 하나로 변경하고 List처리

	```java
	@Embeddable
	@Data
	public class CategoryId {
		private String categoryName;
	}
	```

6. 결과(무시함- 이전과 동일)	
	```bash
	create table class_table (
	   id bigint not null,
		max_enrollment integer,
		min_enrollment integer,
		status varchar(255),
		title varchar(255),
		primary key (id)
	)
	```
  
 ## 3. 주요 변경 내용
 1. class --> lecture  ==> 완료
 2. 강의 분류를 ReadMode

### 3.1 Kafka  연동 정리
1. 수정한 프로그램 목록
   - AbstractEvent
   - PolicyHandler
   - KafkaProcessor
   - application.yml은 
     ```
      group: lecture
      destination: lecture-category
     ```
2. 기능
   - category의 ReadModel  ( 변경 내역을 )      
3. 테스트한 내용
   - 로컬에서 수행중
   - "docker-compose exec -it kafka /bin/bash" 수행한 아무런 동작이 없어, docker-desktop의 'CLI' 명령어 입력
    ```
    sh-4.4$ ./kafka-console-producer --bootstrap-server localhost:29092 --topic lecture-category
    >{"jobType":"INSERT","id":"100","title":"MSA"}
    >{"jobType":"INSERT","id":"100","title":"MSA"}
    ```
4. 테스트 결과
    ```json
    PS D:\APP\GIT-AMF3\backend\kafka> http GET :8081/categories/1
    HTTP/1.1 200 
    Connection: keep-alive
    Content-Type: application/hal+json
    Date: Fri, 01 Jul 2022 07:28:40 GMT
    Keep-Alive: timeout=60
    Transfer-Encoding: chunked
    Vary: Origin
    Vary: Access-Control-Request-Method
    Vary: Access-Control-Request-Headers

    {
        "_links": {
            "category": {
                "href": "http://localhost:8081/categories/1"
            },
            "self": {
                "href": "http://localhost:8081/categories/1"
            }
        },
        "title": "MSA"
    }
    ```    
5. 'id'는 입력받은 대로 그대로 입력
    ```
    sh-4.4$ ./kafka-console-producer --bootstrap-server localhost:29092 --topic lecture-category
    >{"jobType":"INSERT","id":"100","title":"MSA"}
    >{"jobType":"INSERT","id":"100","title":"MSA"}
    >{"jobType":"UPDATE","id":"100","title":"MSA-수정"}
    ```
    ``` json
    PS D:\APP\GIT-AMF3\backend\kafka> http GET :8081/categories/100
    HTTP/1.1 200

    {
        "_links": {
            "category": {
                "href": "http://localhost:8081/categories/100"
            },
            "self": {
                "href": "http://localhost:8081/categories/100"
            }
        },
        "title": "MSA"
    }


    PS D:\APP\GIT-AMF3\backend\kafka> http GET :8081/categories/100
    {
        "_links": {
            "category": {
                "href": "http://localhost:8081/categories/100"
            },
        "title": "MSA-수정"
    }

    ```


### 3.2 Swagger 연동
- 참고한 싸이트: https://gaemi606.tistory.com/entry/Spring-Boot-Swagger2-%EC%82%AC%EC%9A%A9-springfox-boot-starter300

1. pom.xml 수정
   - 'springfox-swagger2', 'springfox-swagger-ui' 는 추가 하지 않음 
    ```xml
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-boot-starter</artifactId>
        <version>3.0.0</version>
    </dependency>
    ```

2. SwaggerCongig 작성
    ```java
    package lecturemgt.config.swagger;

    @Configuration
    public class SpringFoxConfig {
        @Bean
        public Docket api() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.any())
                    .paths(PathSelectors.any())
                    .build();
        }

        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("AMF-Level3 교육")
                    .version("1.0")
                    .description("AMF4차수- 모두의강의")
                    .license("AMF42조")
                    .build();
        }
    }
    ```

3. 테스트
   - http://localhost:8081/swagger-ui/index.html
   - (이전버전) http://localhost/swagger-ui.html

   - (Swagger 문서): https://springfox.github.io/springfox/docs/current 

### 3.3 mariadb 연동
```

```

```

```

```

```