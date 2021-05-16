package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.Model.Etapa;
import com.example.myapplication.Model.GanttData;
import com.example.myapplication.Model.GanttDataEtape;
import com.example.myapplication.Model.Milestone;
import com.example.myapplication.Model.Project;
import com.example.myapplication.Model.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GanttChartEtapeActivity extends AppCompatActivity {

    private AppCompatActivity compatActivity;
    private Project project;
    private ArrayList<Etapa> etape;
    private ArrayList<Milestone> milestones;

    MutableLiveData<String> listen = new MutableLiveData<>();

    public String projectid;

    FirebaseDatabase db;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantt_chart_etape);

        this.compatActivity = this;

        db = FirebaseDatabase.getInstance();

        intent = getIntent();
        projectid = intent.getStringExtra("projectid");

        listen.setValue("old");

        listen.observe(compatActivity,new Observer<String>() {
            @Override
            public void onChanged(String changedValue) {
                //Do something with the changed value

                if (!listen.getValue().equals("old")){

                    GanttDataEtape.initGanttDataEtape(project, etape, milestones);
                    Intent intent = new Intent(compatActivity, GanttEtape.class);
                    startActivity(intent);

                }
            }
        });

        DatabaseReference reference1 = db.getReference("Proiecte").child(projectid);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                project = snapshot.getValue(Project.class);



                Log.d("TAG", "judutduyfifif is: " + snapshot.getValue());
                listen.setValue("cal");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //etapa = new Etapa("1620934153282", "etapa", "etapa", "Jun 23, 2021", "May 13, 2021", "toDo", "1620934122195", "VISIBLE");

        etape = new ArrayList<Etapa>();
        milestones = new ArrayList<>();
        //mTasks.removeAll(mTasks);



        DatabaseReference reference = db.getReference("Etape");

        // DatabaseReference ref = db.getReference("ListTasks").child(etapaid);



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                etape.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Etapa etapa = dataSnapshot.getValue(Etapa.class);

                    Log.d("TAG", "Value is: " + dataSnapshot.getValue());

                    if(etapa.getProjectID().equals(projectid))
                        etape.add(etapa);

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
                GanttDataEtape.initGanttDataEtape(project, etape, milestones);
                Intent intent = new Intent(compatActivity, GanttEtape.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}