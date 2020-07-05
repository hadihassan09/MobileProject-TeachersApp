package com.example.teachersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class addCoursePage extends AppCompatActivity {
    private TextView coursename,courseid;
    private TextView add,cancel;
    private Course C = new Course();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_page);
        getSupportActionBar().hide();

        courseid=findViewById(R.id.courseIDtxt);
        coursename=findViewById(R.id.courseNametxt);
        add=findViewById(R.id.addCourseBtn);
        cancel=findViewById(R.id.cancelBtn);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(courseid.getText() != null && coursename.getText() != null
                            && !courseid.getText().toString().equals("") && !coursename.getText().toString().equals("")) {
                    C.setCID(courseid.getText().toString().trim());
                    C.setCname(coursename.getText().toString().trim());
                    C.setAttendanceCount("60");
                    C.setTime(new Date());
                    C.setKey(new DataManeger().getAlphaNumericString(32));
                    C.setCourseTimeOut("5");
                    FirebaseDatabase.getInstance().getReference().child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean flag = false;
                            for(DataSnapshot key:dataSnapshot.getChildren()){
                                if(key.getKey().toLowerCase().equals(courseid.getText().toString().toLowerCase())){
                                    flag = true;
                                    break;
                                }
                            }
                            if(!flag){
                                Intent x = new Intent(getApplicationContext(),addTeacherPage.class);
                                startActivityForResult(x,1);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Course Already Exists", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                    Toast.makeText(getApplicationContext(), "Please Fill In The Data.", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "No Course was Added.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                ArrayList<String> Arraylist = data.getStringArrayListExtra("result");
                C.setName(Arraylist.get(0));
                C.setAddress(Arraylist.get(1));
                C.setEmail(Arraylist.get(2));
                C.setPhone(Arraylist.get(3));
                C.setPassword(Arraylist.get(4));

                FirebaseDatabase.getInstance().getReference().child("Courses").child(C.getCID()).setValue(C);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "No Course was Added.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
