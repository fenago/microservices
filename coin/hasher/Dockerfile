#pull base image
FROM openjdk:8-jdk-alpine

#maintainer 
MAINTAINER dev.asadriaz@gmail.com

#expose port 8080
EXPOSE 8080

#default command
CMD java -jar /data/hasherSerevice-0.0.1-SNAPSHOT.jar

#copy hello world to docker image
ADD ./data/hasherSerevice-0.0.1-SNAPSHOT.jar /data/hasherSerevice-0.0.1-SNAPSHOT.jar