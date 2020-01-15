I’ve gone ahead and made the following directory structure:
ML-Deploy
* model / Train.py
* app.py
Now if you have your Python installed through Anaconda, then you probably already have all the libraries pre-installed, except for Flask. So fire up the terminal and execute the following:
```
pip install Flask
pip install Flask-RESTful
```
That went well? Good. Let’s dive into some good stuff now.
<h4>Making a Basic Prediction Script</h4>
If you are following along with the directory structure, you should open up the model/Train.py file now. The goal is to load in the Iris dataset and use a simple Decision Tree Classifier to train the model. I will use joblib library to save the model once the training is complete, and I’ll also report the accuracy score back to the user.
Nothing complex here, as machine learning isn’t the point of the article, only the model deployment. Here’s the whole script:
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
<h4>Deploying</h4>
Now you’re ready to open the app.py file and do some imports. You’ll need the os module, a couple of things from Flask and Flask-RESTful, the model training script created 10 seconds ago, and the joblib to load in the trained model:
```
import os
from flask import Flask, jsonify, request
from flask_restful import Api, Resource
from model.Train import train_model
from sklearn.externals import joblib
```
Now you should make an instance of Flask and Api from Flask-RESTful. Nothing complex:
```
app = Flask(__name__)
api = Api(app)
```
The next thing to do is to check whether the model is already trained or not. In the Train.py you’ve declared that the model will be saved in the file iris-model.model, and if that file doesn’t exist, the model should be trained first. Once trained, you can load it in via joblib:
```
if not os.path.isfile('iris-model.model'):
    train_model()

model = joblib.load('iris-model.model')
```
Now you’ll need to declare a class for making predictions. Flask-RESTful uses this coding convention, so your class will need to inherit from the Flask-RESTful Resource module. Inside the class, you can declare get(), post(), or any other method for handling data.
We’ll use post(), so the data isn’t passed directly through the URL. You’ll need to fetch attributes from the user input (prediction is made based on attribute value the user has entered). Then, you can call .predict() function of the loaded model. Just because the target variable of this dataset is in the form (0, 1, 2) instead of (‘Iris-setosa’, ‘Iris-versicolor’, ‘Iris-virginica’), you’ll also want to address that. Finally, you can return JSON representation of the prediction:
```
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

        return jsonify({
            'Prediction': predicted_class
        })
```        
We’re almost there, so hang in tight! You’ll also need to declare a route, the part of URL which will be used to handle requests:
```
api.add_resource(MakePrediction, '/predict')
```
And the final thing is to tell Python to run the app in debug mode:
```
if __name__ == '__main__':
    app.run(debug=True)
```    
And that’s it. You’re ready to launch the model and make predictions, either through Postman or some other tool.
Just in case you missed something, here’s the entire app.py file:
```
import os
from flask import Flask, jsonify, request
from flask_restful import Api, Resource
from model.Train import train_model
from sklearn.externals import joblib

app = Flask(__name__)
api = Api(app)

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

        return jsonify({
            'Prediction': predicted_class
        })


api.add_resource(MakePrediction, '/predict')


if __name__ == '__main__':
    app.run(debug=True)
```    
Okay, are you ready?


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