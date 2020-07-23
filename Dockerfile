FROM alpine:3.4

RUN apk update

FROM openjdk:8

CMD MKDIR /myservice

COPY . /myservice

WORKDIR /myservice

EXPOSE 8080

CMD pwd

CMD java -jar target/userservice-0.0.1-SNAPSHOT.jar


