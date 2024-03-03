# Task Management Service
## Service Description

The Task Management Service is designed to handle the creation, update, and retrieval of tasks with various statuses. 
It includes features such as updating task descriptions, changing task statuses, and automatically marking tasks as "past due" based on their due dates.


## Assumptions Made

- The service assumes that tasks can have different statuses such as "Not Done," "Done," and "Past Due."
- Tasks can be updated, and their statuses can be changed using the provided endpoints.
- Dedicated endpoints are provided for creating, updating, and retrieving tasks.
- The service includes automatic handling of tasks past their due date, marking them as "Past Due." using a scheduled job.
- Tasks with PAST_DUE status cannot be updated.
- The service cannot delete tasks.
- Task can be created with a due date and a description.
- Tasks can be retrieved by their ID, and all tasks can be retrieved as a list.
- H2 in-memory database is used for data storage.
- Different validations are done on the input data to make sure that the data is valid and consistent.

## Tech Stack

### Runtime Environment
- Java Development Kit (JDK) 17
- Maven
- H2 in-memory database
- Docker
- JUnit 5
- Mockito
- ModelMapper
- Lombok

### Frameworks and Libraries
- Spring Boot 3.2.3
- ModelMapper for object mapping
- JUnit and Mockito for testing
- Docker for containerization

## How to Build the Service

Make sure you have JDK 17 and Maven installed on your machine.

## Access h2-console
The H2 in-memory database console is enabled and can be accessed using the following URL: http://localhost:8080/h2-console

##Build and Run the Service in Local Environment
```bash
# Build the project
mvn clean install

# Run tests
mvn test

# Run the service
mvn spring-boot:run
```

# Build the Docker image and run the service in a container
```bash
# Make sure you have Docker installed on your machine
# Run the following commands to build and run the Docker image
# The service will be available on http://localhost:8080
docker build -t task-management-service .
docker run -p 8080:8080 task-management-service
```


## How to Use the Service
Connect to the service using the following base URL: http://localhost:8080 from any REST client such as Postman or cURL.

### Endpoints
The service provides the following endpoints:
Create task: 
**POST** http://localhost:8080/api/v1/task/create
Sample request body:
```json
{
  "description": "T5",
  "dueDate": "2024-07-06"
}
```
Update Status: 
**PATCH** http://localhost:8080/api/v1/task/update-status
Sample request body:
```json
{
  "id": 1,
  "status": "DONE"
}
```
Update Description: 
**PUT** http://localhost:8080/api/v1/task/update-description
Sample request body:
```json
{
  "id": "2",
  "description": "Test"
}
```
Get All Tasks: 
**GET** http://localhost:8080/api/v1/task/filter-by-status
Sample request body:
```json
{
  "fetchAllTasks": false,
  "status": "NOT_DONE"
}
```
Get Task by ID: 
**GET** http://localhost:8080/api/v1/task/{id}
here provide the id of the task in the URL