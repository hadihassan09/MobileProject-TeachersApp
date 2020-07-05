package com.example.teachersapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class masterTeacherViewPage extends AppCompatActivity {
    private ListView listView;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_teacher_view_page);
        getSupportActionBar().hide();

        FirebaseDatabase.getInstance().getReference().child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> ArrayList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ArrayList.add("Course ID: ".concat(ds.getKey().concat("\nCourse Name: "
                            .concat(ds.child("cname").getValue(String.class).concat("\nDoctors Name: "
                                    .concat(ds.child("name").getValue(String.class).concat("\nDoctors Phone: "
                                            .concat(ds.child("phone").getValue(String.class)))))))));
                }
                listView = findViewById(R.id.teacherView);
                Collections.sort(ArrayList);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, ArrayList);
                listView.setAdapter(arrayAdapter);

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final String id = FirebaseDatabase.getInstance().getReference().child("Courses").child(ArrayList.get(i).split("\n")[0].substring(11)).getKey();
                        FirebaseDatabase.getInstance().getReference().child("Courses").child(id).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Student").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    FirebaseDatabase.getInstance().getReference().child("Student").child(ds.getKey()).child("Courses").child(id).removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        ArrayList.remove(i);
                        arrayAdapter.notifyDataSetChanged();
                        listView.setAdapter(arrayAdapter);
                        Toast.makeText(getApplicationContext(), "Course ".concat(id.concat(" And Its Teacher has been removed.")), Toast.LENGTH_SHORT).show();

                        return false;

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
