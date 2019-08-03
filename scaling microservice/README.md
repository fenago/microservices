When you design and build applications at scale, you deal with two significant challenges: scalability and robustness.

You should design your service so that even if it is subject to intermittent heavy loads, it continues to operate reliably.


you're tasked with the challenge of building such application.You're building a store where users can buy their favourite items.You build a microservice to render the web pages and serving the static assets.You also build a backend REST API to process the incoming requests.

.You want the two components to be separated because with the same REST API you could serve the website and mobile apps.You want the two components to be separated because with the same REST API you could serve the website and mobile apps

<h2>About this lab project</h2>

The service has three components: the front-end, the backend, and a message broker.

The front-end is a simple Spring Boot web app with the Thymeleaf templating engine.

The backend is a worker consuming messages from a queue.

And since Spring Boot has excellent integration with JSM, you could use that to send and receive asynchronous messages.
