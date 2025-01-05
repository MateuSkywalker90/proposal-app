In this project we will use microservices together with RabbitMQ, the frontend will be used in a ready-made application running in a docker container

Maven command

mvn package spring-boot:repackage

Command to run a docker compose manually

docker-compose up -d

OBS: the service images that were passed in docker compose must be in docker hub.

GitHub of wait-for-it project

https://github.com/vishnubob/wait-for-it
