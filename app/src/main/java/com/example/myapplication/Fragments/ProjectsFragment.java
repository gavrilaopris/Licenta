package com.example.myapplication.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.myapplication.Adapter.ProjectAdapter;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.Project;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
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


public class ProjectsFragment extends Fragment {

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private EditText projectname, descriere, dateTextStart, dateTextEnd;
    private ImageView arrowStart , arrowEnd;
    private Button btnCreate;

    private RecyclerView recyclerView1;

    private ProjectAdapter projectAdapter;
    private List<Project> mProjects;

    private ImageView addProject;


    FirebaseAuth fAuth;
    FirebaseDatabase db;
    String userId;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        db = FirebaseDatabase.getInstance();
        
        View view = inflater.inflate(R.layout.fragment_projects, container, false);

        recyclerView1 = view.findViewById(R.id.recycler_view1);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));

        mProjects = new ArrayList<Project>();

        addProject = view.findViewById(R.id.addProject);

        readProjects();

        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewDialog();
            }
        });

        
        return view;
    }

    private void readProjects() {

        DatabaseReference reference = db.getReference("Proiecte");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mProjects.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Project project = dataSnapshot.getValue(Project.class);

                    Log.d("TAG", "Value is: " + dataSnapshot.getValue());

                        mProjects.add(project);

                }
                projectAdapter = new ProjectAdapter(getContext(), mProjects);
                recyclerView1.setAdapter(projectAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createNewDialog(){

        builder = new AlertDialog.Builder(getActivity());
        final View createpopupView = getLayoutInflater().inflate(R.layout.popup_project, null);

        projectname = (EditText) createpopupView.findViewById(R.id.projectname);
        dateTextStart = (EditText) createpopupView.findViewById(R.id.dateTextStart);
        dateTextEnd = (EditText) createpopupView.findViewById(R.id.dateTextEnd);
        descriere = (EditText) createpopupView.findViewById(R.id.descriere);

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

                final String name = projectname.getText().toString();
                final String startDte = dateTextStart.getText().toString();
                final String endDate = dateTextEnd.getText().toString();
                final String desc = descriere.getText().toString();


                createProject(name, desc, startDte, endDate);
            }
        });

    }

    private void showDatePickerDialog(EditText text){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
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

    private void createProject(String name, String desc, String startDate, String endDate){

        String timeStamp = ""+System.currentTimeMillis();
        DatabaseReference reference = db.getReference("Proiecte");

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("id", timeStamp);
        hashMap.put("name", name);
        hashMap.put("descriere", desc);
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);
        hashMap.put("status", "toDo");

        reference.push().setValue(hashMap);

    }


}