FROM openjdk:17

VOLUME /tmp

ADD ./build/libs/assignment-0.0.1-SNAPSHOT.jar game-of-three.jar

ENTRYPOINT ["java","-jar","game-of-three.jar"]