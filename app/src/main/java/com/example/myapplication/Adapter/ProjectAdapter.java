package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Project;
import com.example.myapplication.PopupDateChanger;
import com.example.myapplication.PopupDateChangerEtapa;
import com.example.myapplication.PopupDateChangerProject;
import com.example.myapplication.ProjectActivity;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    FirebaseDatabase db;

    private final Context mContext;
    private final List<Project> mProjects;


    public ProjectAdapter(Context mContext, List<Project> mProjects){
        this.mContext = mContext;
        this.mProjects = mProjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.project_item, parent, false);
        return new ProjectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        db = FirebaseDatabase.getInstance();

        final Project project = mProjects.get(position);

        String date = project.getStartDate()+" - "+project.getEndDate();

        holder.projectname.setText(project.getName());
        holder.Date.setText(date);
        holder.status.setText(project.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProjectActivity.class);
                intent.putExtra("projectid", project.getId());
                intent.putExtra("projectName", project.getName());
                intent.putExtra("projectDescriere", project.getDescriere());
                mContext.startActivity(intent);
            }
        });

        holder.Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupDateChangerProject popupDateChangerProject = new PopupDateChangerProject();
                PopupDateChangerProject.projectid= project.getId();
                PopupDateChangerProject.endDate= project.getEndDate();
                popupDateChangerProject.show(((FragmentActivity)mContext).getSupportFragmentManager(), "popup");
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, project.getId());
            }
        });

    }


    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView projectname;
        public TextView Date;
        public TextView status;
        public ImageView menu;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            projectname = itemView.findViewById(R.id.projectname);
            Date = itemView.findViewById(R.id.Date);
            status = itemView.findViewById(R.id.status);
            menu = itemView.findViewById(R.id.menu);
        }
    }

    private void showPopupMenu(View v, String projectid) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.hide:
                        DatabaseReference reference = db.getReference("Proiecte").child(projectid);

                        Map<String,Object> Map = new HashMap<>();

                        Map.put("visibility", "GONE");


                        reference.updateChildren(Map);

                        return true;
                    case R.id.delete:
                        reference = db.getReference("Proiecte").child(projectid);
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
