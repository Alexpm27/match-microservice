FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/match-micro.jar match-micro.jar
EXPOSE 8080
CMD ["java","-jar","match-micro.jar"]