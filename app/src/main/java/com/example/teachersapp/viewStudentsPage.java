package com.example.teachersapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class viewStudentsPage extends AppCompatActivity {
    DataManeger M = new DataManeger();
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    Intent intent;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students_page);
        getSupportActionBar().hide();
        intent=getIntent();
        ID =intent.getStringExtra("ID");

        M.getReferenceStudent().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final ArrayList<String> ArrayList=new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    for(DataSnapshot dc:ds.child("Courses").getChildren()){
                        if(dc.getKey().equals(ID)){
                            ArrayList.add("Students Name: ".concat(ds.child("fname").getValue(String.class).concat(" ".concat(ds.child("lname")
                                    .getValue(String.class).concat("\nStudents ID: ".concat(ds.getKey().concat("\nAttendance Count: ".concat(dc.getValue(String
                                            .class).split(" ")[0]))))))));
                        }
                    }
                }
                listView=findViewById(R.id.coursesList);
                arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,ArrayList);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        M.getReferenceStudent().child(ArrayList.get(i).split("\n")[1].substring(13)).child("Courses").child(ID).removeValue();
                        ArrayList.remove(i);
                        arrayAdapter.notifyDataSetChanged();
                        listView.setAdapter(arrayAdapter);
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(this, ID, Toast.LENGTH_SHORT).show();

    }
}
