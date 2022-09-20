# Application with console interface

Simple Spring Boot application with controller responsible for the resource User.
Application has custom exception handler that provides error descriptions as json in response to the request.

<sub>For integration test in memory database is used</sub>

# Funtionality

- register new user: 
</br>Email, first name, last name, birth date reguired. 
</br>User have to be older than 18 (or other age declared in application.properties)
</br>All inputs must have appropriate format. 
- update one or more user properties:
</br>Date have not violate age restrictions
</br>Any input must have appropriate format
- update all user properties: 
</br>Email, first name, last name, birth date reguired. 
</br>Date have not violate age restrictions
</br>All inputs must have appropriate format.
- delete user
- get all users by birth date within given range
</br>"From" date must be earlier than "To" date

# Endpoints description

| Endpoint | HttpMethod | Action | Inputs | Input example | 
|-------------|-----------------|----------|----------|------------------|
| /users/register    | POST	     | Register new User| Request body: JSON | (1) | 
| /users/{id}    | PATCH	     | Updates one and more user properties by id | Path variable: user id; Request body: JSON | (2) | 
| /users/{id}    | PUT | Updates all user properties by id | Path variable: user idж Request body: JSON | (3) | 
| /users/{id}    | DELETE	     | Delete user by id | Path variable: user id | (4) | 
| /users    | GET	     | Get all users by birth date in a given range | Request parameter: from; Request parameter: to | (5) | 

(1) /users/register
</br> POST
</br> JSON payload: 
</br>{ "data":
</br>    {
</br>        "email": "user@domain.com",
</br>        "firstName": "Name",
</br>        "lastName": "Surname",
</br>        "birthDate": "2000-05-25",
</br>        "address": "Some address",
</br>        "phone": "+380631231212"
</br>    }
</br>}

(2) /users/13
</br> PATCH
</br> JSON payload: 
</br>{ "data":
</br>    {
</br>        "email": "user@domain.com",
</br>    }
</br>}

(3) /users/13
</br> PUT
</br> JSON payload: 
</br>{ "data":
</br>    {
</br>        "email": "user@domain.com",
</br>        "firstName": "Name",
</br>        "lastName": "Surname",
</br>        "birthDate": "2000-05-25",
</br>        "address": "Some address",
</br>        "phone": "+380631231212"
</br>    }
</br>}

(4) /users/13
</br> DELETE

(5) /users?from=1999-01-01&to=2001-01-01
</br> GET

# Installation

1. Clone the project to your machine
2. In src/main/resources/application.properties set following properties:
- spring.datasource.url=DATABASE_URL
- spring.datasource.username=YOUR_USERNAME
- spring.datasource.password=YOUR_PASSWORD
- spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
- spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
- spring.h2.console.enabled=false
- spring.jpa.hibernate.ddl-auto=create-drop
4. Execute comman "mvn clean package" in terminal
5. Run the app