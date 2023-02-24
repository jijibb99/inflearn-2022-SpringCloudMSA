FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/user-service-1.0.jar UserService.jar
#ENTRYPOINT ["java", "-jar", "UserService.jar"]
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,address=*:8051,server=y,suspend=n","-Djava.security.egd=file:/dev/./urandom","-jar","UserService.jar"]

#8051 port는 Remote Debug용으로 추가함 (추가시 space로 구분함)
EXPOSE 8051 