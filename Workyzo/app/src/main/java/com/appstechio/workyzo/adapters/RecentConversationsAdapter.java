package com.appstechio.workyzo.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appstechio.workyzo.activities.Chatmessages_Activity;
import com.appstechio.workyzo.models.Message;
import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.databinding.ChatusersRecentconversationRcvBinding;
import com.appstechio.workyzo.security.ClientEncryption;
import com.appstechio.workyzo.security.DBHandler;
import com.appstechio.workyzo.security.Keys;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversationViewHolder> {

    private ArrayList<User> OtherUsers_Name;
    private  ArrayList<Message> messages;
    private final OnUserchatConversationClickListener onUserchatConversationClickListener;
    private ClientEncryption clientEndtoEnd_Encryption = new ClientEncryption();
    private DBHandler dbHandler;

    public RecentConversationsAdapter(ArrayList<Message> messageArrayList1,ArrayList<User> otherUsers_Name, OnUserchatConversationClickListener conversationClickListener){

        this.messages = messageArrayList1;
        OtherUsers_Name = otherUsers_Name;
        this.onUserchatConversationClickListener = conversationClickListener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(
                ChatusersRecentconversationRcvBinding.inflate(LayoutInflater.from(parent.getContext()),parent
                ,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        try {
            holder.setData(messages.get(position));
           // holder.setData(OtherUsers_Name.get(position));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

     /*   holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.binding.getRoot().getContext(), Chatmessages_Activity.class);
                //String usernametxt = users.get(position).getUsername();
                User user = OtherUsers_Name.get(position);
                intent.putExtra(Constants.KEY_USER,user);
                holder.binding.getRoot().getContext().startActivity(intent);
            }
        });*/
    }


    public void filterList(ArrayList<Message> filteredlist)
    {
        messages = filteredlist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public  interface OnUserchatConversationClickListener {
        void onConversationClick (User user);
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder{
        private final ChatusersRecentconversationRcvBinding binding;

        public ConversationViewHolder(ChatusersRecentconversationRcvBinding chatusersRecentconversationRcvBinding) {
            super(chatusersRecentconversationRcvBinding.getRoot());
            this.binding = chatusersRecentconversationRcvBinding;

        }



        void setData(Message message) throws InvalidKeySpecException, NoSuchAlgorithmException {

            dbHandler = new DBHandler(binding.getRoot().getContext());
            PreferenceManager preferenceManager = new PreferenceManager(binding.getRoot().getContext());
            binding.UserchatProfilepic.setImageBitmap(getUserConversationImage(message.getConversationImage()));
            binding.chatUsername.setText(message.getConversationName());

            String text="";
            if(message.getType() != null){
                if(message.getType().equals("1")){
                    text = "File";
                }else {
                    byte[] data = Base64.decode(message.getContent(), Base64.NO_WRAP);
                    text = new String(data, StandardCharsets.UTF_8);
                }
                binding.chatRecentmessage.setText(text);
            }

            binding.chatLastdatetime.setText(message.getTimestamp());
            binding.getRoot().setOnClickListener(v -> {
                User user = new User();
                user.setUserId(message.getConversationId());
                user.setUsername(message.getConversationName());
                user.setProfile_Image(message.getConversationImage());
                user.setKey(message.getRKey());
                onUserchatConversationClickListener.onConversationClick(user);
            });
        }

        }

        private Bitmap getUserConversationImage (String encodedImage){
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }




    }
//}



