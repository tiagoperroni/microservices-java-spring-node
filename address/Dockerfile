FROM openjdk:11-slim

EXPOSE 8090

WORKDIR /src

COPY /target/*.jar /src/adress-api.jar

ENTRYPOINT [ "java", "-jar", "/src/adress-api.jar" ]