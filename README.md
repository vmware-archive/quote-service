# a quote-service
A simple REST microservice example that provides real-time-ish (15 minute delayed) market data. It  uses the public Yahoo Finance APIs. For more information on the underlying API, please refer to the documentation [here](https://developer.yahoo.com/yql).

Build the service using the maven conventions. From the root directory of the project run:

```bash
mvn clean install
```

to run the service locally you can take advantage of the maven spring-boot plugin:

```bash
mvn spring-boot:run
```

once it's running locally, try out these operations:

<http://localhost:8080/quotes/GOOG>

<http://localhost:8080/quotes/marketSummary>

To deploy to cloud foundry edit the manifest.yml file to give the app a unique name and perform a cf push.

