# game-of-three assignment
___
### Spring Boot Application

#### 
- This project allows users to play a game via their browsers. 
- The API exposes a REST endpoint and a websocket message broker.

### How to play the game
- First open up the browser and go to the http://localhost:7070/index and then click to the Join Game button. 
  Then open up another browser and click on the Join Game button again. 
  Then two player will be automatically play the game without any user input

- The FE part is not smart enough to play when two browsers open and one user 
  clicks on the join button. Then the second browser should refresh the page and click on the join button to play the game.

### Tech Stack
- Java 17
- Spring Boot
- Websocket
- Docker
- JUnit 5

### Prerequisites

---
- Gradle
- ActiveMQ
- Docker

### Build & Run

first go to the terminal and open up the project directory. "~/assignment"

### build

./gradlew clean build

### Run tests

./gradlew test

### Docker

to run the project
- docker-compose up -d --build

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

### Parts to Improve
- I used in memory objects for the storage to make the development time shorter.
- Instead of the in memory noSql db (mongodb or other) is better to use for the storage.
- The values can be set in the properties file and can be retrieved from there based on the environment.
- I added the jars for the metrics but not implemented into the project. It should be implemented as well.
- The integration test which is testing the applicaion end to end should be added.