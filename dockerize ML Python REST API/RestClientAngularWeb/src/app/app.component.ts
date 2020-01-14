import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Validators, FormBuilder, FormGroup, FormControl } from '@angular/forms';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
  app: FormGroup;
  sepal_length=1.0
  sepal_width=1.2
  petal_length=1.0
        petal_width=5.3

  serverData: JSON;
  employeeData: JSON;
  employee:JSON;
  prediction:any;
  result:any;
  constructor(private httpClient: HttpClient) {
  }

  ngOnInit() {
  }

  sayHi() {
    this.httpClient.get('http://127.0.0.1:5002/').subscribe(data => {
      this.serverData = data as JSON;
      console.log(this.serverData);
    })
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
  getAllEmployees() {
    this.httpClient.get('http://127.0.0.1:5002/employees').subscribe(data => {
      this.employeeData = data as JSON;
      console.log(this.employeeData);
    })
  }
  getEmployee() {
    this.httpClient.get('http://127.0.0.1:5002/employees/1').subscribe(data => {
      this.employee = data as JSON;
      console.log(this.employee);
    })
  }
  getpredict() {
    this.httpClient.post('http://127.0.0.1:5002/predict',{
      
        // "sepal_length":1.0,
        // "sepal_width":1.2,
        // "petal_length":1.0,
        // "petal_width":5.3
        "sepal_length":this.sepal_length,
        "sepal_width":this.sepal_width,
        "petal_length":this.petal_length,
        "petal_width":this.petal_width
      
    }).subscribe(data => {
      this.employee = data as JSON;
      console.log(this.employee);
    })
  }
}
