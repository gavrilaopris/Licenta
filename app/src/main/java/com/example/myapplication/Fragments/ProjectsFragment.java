package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapter.ProjectAdapter;
import com.example.myapplication.Model.Project;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProjectsFragment extends Fragment {

    private RecyclerView recyclerView1;

    private ProjectAdapter projectAdapter;
    private List<Project> mProjects;


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

        readProjects();
        
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
}