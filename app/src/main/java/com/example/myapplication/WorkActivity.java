package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.Adapter.TaskAdapter;
import com.example.myapplication.Adapter.UserAdapter;
import com.example.myapplication.Model.ListTasks;
import com.example.myapplication.Model.Task;
import com.example.myapplication.Model.TasksList;
import com.example.myapplication.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WorkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;



    private TaskAdapter taskAdapter;
    private List<Task> mTasks;


    FirebaseAuth fAuth;
    FirebaseDatabase db;
    String userId;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        intent = getIntent();
        userId = intent.getStringExtra("userid");

        mTasks = new ArrayList<Task>();

        readTasks(userId);




    }

    private void readTasks(String userId) {

        DatabaseReference reference = db.getReference("TasksList").child(userId);
        DatabaseReference ref = db.getReference("Tasks");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    TasksList list = dataSnapshot.getValue(TasksList.class);
                    String idtask = list.getTaskid();


                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            mTasks.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Task task = dataSnapshot.getValue(Task.class);

                                Log.d("TAG", "Value is: " + dataSnapshot.getValue());

                                if(idtask.equals(task.getId()))
                                    mTasks.add(task);

                            }
                            taskAdapter = new TaskAdapter(WorkActivity.this, mTasks);
                            recyclerView.setAdapter(taskAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}