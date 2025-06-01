FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src/ src/

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the application with low memory optimization
CMD ["java", \
     "-server", \
     "-Xms256m", \
     "-Xmx512m", \
     "-XX:MetaspaceSize=128m", \
     "-XX:MaxMetaspaceSize=256m", \
     "-XX:+UseG1GC", \
     "-XX:MaxGCPauseMillis=200", \
     "-XX:+UseStringDeduplication", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-Dspring.profiles.active=prod", \
     "-jar", \
     "target/CloudFlare_Api-0.0.1-SNAPSHOT.jar"]
