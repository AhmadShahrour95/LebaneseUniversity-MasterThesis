package com.appstechio.workyzo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.models.User;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUsersAdapter extends RecyclerView.Adapter<ChatUsersAdapter.ViewHolder> {

        private Context context;
        private ArrayList<User> OtherUsers_Name;

        private OnUserchatClickListener UserchatClickListener;


    public ChatUsersAdapter(Context context, ArrayList<User> otherUsers_Name,
                            OnUserchatClickListener onUserchatClickListener ) {

        this.context = context;
        OtherUsers_Name = otherUsers_Name;
        this.UserchatClickListener =onUserchatClickListener;
    }

    @NonNull
    @Override
    public ChatUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatusers_rcv , parent, false);
        return new ViewHolder(view,UserchatClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUsersAdapter.ViewHolder holder, int position) {
            holder.setUserData(OtherUsers_Name.get(position));
            //holder.lastmsg.setText(LastConversation_message);
            //holder.lastdate.setText(LastConversation_date);
    }

    @Override
    public int getItemCount() {
        return OtherUsers_Name.size();
    }

    public  interface OnUserchatClickListener {
        void onchatClick (int position);

    }
    public static  class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        TextView Name,lastmsg,lastdate;
        CircleImageView img;
        //ShapeableImageView onlinestatus;
        private OnUserchatClickListener onUserchatClickListener;

        public ViewHolder(@NonNull View itemView, OnUserchatClickListener UserchatClickListener) {
            super(itemView);

            Name = itemView.findViewById(R.id.chat_username);
            lastdate = itemView.findViewById(R.id.chat_lastdatetime);
            lastmsg = itemView.findViewById(R.id.chat_lastmessage);
            img =itemView.findViewById(R.id.Userchat_profilepic);
           // onlinestatus =itemView.findViewById(R.id.onlinestatusindicator);
            this.onUserchatClickListener = UserchatClickListener;
            itemView.setOnClickListener(this);


        }

        void setUserData(User user){
            img.setImageBitmap(getUserImage(user.getProfile_Image()));
            Name.setText(user.getUsername());



        }
        private Bitmap getUserImage (String encodedImage){
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }

        @Override
        public void onClick(View view) {
            onUserchatClickListener.onchatClick(getBindingAdapterPosition());
        }
    }

    }


