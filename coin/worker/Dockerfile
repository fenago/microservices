#pull base image
FROM openjdk:8-jdk-alpine

#maintainer 
MAINTAINER xyz@email.com



#default command
CMD java -jar /target/worker-0.0.1-SNAPSHOT-jar-with-dependencies.jar

#copy hello world to docker image
ADD ./target/worker-0.0.1-SNAPSHOT-jar-with-dependencies.jar /target/worker-0.0.1-SNAPSHOT-jar-with-dependencies.jar