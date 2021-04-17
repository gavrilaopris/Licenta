package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.EtapeActivity;
import com.example.myapplication.Model.Etapa;
import com.example.myapplication.R;

import java.util.List;

public class EtapeAdapter extends RecyclerView.Adapter<EtapeAdapter.ViewHolder> {

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

        final Etapa etapa = mEtape.get(position);

        String date = etapa.getStartDate()+" - "+etapa.getEndDate();

        holder.projectname.setText(etapa.getTitlu());
        holder.Date.setText(date);
        holder.status.setText(etapa.getStatus());

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

    }

    @Override
    public int getItemCount() {
        return mEtape.size();
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
