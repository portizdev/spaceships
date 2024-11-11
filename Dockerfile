FROM eclipse-temurin:21-jdk  

COPY target/spaceships-0.0.1-SNAPSHOT.jar spaceship-api.jar

ENTRYPOINT ["java", "-jar", "/spaceship-api.jar"]