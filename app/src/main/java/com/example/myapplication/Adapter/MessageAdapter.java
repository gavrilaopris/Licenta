package com.example.myapplication.Adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MessageActivity;
import com.example.myapplication.Model.Chat;
import com.example.myapplication.Model.User;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private final Context mContext;
    private final List<Chat> mChat;
    private final String imageurl;
    String fileExtension;

    StorageReference storageReference;
    FirebaseUser fUser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;

    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, int position) {

        String message = mChat.get(position).getMessage();
        String type = mChat.get(position).getType();


        Chat chat = mChat.get(position);

        if (type.equals("text")){
            holder.show_message.setVisibility(View.VISIBLE);
            holder.messageIv.setVisibility(View.GONE);
            holder.document.setVisibility(View.GONE);

            holder.show_message.setText(chat.getMessage());
        }else if (type.equals("image")){
            holder.show_message.setVisibility(View.GONE);
            holder.document.setVisibility(View.GONE);
            holder.messageIv.setVisibility(View.VISIBLE);
            Picasso.get().load(message).placeholder(R.drawable.ic_image_black).into(holder.messageIv);
        }else if (type.equals("document")){
            holder.show_message.setVisibility(View.GONE);
            holder.messageIv.setVisibility(View.GONE);
            holder.document.setVisibility(View.VISIBLE);



            if(chat.getExtension().equals("application/pdf")) {
                fileExtension = ".pdf";
            }else if(chat.getExtension().equals("text/plain")){
                fileExtension = ".txt";
                holder.imageFilePdf.setVisibility(View.INVISIBLE);
                holder.imageFileTXT.setVisibility(View.VISIBLE);
            }else{
                fileExtension = ".docx";
                holder.imageFilePdf.setVisibility(View.INVISIBLE);
                holder.imageFileDocx.setVisibility(View.VISIBLE);
            }

            holder.fileName.setText(chat.getName());
            holder.fileExt.setText(fileExtension);

            holder.downBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadFile(holder.fileName.getContext(), chat.getName(), fileExtension, DIRECTORY_DOWNLOADS, chat.getMessage());
                }
            });
        }



        if (imageurl.equals("defaut")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else {
            Picasso.get().load(imageurl).into(holder.profile_image);
        }

        if (position == mChat.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
//                if (chat.getType().equals("text")){
//                    RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//                    params.addRule(RelativeLayout.BELOW, R.id.show_message);
//                    holder.txt_seen.setLayoutParams(params);
//                }
//                if (chat.getType().equals("image")){
//                    RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//                    params.addRule(RelativeLayout.BELOW, R.id.messageIv);
//                    holder.txt_seen.setLayoutParams(params);
//                }else if(chat.getType().equals("document")){
//                    RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//                    params.addRule(RelativeLayout.BELOW, R.id.document);
//                    holder.txt_seen.setLayoutParams(params);
//                }
            } else {
                holder.txt_seen.setText("Delivered");

                if (chat.getType().equals("image")){
                    RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.messageIv);
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    holder.txt_seen.setLayoutParams(params);
                }else if(chat.getType().equals("document")){
                    RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.document);
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    holder.txt_seen.setLayoutParams(params);
                }

            }

        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }


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
        return mChat.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image, messageIv;

        public TextView txt_seen;

        //document
        public RelativeLayout document;
        public TextView fileName;
        public TextView fileExt;
        public ImageButton downBtn;
        public ImageView imageFilePdf;
        public ImageView imageFileDocx;
        public ImageView imageFileTXT;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            messageIv = itemView.findViewById(R.id.messageIv);

            //document
            document = itemView.findViewById(R.id.document);
            fileName = itemView.findViewById(R.id.fileName);
            fileExt = itemView.findViewById(R.id.fileExt);
            downBtn = itemView.findViewById(R.id.downBtn);
            imageFilePdf = itemView.findViewById(R.id.imageFilePdf);
            imageFileDocx = itemView.findViewById(R.id.imageFileDocx);
            imageFileTXT = itemView.findViewById(R.id.imageFileTXT);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }

    }
}