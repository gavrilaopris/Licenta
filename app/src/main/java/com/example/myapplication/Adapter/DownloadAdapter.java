package com.example.myapplication.Adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Download;
import com.example.myapplication.Model.Task;
import com.example.myapplication.R;


import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Download> mDown;
    String fileExtension;

    public DownloadAdapter(Context mContext, List<Download> mDown) {
        this.mContext = mContext;
        this.mDown = mDown;
    }

    @NonNull
    @Override
    public DownloadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.element_download, parent, false);
        return new DownloadAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadAdapter.ViewHolder holder, int position) {

        final Download download = mDown.get(position);


        if(download.getExtension().equals("application/pdf")) {
            fileExtension = ".pdf";
        }else if(download.getExtension().equals("text/plain")){
            fileExtension = ".txt";
            holder.imageFilePdf.setVisibility(View.INVISIBLE);
            holder.imageFileTXT.setVisibility(View.VISIBLE);
        }else{
            fileExtension = ".docx";
            holder.imageFilePdf.setVisibility(View.INVISIBLE);
            holder.imageFileDocx.setVisibility(View.VISIBLE);
        }

        holder.fileName.setText(download.getName());
        holder.fileExt.setText(fileExtension);

        holder.downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(holder.fileName.getContext(), download.getName(), fileExtension, DIRECTORY_DOWNLOADS, download.getLink());
            }
        });


    }

    private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadManager.enqueue(request);

    }

    @Override
    public int getItemCount() {
        return mDown.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView fileName;
        public TextView fileExt;
        public ImageButton downBtn;
        public ImageView imageFilePdf;
        public ImageView imageFileDocx;
        public ImageView imageFileTXT;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.fileName);
            fileExt = itemView.findViewById(R.id.fileExt);
            downBtn = itemView.findViewById(R.id.downBtn);
            imageFilePdf = itemView.findViewById(R.id.imageFilePdf);
            imageFileDocx = itemView.findViewById(R.id.imageFileDocx);
            imageFileTXT = itemView.findViewById(R.id.imageFileTXT);

        }
    }
}
