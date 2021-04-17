package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.Model.Task;
import com.example.myapplication.R;
import com.example.myapplication.TaskActivity;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

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

    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView projectname;
        public TextView Date;
        public TextView status;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            projectname = itemView.findViewById(R.id.projectname);
            Date = itemView.findViewById(R.id.Date);
            status = itemView.findViewById(R.id.status);
        }
    }
}
