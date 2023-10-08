FROM openjdk:18-jdk-alpine as builder

WORKDIR /app/mcsv-users

# Copy the pom.xml file to download dependencies
COPY ./pom.xml /app
# Copy the complete project in the container
COPY ./mcsv-users/.mvn ./.mvn
#we need to copy the mvnw file to save the cache of maven
COPY ./mcsv-users/mvnw .
#we copy the file pom to download the dependencies
COPY ./mcsv-users/pom.xml .

#skip test and ejecution in each build and delete the target folder
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

# Copy the complete project in the container
COPY ./mcsv-users/src ./src

#we run the maven command to download the dependencies and run project
RUN ./mvnw clean package -DskipTests

# ------------------ Second stage ------------------
#we cretae a new image with the image previous
FROM openjdk:18-jdk-alpine

WORKDIR /app

COPY --from=builder /app/mcsv-users/target/mcsv-users-0.0.1-SNAPSHOT.jar .

# Run the jar file with port
EXPOSE 8081

#ENTRYPOINT ["java", "-jar", "mcsv-users-0.0.1-SNAPSHOT.jar"]
CMD ["java", "-jar", "mcsv-users-0.0.1-SNAPSHOT.jar"]