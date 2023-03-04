<style>
.burk {
    background-color: red;
    color: yellow;
    display:inline-block;
}
</style>


# B. Multi구성

2021. 2. 24. 19:50
- https://multifrontgarden.tistory.com/277


## 1.  애플리케이션 설정 파일 아키 변경

spring boot 2.4 버전부터(application.yml) 에 대한 구동방식이 변경됐다. 
- 변경된 이유는 k8s 볼륨 마운트 구성때문이라고 함


### 1.1 설정이 단순한 경우

그대로 기동
- 설정파일을 멀티 모듈로 구성하거나 profile 별로 분리해놓지 않았다면 크게 신경 쓸 필요 없이 그대로 구동하면 된다.

### 1.2 설정이 분리되어 있는 경우

애플리케이션 구동시 아래와 같은 워닝 로그가 찍힌다면 마이그레이션이 필요하다.
- 2023-02-28 발생
  ```shell
  WARN [org.springframework.boot.context.config.ConfigDataEnvironment:logTo:258] Property 'spring.profiles' imported from location 'class path resource [application.yml]' is invalid and should be replaced with 'spring.config.activate.on-profile' [origin: class path resource [application.yml] - 2:18]
  ```

1. 레거시 구동방식 유지

    ```shell
    spring.config.use-legacy-processing=true
    ```
2. 마이그레이션
   - '---' 구분자를 사용해서 profile 별 설정을 분리해놓았다면 아래처럼 해당 profile 을 명시했을 것이다.

        ```yaml
        spring:
          profiles: "profile"
        ```

   - 'spring.profile' ==>  'spring.config.activate.on-profile' 로 변경

      ```yaml
      spring:
        config:
          activate:
            on-profile: "profile"
      ```

3. profile 별로 설정 파일을 작성했고, 추가적인 profile 을 주입했다면 이런식으로 설정을 작성했을 것이다.

    ```yaml
    spring:
      config:
        activate:
          on-profile: "profile"
      profiles:
        include: "extra1,extra2"
    ```
    - 하지만 위 설정은 작동하지 않는다. 
    - 2.4 주요 변경사항으로 include 는 특정 profile 이 적용된 곳에서는 사용할 수 없다. 
    - 때문에 <span class=burk>on-profile 과 include 가 공존할 수 없다</span>

    - 변경 방법

        ```yaml
        spring:
          profiles:
            include: "extra"
        ---
        spring:
          config:
            activate:
              on-profile: "profile"
        ```
4. profile 별로 include를 다르게 적용하는 방법 - spring.profiles.group
   - 위와 같은 구성에서는 on-profile 과 무관하게 include 가 되게되는데, 
   - profile 별로 include 가 달라야하는 필요성이 있었다. 
   - 'spring.profiles.group'(2.4 에 추가된 스펙) 속성을 사용해 문제를 해결

    ```yaml
    spring:
      profiles:
        group:
         "profile1": "extra1,extra2"
         "profile2": "extra3,extra4"
    ---
    spring:
      config:
        activate:
          on-profile: "profile1"
    ---
    spring:
      config:
        activate:
          on-profile: "profile2"
    ---
    spring:
      config:
        activate:
          on-profile: "extra1"
    ---
    spring:
      config:
        activate:
          on-profile: "extra3"
    ```
    - spring.profiles.group 속성은 include 와 마찬가지로 spring.config.activate.on-profile 이 있는 상태에서는 사용할 수 없다.

    


## 2. User-Service에 적용한 내용

1. 설정 내용
   - bootstrap에는 default가 없고, application에만 있는 경우 ==> default가 적용되지 않음 
   - bootstrap.yaml
      ```yaml
      spring:
        profiles:
          group:
            "local": "local_profile"
            "docker": "docker_profile"
      ---
      #Local Exec
      spring.config.activate.on-profile: local_profile
      ...
      ---
      #docker
      spring.config.activate.on-profile: docker_profile
      ...
      ```
   - application.yaml

      ```yaml
      spring:
        profiles:
          group:
            "local": "local_profile, default_profile"
            "docker": "docker_profile, default_profile"
      ---
      #ALL
      spring.config.activate.on-profile: default_profile
      ...
      ---
      #Local Exec
      spring.config.activate.on-profile: local_profile
      ...
      ---
      #docker
      spring.config.activate.on-profile: docker_profile
      ...
      ```
2. 결과
   - "spring.profiles.active=docker"를 지정한 경우==> docker, docker_profile만 적용됨
   - application에 "default_profile" 가 적용안됨
3. 수정한 방법
   - 2023-03-01 (일단 해결은 되었으나 ....)
   - bootstrap.yaml에도 default-profile을 정의 했음, 구래서 group의 정의를 application.yaml과 동일하게 등록함