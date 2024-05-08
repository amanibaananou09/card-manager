FROM eclipse-temurin:17-jre-alpine

COPY target/*.jar app.jar

VOLUME /home/essrecette/ess/photos

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]