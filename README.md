# quote-service
Quotes microservice refactored from the SpringTrader application, as described in Part 2 of the *Refactoring a Monolith into a Cloud-Native Application* blog series.

The version in this branch uses cloud connectors to bind to a mysql datasource using hibernate.

Per the manifest, create a mysql service:

```bash
$ cf marketplace
Getting services from marketplace in org foo / space bar as bazz...
OK

service     plans         description   
MariaDB     MariaDB       some sort of mysql database   

$ cf create-service MariaDB MariaDB testds
$ mvn clean install
$ cf push
```

Once it's running, try out some operations:

<http://your.host.here/env>

<http://your.host.here/mappings>

<http://your.host.here/trace>

<http://your.host.here/quotes/AAPL>

<http://your.host.here/quotes/marketSummary>

<http://your.host.here/quotes/topGainers>

<http://your.host.here/quotes/topLosers>

