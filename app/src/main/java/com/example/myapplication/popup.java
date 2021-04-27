package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Adapter.UserAdapter;
import com.example.myapplication.EtapeActivity;
import com.example.myapplication.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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



public class popup extends AppCompatDialogFragment {

    private AlertDialog.Builder builder ;
    private AlertDialog alertDialog;

    private EditText taskname, descrie, dateTextStart, dateTextEnd;

    private ImageView arrowStart , arrowEnd;
    private Button btnCreate;

    private RecyclerView recyclerView1;

    private UserAdapter userAdapter;
    private List<User> mUsers;

    private String  etapaid ;




    FirebaseDatabase db;


    Intent intent, intent2;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_task, null);

        db = FirebaseDatabase.getInstance();

        taskname = (EditText) view.findViewById(R.id.taskname);
        dateTextStart = (EditText) view.findViewById(R.id.dateTextStart);
        dateTextEnd = (EditText) view.findViewById(R.id.dateTextEnd);
        descrie = (EditText) view.findViewById(R.id.descriere);



        arrowStart = (ImageView) view.findViewById(R.id.arrowStart);
        arrowEnd = (ImageView) view.findViewById(R.id.arrowEnd);

        btnCreate = (Button) view.findViewById(R.id.btnCreate);



       //intent = intent.getDataString()
       //etapaid = intent2.getStringExtra("etapaid");
//       etapaid = intent2.getDataString();

       etapaid = getArguments().getString("etapaid");





        builder.setView(view);



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

        return builder.create();
    }

    private void showDatePickerDialog(EditText text) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
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

    private void DialogUsers() {
        builder = new AlertDialog.Builder(getActivity());
        final View createView = getLayoutInflater().inflate(R.layout.users_list, null);

        recyclerView1 = (RecyclerView) createView.findViewById(R.id.recycler_view1);
        recyclerView1.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView1.setLayoutManager(linearLayoutManager1);

        mUsers = new ArrayList<User>();

        readUsers();

        builder.setView(createView);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();




        DatabaseReference reference = db.getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mUsers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);





                    mUsers.add(user);

                }
                userAdapter = new UserAdapter(getContext(), mUsers, false, true, false);
                recyclerView1.setAdapter(userAdapter);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void createProject(String titlu, String desc, String startDate, String endDate, String etapaid){

        String timeStamp = ""+System.currentTimeMillis();

        DatabaseReference reference = db.getReference("Tasks").child(timeStamp);

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("id", timeStamp);
        hashMap.put("titlu", titlu);
        hashMap.put("etapaID", etapaid);
        hashMap.put("imageUrl", "default");
        hashMap.put("descriere", desc);
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);
        hashMap.put("status", "toDo");
        hashMap.put("visibility", "VISIBLE");

        reference.setValue(hashMap);

    }
}