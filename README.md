## stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Gradle 7.5

## get started

To run the project you will need *Java 17*, *Spring Boot 3.+*, *Gradle 7.5* and *PostgreSQL*

To start the project you need to perform the following steps:
1. pull this project
2. сreate a *PostgreSQL* database
3. in the *application.properties* file which is located along the path *src/main/resources/*<br>
   set the **```port```**, **```database name```**, **```username```** and **```password```**
4. using the *terminal* or *cmd* go to the folder with this project
5. enter the command ***gradle build*** and wait for it to complete

Entities **```GiftCertificate```** and **```Tag```** are in a **```Many-To-Many```** relationship, **```GiftCertificate```** may not contain tags.<br/>
Entities **```Order```** and **```GiftCertificate```** are in a **```Many-To-One```** relationship.<br/>
Entities **```User```** and **```Order```** are in a **```One-To-Many```** relationship, **```User```** may not contain orders.<br/>

## endpoints

The project is running on **```localhost:8080```**

**```GET```** - ***/api/v1/gift-certificates***
>description - getting all gift certificates<br>
>url example - ***localhost:8080/api/v1/gift-certificates***

**```GET```** - ***/api/v1/gift-certificates?name=name&description=description&price=price&duration=duration&page=page&size=size&sort=sort***
>description - getting all gift certificates with opportunity pagination, sorting and filtering (all parameters is not required)<br>
>url example - ***localhost:8080/api/v1/gift-certificates?description=gift&page=1&size=10&sort=name,desc***

**```GET```** - ***/api/v1/gift-certificates?tagName=tagName&page=page&size=size&sort=sort***
>description - getting gift certificates by tag name (required) with opportunity pagination and sorting (not required)<br>
>url example - ***localhost:8080/api/v1/gift-certificates?tagName=health***

**```GET```** - ***/api/v1/gift-certificates?part=part&page=page&size=size&sort=sort***
>description - getting gift certificates by part of the name or description (required) with opportunity pagination and sorting (not required)<br>
>url example - ***localhost:8080/api/v1/gift-certificates?part=certificate***

**```GET```** - ***/api/v1/gift-certificates/{id}***
>description - getting a gift certificate<br>
>url example - ***localhost:8080/api/v1/gift-certificates/1***

**```POST```** - ***/api/v1/gift-certificates***
>description - saving a gift certificate<br>
>request body example:<br>
>```javascript
>{
>  "name": "Spa",
>  "description": "Spa gift certificate",
>  "price": 150.0,
>  "duration": 365,
>  "tags": ["health", "relax"]
>}

**```PUT```** - ***/api/v1/gift-certificates/{id}***
>description - updating a gift certificate in whole or in part<br>
>url example - ***localhost:8080/api/v1/gift-certificates/1<br>***
><br>request body example in whole updating:<br>
>```javascript
>{
>  "name": "Massage",
>  "description": "Massage gift certificate",
>  "price": 100.0,
>  "duration": 180,
>  "tags": ["massage", "relax"]
>}

>request body example in part updating:<br>
>```javascript
>{
>  "name": "Massage",
>  "price": 100.0,
>  "tags": ["massage", "relax"]
>}

**```DELETE```** - ***/api/v1/gift-certificates/{id}***
>description - deleting a gift certificate<br>
>url example - ***localhost:8080/api/v1/gift-certificates/1***

**```GET```** - ***/api/v1/tags***
>description - getting all tags<br>
>url example - ***localhost:8080/api/v1/tags***

**```GET```** - ***/api/v1/tags?name=name&page=page&size=size&sort=sort***
>description - getting all gift certificates with opportunity pagination, sorting and filtering (all parameters is not required)<br>
>url example - ***localhost:8080/api/v1/tags?name=a&page=1&size=10&sort=name,desc***

**```GET```** - ***/api/v1/tags/{id}***
>description - getting a tag<br>
>url example - ***localhost:8080/api/v1/tags/1***

**```GET```** - ***/api/v1/tags/most-widely-used***
>description - getting a most widely used tag by user with the highest amount orders<br>
>url example - ***localhost:8080/api/v1/tags/most-widely-used***

**```POST```** - ***/api/v1/tags***
>description - saving a tag<br>
>request body example:<br>
>```javascript
>{
>  "name": "beauty"
>}

**```PUT```** - ***/api/v1/tags/{id}***
>description - updating a tag<br>
>url example - ***localhost:8080/api/v1/tags/1<br>***
>request body example:<br>
>```javascript
>{
>  "name": "happiness"
>}

**```DELETE```** - ***/api/v1/tags/{id}***
>description - deleting a tag<br>
>url example - ***localhost:8080/api/v1/tags/1***

**```GET```** - ***/api/v1/orders***
>description - getting all orders<br>
>url example - ***localhost:8080/api/v1/orders***

**```GET```** - ***/api/v1/orders?price=price&page=page&size=size&sort=sort***
>description - getting all orders with opportunity pagination, sorting and filtering (all parameters is not required)<br>
>url example - ***localhost:8080/api/v1/orders?price=100&page=1&size=10&sort=price,desc***

**```GET```** - ***/api/v1/orders/{id}***
>description - getting a order<br>
>url example - ***localhost:8080/api/v1/orders/1***

**```POST```** - ***/api/v1/orders***
>description - saving a order<br>
>request body example:<br>
>```javascript
>{
>  "userId": 1,
>  "giftCertificateId": 1
>}

**```GET```** - ***/api/v1/users***
>description - getting all users<br>
>url example - ***localhost:8080/api/v1/users***

**```GET```** - ***/api/v1/users?username=username&firstName=firstName&lastName=lastName&email=email&page=page&size=size&sort=sort***
>description - getting all users with opportunity pagination, sorting and filtering (all parameters is not required)<br>
>url example - ***localhost:8080/api/v1/users?username=a&email=mail.ru&page=1&size=10&sort=username,desc***

**```GET```** - ***/api/v1/users/{id}***
>description - getting a user<br>
>url example - ***localhost:8080/api/v1/users/1***

**```GET```** - ***/api/v1/users/{id}/orders***
>description - getting user orders<br>
>url example - ***localhost:8080/api/v1/users/1/orders***

**```POST```** - ***/api/v1/users***
>description - saving a user<br>
>request body example:<br>
>```javascript
>{
>  "username": "ivan",
>  "firstName": "Ivan",
>  "lastName": "Ivanov",
>  "email": "ivanov@mail.ru"
>}
