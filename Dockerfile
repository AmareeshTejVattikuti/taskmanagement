FROM amazoncorretto:17.0.4

WORKDIR /app

# Copy the application JAR file and other necessary files to the container
COPY target/taskmanagement-0.0.1-SNAPSHOT.jar /app/taskmanagement-0.0.1-SNAPSHOT.jar

# Expose the port the application runs on
EXPOSE 8080

# Specify the command to run on container start
ENTRYPOINT ["java","-jar","/app/taskmanagement-0.0.1-SNAPSHOT.jar"]
