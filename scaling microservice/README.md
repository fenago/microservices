When you design and build applications at scale, you deal with two significant challenges: scalability and robustness.

You should design your service so that even if it is subject to intermittent heavy loads, it continues to operate reliably.


you're tasked with the challenge of building such application.You're building a store where users can buy their favourite items.You build a microservice to render the web pages and serving the static assets.You also build a backend REST API to process the incoming requests.

.You want the two components to be separated because with the same REST API you could serve the website and mobile apps.You want the two components to be separated because with the same REST API you could serve the website and mobile apps

<h2>About this lab project</h2>

The service has three components: the front-end, the backend, and a message broker.

The front-end is a simple Spring Boot web app with the Thymeleaf templating engine.

The backend is a worker consuming messages from a queue.

And since Spring Boot has excellent integration with JSM, you could use that to send and receive asynchronous messages.

<h2>Why we need kubernate when we already have dockers???</h2>

 when managing applications that could have hundreds of containers working across multiple servers or hosts they are inadequate. So our applications weren’t exactly as amazing as we had thought. we needed something to manage the containers, because they needed to be connected to the outside world for tasks such as load balancing, distribution and scheduling. As our applications started to be used by more and more people, our services weren’t able to support a lot of requests; this is the part of the story where “Kubernetes” comes in.
 Kubernetes is an open source orchestrator developed by Google for deploying containerized applications. It provides developers and DevOps with the software they need to build and deploy distributed, scalable, and reliable systems.


Ok, so maybe you are asking yourself, “How could Kubernetes help me?” Kubernetes helped us with one constant in the life of every application: change. The only applications that do not change are dead ones; as long as an application is alive, new requirements will come in, more code will be shipped, and it will be packaged and deployed. This is the normal lifecycle of all applications, and developers around the world have to take this reality into account when looking for solutions.
<h2>how Kubernetes is structured, </h2>

● The smallest unit is the node. A node is a worker machine, a VM or physical machine, depending on the cluster.

● A group of nodes is a cluster.

● A container image wraps an application and its dependencies.

● One or more containers are wrapped into a higher-level structure called a “pod.”

● Pods are usually managed by one more layer of abstraction: deployment.

● A group of pods working as a load balancer to direct traffic to running containers is called “services.”

● A framework responsible for ensuring that a specific number of pod replicas are scheduled and running at any given time is a “replication controller.”

● The key-value tags (i.e. the names) assigned to identify pods, services, and replication controllers are known as “labels.”
