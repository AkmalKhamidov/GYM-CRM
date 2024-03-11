# GYM CRM - NOSQL
Epam Learning - Spring Boot REST. GymCRM Project.

This project is a Gym Customer Relationship Management (CRM) system implemented using the Spring framework. It provides functionality to manage trainees, trainers, and training sessions.

Project: Spring Boot, JWT Authentication (JJWT), Swagger v3.0 (OpenAPI), Gradle (Groovy DSL), Spring Security, Eureka, OpenFeign, Async Messaging (JMS + ActiveMQ), MongoDB

## Microservices:
eureka-server: http://localhost:8761/
main-microservice: http://localhost:8082/api/v1
Responsible to create user (roles: trainee and trainer) and modify them, create trainings.

report-microservice: http://localhost:8085/api/v1
Responsible to manage reports for trainer/trainings.

## Logs:
logging.log
report-logging.log

## Swagger:
(main-microservice) dev: http://localhost:8083/swagger-ui.html

## Roles:
- TRAINEE
- TRAINER

## Project Structure

The project is structured into the following packages:

- **configs**: Contains configuration classes for Spring.
- **controllers**: Contains REST controllers for Trainee, Trainer, and Training entities.
- **dto**: Data transfer objects (DTOs) for Trainee, Trainer, and Training entities.
- **exceptions**: Custom exceptions, such as `NotFoundException`, `NotAuthenticated`, `NotAuthorized`.
- **mapper**: Mapper classes for Trainee, Trainer, and Training entities. Used MapStruct.
- **models**: Entity classes representing the domain model.
- **repositories**: Repository interfaces for Trainee, Trainer, and Training entities.
- **security**: Security configuration classes for JWT authentication.
- **services**: Service classes for Trainee, Trainer, and Training entities.

## Instruction:

1. Add database properties to application-prod.yml/application-dev.yml file to connect to your database. (MAIN-MICROSERVICE
   (Tables will be created automatically for dev profile if there is no such tables in your database)
   1.1 Add MongoDB database properties to application.yml. (REPORT-MICROSERVICE)
2. Run the project.

## Actuator
Added Spring Boot Actuator for monitoring and managing the application. Custom health checks added:
- DatabaseHealthCheck
- DownstreamServiceHealthCheck (for example, to check if the external service is available) URL is configurable in downstream-service.properties file.

## Async Messaging
- Manage Trainer Workload

## Metrics
Added Spring Boot Metrics by using prometheus for monitoring and managing the application. Custom metrics added:
- TraineeRegistrationCounter
- TrainerRegistrationCounter
- TrainingRegistrationCounter

## Configuration
`OpenAPI30Config` - configuration for Swagger v3.0 (OpenAPI).
`SecurityConfig` - configuration for Spring Security.
