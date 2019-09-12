When you design and build applications at scale, you deal with two significant challenges: scalability and robustness.

You should design your service so that even if it is subject to intermittent heavy loads, it continues to operate reliably.

Take the Apple Store as an example.

Every year millions of Apple customers preregister to buy a new iPhone.

That's millions of people all buying an item at the same time.




<h2>Coding a Spring application</h2>

The service has three components: the front-end, the backend and a message broker.

The front-end is a simple Spring Boot web app with the Thymeleaf templating engine.

The backend is a worker consuming messages from a queue.

And since Spring Boot has excellent integration with JMS, you could use that to send and receive asynchronous messages.

You can find a sample project with a front-end and backend application connected to JMS at learnk8s/spring-boot-k8s-hpa.

    Please note that the application is written in Java 10 to leverage the improved Docker container integration.

There's a single code base, and you can configure the project to run either as the front-end or backend.

You should know that the app has:

    a homepage where you can buy items
    an admin panel where you can inspect the number of messages in the queue
    a /health endpoint to signal when the application is ready to receive traffic
    a /submit endpoint that receives submissions from the form and creates messages in the queue
    a /metrics endpoint to expose the number of pending messages in the queue (more on this later)

The application can function in two modes:

As frontend, the application renders the web page where people can buy items.

<h2>Step 1</h2>
start a cluster with 8GB of RAM and some extra configuration:

```

minikube start \
  --memory 8096 \
  --extra-config=controller-manager.horizontal-pod-autoscaler-upscale-delay=1m \
  --extra-config=controller-manager.horizontal-pod-autoscaler-downscale-delay=2m \
  --extra-config=controller-manager.horizontal-pod-autoscaler-sync-period=10s
  
```
verify that the installation was successful with: `kubectl get all`
You should see a few resources listed as a table.

First, connect your Docker client to minikube by following the instruction printed by this command: `minikube docker-env`
```
Please note that if you switch terminal, you need to reconnect to the Docker daemon inside minikube. You should follow the same instructions every time you use a different terminal.

```
<h2>Step 2</h2>
connect your Docker client to minikube by following the instruction printed by this command:
` minikube docker-env `
if you get error something like 
``` 
port DOCKER_CERT_PATH="/home/asad/.minikube/certs"
# Run this command to configure your shell:
# eval $(minikube docker-env)
```
then run eval $(minikube docker-env) too
```
lease note that if you switch terminal, you need to reconnect to the Docker daemon inside minikube. You should follow the same instructions every time you use a different terminal.
```
<h2>Step 3</h2>
from the root of the project build the container image with:
` docker build -t spring-k8s-hpa . `

You can verify that the image was built and is ready to run with:
`docker images | grep spring`

Great!

The cluster is ready, you packaged your application, perhaps you're ready to deploy now?

Yes, you can finally ask Kubernetes to deploy the applications.

<h1>Deploying your application to Kubernetes</h1>
   
 Application has three components:

    the Spring Boot application that renders the frontend
    ActiveMQ as a message broker
    the Spring Boot backend that processes transactions

You should deploy the three component separately.

For each of them you should create:

    A Deployment object that describes what container is deployed and its configuration
    A Service object that acts as a load balancer for all the instances of the application created by the Deployment

Each instance of your application in a deployment is called a Pod.

<h2>step3<h2>
Deploy ActiveMQ

Let's start with ActiveMQ.

You should create a activemq-deployment.yaml file with the following content:


