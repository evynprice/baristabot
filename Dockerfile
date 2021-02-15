FROM openjdk:8

WORKDIR /barista
COPY /build/libs/*.jar /barista/barista.jar

CMD ["java", "-jar","barista.jar"]