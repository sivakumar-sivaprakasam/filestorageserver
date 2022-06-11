# File Storage Server Application

A simple file storage server with bunch of REST APIs 

Systems Required: 

- OpenJDK 11
- Maven 3.5
- MySQL 8.0 & Workbench CE
- Docker Desktop
- CURL or Postman

Following services are exposed in this application

- Upload a file
- Delete a file
- Download a file
- List uploaded files
- Search files by name, size, type

# Running File Storage Server Application

Currently, I am using `mediumblob` datatype in MySQL for storing files. This datatype's max size is 16Mb. In case if you want to store files of size more than 16Mb, please change it in `scripts/schema.sql`

Also, this application currently supports files with following mimetype:

`text/plain, text/csv, application/json, application/pdf, video/mpeg, video/mp4, application/gzip, application/zip, application/x-7z-compressed`

If you would like to add/modify types of files, you can change it in `src/main/resources/application.properties`

```bash
server.compression.mime-types=text/plain, text/csv, application/json, application/pdf, video/mpeg, video/mp4, application/gzip, application/zip, application/x-7z-compressed
```

## Build & Run application from host machine

Prepare database using mysql:

Refer script file kept in `scripts/schema.sql`

Build & run file storage server application:

```bash
mvn clean spring-boot:run
```

Once the application started successfully, you can use rest client tools to test the APIs

## Build & Run application within docker container

You dont need to do anything specific to DB. MySQL will be launched in another container and initialize the DB as per the scripts from `scripts/schema.sql` 

Build image using maven command:

```bash
mvn clean package
```

Once image is created and pushed to docker hub, you can verify the images by running `docker images` command

Then, you can launch the containers with docker-compose

```docker
docker-compose up -d
```

Once containers are started successfully, you can access the application from http://localhost:9090/files/ and use rest client tools to test the APIs

# Jacoco Code coverage

Code coverage report can be accessed from: <project folder>/target/site

# Swagger UI

- Aplication launched from host machine: http://localhost:8080/swagger-ui/index.html
- Application launched from container: http://localhost:9090/swagger-ui/index.html

# API Documents (JSON)

- When launched from host machine: http://localhost:8080/v3/api-docs
- When launched from container: http://localhost:9090/v3/api-docs

