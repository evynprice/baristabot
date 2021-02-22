FROM gradle:6.8-jdk8 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar

FROM openjdk:10-jre-slim
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/barista.jar
WORKDIR /app
CMD ["java", "-jar","barista.jar"]
