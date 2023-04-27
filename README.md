## stack

- Java 17
- Spring MVC
- Hibernate
- Apache Tomcat 10.1.8
- PostgreSQL
- Gradle 7.5

## get started

To run the project you will need *Java 17*, *Spring MVC 6.+*, *Hibernate 6.+*, *Apache Tomcat version 10.+*, *Gradle 7.5* and *PostgreSQL*

To start the project you need to perform the following steps:
1. pull this project
2. сreate a *PostgreSQL* database
3. in the *application.properties* file which is located along the path *src/main/resources/*<br>
   set the **```port```**, **```database name```**, **```username```** and **```password```**
4. using the *terminal* or *cmd* go to the folder with this project
5. enter the command ***gradle build*** and wait for it to complete

Entites **```GiftCertificate```** and **```Tag```** are in a **```Many-To-Many```** relationship and **```GiftCertificate```** may not contain tags

## endpoints

The project is running on **```localhost:8080```**

**```GET```** - ***/api/v1/gift-certificates***
>description - getting all gift certificates<br>
>url example - ***localhost:8080/api/v1/gift-certificates***

**```GET```** - ***/api/v1/gift-certificates?tagName=tagName***
>description - getting gift certificates by tag name<br>
>url example - ***localhost:8080/api/v1/gift-certificates?tagName=health***

**```GET```** - ***/api/v1/gift-certificates?part=part***
>description - getting gift certificates by part of the name or description<br>
>url example - ***localhost:8080/api/v1/gift-certificates?part=certificate***

**```GET```** - ***/api/v1/gift-certificates?sortBy=sortBy&order=order***
>description - getting gift certificates sorted by parameter (by default - name) and in order ASC/DESC (by default - ASC)<br>
>url example - ***localhost:8080/api/v1/gift-certificates?sortBy=name&order=asc***

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

**```GET```** - ***/api/v1/tags/{id}***
>description - getting a tag<br>
>url example - ***localhost:8080/api/v1/tags/1***

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