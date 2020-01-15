
<h2>REST API client GUI using angular</h2>
First we need to create a new angular project using following command in terminal to install angular and create new project 

<h4>Step 1</h4>

```
npm install -g @angular/cli
ng new RestClientAngularWeb
cd RestClientAngularWeb
ng serve
```
if everything works as expected then open browser and navigate to `http://localhost:4200/` and you should see the default angular homepage


<h4>Step 2</h4>
Now open the project folder with visual code or any of your favourite editor

Open the file `src/app/app.module.ts`
we need to import httpclient,FormsModule,ReactiveFormsModule
```
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';  //added line
import { AppComponent } from './app.component';
import { FormsModule,ReactiveFormsModule } from '@angular/forms';   //added line
@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule, //added line
    FormsModule,  //added line
    ReactiveFormsModule  //added line
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
```
<h4>Step 3</h4>
Now Open the file  `src/app/app.component.ts`

and remove all the code and paste the following code 
```
import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {FormGroup } from '@angular/forms';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
  app: FormGroup;
  prediction:any;
  result:any;
  constructor(private httpClient: HttpClient) {
  }

  ngOnInit() {
  }


  onSubmit(formData) {
    console.log(formData);
    this.httpClient.post('http://127.0.0.1:5002/predict',
    formData).subscribe(data => {
      this.prediction = data as JSON;
      this.result=this.prediction.Prediction;
      console.log(this.prediction.Prediction);
    })
  }

}

```
here we created a method `onSubmit(formData)` which will take the JSON data from the form and post it to our flask server `(ENDPOINT predict)` and then get the the result and display on GUI.Basically in this step we are calling RESTFUL api.If we have any new Model then we just need to call it api in this step
<h4>Step 4 </h4>
Now Open the file  `src/app/app.component.html`

and remove all the code and paste the following code 

```
<div style="text-align:center">
  <h1>
    Welcome to Angular + Python + Flask Demo!
  </h1>
</div>

<br>


<form #f="ngForm" (ngSubmit)="onSubmit(f.value)">
<br>
  <div>Sepal Length:<input type="text"     name="sepal_length" ngModel></div>
<br>
  <div>Sepal Width:&nbsp;<input type="text"     name="sepal_width"      ngModel></div>
<br>
  <div>Petal Length:<input type="text" name="petal_length" ngModel></div>
<br>
  <div>Petal Width :&nbsp;<input type="patextssword" name="petal_width"  ngModel></div>
<br>
  <button type="submit">Predict</button>
</form>
<br>
<div>
  
  <b><span>Prediction: {{result}}</span></b>
</div>

```
In this step we create a form to take input from user and submit to call FLASK REST API .we can modify the form to take more input if our model required 
 
<h4>Step 5 </h4>

!!!Important!!!
make sure that flask server is up and running we disscussed in part1
!!!!!!!!!!!!!!!


Now run the command `ng serve` from terminal inside the angular project folder 
if everything works as expected then open browser and navigate to `http://localhost:4200/` and you will see the homepage with form to enter the data for prediction and you result will be on bottom coming from REST server

<h4>Step 6 </h4>
Now   create  the `Dockerfile` and add following commands to docker
 
```
# base image
FROM node:12.2.0

# install chrome for protractor tests
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list'
RUN apt-get update && apt-get install -yq google-chrome-stable

# set working directory
WORKDIR /app

# add `/app/node_modules/.bin` to $PATH
ENV PATH /app/node_modules/.bin:$PATH

# install and cache app dependencies
COPY package.json /app/package.json
RUN npm install
RUN npm install -g @angular/cli

# add app
COPY . /app

# start app
CMD ng serve --host 0.0.0.0
```
 
Now we are done for this step.its time to create and deploy our docker image
<h4>Step 7</h4>
Create a .dockerignore as well and add following lines:

```
node_modules
.git
.gitignore
```

This will speed up the Docker build process as our local dependencies and git repo will not be sent to the Docker daemon.
<h4>Step 7 </h4>
<h5>Deploy with docker</h5>
Run Following command to create image of docker for our app (Run this command in terminal after nvaigating to project directory)

```
docker build -t angel:latest .
```
![https://github.com/fenago/microservices/blob/master/coin/mdimg/angularB1.png](https://github.com/fenago/microservices/blob/master/coin/mdimg/angularB1.png "Logo Title Text 1")
here `abcdef` is the name of image you change it too
now run the following command in terminal to up the app

```
docker run -v "${PWD}:/app" -v /app/node_modules -p 4200:4200 --rm angel:latest
```
![https://github.com/fenago/microservices/blob/master/coin/mdimg/angularB2.png](https://github.com/fenago/microservices/blob/master/coin/mdimg/angularB2.png "Logo Title Text 1")
now our app is listening on port 4200
to check the if your image is running or not  use the following command

```
docker ps -a
```
you will see the name of your image in list `angel` (in this case) and status should be up.Now navigate to `http://localhost:4200/` and you will see GUI with form to take parameters from user
![https://github.com/fenago/microservices/blob/master/coin/mdimg/angularGUI1.png](https://github.com/fenago/microservices/blob/master/coin/mdimg/angularGUI1.png "Logo Title Text 1")

![https://github.com/fenago/microservices/blob/master/coin/mdimg/angularGUI2.png](https://github.com/fenago/microservices/blob/master/coin/mdimg/angularGUI2.png "Logo Title Text 1")

