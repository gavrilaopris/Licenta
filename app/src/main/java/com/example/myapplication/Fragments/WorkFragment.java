package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myapplication.Adapter.UserAdapter;
import com.example.myapplication.Model.User;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class WorkFragment extends Fragment {


    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> mUsers;





    FirebaseAuth fAuth;
    FirebaseDatabase db;
    String userId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        View view = inflater.inflate(R.layout.fragment_work, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<User>();

        readUsers();


        return view;
    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();


        DatabaseReference reference = db.getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    mUsers.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);


//                        assert user != null;
//                        assert userId != null;
//                        if (!user.getId().equals(userId)) {

                            mUsers.add(user);
//                        }
                    }
                    userAdapter = new UserAdapter(getContext(), mUsers, false, false, true);
                    recyclerView.setAdapter(userAdapter);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}