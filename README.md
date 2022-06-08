# File Storage Server Application

A simple file storage server with REST APIs

Systems Required: 

- OpenJDK 11
- Maven 3.5
- MySQL 8.0 & Workbench CE
- Docker Desktop
- CURL or Postman

Following services are exposed from this File Storage Server Application

- Upload a file
- Delete a file
- Download a file
- List uploaded files
- Search files by name, size, type



# Running File Storage Server Application

Currently, I am using `mediumblob` datatype in MySQL for storing files. This datatype's max size is 16Mb. In case if you want to change it, please modify it in `scripts/schema.sql`

Also, this application accepts following mimetype files:

`text/plain, text/csv, application/json, application/pdf, video/mpeg, video/mp4, application/gzip, application/zip, application/x-7z-compressed`

In case if you would like to allow more types of files, feel free to modify it in `application.properties`

```bash
server.compression.mime-types=text/plain, text/csv, application/json, application/pdf, video/mpeg, video/mp4, application/gzip, application/zip, application/x-7z-compressed
```

## Run application in host machine

Prepare database using mysql:

Refer script file kept in `scripts/schema.sql`

Build & run file storage server application:

```bash
mvn clean spring-boot:run
```

Once the application started successfully, you can use rest client tools to test the APIs

## Run Application in docker container

Build image using maven command:

```bash
mvn clean package
```

Once image is created and pushed to docker hub, you can launch the containers with docker-compose

```docker
docker-compose up -d
```

Once the containers are started successfully, you can access the application from http://localhost:9090/files/ and you can use rest client tools to test the APIs

# Jacoco Code coverage

Code coverage report can be accessed from: <project folder>/target/site

# Swagger UI

- Aplication launched from host machine: http://localhost:8080/swagger-ui/index.html
- Application launched from container: http://localhost:9090/swagger-ui/index.html

# API Documents (JSON)

- When launched from host machine: http://localhost:8080/v3/api-docs
- When launched from container: http://localhost:9090/v3/api-docs

