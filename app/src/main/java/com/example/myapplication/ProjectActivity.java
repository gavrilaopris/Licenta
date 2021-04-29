package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

import com.example.myapplication.Adapter.EtapeAdapter;
import com.example.myapplication.Adapter.ProjectAdapter;
import com.example.myapplication.Adapter.TaskAdapter;
import com.example.myapplication.Model.Etapa;
import com.example.myapplication.Model.ListTasks;
import com.example.myapplication.Model.Project;
import com.example.myapplication.Model.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private EditText stagename, descrie, dateTextStart, dateTextEnd;
    private ImageView arrowStart , arrowEnd;
    private Button btnCreate;
    private ImageButton eye;


    TextView titlu;
    TextView descriere;

    RecyclerView recyclerView;

    private EtapeAdapter etapeAdapter;
    private List<Etapa> mEtape;

    private ArrayList<String> str;


    private ImageView addEtapa;

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
        str = new ArrayList<String>();

        addEtapa = findViewById(R.id.addEtapa);
        eye = findViewById(R.id.eye);

        readEtape(projectid);

        addEtapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewDialog();
            }
        });


        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateVisibility(projectid);
            }
        });




    }

    private void updateVisibility(String projectid) {
        DatabaseReference ref = db.getReference("Etape");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                str.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Etapa pro = dataSnapshot.getValue(Etapa.class);
                    if (pro.getProjectID().equals(projectid))
                    str.add(pro.getId());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        String str_a[]= new String[str.size()];

        for(int j = 0; j< str.size(); j++){
            str_a[j] = str.get(j);
        }

        for (String id : str_a) {

            DatabaseReference refe = db.getReference("Etape").child(id);

            Map<String, Object> Map = new HashMap<>();

            Map.put("visibility", "VISIBLE");
            refe.updateChildren(Map);

        }
    }


    private void readEtape(String projectid) {

        DatabaseReference reference = db.getReference("Etape");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mEtape.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Etapa etapa = dataSnapshot.getValue(Etapa.class);

                    Log.d("TAG", "Value is: " + dataSnapshot.getValue());

                    if(etapa.getProjectID().equals(projectid) && etapa.getVisibility().equals("VISIBLE"))
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

    private void createNewDialog(){

        builder = new AlertDialog.Builder(this);
        final View createpopupView = getLayoutInflater().inflate(R.layout.popup_etape, null);

        stagename = (EditText) createpopupView.findViewById(R.id.stagename);
        dateTextStart = (EditText) createpopupView.findViewById(R.id.dateTextStart);
        dateTextEnd = (EditText) createpopupView.findViewById(R.id.dateTextEnd);
        descrie = (EditText) createpopupView.findViewById(R.id.descriere);

        arrowStart = (ImageView) createpopupView.findViewById(R.id.arrowStart);
        arrowEnd = (ImageView) createpopupView.findViewById(R.id.arrowEnd);

        btnCreate = (Button) createpopupView.findViewById(R.id.btnCreate);

        builder.setView(createpopupView);
        alertDialog = builder.create();
        alertDialog.show();

        arrowStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dateTextStart);
            }
        });


        arrowEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dateTextEnd);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String titlu = stagename.getText().toString();
                final String startDate = dateTextStart.getText().toString();
                final String endDate = dateTextEnd.getText().toString();
                final String desc = descrie.getText().toString();

                if (TextUtils.isEmpty(titlu)) {
                    stagename.setError("Title is Require");
                    return;
                }
                if (TextUtils.isEmpty(desc)) {
                    descrie.setError("Description is Require");
                    return;
                }
                if (TextUtils.isEmpty(startDate)) {
                    dateTextStart.setError("StartDate is Require");
                    return;
                }
                if (TextUtils.isEmpty(endDate)) {
                    dateTextEnd.setError("EndDate is Require");
                    return;
                }


                createProject(titlu, desc, startDate, endDate, projectid);
            }
        });

    }

    private void showDatePickerDialog(EditText text){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                        String date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());
                        text.setText(date);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void createProject(String titlu, String desc, String startDate, String endDate, String projectid){

        String timeStamp = ""+System.currentTimeMillis();
        DatabaseReference reference = db.getReference("Etape").child(timeStamp);

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("id", timeStamp);
        hashMap.put("titlu", titlu);
        hashMap.put("projectID", projectid);
        hashMap.put("descriere", desc);
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);
        hashMap.put("status", "toDo");
        hashMap.put("visibility", "VISIBLE");

        reference.setValue(hashMap);

    }



}