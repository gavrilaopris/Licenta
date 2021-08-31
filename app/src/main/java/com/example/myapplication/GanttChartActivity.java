package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.Adapter.TaskAdapter;
import com.example.myapplication.Model.Etapa;
import com.example.myapplication.Model.GanttData;
import com.example.myapplication.Model.Milestone;
import com.example.myapplication.Model.Project;
import com.example.myapplication.Model.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GanttChartActivity extends AppCompatActivity {

    private AppCompatActivity compatActivity;
    private Etapa etapa;
    private ArrayList<Task> tasks;
    private ArrayList<Milestone> milestones;

    MutableLiveData<String> listen = new MutableLiveData<>();

    public String etapaid;

    FirebaseDatabase db;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantt_chart);
        this.compatActivity = this;


        db = FirebaseDatabase.getInstance();

        intent = getIntent();
        etapaid = intent.getStringExtra("etapaid");

        listen.setValue("old");

        listen.observe(compatActivity,new Observer<String>() {
            @Override
            public void onChanged(String changedValue) {
                //Do something with the changed value

                if (!listen.getValue().equals("old")){

                    GanttData.initGanttData(etapa, tasks, milestones);
                    Intent intent = new Intent(compatActivity, Gantt.class);
                    startActivity(intent);

                }
            }
        });

        DatabaseReference reference1 = db.getReference("Etape").child(etapaid);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                etapa = snapshot.getValue(Etapa.class);



                Log.d("TAG", "judutduyfifif is: " + snapshot.getValue());
                listen.setValue("cal");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //etapa = new Etapa("1620934153282", "etapa", "etapa", "Jun 23, 2021", "May 13, 2021", "toDo", "1620934122195", "VISIBLE");

        tasks = new ArrayList<Task>();
        milestones = new ArrayList<>();
        //mTasks.removeAll(mTasks);



        DatabaseReference reference = db.getReference("Tasks");

        // DatabaseReference ref = db.getReference("ListTasks").child(etapaid);



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasks.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Task task = dataSnapshot.getValue(Task.class);

                    Log.d("TAG", "Value is: " + dataSnapshot.getValue());

                    if(task.getEtapaID().equals(etapaid))
                       tasks.add(task);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.gantt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_gantt:
                GanttData.initGanttData(etapa, tasks, milestones);
                Intent intent = new Intent(compatActivity, Gantt.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}