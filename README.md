# GYM CRM - REST APPLICATION
Epam Learning - Spring REST. GymCRM Project.

This project is a Gym Customer Relationship Management (CRM) system implemented using the Spring framework. It provides functionality to manage trainees, trainers, and training sessions.

Project: Spring WebMVC, JWT Authentication, Swagger v3.0 (OpenAPI)

## Swagger:
Go to http://localhost:8080/swagger-ui/index.html

## Project Structure

The project is structured into the following packages:

- **configs**: Contains configuration classes for Spring.
- **controllers**: Contains REST controllers for Trainee, Trainer, and Training entities.
- **dto**: Data transfer objects (DTOs) for Trainee, Trainer, and Training entities.
- **exceptions**: Custom exceptions, such as `NotFoundException`, `NotAuthenticated`.
- **mapper**: Mapper classes for Trainee, Trainer, and Training entities.
- **models**: Entity classes representing the domain model.
- **repositories**: Repository interfaces for Trainee, Trainer, and Training entities.
- **security**: Security configuration classes for JWT authentication.
- **services**: Service classes for Trainee, Trainer, and Training entities.

## Instruction:

1. Add database properties to application.yml file to connect to your database.
   (Tables will be created automatically if there is no such tables in your database)
2. Run your Tomcat server. (I used Tomcat 10.1.13)
3. Run the project.


## Configuration

`ApplicationConfig` - application configuration is defined.
`WebApplicationInitializer` - used to configure the DispatcherServlet and initializing a web application.
`OpenAPI30Config` - configuration for Swagger v3.0 (OpenAPI).
`YamlPropertySourceFactory` - (NOT configuration) used to read properties from application.yml file.



