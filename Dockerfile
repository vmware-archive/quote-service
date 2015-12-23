FROM maven:3.2-jdk-7-onbuild
EXPOSE 8080
CMD ["mvn", "spring-boot:run"]
