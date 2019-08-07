<!----- Conversion time: 0.404 seconds.


Using this Markdown file:

1. Cut and paste this output into your source file.
2. See the notes and action items below regarding this conversion run.
3. Check the rendered output (headings, lists, code blocks, tables) for proper
   formatting and use a linkchecker before you publish this page.

Conversion notes:

* Docs to Markdown version 1.0β17
* Sat Jul 06 2019 13:05:50 GMT-0700 (PDT)
* Source doc: https://docs.google.com/open?id=1JWk3jR7lMH-V69xjznUWblG0O-LzHCCQMyuuBCL5Syk
----->


Lab 6 : Read Custom Property Files



1. Add the following property to the `application.properties` file:
2. 
3. `learningvoyage.customproperty=HiErnesto`
4. Then, edit the `GreetingController` class as follows:
5. 
6. ```
@Autowired
Environment env;

Greet greet(){
    logger.info("bootrest.customproperty "+ env.getProperty("bootrest.customproperty"));
    return new Greet("Hello World!");
}
```
7. Rerun the application. The log statement prints the custom variable in the console, as follows:
8. 
9. `org.rvslab.chapter2.GreetingController   : bootrest.customproperty hello`

<h3>Using a .yaml file for configuration</h3>


As an alternate to `application.properties`, one may use a `.yaml` file. YAML provides a JSON-like structured configuration compared to the flat properties file.

To see this in action, simply replace `application.properties` with `application.yaml` and add the following property:


```
server
  port: 9080
```


Rerun the application to see the port printed in the console.

<h3>Using multiple configuration profiles</h3>


Furthermore, it is possible to have different profiles such as development, testing, staging, production, and so on. These are logical names. Using these, one can configure different values for the same properties for different environments. This is quite handy when running the Spring Boot application against different environments. In such cases, there is no rebuild required when moving from one environment to another.

Update the `.yaml` file as follows. The Spring Boot group profiles properties based on the dotted separator:


```
spring:
    profiles: 
active:development
server:
      port: 9090
---

spring:
    profiles: 
active:production
server:
      port: 8080
```


Run the Spring Boot application as follows to see the use of profiles:


```
mvn -Dspring.profiles.active=production install
mvn -Dspring.profiles.active=development install
```


Active profiles can be specified programmatically using the `@ActiveProfiles` annotation, which is especially useful when running test cases, as follows:


```
@ActiveProfiles("test")
```


<h3>Other options to read properties</h3>


The properties can be loaded in a number of ways, such as the following:



*   Command-line parameters `(-Dhost.port =9090`)
*   Operating system environment variables
*   JNDI (`java:comp/env`)

<!-- Docs to Markdown version 1.0β17 -->
