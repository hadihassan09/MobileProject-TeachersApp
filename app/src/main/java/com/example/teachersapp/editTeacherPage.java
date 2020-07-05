package com.example.teachersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class editTeacherPage extends AppCompatActivity {
    private TextView teacherName,teacherPhone,teacherPassword,teacherAddress,teacherEmail;
    private TextView confirm,cancel;
    private Button editCourse;
    private String fullinfo;
    private ArrayList<String> Arraylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teacher_page);
        getSupportActionBar().hide();



        teacherName=findViewById(R.id.teacherNameTXT);
        teacherAddress=findViewById(R.id.teacherAddressTXT);
        teacherPassword=findViewById(R.id.teacherPasswordTXT);
        teacherPhone=findViewById(R.id.teacherPhoneTXT);
        teacherEmail=findViewById(R.id.teacherEmailTXT);
        confirm=findViewById(R.id.editTeacherBtn);
        cancel=findViewById(R.id.cancelBtn);
        editCourse=findViewById(R.id.editCourse);

        fullinfo = getIntent().getStringExtra("fullinfo");

        String data=savedSharedPreferences.getUserName(this);
        final Gson gson=new Gson();
        Arraylist =gson.fromJson(data, ArrayList.class);

        Toast.makeText(getApplicationContext(), Arraylist.toString(), Toast.LENGTH_SHORT).show();
        //0 id
        //1 name
        //2 email
        //3 phone
        //4 address
        //5 password

        teacherName.setText(Arraylist.get(1));
        teacherAddress.setText(Arraylist.get(4));
        teacherEmail.setText(Arraylist.get(2));
        teacherPassword.setText(Arraylist.get(5));
        teacherPhone.setText(Arraylist.get(3));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(teacherEmail.getText().equals("") || teacherName.getText().equals("") || teacherPassword.
                        getText().equals("") || teacherPhone.getText().equals("") ) {
                    Toast.makeText(getApplicationContext(), "Please fill in the data.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(isValidEmail(teacherEmail.getText().toString()) && isValidPhonenumber(teacherPhone.getText().toString())) {
                        FirebaseDatabase.getInstance().getReference().child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean flag = false;
                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                    if (d.getKey().toLowerCase().equals(Arraylist.get(0).toLowerCase()))
                                        continue;
                                    if (d.child("email").getValue(String.class).toLowerCase().equals(teacherEmail.getText().toString().toLowerCase())) {
                                        flag = true;
                                        break;
                                    }
                                }
                                if (flag) {
                                    Toast.makeText(getApplicationContext(), "Email Already Exists.", Toast.LENGTH_SHORT).show();
                                } else {
                                    DatabaseReference editor = FirebaseDatabase.getInstance().getReference().child("Courses").child(Arraylist.get(0));
                                    Arraylist.clear();
                                    Arraylist.add(editor.getKey());
                                    Arraylist.add(teacherName.getText().toString());
                                    Arraylist.add(teacherEmail.getText().toString());
                                    Arraylist.add(teacherPhone.getText().toString());
                                    Arraylist.add(teacherAddress.getText().toString());
                                    Arraylist.add(teacherPassword.getText().toString());
                                    Gson gson = new Gson();
                                    String json = gson.toJson(Arraylist);
                                    savedSharedPreferences.setUserName(getApplicationContext(), json);
                                    editor.child("address").setValue(teacherAddress.getText().toString());
                                    editor.child("email").setValue(teacherEmail.getText().toString());
                                    editor.child("phone").setValue(teacherPhone.getText().toString());
                                    editor.child("password").setValue(teacherPassword.getText().toString());
                                    editor.child("name").setValue(teacherName.getText().toString());
                                    Intent backtomain = new Intent(getApplicationContext(),mainTeacherActivity.class);
                                    startActivity(backtomain);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else{
                        if(!isValidEmail(teacherEmail.getText().toString()) && !isValidPhonenumber(teacherPhone.getText().toString())){
                            Toast.makeText(getApplicationContext(),"Wrong Email and Phonenumber format",Toast.LENGTH_SHORT).show();
                        }
                        else if(!isValidPhonenumber(teacherPhone.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Wrong Phonenumber Format", Toast.LENGTH_SHORT).show();
                        }
                        else if(!isValidEmail(teacherEmail.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Wrong Email Format.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), editCoursePage.class);
                intent.putExtra("ID",Arraylist.get(0));
                startActivityForResult(intent, 1);
            }
        });




    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                final String oldId;
                final ArrayList<String> result=data.getStringArrayListExtra("result");
                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT).show();
                oldId = Arraylist.get(0);
                DatabaseReference D = FirebaseDatabase.getInstance().getReference().child("Courses");
                D.child(Arraylist.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FirebaseDatabase.getInstance().getReference().child("Courses")
                                .child(result.get(0)).child("fullInfo").setValue(dataSnapshot.child("fullInfo")
                                    .getValue(String.class).split(" ")[0].concat(" ".concat(result.get(1).concat(" "
                                        .concat(result.get(2))))));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                D.child(result.get(0)).child("cid").setValue(result.get(0));
                D.child(result.get(0)).child("name").setValue(Arraylist.get(1));
                D.child(result.get(0)).child("password").setValue(Arraylist.get(5));
                D.child(result.get(0)).child("phone").setValue(Arraylist.get(3));
                D.child(result.get(0)).child("email").setValue(Arraylist.get(2));
                D.child(result.get(0)).child("address").setValue(Arraylist.get(4));



                ArrayList<String> Alist = new ArrayList<>();
                Alist.add(result.get(0));
                Alist.add(Arraylist.get(1));
                Alist.add(Arraylist.get(2));
                Alist.add(Arraylist.get(3));
                Alist.add(Arraylist.get(4));
                Alist.add(Arraylist.get(5));
                Gson gson = new Gson();
                String json = gson.toJson(Alist);
                savedSharedPreferences.setUserName(getApplication(),json);
                Arraylist.clear();
                Arraylist.add(result.get(0));
                Arraylist.add(Alist.get(1));
                Arraylist.add(Alist.get(2));
                Arraylist.add(Alist.get(3));
                Arraylist.add(Alist.get(4));
                Arraylist.add(Alist.get(5));
                Handler H = new Handler();
                H.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseDatabase.getInstance().getReference().child("Courses").child(oldId).removeValue();
                    }
                },1500);
            }
        }
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
