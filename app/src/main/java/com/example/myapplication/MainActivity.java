package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.myapplication.Fragments.ChatsFragment;
import com.example.myapplication.Fragments.ProjectsFragment;
import com.example.myapplication.Fragments.UsersFragment;
import com.example.myapplication.Fragments.WorkFragment;
import com.example.myapplication.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profile_image;

    TextView username;

    String userId;
    FirebaseUser firebaseUser;
    StorageReference storageReference;

    FirebaseAuth fAuth;
    FirebaseDatabase db;
    DatabaseReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

//        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
//        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(profile_image);
//            }
//        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,Profile.class);
                startActivity(intent);
            }
        });

        userId = fAuth.getCurrentUser().getUid();


//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//
//
//                User user = documentSnapshot.toObject(User.class);
//                username.setText(user.getFName());
//
//            }
//        });

//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                assert value != null;
//                User user = value.toObject(User.class);
//                assert user != null;
//                username.setText(user.getFName());
//            }
//        });

        db = FirebaseDatabase.getInstance();

        documentReference = db.getReference("Users").child(userId);

        documentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getFName());
                if (user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Picasso.get().load(user.getImageURL()).into(profile_image);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        db.collection("users").document(userId)
//                .get()
//                .addOnCompleteListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@NotNull DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                        if (value.exists()) {
//
//
//                            User user = value.toObject(User.class);
//
//                            username.setText(user.getFName());
//
//
//
//                        } else {
//                            Log.w("TAG", "Error getting documents.", task.getException());
//                        }
//                    }
//
//
//
//                                       });

//        db.collection("users").document(userId)
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                User user = task.getResult().toObject(User.class);
//                username.setText(user.getFName());
//                if (user.getImageURL().equals("default")){
//                    profile_image.setImageResource(R.mipmap.ic_launcher);
//                }else{
//                    Picasso.get().load(user.getImageURL()).into(profile_image);
//
//                }
//
//            }
//        });


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());



        viewPagerAdapter.addFragment(new ProjectsFragment(), "Project");
        viewPagerAdapter.addFragment(new WorkFragment(), "Work");
        viewPagerAdapter.addFragment(new ChatsFragment(), "Chat");
        viewPagerAdapter.addFragment(new UsersFragment(), "Users");


        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        // Ctrl + o


        @androidx.annotation.Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status){

        documentReference = db.getReference("Users").child(userId);

        Map<String, Object> hashMap =new HashMap<>();
        hashMap.put("status", status);

        documentReference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {

        super.onResume();
        status("online");
    }

    protected void onPause() {

        super.onPause();
        status("offline");
    }

}