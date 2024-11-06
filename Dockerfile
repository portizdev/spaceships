FROM eclipse-temurin:23-jdk  

COPY target/spaceship-api.jar spaceship-api.jar

ENTRYPOINT ["java", "-jar", "/spaceship-api.jar"]