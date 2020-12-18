# https://spring.io/guides/gs/spring-boot-docker/
FROM adoptopenjdk:11-jre
RUN adduser --system --group spring && \
    mkdir /opt/app && \
    chown spring:spring /opt/app
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /opt/app/app.jar
ENTRYPOINT ["java","-jar","/opt/app/app.jar"]
