package com.example.teachersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class mainTeacherActivity extends AppCompatActivity {

    private  String data, teacherId;

    private ArrayList getdata;

    private TextView name,id,phone,address,email,gender,generateBrCode,viewStudents,logout,editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_teacher);
        getSupportActionBar().hide();

        viewStudents = findViewById(R.id.viewStudentsBtn);
        name=findViewById(R.id.teacherTXT);
        id=findViewById(R.id.idTXT);
        phone=findViewById(R.id.phoneTXT);
        address=findViewById(R.id.addressTXT);
        gender=findViewById(R.id.genderTXT);
        email=findViewById(R.id.emailTXT);
        generateBrCode=findViewById(R.id.generateQRCode);
        logout=findViewById(R.id.logoutBtn);
        editProfile=findViewById(R.id.editInfoBtn);

        final Intent intent3=new Intent(this,teacherQRCode.class);


        data=savedSharedPreferences.getUserName(this);
        Gson gson=new Gson();
        getdata=gson.fromJson(data, ArrayList.class);

        teacherId=getdata.get(0).toString();
        id.setText(getdata.get(0).toString());
        name.setText(getdata.get(1).toString());
        email.setText("Email Address:"+getdata.get(2).toString());
        phone.setText("Phone:"+getdata.get(3).toString());
        address.setText("Address:"+getdata.get(4).toString());


        Toast.makeText(this, getdata.toString(), Toast.LENGTH_SHORT).show();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Courses").child(getdata.get(0).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Intent intent=new Intent(getApplicationContext() ,editTeacherPage.class);
                        intent.putExtra("fullinfo",dataSnapshot.child("fullInfo").getValue(String.class));
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        viewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),viewStudentsPage.class);
                intent.putExtra("ID",getdata.get(0).toString());
                startActivity(intent);
               // dataManeger.getAllStudentsNames(getApplicationContext(),teacherId);
            }
        });

        generateBrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mainTeacherActivity.this, data, Toast.LENGTH_SHORT).show();
                intent3.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent3.putExtra("data",data);
                startActivity(intent3);
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                savedSharedPreferences.setUserName(getApplicationContext(),"");
                //savedSharedPreferences.clear(getApplicationContext());
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
