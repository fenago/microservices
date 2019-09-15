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

![https://github.com/fenago/microservices/blob/master/part%202%20scaling%20microservice/mdimg/1.png](https://github.com/fenago/microservices/blob/master/part%202%20scaling%20microservice/mdimg/1.png"Logo Title Text 1")
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

<h2>step4</h2>
Deploy ActiveMQ

Let's start with ActiveMQ.

You should create a activemq-deployment.yaml file with the following content:

```

apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: queue
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: queue
    spec:
      containers:
      - name: web
        image: webcenter/activemq:5.14.3
        imagePullPolicy: IfNotPresent
        ports:
          - containerPort: 61616
        resources:
          limits:
            memory: 512Mi

apiVersion: v1
kind: Service
metadata:
  name: queue
spec:
  ports:
  - port: 61616
    targetPort: 61616
  selector:
    app: queue
  
```

The template is verbose but straightforward to read:

    you asked for an activemq container from the official registry named webcenter/activemq
    the container exposes the message broker on port 61616
    there're 512MB of memory allocated for the container
    you asked for a single replica — a single instance of your application
    
        you created a load balancer that exposes port 61616
    the incoming traffic is distributed to all Pods (see deployment above) that has a label of type app: queue
    the targetPort is the port exposed by the Pods
    
    <h2>step4a</h2>
    create the resources with:
    ` kubectl create -f activemq-deployment.yaml`
   verify that one instance of the database is running with:
   `kubectl get pods -l=app=queue`
    <h2>step5</h2>
Create a `fe-deployment.yaml` file with the following content:
```
---
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: frontend
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: frontend
    spec:
      containers:
      - name: frontend
        image: spring-boot-hpa
        imagePullPolicy: IfNotPresent
        env:
        - name: ACTIVEMQ_BROKER_URL
          value: "tcp://queue:61616"
        - name: STORE_ENABLED
          value: "true"
        - name: WORKER_ENABLED
          value: "false"
        ports:
          - containerPort: 8080
        readinessProbe:
          initialDelaySeconds: 5
          periodSeconds: 5
          httpGet:
            path: /health
            port: 8080
        resources:
          limits:
            memory: 512Mi
---
apiVersion: v1
kind: Service
metadata:
  name: frontend
spec:
  ports:
  - nodePort: 32000
    port: 80
    targetPort: 8080
  selector:
    app: frontend
  type: NodePort
```
The Deployment looks a lot like the previous one.

There're some new fields, though:

    there's a section where you can inject environment variables
    there's the liveness probe that tells you when the application is ready to accept traffic<>
    
    
<h2>Step 5A</h2>
create the resources with:
`kubectl create -f fe-deployment.yaml`
You can verify that one instance of the front-end application is running with:
`kubectl get pods -l=app=fe`


<h2>Step6</h2>
Create a `backend-deployment.yaml` file with the following content:
    
   ```
   ---
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: backend
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: backend
      annotations:
        prometheus.io/scrape: 'true'
    spec:
      containers:
      - name: backend
        image: spring-boot-hpa
        imagePullPolicy: IfNotPresent
        env:
        - name: ACTIVEMQ_BROKER_URL
          value: "tcp://queue:61616"
        - name: STORE_ENABLED
          value: "false"
        - name: WORKER_ENABLED
          value: "true"
        ports:
          - containerPort: 8080
        readinessProbe:
          initialDelaySeconds: 5
          periodSeconds: 5
          httpGet:
            path: /health
            port: 8080
        resources:
          limits:
            memory: 256Mi
---
apiVersion: v1
kind: Service
metadata:
  name: backend
spec:
  ports:
  - nodePort: 31000
    port: 80
    targetPort: 8080
  selector:
    app: backend
  type: NodePort
   ```
   <step6a>
    You can create the resources with:
    `kubectl create -f backend-deployment.yaml`
    You can verify that one instance of the backend is running with:
    `kubectl get pods -l=app=backend`
   
Deployment completed.

Does it really work, though?
<step7>
You can visit the application in your browser with the following command: 
    `minikube service backend`
 and 
  `minikube service frontend`
  
 If it works, you should try to buy some items!

Is the worker processing transactions?

Yes, given enough time, the worker will process all of the pending messages.

Congratulations!

You just deployed the application to Kubernetes!


<h1>Scaling manually to meet increasing demand</h1>
A single worker may not be able to handle a large number of messages.

In fact, it can only handle one message at the time.

If you decide to buy thousands of items, it will take hours before the queue is cleared.

At this point you have two options:

    you can manually scale up and down
    you can create autoscaling rules to scale up or down automatically

Let's start with the basics first.
`kubectl scale --replicas=5 deployment/backend`
You can verify that Kubernetes created five more instances with:
`kubectl get pods`
And the application can process five times more messages.

Once the workers drained the queue, you can scale down with:
`kubectl scale --replicas=1 deployment/backend`
Manually scaling up and down is great — if you know when the most traffic hits your service.
