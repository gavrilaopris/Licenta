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

import com.example.myapplication.EtapeActivity;
import com.example.myapplication.Model.Etapa;
import com.example.myapplication.PopupDateChanger;
import com.example.myapplication.PopupDateChangerEtapa;
import com.example.myapplication.PopupDateChangerProject;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EtapeAdapter extends RecyclerView.Adapter<EtapeAdapter.ViewHolder> {

    FirebaseDatabase db;

    private final Context mContext;
    private final List<Etapa> mEtape;


    public EtapeAdapter(Context mContext, List<Etapa> mEtape){
        this.mContext = mContext;
        this.mEtape = mEtape;
    }

    @NonNull
    @Override
    public EtapeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.project_item, parent, false);
        return new EtapeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EtapeAdapter.ViewHolder holder, int position) {

        db = FirebaseDatabase.getInstance();

        final Etapa etapa = mEtape.get(position);

        String date = etapa.getStartDate()+" - "+etapa.getEndDate();

        holder.projectname.setText(etapa.getTitlu());
        holder.Date.setText(date);
        holder.status.setText(etapa.getStatus());
        if (etapa.getStatus().equals("Done")){
            holder.status.setBackgroundResource(R.drawable.rounded_corners_green);
        } else if (etapa.getStatus().equals("Stuck")) {
            holder.status.setBackgroundResource(R.drawable.rounded_corners_red);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EtapeActivity.class);
                intent.putExtra("etapaid", etapa.getId());
                intent.putExtra("etapaTitlu", etapa.getTitlu());
                intent.putExtra("etapaDescriere", etapa.getDescriere());
                mContext.startActivity(intent);
            }
        });

        holder.Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupDateChangerEtapa popupDateChangerEtapa = new PopupDateChangerEtapa();
                PopupDateChangerEtapa.etapaid= etapa.getId();
                PopupDateChangerEtapa.endDate= etapa.getEndDate();
                popupDateChangerEtapa.show(((FragmentActivity)mContext).getSupportFragmentManager(), "popup");
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, etapa.getId());
            }
        });

        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenuStatus(v, etapa.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mEtape.size();
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

    private void showPopupMenu(View v, String etapaid) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.hide:
                        DatabaseReference reference = db.getReference("Etape").child(etapaid);

                        Map<String,Object> Map = new HashMap<>();

                        Map.put("visibility", "GONE");


                        reference.updateChildren(Map);

                        return true;
                    case R.id.delete:
                        reference = db.getReference("Etape").child(etapaid);
                        reference.removeValue();

                        return true;

                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void showPopupMenuStatus(View v, String etapaid) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.inflate(R.menu.popup_menu_status);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Working:
                        DatabaseReference reference = db.getReference("Etape").child(etapaid);

                        Map<String,Object> Map = new HashMap<>();

                        Map.put("status", "Working");


                        reference.updateChildren(Map);

                        return true;
                    case R.id.Done:
                        reference = db.getReference("Etape").child(etapaid);
                        Map = new HashMap<>();

                        Map.put("status", "Done");


                        reference.updateChildren(Map);

                        return true;

                    case R.id.Stuck:
                        reference = db.getReference("Etape").child(etapaid);
                        Map = new HashMap<>();

                        Map.put("status", "Stuck");


                        reference.updateChildren(Map);

                        return true;

                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
}
