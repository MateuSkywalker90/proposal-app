FROM  openjdk:17

COPY target/proposal-app-1.0.0.jar proposal-app-1.0.0.jar

ENTRYPOINT ["java","-jar","proposal-app-1.0.0.jar"]