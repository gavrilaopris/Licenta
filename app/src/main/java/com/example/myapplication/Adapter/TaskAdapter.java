package com.example.myapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.EtapeActivity;
import com.example.myapplication.Model.Task;
import com.example.myapplication.Model.User;
import com.example.myapplication.PopupUserList;
import com.example.myapplication.R;
import com.example.myapplication.TaskActivity;
import com.example.myapplication.popup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    FirebaseDatabase db;

    private final Context mContext;
    private final List<Task> mTasks;


    public TaskAdapter(Context mContext, List<Task> mTasks){
        this.mContext = mContext;
        this.mTasks = mTasks;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.project_item, parent, false);
        return new TaskAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {

        db = FirebaseDatabase.getInstance();

        final Task task = mTasks.get(position);

        String date = task.getStartDate()+" - "+task.getEndDate();

        holder.projectname.setText(task.getTitlu());

        if (task.getImageUrl().equals("default")){
            holder.userImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Picasso.get().load(task.getImageUrl()).into(holder.userImage);

        }

        holder.Date.setText(date);
        holder.status.setText(task.getStatus());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TaskActivity.class);
                intent.putExtra("taskid", task.getId());
                intent.putExtra("taskTitlu", task.getTitlu());
                intent.putExtra("taskDescriere", task.getDescriere());
                mContext.startActivity(intent);
            }
        });

        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupUserList popupUserList = new PopupUserList();
//                Bundle data = new Bundle();//create bundle instance
//                data.putString("taskid", task.getId());//put string to pass with a key value
                PopupUserList.taskid= task.getId();
//                popupUserList.setArguments(data);
                popupUserList.show(((FragmentActivity)mContext).getSupportFragmentManager(), "popup");
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, task.getId());
            }
        });


    }



    @Override
    public int getItemCount() {
        return mTasks.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView projectname;
        public TextView Date;
        public TextView status;
        public ImageView userImage;
        public ImageView menu;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            projectname = itemView.findViewById(R.id.projectname);
            Date = itemView.findViewById(R.id.Date);
            status = itemView.findViewById(R.id.status);
            userImage = itemView.findViewById(R.id.userImage);
            menu = itemView.findViewById(R.id.menu);
        }
    }

    private void showPopupMenu(View v, String taskid) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.hide:
                        DatabaseReference reference = db.getReference("Tasks").child(taskid);

                        Map<String,Object> Map = new HashMap<>();

                        Map.put("visibility", "GONE");


                        reference.updateChildren(Map);

                        return true;
                    case R.id.delete:
                         reference = db.getReference("Tasks").child(taskid);
                         reference.removeValue();

                        return true;

                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }






}
