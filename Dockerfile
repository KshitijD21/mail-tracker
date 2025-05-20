# Importing JDK and copying required files
FROM openjdk:17-jdk AS build
WORKDIR /app
COPY pom.xml .
COPY src src

# Copy Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Set execution permission for the Maven wrapper
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final Docker image using OpenJDK 19
FROM openjdk:17-jdk
VOLUME /tmp

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]
ENTRYPOINT ["java", "-Dhttps.protocols=TLSv1.2,TLSv1.3", "-jar", "/app.jar"]
# ENTRYPOINT ["sleep", "3600"]
EXPOSE 8080
