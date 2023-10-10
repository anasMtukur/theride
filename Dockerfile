# Use the official OpenJDK base image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/theride-1.0.0.jar /app/app.jar

# Expose the port that the application will run on
EXPOSE 9191

# Specify the command to run on container start
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=demo", "--spring.flyway.baseline-on-migrate=true"]
