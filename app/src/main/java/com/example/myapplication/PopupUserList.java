package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Adapter.UserAdapter;
import com.example.myapplication.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PopupUserList extends AppCompatDialogFragment {

    private RecyclerView recyclerView1;
    private Button btn;

    private UserAdapter userAdapter;
    private List<User> mUsers;

    FirebaseDatabase db;

   public static String userid, taskid, etapaid;

    private String imageUrl;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.users_list, null);

        builder.setView(view);

        db = FirebaseDatabase.getInstance();

        recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler_view1);
        recyclerView1.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView1.setLayoutManager(linearLayoutManager1);

        btn = (Button) view.findViewById(R.id.btn);

        mUsers = new ArrayList<User>();

        readUsers();

//        taskid = getArguments().getString("taskid");
//        userid = getArguments().getString("userid");


        Toast.makeText(getContext(), ""+userid, Toast.LENGTH_SHORT).show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update(userid,etapaid,taskid);
            }
        });



        return builder.create();
    }

    private void Update(String userid,String etapaid, String taskid) {

        DatabaseReference reference = db.getReference("TasksList").child(taskid);

        HashMap<String,String> hashMap = new HashMap<>();

        hashMap.put("userid", userid);

        reference.child(userid).setValue(hashMap);

        DatabaseReference ref = db.getReference("Users").child(userid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);
                imageUrl = user1.getImageURL();
                Toast.makeText(getContext(), imageUrl, Toast.LENGTH_LONG).show();
                DatabaseReference reference1 = db.getReference("Tasks").child(etapaid).child(taskid);

                Map<String,Object> Map = new HashMap<>();

                Map.put("imageUrl", imageUrl);


                reference1.updateChildren(Map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void readUsers() {

        DatabaseReference reference = db.getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mUsers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);





                    mUsers.add(user);

                }
                userAdapter = new UserAdapter(getContext(), mUsers, false, true);
                recyclerView1.setAdapter(userAdapter);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}