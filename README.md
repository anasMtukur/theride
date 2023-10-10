# The Ride System Design Task

The goal is to design and implement a ride sharing service. The solution should have
a Frontend in Angular and Backend in Java Spring Boot or NodeJS.

As mandatory use cases,
1. The company can onboard drivers
2. Customer can request a ride, and will be match with the nearby drivers
3. Driver can accept a request of a customer

## Development Environment

The system is developed with Java 11 and requires Apache Kafka & Zookeeper to develop. You will need:

- Java 11
- Maven
- Postgres Database
- Kafka & Zookeeper

## Running the system on native development environment

Clone the repository and enter the project directory.

### Building the application

```$ mvn clean package```

### Running the application

```$ mvn spring-boot:run```

### Running tests

```$ mvn test```

## Running the system on docker

The application also contains a Docker file and docker-compose.yml that allow running the application in dockerised containers.

In the project root folder, run the docker compose command:

```sudo docker compose up```

You can add ``` -d ``` option to detach after running the containers.