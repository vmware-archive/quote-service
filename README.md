# quote-service
Quotes microservice refactored from the SpringTrader application, turned into a kubernetes deployment.

This version of the service is backed by a relational database, and responds in with [HATEOAS](https://spring.io/understanding/HATEOAS) formatted JSON.

To get the source code, run the following from a clean directory:

```bash
git clone git@github.com:cf-platform-eng/quote-service.git
cd quote-service
git checkout k8s
```

Build the service using the maven conventions (from the root directory of the project):

```bash
mvn clean install -DskipTests
```

Or, to run the tests, get postgres running locally in docker: 
`docker run --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=pass1234 -d postgres`

dockerize the app: `docker build -t <your docker repo>/quote-service:2.0.2 .` and push it `docker push <your docker repo>/quote-service`

then, to get it running in k8s, you will need to first deploy postgres. Try [this](https://github.com/kubernetes/charts/tree/master/stable/postgresql)
and then `kubectl apply -f quote-rs.yaml && kubectl apply -f quote-svc.yaml`

Once it's running, try out some operations:

<http://k8s-lb-host/quotes/>

<http://k8s-lb-host/quotes/GOOG>

<http://k8s-lb-host/quotes/marketSummary>

<http://k8s-lb-host/quotes/topGainers>

<http://k8s-lb-host/quotes/topLosers>

<http://k8s-lb-host/quotes/symbols>
