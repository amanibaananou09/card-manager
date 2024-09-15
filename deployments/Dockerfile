FROM eclipse-temurin:17-jre-alpine

COPY target/*.jar app.jar

COPY deployments/certs/java/keycloak.crt .

RUN keytool -importcert -v -trustcacerts -alias keycloak -file keycloak.crt -keypass secret -cacerts -noprompt -storepass changeit;

VOLUME /home/essrecette/ess/photos

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]