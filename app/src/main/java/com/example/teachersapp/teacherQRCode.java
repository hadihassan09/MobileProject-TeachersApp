package com.example.teachersapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class teacherQRCode extends AppCompatActivity {
    ImageView imageView;
    TextView teacherName,teacherId;
    ArrayList<String> dataGet;
    DataManeger M = new DataManeger();
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_qrcode);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Gson gson=new Gson();
        dataGet=gson.fromJson(data,ArrayList.class);


        final String Key = M.getAlphaNumericString(32);
        final Date Time = new Date();
        teacherId=findViewById(R.id.teacherID);
        teacherName=findViewById(R.id.teacherName);
        teacherName.setText("Doctors's Name: ".concat( dataGet.get(1)));
        teacherId.setText("Course ID: ".concat(dataGet.get(0).substring(1)));
        imageView=findViewById(R.id.qrCode);


        M.getReferenceTest().child("Courses").child(dataGet.get(0)).child("key").setValue(Key);

        M.getReferenceTest().child("Courses").child(dataGet.get(0)).child("time").setValue(Time);

        M.getReferenceTest().child("Courses").child(dataGet.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Info;
                Info = dataSnapshot.child("fullInfo").getValue(String.class);
                String []A = Info.split(" ");
                M.getReferenceTest().child("Courses").child(dataGet.get(0)).child("fullInfo").setValue((String.valueOf(Integer.parseInt(A[0])+1)
                                                                .concat(" ".concat(Key.concat(" ".concat(Time.toString()))))));/*.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });*/
                new QRGenerator(dataGet.get(0).concat(" ".concat(Key)),imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button button=findViewById(R.id.doneBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), mainTeacherActivity.class);
                startActivity(intent2);
                finish();
            }
        });

        //QRGenerator qrGenerator=new QRGenerator(dataGet.get(0).concat(" ".concat(Key)),imageView);
        Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT).show();

    }

}
