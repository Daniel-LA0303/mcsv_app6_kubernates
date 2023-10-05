FROM openjdk:18.0.2.1-slim

WORKDIR /app

# Copy the jar file to the container
COPY ./target/mcsv-users-0.0.1-SNAPSHOT.jar .

# Run the jar file with port
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "mcsv-users-0.0.1-SNAPSHOT.jar"]