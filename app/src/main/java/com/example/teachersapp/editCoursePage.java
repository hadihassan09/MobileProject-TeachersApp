package com.example.teachersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class editCoursePage extends AppCompatActivity {
    private EditText courseName,courseID,courseKeyTimeout,courseMaxAttendance;
    private TextView confirm,cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course_page);
        getSupportActionBar().hide();

        DataManeger M = new DataManeger();
        final String key = M.getAlphaNumericString(32);
        final Date time = new Date();

        courseName=findViewById(R.id.courseNameTXT);
        courseID=findViewById(R.id.courseIdTXT);
        courseKeyTimeout=findViewById(R.id.courseKeyTimeoutTXT);
        courseMaxAttendance=findViewById(R.id.maxAttendTXT);
        confirm=findViewById(R.id.editCourseBtn);
        cancel=findViewById(R.id.cancelBtn);

        final String id = getIntent().getStringExtra("ID");

        courseID.setText(id.substring(1));
        FirebaseDatabase.getInstance().getReference().child("Courses").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseName.setText(dataSnapshot.child("cname").getValue(String.class));
                courseKeyTimeout.setText(dataSnapshot.child("CourseTimeOut").getValue(String.class));
                courseMaxAttendance.setText(dataSnapshot.child("attendanceCount").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(courseID.getText().toString().equals("") || courseKeyTimeout.getText().toString().equals("")
                    || courseMaxAttendance.getText().toString().equals("") || courseName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please fill in the data", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean flag = false;
                            if (!courseID.getText().toString().toLowerCase().equals(id.toLowerCase())) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (ds.getKey().toLowerCase().equals(courseID.getText().toString().toLowerCase())){
                                        flag = true;
                                        break;
                                    }
                                }
                                if(!flag){
                                    final DatabaseReference editor = FirebaseDatabase.getInstance().getReference().child("Courses").child(courseID.getText().toString());
                                    editor.child("cname").setValue(courseName.getText().toString());
                                    editor.child("CourseTimeOut").setValue(courseKeyTimeout.getText().toString());
                                    editor.child("attendanceCount").setValue(courseMaxAttendance.getText().toString());
                                    editor.child("cid").setValue(courseID.getText().toString());
                                    editor.child("key").setValue(key);
                                    editor.child("time").setValue(time);
                                    Intent returnIntent = new Intent();
                                    ArrayList<String> S = new ArrayList<>();

                                    S.add("S".concat(courseID.getText().toString()));
                                    S.add(key);
                                    S.add(time.toString());

                                    returnIntent.putStringArrayListExtra("result",S);
                                    setResult(Activity.RESULT_OK,returnIntent);
                                    finish();
                                }else
                                    Toast.makeText(getApplicationContext(), "Course ID Already Exists.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                DatabaseReference editor = FirebaseDatabase.getInstance().getReference().child("Courses").child(id);
                                editor.child("cname").setValue(courseName.getText().toString());
                                editor.child("CourseTimeOut").setValue(courseKeyTimeout.getText().toString());
                                editor.child("attendanceCount").setValue(courseMaxAttendance.getText().toString());
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
