package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.DownloadAdapter;
import com.example.myapplication.Adapter.EtapeAdapter;
import com.example.myapplication.Adapter.TaskAdapter;
import com.example.myapplication.Model.Download;
import com.example.myapplication.Model.Etapa;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    TextView titlu;
    TextView descriere;
    ImageButton imageButton;

    RecyclerView recyclerView;

    private EtapeAdapter etapeAdapter;
    private List<Download> mDown;
    private DownloadAdapter downloadAdapter;

    FirebaseDatabase db;

    private String taskid;
    private String taskDescriere;
    private String taskName;

    DatabaseReference reference;


    Intent intent;

    private static final int PICK_FILE = 1;

    ArrayList<Uri> FileList = new ArrayList<Uri>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing Please wait.....");

        db = FirebaseDatabase.getInstance();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(linearLayoutManager);


        titlu = findViewById(R.id.titlu);
        descriere = findViewById(R.id.descriere);
        imageButton = findViewById(R.id.attachBtn);


        intent = getIntent();
        taskid = intent.getStringExtra("taskid");
        taskName = intent.getStringExtra("taskTitlu");
        taskDescriere = intent.getStringExtra("taskDescriere");


        titlu.setText(taskName);
        descriere.setText(taskDescriere);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent,PICK_FILE);
            }
        });

        mDown = new ArrayList<Download>();

        dataFromFireBase();



    }

    private void dataFromFireBase() {


        DatabaseReference reference = db.getReference("Documente").child(taskid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDown.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Download download = dataSnapshot.getValue(Download.class);

                    Log.d("TAG", "Value is: " + dataSnapshot.getValue());

                    mDown.add(download);

                }
                downloadAdapter = new DownloadAdapter(TaskActivity.this, mDown);
                recyclerView.setAdapter(downloadAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE){
            if (resultCode == RESULT_OK){

                Uri File = data.getData();

                AddFile(File);
            }
        }
    }

    public void AddFile(Uri uri) {


        progressDialog.show();


            String fileNameAndPath = "Files/"+"file"+uri.getLastPathSegment();

            StorageReference folder = FirebaseStorage.getInstance().getReference().child(fileNameAndPath);


            folder.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    String name = taskSnapshot.getStorage().getName();
                    //Task<StorageMetadata> metaext = taskSnapshot.getStorage().getMetadata();
                    while (!uriTask.isSuccessful());
                    String downloadUri = uriTask.getResult().toString();
                    //String ext = metaext.getResult().getContentType();

                    if (uriTask.isSuccessful()){
                    folder.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata) {
                            String ext = storageMetadata.getContentType();
                           // String name = storageMetadata.getName();
                            DatabaseReference databaseReference = db.getReference("Documente").child(taskid);
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("link", downloadUri);
                            hashMap.put("name", name);
                            hashMap.put("extension", ext);
                            databaseReference.push().setValue(hashMap);
                            progressDialog.dismiss();
                        }
                    });



                      }

                }
            });




        }




}