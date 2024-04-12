FROM eclipse-temurin:17-jdk-alpine

COPY target/*.jar app.jar

VOLUME /home/essrecette/ess/photos

EXPOSE 8080-8200

ENTRYPOINT ["java","-jar","/app.jar"]