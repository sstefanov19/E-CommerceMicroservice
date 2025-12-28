# Stage 1: Build the application
FROM eclipse-temurin:21-jre-jammy
WORKDIR /opt
ENV PORT 8761
EXPOSE 8761

COPY ./target/*.jar /opt/app.jar

ENTRYPOINT exec java $JAVA_OPT -jar app.jar
