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
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;

import com.example.myapplication.Adapter.EtapeAdapter;
import com.example.myapplication.Adapter.ProjectAdapter;
import com.example.myapplication.Adapter.TaskAdapter;
import com.example.myapplication.Model.Etapa;
import com.example.myapplication.Model.Project;
import com.example.myapplication.Model.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivity extends AppCompatActivity {

    TextView titlu;
    TextView descriere;

    RecyclerView recyclerView;

    private EtapeAdapter etapeAdapter;
    private List<Etapa> mEtape;

    FirebaseDatabase db;

    private String projectid;
    private String projectDescriere;
    private String projectName;

    DatabaseReference reference;


    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProjectActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        db = FirebaseDatabase.getInstance();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        titlu = findViewById(R.id.titlu);
        descriere = findViewById(R.id.descriere);


        intent = getIntent();
        projectid = intent.getStringExtra("projectid");
        projectName = intent.getStringExtra("projectName");
        projectDescriere = intent.getStringExtra("projectDescriere");


        titlu.setText(projectName);
        descriere.setText(projectDescriere);

        mEtape = new ArrayList<Etapa>();

        readEtape(projectid);


    }


    private void readEtape(String projectid) {

        DatabaseReference reference = db.getReference("Etape").child(projectid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mEtape.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Etapa etapa = dataSnapshot.getValue(Etapa.class);

                    Log.d("TAG", "Value is: " + dataSnapshot.getValue());

                    mEtape.add(etapa);

                }
                etapeAdapter = new EtapeAdapter(ProjectActivity.this, mEtape);
                recyclerView.setAdapter(etapeAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}