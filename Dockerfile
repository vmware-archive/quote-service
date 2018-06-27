FROM openjdk:8-alpine
VOLUME /tmp
ADD ./target/quote-service.jar /quote-service.jar
RUN sh -c 'touch /quote-service.jar'
ENTRYPOINT ["java","-jar","/quote-service.jar"]