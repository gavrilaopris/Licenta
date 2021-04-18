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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Adapter.EtapeAdapter;
import com.example.myapplication.Adapter.TaskAdapter;
import com.example.myapplication.Model.Etapa;
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

public class EtapeActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private EditText taskname, descrie, dateTextStart, dateTextEnd;
    private ImageView arrowStart , arrowEnd;
    private Button btnCreate;

    TextView titlu;
    TextView descriere;

    RecyclerView recyclerView;


    private TaskAdapter taskAdapter;
    private List<Task> mTasks;

    private ImageView addTask;

    FirebaseDatabase db;

    private String etapaid;
    private String etapaDescriere;
    private String etapaName;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etape);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EtapeActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        etapaid = intent.getStringExtra("etapaid");
        etapaName = intent.getStringExtra("etapaTitlu");
        etapaDescriere = intent.getStringExtra("etapaDescriere");


        titlu.setText(etapaName);
        descriere.setText(etapaDescriere);


        mTasks = new ArrayList<Task>();

        addTask = findViewById(R.id.addTask);

        readTask(etapaid);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewDialog();
            }
        });




    }

    private void readTask(String etapaid) {

        DatabaseReference reference = db.getReference("Tasks").child(etapaid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mTasks.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Task task = dataSnapshot.getValue(Task.class);

                    Log.d("TAG", "Value is: " + dataSnapshot.getValue());

                    mTasks.add(task);

                }
                taskAdapter = new TaskAdapter(EtapeActivity.this, mTasks);
                recyclerView.setAdapter(taskAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void createNewDialog(){

        builder = new AlertDialog.Builder(this);
        final View createpopupView = getLayoutInflater().inflate(R.layout.popup_task, null);

        taskname = (EditText) createpopupView.findViewById(R.id.taskname);
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

                final String titlu = taskname.getText().toString();
                final String startDate = dateTextStart.getText().toString();
                final String endDate = dateTextEnd.getText().toString();
                final String desc = descrie.getText().toString();

                if (TextUtils.isEmpty(titlu)) {
                    taskname.setError("Title is Require");
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


                createProject(titlu, desc, startDate, endDate, etapaid);
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

    private void createProject(String titlu, String desc, String startDate, String endDate, String etapaid){

        String timeStamp = ""+System.currentTimeMillis();
        DatabaseReference reference = db.getReference("Tasks").child(etapaid);

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("id", timeStamp);
        hashMap.put("titlu", titlu);
        hashMap.put("descriere", desc);
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);
        hashMap.put("status", "toDo");

        reference.push().setValue(hashMap);

    }
}