import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http'; //added line
import { AppComponent } from './app.component';
import { FormsModule,ReactiveFormsModule } from '@angular/forms';//added line
@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,//added line
    FormsModule,//added line
    ReactiveFormsModule //added line
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
