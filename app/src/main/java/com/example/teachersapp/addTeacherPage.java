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

import java.util.ArrayList;

public class addTeacherPage extends AppCompatActivity {
    private TextView teacherName,teacherPhone,teacherAddress,teacherEmail,teacherPassword,add,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher_page);
        getSupportActionBar().hide();

        teacherName=findViewById(R.id.teacherNamee);
        teacherAddress=findViewById(R.id.teacherAddresss);
        teacherEmail=findViewById(R.id.teacherEmaill);
        teacherPhone=findViewById(R.id.teacherPhonee);
        teacherPassword=findViewById(R.id.teacherPasswordd);

        add=findViewById(R.id.addTeacherbtnn);
        cancel=findViewById(R.id.cancelBtn);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(teacherName.getText() != null && teacherAddress.getText() != null
                        && teacherEmail.getText() != null && teacherPhone.getText() != null
                                && teacherPassword.getText() != null && !teacherName.getText().toString().equals("")
                                    && !teacherAddress.getText().toString().equals("") && !teacherPassword.getText().toString().equals("")
                                          && !teacherEmail.getText().toString().equals("") && teacherPhone.getText() != "" ) {
                    final ArrayList<String> Arraylist = new ArrayList<>();
                    Arraylist.add(teacherName.getText().toString().trim());
                    Arraylist.add(teacherAddress.getText().toString().trim());
                    Arraylist.add(teacherEmail.getText().toString().trim());
                    Arraylist.add(teacherPhone.getText().toString().trim());
                    Arraylist.add(teacherPassword.getText().toString().trim());
                    if(isValidEmail(teacherEmail.getText().toString()) && isValidPhonenumber(teacherPhone.getText().toString())) {

                        FirebaseDatabase.getInstance().getReference().child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean flag = false;
                                for(DataSnapshot dc : dataSnapshot.getChildren()){
                                    if(dc.child("email").getValue(String.class).toLowerCase().equals(teacherEmail.getText().toString().toLowerCase())) {
                                        flag = true;
                                        break;
                                    }
                                }
                                if(!flag){
                                    Intent returnIntent = new Intent();
                                    returnIntent.putStringArrayListExtra("result", Arraylist);
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                                else
                                    Toast.makeText(getApplicationContext(), "Email Already Registered.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else{
                        if(!isValidEmail(teacherEmail.getText().toString()) && !isValidPhonenumber(teacherPhone.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Invalid Email and Phonenumber Format.", Toast.LENGTH_SHORT).show();
                        }
                        else if(!isValidEmail(teacherEmail.getText().toString()))
                            Toast.makeText(getApplicationContext(), "Invalid Email Format.", Toast.LENGTH_SHORT).show();
                        else if(!isValidPhonenumber(teacherPhone.getText().toString()))
                            Toast.makeText(getApplicationContext(), "Invalid Phonenumber Format", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "Please Fill The Data", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });
    }
    public boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    public boolean isValidPhonenumber(String phone){
        String regex = "^(?:\\+961|961)?(1|0?3[0-9]?|[4-6]|70|71|76|78|79|81?|9)\\d{6}$";
        return phone.matches(regex);
    }
}
