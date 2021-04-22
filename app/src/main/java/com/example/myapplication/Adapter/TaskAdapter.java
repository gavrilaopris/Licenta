package com.example.myapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private AlertDialog.Builder builder ;
    private AlertDialog alertDialog;

    private RecyclerView recyclerView1;


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

        final Task task = mTasks.get(position);

        String date = task.getStartDate()+" - "+task.getEndDate();

        holder.projectname.setText(task.getTitlu());
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
                Bundle data = new Bundle();//create bundle instance
                data.putString("taskid", task.getId());//put string to pass with a key value
                popupUserList.setArguments(data);
                popupUserList.show(((FragmentActivity)mContext).getSupportFragmentManager(), "popup");
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            projectname = itemView.findViewById(R.id.projectname);
            Date = itemView.findViewById(R.id.Date);
            status = itemView.findViewById(R.id.status);
            userImage = itemView.findViewById(R.id.userImage);
        }
    }



}
