package com.example.teachersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class masterCourseViewPage extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> courseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_course_view_page);
        getSupportActionBar().hide();

        //Get Data and save them inn courseList

        listView=findViewById(R.id.courseView);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,courseList);
        listView.setAdapter(arrayAdapter);

    }
}
