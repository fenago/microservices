

<h2>Deploy Your Machine Learning Model as a REST API with Docker using pyhton
</h2>

<h2>****  PART1 ****</h2>
<h3>Machine Learning Model using REST API using FLASK</h3>
<h4>Step 1</h4>
First we need to install following libraries (Python 3) using pip3 command

```
pip3 install -U scikit-learn scipy matplotlib
pip3 install Flask
pip3 install Flask-RESTful
pip3 install -U flask-cors
```

<h4>Step 2</h4>
Create a new Python file `app.py` and insert the below  code in it 

```
from flask import Flask, request,jsonify
from flask_cors import CORS, cross_origin
from flask_restful import Resource, Api
from json import dumps
from model.Train import train_model
from sklearn.externals import joblib
import os
app = Flask(__name__)
api = Api(app)
CORS(app)


if not os.path.isfile('iris-model.model'):
    train_model()

model = joblib.load('iris-model.model')


class MakePrediction(Resource):
    @staticmethod
    def post():
        posted_data = request.get_json()
        sepal_length = posted_data['sepal_length']
        sepal_width = posted_data['sepal_width']
        petal_length = posted_data['petal_length']
        petal_width = posted_data['petal_width']

        prediction = model.predict([[sepal_length, sepal_width, petal_length, petal_width]])[0]
        if prediction == 0:
            predicted_class = 'Iris-setosa'
        elif prediction == 1:
            predicted_class = 'Iris-versicolor'
        else:
            predicted_class = 'Iris-virginica'

        response = jsonify({
            'Prediction': predicted_class
        })
        response.headers.add('Access-Control-Allow-Origin', '*')
        return response
@app.route("/")    
def hello():
    return jsonify({'text':'Hello World!'})



api.add_resource(MakePrediction, '/predict')


if __name__ == '__main__':
    app.run('0.0.0.0', 5002, debug=True)
```
<h4>Step 3</h4>
<h5>Predictions Scripts</h5>

    The goal is to load in the Iris dataset and use a simple Decision Tree Classifier to train the model. we will use joblib library to save the model once the training is complete, and  we will  also report the accuracy score back to the user
    
   Create new subfolder inside  project folder then create new  python file `Train.py` inside model folder and write the following code in  `model/Train.py` 
   
   ```
from sklearn import datasets
from sklearn.tree import DecisionTreeClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sklearn.externals import joblib


def train_model():
    iris_df = datasets.load_iris()

    x = iris_df.data
    y = iris_df.target

    X_train, X_test, y_train, y_test = train_test_split(x, y, test_size=0.25)
    dt = DecisionTreeClassifier().fit(X_train, y_train)
    preds = dt.predict(X_test)

    accuracy = accuracy_score(y_test, preds)
    joblib.dump(dt, 'iris-model.model')
    print('Model Training Finished.\n\tAccuracy obtained: {}'.format(accuracy))
   ```
   <h4>Step4</h4>
   now run the file `app.py` if no error occur open browser and navigate to `http://localhost:5002/` and you will see 'Hello World!'.
   Now our server is online and listening to request  we will disscuss code later on.
   
  <h2>****  PART2 ****</h2>
<h3>REST API client GUI using angular</h3>
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
we need to import httpclient, form module here
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
<h4>Step 5 </h4>
!!!Important!!!
make sure that flask server is up and running we disscussed in part1
!!!!!!!!!!!!!!!
Now run the command `ng serve` from terminal inside the angular project folder 
if everything works as expected then open browser and navigate to `http://localhost:4200/` and you will see the homepage with form to enter the data for prediction and you result will be on bottom coming from REST server

<h2>****  PART3 ****</h2>
<h3>Dockerize our application for ready to Deploy anywhere</h3>

   
