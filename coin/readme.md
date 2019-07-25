## What's this application?

-   It is a DockerCoin miner! 
    
-   How DockerCoins works:
    
    -   generate a few random bytes
        
    -   hash these bytes
        
    -   increment a counter (to keep track of speed)
        
    -   repeat forever!
        

## DockerCoins in the microservices era

-   DockerCoins is made of 5 services:
    
    -   `rng` = web service generating random bytes
        
    -   `hasher` = web service computing hash of POSTed data
        
    -   `worker` = background process calling `rng` and `hasher`
        
    -   `webui` = web interface to watch progress
        
    -   `redis` = data store (holds a counter updated by `worker`)
        
-   These 5 services are visible in the application's Compose file, [docker-compose.yml]
  
  
## How DockerCoins works

-   `worker` invokes web service `rng` to generate random bytes
    
-   `worker` invokes web service `hasher` to hash these bytes
    
-   `worker` does this in an infinite loop
    
-   every second, `worker` updates `redis` to indicate how many loops were done
    
-   `webui` queries `redis`, and computes and exposes "hashing speed" in our browser

*** See the image***
https://github.com/fenago/microservices/blob/master/dockercoins-diagram.svg

<h2>**How to complete this lab**</h2>

 <h2>**Create Random Number Generator Service**</h2>
  

Create a Spring Starter Project for rng service

  

<h2>**Create a Random Number Generator microservice**</h2>

  
  

Create a new controller for your Spring application:

  
  
  

src/main/java/com/rnd/service/HelloController.java (or wherever your Controller resides)

```

package com.rnd.service;


import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class HelloController {
    
   
        @RequestMapping("/rng")
	@ResponseBody
	String home() {
		return "RNG running on rng";
	}
	@RequestMapping("/rng/{howMany}")
	@ResponseBody
	public String getFRandomNumber(
			  @PathVariable("howMany") int howMany) {
		
		SecureRandom rnd = new SecureRandom();
		byte[] token = new byte[howMany];
		rnd.nextBytes(token);
			    return  Base64.getEncoder().encodeToString(token);
			}
	    
}

```

  *  `@RequestMapping` his annotation maps HTTP requests to handler methods of MVC and REST controllers with input coming from request (as you can see "rng/{how many}"   ).

*  `@ResponseBody` annotation tells a controller that the object returned is automatically serialized into JSON and passed back into the _HttpResponse_ object.
  

<h2>**Make the application executable**</h2>

  
  

Although it is possible to package this service as a traditional [WAR](https://spring.io/understanding/WAR) file for deployment to an external application server, the simpler approach demonstrated below creates a standalone application. You package everything in a single, executable JAR file, driven by a good old Java `main()` method. Along the way, you use Spring’s support for embedding the [Tomcat](https://spring.io/understanding/Tomcat) servlet container as the HTTP runtime, instead of deploying to an external instance.

  

src/main/java/com/rnd/service/RngServiceApplication.java

```

package com.rnd.service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class RngServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RngServiceApplication.class, args);
	}
	
	

}

```

  
  

`@SpringBootApplication` is a convenience annotation that adds all of the following:

  
  
  

*  `@Configuration` tags the class as a source of bean definitions for the application context.

*  `@EnableAutoConfiguration` tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.



*  `@ComponentScan` tells Spring to look for other components, configurations, and services in the `hello` package, allowing it to find the `HelloController`.

 
 
<h3>**Build an executable JAR**</h3>

  
  

 You can run the application from the command line with Gradle or Maven. Or you can build a single executable JAR file that contains all the necessary dependencies, classes, and resources, and run that. This makes it easy to ship, version, and deploy the service as an application throughout the development lifecycle, across different environments, and so forth.

  

 you can build the JAR file with `./mvn clean package`inside the folder. Then you can run the JAR file:

  
  

```

java -jar target/rngService-0.0.1-SNAPSHOT.jar

``````

  

The procedure above will create a runnable JAR (microservice).

  
  

Logging output is displayed. The service should be up and running within a few seconds.

Open browser and go to link  https://localhost:8080/rng

 - you should see the output!   `
RNG running on rng
` 
- Now enter   https://localhost:8080/rng/32 in browser you will see the random number generated by our  rng microservice and response back to browser

<h2>**Create Docker File**</h2>
Now create a text file and name it  Dockerfile  and add following lines



```
#pull base image
FROM openjdk:8-jdk-alpine

#maintainer add your email id here 
MAINTAINER xyz@email.com

#expose port 8080 for ouside world
EXPOSE 8080

#default command to run jar
CMD java -jar /target/rngService-0.0.1-SNAPSHOT.jar

#copy hello world to docker image
ADD ./target/rngService-0.0.1-SNAPSHOT.jar /target/rngService-0.0.1-SNAPSHOT.jar

```
  
  Let’s now build the docker image by typing the following command -
    `
docker build -t rng-docker . 
`

That’s it. You can now see the list of all the docker images on your system using the following command
`
docker image ls
`

### 4. Running the docker image

Once you have a docker image, you can run it using `docker run` command like so -

`
 docker run -p 8080:8080 rng-docker
`
Now your random generator service container is up and running you can see again  by open browser and go to link [https://localhost:8080/rng](https://localhost:8080/rng)








  

Create a Spring Starter Project for rng service

  

<h2>**Create a Hashing Number Generator microservice**</h2>

  
  

Create a new controller for your Spring application:

  
  
  

src/main/java/com/hasher/service/HelloController.java (or wherever your Controller resides)

```

package com.hasher.service;


import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class HelloController {
    
	// hasher........................//////////////////////////////////////////////////////////////
				
	            @RequestMapping("/hasher")
				@ResponseBody
				String hellohasher() {
					return "hasher running on hasher";
				}
				@RequestMapping("/hasher/{data}")
				@ResponseBody
				
				public String gethash(
						  @PathVariable("data") String data) {
					MessageDigest digest;
					try {
						digest = MessageDigest.getInstance("SHA-256");
						byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
						  return  Base64.getEncoder().encodeToString(hash);
						
					  
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "error............";
					
					
						}



    
}

```

  *  `@RequestMapping` his annotation maps HTTP requests to handler methods of MVC and REST controllers with input coming from request (as you can see "/hasher/{data}"   ).

*  `@ResponseBody` annotation tells a controller that the object returned is automatically serialized into JSON and passed back into the _Http Response_ object.
  

<h2>**Make the application executable**</h2>

  
  

Although it is possible to package this service as a traditional [WAR](https://spring.io/understanding/WAR) file for deployment to an external application server, the simpler approach demonstrated below creates a standalone application. You package everything in a single, executable JAR file, driven by a good old Java `main()` method. Along the way, you use Spring’s support for embedding the [Tomcat](https://spring.io/understanding/Tomcat) servlet container as the HTTP runtime, instead of deploying to an external instance.

  

src/main/java/com/hasher/service/HasherServiceApplication.java

```

package com.hasher.service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class HasherServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HasherServiceApplication.class, args);
	}
	
	

}

```

  
  

`@SpringBootApplication` is a convenience annotation that adds all of the following:

  
  
  

*  `@Configuration` tags the class as a source of bean definitions for the application context.

*  `@EnableAutoConfiguration` tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.



*  `@ComponentScan` tells Spring to look for other components, configurations, and services in the `hello` package, allowing it to find the `HelloController`.

 
 
<h3>**Build an executable JAR**</h3>

  
  

 You can run the application from the command line with Gradle or Maven. Or you can build a single executable JAR file that contains all the necessary dependencies, classes, and resources, and run that. This makes it easy to ship, version, and deploy the service as an application throughout the development lifecycle, across different environments, and so forth.

  

 you can build the JAR file with `./mvn clean package`inside the folder. Then you can run the JAR file:

  
  

```

java -jar target/hasherService-0.0.1-SNAPSHOT.jar

``````

  

The procedure above will create a runnable JAR (microservice).

  
  

Logging output is displayed. The service should be up and running within a few seconds.

Open browser and go to link  https://localhost:8080/hasher

 - you should see the output!   `
hasher running on hasher
` 
- Now enter   https://localhost:8080/hasher/akdf2373vkwqekf in browser you will see the hash number generated by our  hasher microservice and response back to browser

<h2>**Create Docker File**</h2>
Now create a text file and name it  Dockerfile  and add following lines



```
#pull base image
FROM openjdk:8-jdk-alpine

#maintainer add your email id here 
MAINTAINER xyz@email.com

#expose port 8080 for ouside world
EXPOSE 8080

#default command to run jar
CMD java -jar /target/hasherService-0.0.1-SNAPSHOT.jar

#copy hello world to docker image
ADD ./target/hasherService-0.0.1-SNAPSHOT.jar /target/hasherService-0.0.1-SNAPSHOT.jar

```
  
  Let’s now build the docker image by typing the following command -
    `
docker build -t rng-docker . 
`

That’s it. You can now see the list of all the docker images on your system using the following command
`
docker image ls
`

### 4. Running the docker image

Once you have a docker image, you can run it using `docker run` command like so -

`
 docker run -p 8080:8080 hasher-docker
`
Now your random generator service container is up and running you can see again  by open browser and go to link [https://localhost:8080/hasher](https://localhost:8080/hasher)


