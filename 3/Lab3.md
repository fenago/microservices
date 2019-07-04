<!----- Conversion time: 1.207 seconds.


Using this Markdown file:

1. Cut and paste this output into your source file.
2. See the notes and action items below regarding this conversion run.
3. Check the rendered output (headings, lists, code blocks, tables) for proper
   formatting and use a linkchecker before you publish this page.

Conversion notes:

* Docs to Markdown version 1.0β17
* Thu Jul 04 2019 14:29:21 GMT-0700 (PDT)
* Source doc: https://docs.google.com/open?id=1IWtasxgwfnmoyN_5MvbzWQRLz9L5lNXpZ9_5nkZcrnM
* This document has images: check for >>>>>  gd2md-html alert:  inline image link in generated source and store images to your server.
----->


<p style="color: red; font-weight: bold">>>>>>  gd2md-html alert:  ERRORs: 0; WARNINGs: 0; ALERTS: 3.</p>
<ul style="color: red; font-weight: bold"><li>See top comment block for details on ERRORs and WARNINGs. <li>In the converted Markdown or HTML, search for inline alerts that start with >>>>>  gd2md-html alert:  for specific instances that need correction.</ul>

<p style="color: red; font-weight: bold">Links to alert messages:</p><a href="#gdcalert1">alert1</a>
<a href="#gdcalert2">alert2</a>
<a href="#gdcalert3">alert3</a>

<p style="color: red; font-weight: bold">>>>>> PLEASE check and correct alert issues and delete this message and the inline alerts.<hr></p>


Lab 3 - Create a Spring Boot Java Microservice with STS



1. Step 1:  Launch STS4
2. Open STS, right-click within the **Project Explorer** window, navigate to **New** | **Project**, and 
3. Step 2: Launch Spring Starter Project
4. select **Spring Starter Project**, and click on **Next**:
5. Spring Starter Project is a basic template wizard that provides a number of other starter libraries to select from.
6. Type the project name as `module2.bootrest` or any other name of your choice. It is important to choose the packaging as JAR. In traditional web applications, a war file is created and then deployed to a servlet container, where Spring Boot packages all the dependencies to a self-contained, autonomous JAR file with an embedded HTTP listener.
7. Select 1.8 under **Java Version**. Java 1.8 is recommended for Spring 4 applications. Change the other Maven properties such as **Group**, **Artifact**, and **Package**, as shown in the following screenshot:

<p id="gdcalert1" ><span style="color: red; font-weight: bold">>>>>>  gd2md-html alert: inline image link here (to images/Lab-30.png). Store image on your image server and adjust path/filename if necessary. </span><br>(<a href="#">Back to top</a>)(<a href="#gdcalert2">Next alert</a>)<br><span style="color: red; font-weight: bold">>>>>> </span></p>


![alt_text](images/Lab-30.png "image_tooltip")

8. Once completed, click on **Next**.
9. The wizard will show the library options. In this case, as the REST service is developed, select **Web** under **Web**. This is an interesting step that tells Spring Boot that a Spring MVC web application is being developed so that Spring Boot can include the necessary libraries, including Tomcat as the HTTP listener and other configurations, as required:
10. 

<p id="gdcalert2" ><span style="color: red; font-weight: bold">>>>>>  gd2md-html alert: inline image link here (to images/Lab-31.png). Store image on your image server and adjust path/filename if necessary. </span><br>(<a href="#">Back to top</a>)(<a href="#gdcalert3">Next alert</a>)<br><span style="color: red; font-weight: bold">>>>>> </span></p>


![alt_text](images/Lab-31.png "image_tooltip")

11. Click on **Finish**.
12. This will generate a project named `module2.bootrest` in **Project Explorer** in STS:
13. 

<p id="gdcalert3" ><span style="color: red; font-weight: bold">>>>>>  gd2md-html alert: inline image link here (to images/Lab-32.png). Store image on your image server and adjust path/filename if necessary. </span><br>(<a href="#">Back to top</a>)(<a href="#gdcalert4">Next alert</a>)<br><span style="color: red; font-weight: bold">>>>>> </span></p>


![alt_text](images/Lab-32.png "image_tooltip")

14. Take a moment to examine the generated application. Files that are of interest are:
    *   `pom.xml`
    *   `Application.java`
    *   `Application.properties`
    *   `ApplicationTests.java`

<!-- Docs to Markdown version 1.0β17 -->
