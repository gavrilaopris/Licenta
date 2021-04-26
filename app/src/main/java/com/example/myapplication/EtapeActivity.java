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
import com.example.myapplication.Adapter.UserAdapter;
import com.example.myapplication.Model.Etapa;
import com.example.myapplication.Model.ListTasks;
import com.example.myapplication.Model.Task;
import com.example.myapplication.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.Objects;

public class EtapeActivity extends AppCompatActivity {

    private AlertDialog.Builder builder, builder1;
    private AlertDialog alertDialog, alertDialog1;

    private EditText taskname, descrie, dateTextStart, dateTextEnd;
    private ImageView arrowStart , arrowEnd, choseUser;
    private Button btnCreate;

    private RecyclerView recyclerView1;

    private UserAdapter userAdapter;
    private List<User> mUsers;

    private String userid;


    TextView titlu;
    TextView descriere;

    RecyclerView recyclerView;


    private TaskAdapter taskAdapter;
    private List<Task> mTasks;

    private ImageView addTask;

    FirebaseDatabase db;

    public String etapaid;
    private String etapaDescriere;
    private String etapaName;

    Intent intent, intent1;

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

        PopupUserList.etapaid= etapaid;

        titlu.setText(etapaName);
        descriere.setText(etapaDescriere);


        mTasks = new ArrayList<Task>();

        addTask = findViewById(R.id.addTask);

       // mTasks.clear();
        readTask(etapaid);


        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // createNewDialog();
              //  mTasks.removeAll(mTasks);

                openDialog();
            }
        });




    }

    private void openDialog() {
        popup popup = new popup();
        Bundle data = new Bundle();//create bundle instance
        data.putString("etapaid", etapaid);//put string to pass with a key value
        popup.setArguments(data);
        popup.show(getSupportFragmentManager(), "popup");

    }

    private void readTask(String etapaid) {


        //mTasks.removeAll(mTasks);
        DatabaseReference reference = db.getReference("Tasks");

       // DatabaseReference ref = db.getReference("ListTasks").child(etapaid);



                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                           mTasks.clear();

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Task task = dataSnapshot.getValue(Task.class);

                                Log.d("TAG", "Value is: " + dataSnapshot.getValue());

                                if(task.getEtapaID().equals(etapaid))
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












}