# Stage 1: Build Stage
FROM eclipse-temurin:21.0.2_13-jre-alpine AS build
WORKDIR extracted
ADD build/libs/report-microservice-0.0.1-SNAPSHOT.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 2: Runtime Stage
FROM eclipse-temurin:21.0.2_13-jre-alpine
WORKDIR extracted
COPY --from=build /extracted/dependencies/ ./
COPY --from=build /extracted/spring-boot-loader/ ./
COPY --from=build /extracted/snapshot-dependencies/ ./
COPY --from=build /extracted/application/ ./

CMD ["java", "org.springframework.boot.loader.launch.JarLauncher"]
#CMD ["java", "-jar", "app.jar", "--agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8780"]