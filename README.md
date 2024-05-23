# aggregator-service assignment
___
### Spring Boot Application

#### 
- This project allows users to play a game via their browsers.
- The API exposes a REST endpoint and a websocket message broker.

### Tech Stack
- Java 17
- Spring Boot
- Websocket message broker
- JUnit 5
- 
### Prerequisites

---
- Gradle
- Docker

### Build & Run

first go to the terminal and open up the project directory. "~/assignment"

### build

./gradlew clean build

### Run tests

./gradlew test

### Docker

### explicitly building docker images
- docker run -p 61616:61616 -p 8161:8161 rmohr/activemq

### API DOCUMENTATION (Swagger)

- After project runs you will be able to reach the url below where you can see the API doc.
- http://localhost:7070/swagger-ui/index.html

### Metrics

- Some metrics are enabled on the actuator api. We can observe the system on production.
- http://localhost:7070/actuator

### Prometheus
http://localhost:7070/actuator/prometheus