ARG MCSV_NAME=mcsv-users

FROM openjdk:18-jdk-alpine as builder

ARG MCSV_NAME

WORKDIR /app/$MCSV_NAME

# Copy the pom.xml file to download dependencies
COPY ./pom.xml /app
# Copy the complete project in the containers
COPY ./$MCSV_NAME/.mvn ./.mvn
#we need to copy the mvnw file to save the cache of maven
COPY ./$MCSV_NAME/mvnw .
#we copy the file pom to download the dependencies
COPY ./$MCSV_NAME/pom.xml .

#skip test and ejecution in each build and delete the target folder
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

# Copy the complete project in the container
COPY ./$MCSV_NAME/src ./src

#we run the maven command to download the dependencies and run project
RUN ./mvnw clean package -DskipTests

# ------------------ Second stage ------------------
#we cretae a new image with the image previous
FROM openjdk:18-jdk-alpine

WORKDIR /app

#we create a directory to save the logs of spring boot
RUN mkdir ./logs

ARG MCSV_NAME

ARG TARGET_FOLDER=/app/$MCSV_NAME/target

COPY --from=builder /$TARGET_FOLDER/mcsv-users-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=8081

#this is the port as variable, we can change it in the command line
ENV PORT $PORT_APP

# Run the jar file with port
EXPOSE $PORT

#ENTRYPOINT ["java", "-jar", "mcsv-users-0.0.1-SNAPSHOT.jar"]
CMD ["java", "-jar", "mcsv-users-0.0.1-SNAPSHOT.jar"]