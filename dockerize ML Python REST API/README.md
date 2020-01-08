

<h2>Deploy Your Machine Learning Model as a REST API with Docker using pyhton
</h2>

<h3>****  PART1 ****</h3>
<h3>Machine Learning Model using REST API using FLASK</h3>
First we need to install following libraries (Python 3) using pip command

```
pip3 install -U scikit-learn scipy matplotlib
pip3 install Flask
pip install Flask-RESTful
```
<h2>Predictions Scripts</h2>

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
  <h3>****  PART2 ****</h3>
<h3>REST API client GUI using angular</h3>


<h3>****  PART3 ****</h3>
<h3>Dockerize our application for ready to Deploy anywhere</h3>

   
