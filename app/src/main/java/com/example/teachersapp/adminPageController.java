package com.example.teachersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class adminPageController extends AppCompatActivity {
    private TextView addTeacher, viewAllTeachers;
    private Button logout;
    private DataManeger adminDataManeger=new DataManeger();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page_controller);
        getSupportActionBar().hide();

        addTeacher=findViewById(R.id.addTeacher);
        viewAllTeachers=findViewById(R.id.viewAllTeachers);
        logout=findViewById(R.id.logout);

        addTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),addCoursePage.class);
                startActivity(intent);
            }
        });

        viewAllTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), masterTeacherViewPage.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
