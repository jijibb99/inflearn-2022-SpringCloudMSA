FROM openjdk:15-jdk-alpine
#FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.6_10-slim
COPY target/*SNAPSHOT.jar app.jar
EXPOSE 8080 8072

ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENV JAVA_OPTS="-Xmx512m"

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,address=*:8072,server=y,suspend=n","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
#["java","-agentlib:jdwp=transport=dt_socket,address=*:8071,server=y,suspend=n","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=docker"]
#-Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker