# Lecture-Swagger


## 1. Open API 3.0 Swagger v3 상세설정

https://jeonyoungho.github.io/posts/Open-API-3.0-Swagger-v3-상세설정

1. api 그룹 설정: @Tag
   - Target: ANNOTATION_TYPE, METHOD, TYPE
     - name: 태그의 이름
     - description: 태그에 대한 설명
   - Tag에 설정된 name이 같은 것 끼리 하나의 api 그룹으로 묶는다. 주로 Controller나 Controller의 Method 영역에 설정한다.
    ```java
    @Tag(name = "leatures", description = "강의등록하기: 신규 강의를 개설하기 위하여 강의 요청내역을 입력")
    @RestController
    //@RequestMapping("/lectures")
    public class LectureController {

    ```
    - @Tag 어노테이션 설정 한 후 Swagger UI 화면이다. 설정한 태그명과 설명이 각 태그에 표시
2. api Schema 설정 : @Schema
   - Target : ANNOTATION_TYPE, FIELD, METHOD, PARAMETER, TYPE
     - description : 한글명
     - defaultValue : 기본값
     - allowableValues :
   - Schmea(= Model)에 대한 정보를 작성하는 곳
    ```java
    @Schema(description = "사용자")
    @Getter @Setter
    public class UserValue {

        @Pattern(regexp = "[0-2]")
        @Schema(description = "유형", defaultValue = "0", allowableValues = {"0", "1", "2"})
        private String type;

        @Email
        @Schema(description = "이메일", nullable = false, example = "abc@jiniworld.me")
        private String email;

        @Schema(description = "이름")
        private String name;

        @Pattern(regexp = "[1-2]")
        @Schema(description = "성별", defaultValue = "1", allowableValues = {"1", "2"})
        private String sex;

        @DateTimeFormat(pattern = "yyMMdd")
        @Schema(description = "생년월일", example = "yyMMdd", maxLength = 6)
        private String birthDate;

        @Schema(description = "전화번호")
        private String phoneNumber;

        @Schema(description = "비밀번호")
        private String password;
    }
    ```
   - Model 클래스에 대한 한글명을 설정하였고(첫번째 라인) 각 필드값에 대한 설정을 하였다.
     - description에 한글명을 작성하고, defaultValue를 통해 기본값을 제공
     - allowableValues를 설정하여 Schema 정보에서 리스트 형태로 들어갈 수 있는 데이터 정보

3. api 상세 정보 설정 : @Operation
   - Target: ANNOTATION_TYPE, METHOD
     - summary: api에 대한 간략한 설명
     - description: api에 대한 상세 설명
     - response: api Response 리스트
     - parameters: api 파라미터 리스트
    ```java
    public class LectureController {

        @Tag(name = "leatures")    //swagger용
        @GetMapping("/Leatures/{id}")
        @Operation(summary = "신규 강의 세부내역 조회", description = "\"강의 내역1건에 대하여 전체 내역을 조회한다\"",
        responses = {
                @ApiResponse(responseCode = "200", description = "회원 조회 성공",
                        content = @Content(schema = @Schema(implementation = LecturesGetDetailOutDTO.class))),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
                        content = @Content(schema = @Schema(implementation = BadRequestAlertException.class))) })
        public ResponseEntity<LecturesGetDetailOutDTO> getLeature(@PathVariable Long id) {

    ```
4. api response 설정 : @ApiResponse
   - Target: ANNOTATION_TYPE, METHOD, TYPE
     - responseCode: http 상태코드
     - description: response에 대한 설명
     - content: Response payload 구조
     - schema: payload에서 이용하는 Schema
     - hidden: Schema 숨김 여부
     - implementation: Schema 대상 클래스

5. api parameter 설정 : @Parameter
- Target: ANNOTATION_TYPE, FIELD, METHOD, PARAMETER
  - name: 파라미터 이름
  - description: 파라미터 설명
  - in: 파라미터 위치
    - query, header, path, cookie
    ```java
    @Operation(summary = "게시글 조회", description = "id를 이용하여 posts 레코드를 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = PostsResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/posts/{id}")
    public PostsResponseDto findById(@Parameter(name = "id", description = "posts 의 id", in = ParameterIn.PATH)
                                            @PathVariable Long id) {
        return postsService.findById(id);
    }
    ```

## 2. Swagger 연동
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

2. SwaggerConfig 작성
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


