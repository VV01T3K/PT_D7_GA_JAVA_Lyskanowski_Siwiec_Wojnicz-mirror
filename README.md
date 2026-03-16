# PT_D7_GA Java Project

Java 21 + Maven project for managing people, companies, and reporting hierarchies. The repo combines several lab-style applications in one codebase: data generation, multithreaded file processing, socket client/server communication, and a terminal CRUD app backed by H2 + Hibernate.

## Main parts

- `pl.edu.pg.Main` - multithreaded processing app with configurable sorting and worker count
- `pl.edu.pg.TestRepo` - test-data generator and JSON/query preparation
- `pl.edu.pg.networking.Server` / `Client` - simple socket communication layer
- `pl.edu.pg.persistance.Terminal` - terminal interface for create, delete, select, query, and seed operations

## Requirements

- Java 21
- Maven

## Build and test

```bash
mvn clean package
mvn test
```

## Run

```bash
java -jar target/JavaLab1-app.jar --threads 2 --order AGE
java -jar target/JavaLab1-data-generator.jar
java -jar target/JavaLab4-server.jar
java -jar target/JavaLab4-client.jar
java -jar target/JavaLab5-terminal.jar
```

## Notes

- Persistence uses an H2 file database stored as `lab5.db`
- The processing/data-generation flow creates working files under `Data/`
- Logging is configured with Log4j2
