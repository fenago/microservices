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

![alt text](https://github.com/fenago/microservices/blob/master/dockercoins-diagram.svg "Logo Title Text 1")
<h2>**How to complete this lab**</h2>


Create a Spring Starter Project for rng service

# Welcome to StackEdit!

Hi! I'm your first Markdown file in **StackEdit**. If you want to learn about StackEdit, you can read me. If you want to play with Markdown, you can edit me. Once you have finished with me, you can create new files by opening the **file explorer** on the left corner of the navigation bar.


# Files

StackEdit stores your files in your browser, which means all your files are automatically saved locally and are accessible **offline!**

## Create files and folders

The file explorer is accessible using the button in left corner of the navigation bar. You can create a new file by clicking the **New file** button in the file explorer. You can also create folders by clicking the **New folder** button.

## Switch to another file

All your files and folders are presented as a tree in the file explorer. You can switch from one to another by clicking a file in the tree.

## Rename a file

You can rename the current file by clicking the file name in the navigation bar or by clicking the **Rename** button in the file explorer.

## Delete a file

You can delete the current file by clicking the **Remove** button in the file explorer. The file will be moved into the **Trash** folder and automatically deleted after 7 days of inactivity.

## Export a file

You can export the current file by clicking **Export to disk** in the menu. You can choose to export the file as plain Markdown, as HTML using a Handlebars template or as a PDF.


# Synchronization

Synchronization is one of the biggest features of StackEdit. It enables you to synchronize any file in your workspace with other files stored in your **Google Drive**, your **Dropbox** and your **GitHub** accounts. This allows you to keep writing on other devices, collaborate with people you share the file with, integrate easily into your workflow... The synchronization mechanism takes place every minute in the background, downloading, merging, and uploading file modifications.

There are two types of synchronization and they can complement each other:

- The workspace synchronization will sync all your files, folders and settings automatically. This will allow you to fetch your workspace on any other device.
	> To start syncing your workspace, just sign in with Google in the menu.

- The file synchronization will keep one file of the workspace synced with one or multiple files in **Google Drive**, **Dropbox** or **GitHub**.
	> Before starting to sync files, you must link an account in the **Synchronize** sub-menu.

## Open a file

You can open a file from **Google Drive**, **Dropbox** or **GitHub** by opening the **Synchronize** sub-menu and clicking **Open from**. Once opened in the workspace, any modification in the file will be automatically synced.

## Save a file

You can save any file of the workspace to **Google Drive**, **Dropbox** or **GitHub** by opening the **Synchronize** sub-menu and clicking **Save on**. Even if a file in the workspace is already synced, you can save it to another location. StackEdit can sync one file with multiple locations and accounts.

## Synchronize a file

Once your file is linked to a synchronized location, StackEdit will periodically synchronize it by downloading/uploading any modificati


  

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

 
  

<h2>**Make the application executable**</h2>

  
  


  

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

  
  


 
 
<h3>**Build an executable JAR**</h3>

  


  

 you can build the JAR file with `./mvn clean package`inside the folder. Then you can run the JAR file:

  
  

```

java -jar target/hasherService-0.0.1-SNAPSHOT.jar

``````

  

The service should be up and running within a few seconds.

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
docker build -t hasher-docker . 
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



  

<h2>**Create  worker microservice**</h2>

  
  Create a simple  Java Maven  Project for worker  service(remember this not spring project)
which will run in background and call `rng` and `hasher` service and then update `redis`


<h2>**add jedis and spring library in you maven project pom**</h2>

  
  
add two dependency in your pom so that it should download the required library 

  

pom.xml

```
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.worker.service</groupId>
  <artifactId>com.worker.service</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <name>worker</name>
  	<dependencies>
	   <dependency>
		    <groupId>org.springframework</groupId>
		    <artifactId>spring-web</artifactId>
		    <version>4.3.1.RELEASE</version>
		</dependency>
		<dependency>
		    <groupId>redis.clients</groupId>
		    <artifactId>jedis</artifactId>
		    <version>2.9.0</version>
		    <type>jar</type>
		</dependency>

	</dependencies>
	<build>
  <plugins>
  <plugin>
  <artifactId>maven-assembly-plugin</artifactId>
  <configuration>
    <archive>
      <manifest>
        <mainClass>com.worker.service.main</mainClass>
      </manifest>
    </archive>
    <descriptorRefs>
      <descriptorRef>jar-with-dependencies</descriptorRef>
    </descriptorRefs>
  </configuration>
  <executions>
    <execution>
      <id>make-assembly</id> <!-- this is used for inheritance merges -->
      <phase>package</phase> <!-- bind to the packaging phase -->
      <goals>
        <goal>single</goal>
      </goals>
    </execution>
  </executions>
</plugin>
    
  </plugins>
</build>
</project>

```

Save and update your project with maven........


<h2>**Create worker microservice**</h2>
Create worker java worker class and write the following code :

  
  
  

src/main/java/com/worker/service/worker.java (this is our worker thread

```

package com.worker.service;



import org.springframework.web.client.RestTemplate;


import redis.clients.jedis.Jedis;

public class Worker implements Runnable {
//	private RestTemplate restTemplate;
	Jedis jedis = null; //jedis is redis client library for java 
	private RestTemplate restTemplate; //it will use to make HTTP call and get result 

	public void run() {

		try {
			Thread.sleep(3000);

			jedis = new Jedis("redis");
			this.restTemplate = new RestTemplate();	
			startWorking();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void startWorking() {

		while (true) {  //forever mining loop
			try {

				work_loop(1000); //interval of 1000 milisec or 1 sec 
			} catch (Exception e) {


				// TODO Auto-generated catch block
				try {
					Thread.sleep(1000); // restart  the loop after 1sec
					System.out.println("trying again ............. "+e.getMessage()+" "+ e.getCause());
				} catch (InterruptedExceptipackage com.hasher.service;


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
add jedis and spring library in you maven project pomadd jedis and spring library in you maven project pom
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
		

    
}
				
					  
	

    
}
				} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	

    
}
				return "error............";
					
					
						}



    
}on e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}

	private void work_loop(int interval) {

		int deadline = 0;
		int loops_done = 0;
		while (true)
		{
			if (System.currentTimeMillis() > deadline) { //check if 1 sec is complete or not 
				System.out.println("units of work done, updating hash counter ............."+ loops_done);
				jedis.incrBy("hashes", loops_done); //increment the hashes loop done in one sec  
				loops_done = 0;
				deadline = (int) (System.currentTimeMillis() + interval);

			}
			try {
				work_once(); //coin mining function calling
				loops_done += 1;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("trying again "+e.getMessage()+" "+ e.getCause());
			}
			
		}
		
	}

	private void work_once() throws InterruptedException {
		
        System.out.println("Doing one unit of work");
		Thread.sleep(100);
		
	    String random_bytes = get_random_bytes(); //get random numbers from rng microservice running in docker
	    System.out.println("rng rec:  "+ random_bytes);
	    String  hex_hash = hash_bytes(random_bytes);//get hash numbers from hasher microservice running in docker
	    System.out.println("hashers rec: "+hex_hash);
	    if(!hex_hash.startsWith("0")) //coin must start with zero 
	    {
	    	System.out.println("No coin found");
	        return;
	    }
	    	
	        		System.out.println("Coin found: { "+ hex_hash +" }...");
	     long created = jedis.hset("wallet", hex_hash, random_bytes);
	    if (created>0){ //redis status if aready exist it will not added
	    	
	    	System.out.println("We already had that coin");
	    }
	    	

	}

	String get_random_bytes() {
		String url= "http://rng:8080/rng/{howmany}";
		// if you are running worker class outside docker then use following url
		//  String url= "http://localhost:8080/rng/{howmany}";
		String result = restTemplate.getForObject(url, String.class, "32");
		return result;

	}

	String hash_bytes(String random_bytes) {
		String url= "http://hasher:8080/hasher/{data}";
		// if you are running worker class outside docker then use following url
		//  String url= "http://localhost:8080/hasher/{data}";
		
		String result = restTemplate.getForObject(url, String.class, random_bytes);
		return result;

	}

}


```

 
  

<h2>**calling worker thread from main class**</h2>

  
  


  

src/main/java/com/worker/service/main.java

```

package com.worker.service;


public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread worker = new Thread (new Worker());
		worker.start();
	}

}

```

  
  



 
 
<h3>**Build an executable JAR**</h3>

  


  

 you can build the JAR file with `./mvn clean package`inside the folder. You will see two jar inside target folder newly created `com.worker.service-0.0.1-SNAPSHOT.jar` and   `com.worker.service-0.0.1-SNAPSHOT-jar-with-dependencies.jar`

data
  we will use second one with all dependencies
  `com.worker.service-0.0.1-SNAPSHOT-jar-with-dependencies.jar`


  



<h2>**Create Docker File**</h2>
Now create a text file and name it  Dockerfile  and add following lines



```
#pull base image
FROM openjdk:8-jdk-alpine

#maintainer 
MAINTAINER dev.asadriaz@gmail.com



#default command
CMD java -jar /target/com.worker.service-0.0.1-SNAPSHOT-jar-with-dependencies.jar

#copy hello world to docker image
ADD ./target//target/com.worker.service-0.0.1-SNAPSHOT-jar-with-dependencies.jar /target//target/com.worker.service-0.0.1-SNAPSHOT-jar-with-dependencies.jar

```
  
our worker service is complete now next step is to create webui to view graphical representation of the worker service  in browser



