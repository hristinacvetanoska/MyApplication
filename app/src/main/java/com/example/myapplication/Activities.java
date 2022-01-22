package com.example.myapplication;


public  class Activities {
String title,description, deadline, frequencyOfActivity,urgencyOfActivity, id;
public Activities(){

}

public Activities(String title, String description, String deadLine, String frequencyOfActivity, String urgencyOfActivity){
    this.title=title;
    this.description=description;
    this.deadline = deadLine;
    this.frequencyOfActivity=frequencyOfActivity;
    this.urgencyOfActivity=urgencyOfActivity;
}
}