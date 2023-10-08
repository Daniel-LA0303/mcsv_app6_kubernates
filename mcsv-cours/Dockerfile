FROM openjdk:18-jdk-alpine as builder

WORKDIR /app/mcsv-cours

# Copy the pom.xml file to download dependencies
COPY ./pom.xml /app
# Copy the complete project in the container
COPY ./mcsv-cours/.mvn ./.mvn
#we need to copy the mvnw file to save the cache of maven
COPY ./mcsv-cours/mvnw .
#we copy the file pom to download the dependencies
COPY ./mcsv-cours/pom.xml .

#skip test and ejecution in each build and delete the target folder
#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
RUN ./mvnw dependency:go-offline

# Copy the complete project in the container
COPY ./mcsv-cours/src ./src

#we run the maven command to download the dependencies and run project
RUN ./mvnw clean package -DskipTests

# ------------------ Second stage ------------------
#we cretae a new image with the image previous
FROM openjdk:18-jdk-alpine

WORKDIR /app

#we create a directory to save the logs of spring boot
RUN mkdir ./logs

COPY --from=builder /app/mcsv-cours/target/mcsv-cours-0.0.1-SNAPSHOT.jar .

# Run the jar file with port
EXPOSE 8082

#ENTRYPOINT ["java", "-jar", "mcsv-users-0.0.1-SNAPSHOT.jar"]
CMD ["java", "-jar", "mcsv-cours-0.0.1-SNAPSHOT.jar"]