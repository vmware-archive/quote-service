# quote-service
Quotes microservice refactored from the SpringTrader application, as described in Part 3 of the *Refactoring a Monolith into a Cloud-Native Application* blog series.

This version of the service is backed by a relational database, and responds in with [HATEOAS](https://spring.io/understanding/HATEOAS) formatted JSON. This version also registers itself with [Eureka](https://github.com/Netflix/eureka/wiki/Eureka-at-a-glance), the location of which is configurable via the [manifest.yml](https://github.com/cf-platform-eng/quote-service/blob/part3db/manifest.yml) file.

To get the source code, run the following from a clean directory:

```bash
git clone git@github.com:cf-platform-eng/quote-service.git
cd quote-service
git checkout part3db
```

Build the service using the maven conventions (from the root directory of the project):

```bash
mvn clean install
```

To run the service locally you can take advantage of the maven spring-boot plugin:

```bash
mvn spring-boot:run
```

Once it's running locally, try out some operations:

<http://localhost:8080/quotes/>

<http://localhost:8080/quotes/GOOG>

<http://localhost:8080/quotes/marketSummary>

<http://localhost:8080/quotes/topGainers>

<http://localhost:8080/quotes/topLosers>

<http://localhost:8080/quotes/symbols>

To deploy to cloud foundry, edit the manifest.yml file to give the app a unique name and point it to your Eureka instance. 

You will also need to create a datasource service using a command such as below:
```bash
cf create-service p-mysql 100mb quote-db
```
Then, perform a cf push.
