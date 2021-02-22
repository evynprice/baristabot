FROM openjdk:8

WORKDIR /barista
COPY /build/libs/BaristaBot-1.4.0-all.jar /barista/barista.jar

CMD ["java", "-jar","/barista/barista.jar"]