package com.appstechio.workyzo.adapters;

import android.app.DownloadManager;
import android.content.Context;

import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.activities.SignUp_Activity;
import com.appstechio.workyzo.models.Message;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.appstechio.workyzo.databinding.ReceivedMessagesBinding;
import com.appstechio.workyzo.databinding.ReceivedFilemessagesBinding;
import com.appstechio.workyzo.databinding.SentMessagesBinding;
import com.appstechio.workyzo.databinding.SentFilemessagesBinding;
import com.appstechio.workyzo.security.ClientEncryption;

import com.appstechio.workyzo.security.DBHandler;
import com.appstechio.workyzo.security.Keys;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.bumptech.glide.Glide;


import javax.crypto.Cipher;


public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final ArrayList<Message> messages;
    private final String SenderId;
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_SENT_FILE = 2;
    public static final int VIEW_TYPE_RECEIVED = 3;
    public static final int VIEW_TYPE_RECEIVED_FILE = 4;
    public static final int VIEW_TYPE_START_CHAT = 0;

    public MessagesAdapter(ArrayList<Message> messages, String senderId) {
        this.messages = messages;
        SenderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_SENT) {

            return new SentMessageViewHolder(
                    SentMessagesBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );

        } else if (viewType == VIEW_TYPE_SENT_FILE) {

            return new SentFileMessageViewHolder(
                    SentFilemessagesBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    ));

        } else if (viewType == VIEW_TYPE_RECEIVED_FILE) {
            return new ReceivedFileMessageViewHolder(
                    ReceivedFilemessagesBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    ));
        } else {
            return new ReceivedMessageViewHolder(
                    ReceivedMessagesBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Message msg = messages.get(position);

        //  if(msg.isSeen()){

        //  ((SentMessageViewHolder) holder).sent_binding.MessageSeenIcon.setColorFilter
        //    (ContextCompat.getColor( ((SentMessageViewHolder) holder).sent_binding.MessageSeenIcon.getContext(), R.color.SkyBlue));
        // }

        if (position > 0) {
            if (messages.get(position - 1) != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                // parse old string to date
                LocalDate datestr = LocalDate.parse(getReadableDateTime(messages.get(position).getCreatedAt()), DateTimeFormatter.ofPattern("MMMM,dd,yyyy - hh:mm a"));
                LocalDate datestr1 = LocalDate.parse(getReadableDateTime(messages.get(position - 1).getCreatedAt()), DateTimeFormatter.ofPattern("MMMM,dd,yyyy - hh:mm a"));
                // format date to string
                String newStr = datestr.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String newStr1 = datestr1.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                try {
                    Date date1 = formatter.parse(newStr);
                    Date date2 = formatter.parse(newStr1);

                    if (date1.compareTo(date2) != 0) {
                        // This is a different day than the previous message, so show the Date

                        if (getItemViewType(position) == VIEW_TYPE_SENT) {
                            ((SentMessageViewHolder) holder).setData(messages.get(position));
                        } else if (getItemViewType(position) == VIEW_TYPE_RECEIVED) {
                            ((ReceivedMessageViewHolder) holder).setData(messages.get(position));
                        }

                        if (getItemViewType(position) == VIEW_TYPE_SENT_FILE) {
                            Log.d("SENTIMG", "SENT IMG X");
                            ((SentFileMessageViewHolder) holder).setData(messages.get(position));
                        } else if (getItemViewType(position) == VIEW_TYPE_RECEIVED_FILE) {
                            ((ReceivedFileMessageViewHolder) holder).setData(messages.get(position));
                        }

                    } else {
                        // Same day, so hide the Date
                        if (getItemViewType(position) == VIEW_TYPE_SENT) {
                            ((SentMessageViewHolder) holder).setData_withoutdate(messages.get(position));
                        } else if (getItemViewType(position) == VIEW_TYPE_RECEIVED) {
                            ((ReceivedMessageViewHolder) holder).setData_withoutdate(messages.get(position));
                        }

                        if (getItemViewType(position) == VIEW_TYPE_SENT_FILE) {
                            ((SentFileMessageViewHolder) holder).setData_withoutdate(messages.get(position));
                        } else if (getItemViewType(position) == VIEW_TYPE_RECEIVED_FILE) {
                            ((ReceivedFileMessageViewHolder) holder).setData_withoutdate(messages.get(position));
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {

            // This is the first message, so show the date
            if (getItemViewType(position) == VIEW_TYPE_SENT) {
                try {
                    ((SentMessageViewHolder) holder).setData(messages.get(position));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (getItemViewType(position) == VIEW_TYPE_RECEIVED) {
                try {
                    ((ReceivedMessageViewHolder) holder).setData(messages.get(position));
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }

            if (getItemViewType(position) == VIEW_TYPE_SENT_FILE) {
                ((SentFileMessageViewHolder) holder).setData(messages.get(position));
            } else if (getItemViewType(position) == VIEW_TYPE_RECEIVED_FILE) {
                ((ReceivedFileMessageViewHolder) holder).setData(messages.get(position));
            }
        }

    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    private static String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM,dd,yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String ChangeDateFormat(String startchatdate) throws ParseException {
        String currentdate = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        // parse old string to date
        LocalDate datestr = LocalDate.parse(startchatdate, DateTimeFormatter.ofPattern("MMMM,dd,yyyy - hh:mm a"));
        // format date to string
        String newStr = datestr.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String todaydate = formatter.format(date);
        Date date1 = formatter.parse(todaydate);
        Date date2 = formatter.parse(newStr);  //firebase date stored when start chat Timestamp
        Calendar SixDaysAgo = Calendar.getInstance();
        Calendar OneDayAgo = Calendar.getInstance();

        SixDaysAgo.add(Calendar.DAY_OF_MONTH, -6);
        OneDayAgo.add(Calendar.DAY_OF_MONTH, -2);
        // convert Calendar to Date
        Date SixDaysAgoDate = SixDaysAgo.getTime();
        Date OneDayAgoDate = OneDayAgo.getTime();

        Log.d("Time", String.valueOf(OneDayAgoDate));

        if (date2.compareTo(date1) == 0) {
            //LocalTime datestr1 = LocalTime.parse(startchatdate, DateTimeFormatter.ofPattern("MMMM,dd,yyyy - hh:mm a"));
            // String newStr1 = datestr1.format(DateTimeFormatter.ofPattern("HH:mm"));
            currentdate = "Today";

        } else if (date2.compareTo(date1) < 0 && date2.after(OneDayAgoDate)) {
            currentdate = "Yesterday";

        } else if (date2.compareTo(date1) < 0 && date2.after(SixDaysAgoDate)) {
            SimpleDateFormat format_Day = new SimpleDateFormat("EEEE");
            String day = format_Day.format(date2);
            currentdate = day;

        } else {
            SimpleDateFormat format_fulldate = new SimpleDateFormat("dd/MM/yyyy");
            String fulldate = format_fulldate.format(date2);
            currentdate = fulldate;
        }

        return currentdate;
    }


    @Override
    public int getItemViewType(int position) {

        String text="";
 /*       if(messages.get(position).getContent().contains("==")){
            text = messages.get(position).getContent();
        }else {
            byte[] data = Base64.decode(messages.get(position).getContent(), Base64.NO_WRAP);
            text = new String(data, StandardCharsets.UTF_8);
        }*/
        if(messages.get(position).getMessageDigest() != null){
            text = messages.get(position).getContent();
        }else {
            byte[] data = Base64.decode(messages.get(position).getContent(), Base64.NO_WRAP);
            text = new String(data, StandardCharsets.UTF_8);
        }

        if (messages.get(position).getSenderID().equals(SenderId) && text.contains("https://firebasestorage.googleapis.com")) {
            return VIEW_TYPE_SENT_FILE;
        } else if (messages.get(position).getSenderID().equals(SenderId)) {
            return VIEW_TYPE_SENT;
        } else if (!messages.get(position).getSenderID().equals(SenderId) && text.contains("https://firebasestorage.googleapis.com")) {
            return VIEW_TYPE_RECEIVED_FILE;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }


    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private ClientEncryption clientEndtoEnd_Encryption = new ClientEncryption();
        private final SentMessagesBinding sent_binding;
        private DBHandler dbHandler;

        SentMessageViewHolder(SentMessagesBinding sentMessagesBinding) {
            super(sentMessagesBinding.getRoot());
            sent_binding = sentMessagesBinding;

        }




        @RequiresApi(api = Build.VERSION_CODES.O)
        void setData(Message message) throws Exception {
            PreferenceManager preferenceManager = new PreferenceManager(sent_binding.getRoot().getContext());

            byte[] publicKeyByteServer = Base64.decode(message.getSKey(), Base64.NO_WRAP);
            KeyFactory keyFactory = null;
            try {
                keyFactory = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            PublicKey publicKeyServer = null;
            try {
                publicKeyServer = (PublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByteServer));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }

   /*         //GET PRIVATE KEY FROM PREF
            byte[] privateKeyByteServer = Base64.decode(preferenceManager.getString("PRIVATE"), Base64.NO_WRAP);
            KeyFactory keyFactory1 = null;
            try {
                keyFactory1 = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            PrivateKey privateKeyServer = null;
            try {
                privateKeyServer = (PrivateKey) keyFactory1.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByteServer));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }*/

            dbHandler = new DBHandler(sent_binding.getRoot().getContext());
            ArrayList<Keys> keysArrayList = new ArrayList<>();
            String priKey= null;
            keysArrayList = dbHandler.readKeys();
            for (int i = 0; i < keysArrayList.size(); i ++) {
                if (keysArrayList.get(i).getUser_UID().equals(preferenceManager.getString(Constants.KEY_USERID))){
                    priKey = keysArrayList.get(i).getKeyValue().toString();
                }
            }
            //GET PRIVATE KEY FROM sqllite
            byte[] privateKeyByteServer = Base64.decode(priKey, Base64.NO_WRAP);
            KeyFactory keyFactoryP = null;
            try {
                keyFactoryP = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            PrivateKey privateKeyServer = null;
            try {
                privateKeyServer = (PrivateKey) keyFactoryP.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByteServer));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }

            //RSA DECRYPTION

            //Message
            byte[] decodedBytes = null;
            try {
                Cipher c = Cipher.getInstance("RSA");
                c.init(Cipher.DECRYPT_MODE, privateKeyServer);
                decodedBytes = c.doFinal(Base64.decode(message.getContent().substring(344), Base64.NO_WRAP));
            } catch (Exception e) {
                Log.e("Crypto", "RSA decryption");
            }

            //Digest
            byte[] decodedBytesDigest = null;
            try {
                Cipher a = Cipher.getInstance("RSA");
                a.init(Cipher.DECRYPT_MODE, publicKeyServer);
                decodedBytesDigest = a.doFinal(Base64.decode(message.getMessageDigest(), Base64.NO_WRAP));
            } catch (Exception e) {
                Log.e("Crypto", e.getMessage());
            }

            if (decodedBytes != null && decodedBytesDigest != null) {

                String plainmsg = new String(decodedBytes);

                String Digest = new String(decodedBytesDigest);

                boolean matched = clientEndtoEnd_Encryption.validatePassword(plainmsg,Digest);

                if(matched){
                    sent_binding.textGchatMessageMe.setText(plainmsg);
                }

            }

            sent_binding.textGchatTimestampMe.setText(message.getTimestamp());

            try {
                sent_binding.ConvStartLastDate.setText(ChangeDateFormat(getReadableDateTime(message.getCreatedAt())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        void setData_withoutdate(Message message) throws Exception {

            //Get Public Key from Server
            PreferenceManager preferenceManager = new PreferenceManager(sent_binding.getRoot().getContext());
            byte[] publicKeyByteServer = Base64.decode(message.getSKey(), Base64.NO_WRAP);
            KeyFactory keyFactory = null;
            try {
                keyFactory = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            PublicKey publicKeyServer = null;
            try {
                publicKeyServer = (PublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByteServer));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }

            //GET PRIVATE KEY FROM shared
        /*    byte[] privateKeyByteServer = Base64.decode(preferenceManager.getString("PRIVATE"), Base64.NO_WRAP);
            KeyFactory keyFactory1 = null;
            try {
                keyFactory1 = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            PrivateKey privateKeyServer = null;
            try {
                privateKeyServer = (PrivateKey) keyFactory1.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByteServer));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }*/

            dbHandler = new DBHandler(sent_binding.getRoot().getContext());
            ArrayList<Keys> keysArrayList = new ArrayList<>();
            String priKey= null;
            keysArrayList = dbHandler.readKeys();
            for (int i = 0; i < keysArrayList.size(); i ++) {
                if (keysArrayList.get(i).getUser_UID().equals(preferenceManager.getString(Constants.KEY_USERID))){
                    priKey = keysArrayList.get(i).getKeyValue().toString();
                }
            }
            //GET PRIVATE KEY FROM sqllite
            byte[] privateKeyByteServer = Base64.decode(priKey, Base64.NO_WRAP);
            KeyFactory keyFactoryP = null;
            try {
                keyFactoryP = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            PrivateKey privateKeyServer = null;
            try {
                privateKeyServer = (PrivateKey) keyFactoryP.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByteServer));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }

            //RSA DECRYPTION

            //Message
            byte[] decodedBytes = null;
            try {
                Cipher c = Cipher.getInstance("RSA");
                c.init(Cipher.DECRYPT_MODE, privateKeyServer);
                decodedBytes = c.doFinal(Base64.decode(message.getContent().substring(344), Base64.NO_WRAP));
            } catch (Exception e) {
                Log.e("Crypto", "RSA decryption");
            }

            //Digest
            byte[] decodedBytesDigest = null;
            try {
                Cipher a = Cipher.getInstance("RSA");
                a.init(Cipher.DECRYPT_MODE, publicKeyServer);
                decodedBytesDigest = a.doFinal(Base64.decode(message.getMessageDigest(), Base64.NO_WRAP));
            } catch (Exception e) {
                Log.e("Crypto", e.getMessage());
            }

            if (decodedBytes != null && decodedBytesDigest != null) {

                String plainmsg = new String(decodedBytes);

                String Digest = new String(decodedBytesDigest);

                boolean matched = clientEndtoEnd_Encryption.validatePassword(plainmsg,Digest);

                if(matched){
                    sent_binding.textGchatMessageMe.setText(plainmsg);
                }

            }

            sent_binding.textGchatTimestampMe.setText(message.getTimestamp());
            try {
                sent_binding.ConvStartLastDate.setText(ChangeDateFormat(getReadableDateTime(message.getCreatedAt())));
                sent_binding.ConvStartLastDate.setVisibility(View.GONE);

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    }

        static class SentFileMessageViewHolder extends RecyclerView.ViewHolder {

            private final SentFilemessagesBinding sent_binding;

            SentFileMessageViewHolder(SentFilemessagesBinding sentMessagesBinding) {
                super(sentMessagesBinding.getRoot());
                sent_binding = sentMessagesBinding;

            }


            @RequiresApi(api = Build.VERSION_CODES.O)
            void setData(Message message) {

                //PROFILE PICTURE
                // String FreelancerUser_Image = message.getContent();
                //CONVERT STRING BASE 64 TO BITMAP
                // byte[] decodedString = Base64.decode(FreelancerUser_Image, Base64.DEFAULT);
                //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                // freelancerprofileBinding.freelancerProfilepicView.setImageBitmap(decodedByte);
                byte[] data = Base64.decode(message.getContent(), Base64.NO_WRAP);
                String text = new String(data, StandardCharsets.UTF_8);
                int index = text.indexOf("https");
                //System.out.println("FILEDecrypted : "+ text);
                //int index = text.indexOf("https");


                if(text.contains("Vi")){

                    sent_binding.textGchatMessageMe.setVisibility(View.GONE);
                    sent_binding.textFilechatTimestampMe.setVisibility(View.GONE);
                    sent_binding.MessageSentIcon.setVisibility(View.GONE);
                    sent_binding.textImagechatMessageMe.setVisibility(View.VISIBLE);
                    sent_binding.textImagechatTimestampMe.setVisibility(View.VISIBLE);
                    sent_binding.MessageFileSentIcon.setVisibility(View.VISIBLE);



                    Glide.with(sent_binding.getRoot().getContext()).load(text.substring(index).trim())
                            .centerCrop()
                            .placeholder(R.drawable.ic_cloud_upload)
                            .error(R.drawable.ic_baseline_lock_24)
                            .into(sent_binding.textImagechatMessageMe);

                    sent_binding.PlayIcon.setVisibility(View.VISIBLE);

                    sent_binding.textImagechatTimestampMe.setText(message.getTimestamp());
                    try {
                       // sent_binding.ConvStartFileLastDate.setVisibility(View.VISIBLE);
                        sent_binding.ConvStartFileLastDate.setText(ChangeDateFormat(getReadableDateTime(message.getCreatedAt())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else if (text.contains("Im")){

                    sent_binding.textGchatMessageMe.setVisibility(View.GONE);
                    sent_binding.textFilechatTimestampMe.setVisibility(View.GONE);
                    sent_binding.MessageSentIcon.setVisibility(View.GONE);
                    sent_binding.textImagechatMessageMe.setVisibility(View.VISIBLE);
                    sent_binding.textImagechatTimestampMe.setVisibility(View.VISIBLE);
                    sent_binding.MessageFileSentIcon.setVisibility(View.VISIBLE);

                    //int index1 = text.indexOf("https");
                    Glide.with(sent_binding.getRoot().getContext()).load(text.substring(index).trim())
                            .centerCrop()
                            .placeholder(R.drawable.ic_cloud_upload)
                            .error(R.drawable.ic_baseline_lock_24)
                            .into(sent_binding.textImagechatMessageMe);
                    sent_binding.PlayIcon.setVisibility(View.GONE);

                    sent_binding.textImagechatTimestampMe.setText(message.getTimestamp());


                }else {

                    sent_binding.PlayIcon.setVisibility(View.GONE);
                    sent_binding.textImagechatMessageMe.setVisibility(View.GONE);
                    sent_binding.textImagechatTimestampMe.setVisibility(View.GONE);
                    sent_binding.MessageFileSentIcon.setVisibility(View.GONE);

                    sent_binding.textGchatMessageMe.setVisibility(View.VISIBLE);
                    sent_binding.textFilechatTimestampMe.setVisibility(View.VISIBLE);
                    sent_binding.MessageSentIcon.setVisibility(View.VISIBLE);


                    //int index = text.indexOf("https");
                    sent_binding.textGchatMessageMe.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                    sent_binding.textGchatMessageMe.setText(text.substring(2,index));
                    sent_binding.textFilechatTimestampMe.setText(message.getTimestamp());
                }

                try {

                    sent_binding.ConvStartFileLastDate.setText(ChangeDateFormat(getReadableDateTime(message.getCreatedAt())));
                    System.out.println("FILEDecry: "+ sent_binding.ConvStartFileLastDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }


            @RequiresApi(api = Build.VERSION_CODES.O)
            void setData_withoutdate(Message message) {



                byte[] data = Base64.decode(message.getContent(), Base64.NO_WRAP);
                String text = new String(data, StandardCharsets.UTF_8);
                int index = text.indexOf("https");


                if(text.contains("Vi")){

                    sent_binding.textGchatMessageMe.setVisibility(View.GONE);
                    sent_binding.textFilechatTimestampMe.setVisibility(View.GONE);
                    sent_binding.MessageSentIcon.setVisibility(View.GONE);
                    sent_binding.textImagechatMessageMe.setVisibility(View.VISIBLE);
                    sent_binding.textImagechatTimestampMe.setVisibility(View.VISIBLE);
                    sent_binding.MessageFileSentIcon.setVisibility(View.VISIBLE);

                    Glide.with(sent_binding.getRoot().getContext()).load(text.substring(index).trim())
                            .centerCrop()
                            .placeholder(R.drawable.ic_cloud_upload)
                            .error(R.drawable.ic_baseline_lock_24)
                            .into(sent_binding.textImagechatMessageMe);

                    sent_binding.PlayIcon.setVisibility(View.VISIBLE);

                    sent_binding.textImagechatTimestampMe.setText(message.getTimestamp());

                }else if (text.contains("Im")){

                    sent_binding.textGchatMessageMe.setVisibility(View.GONE);
                    sent_binding.textFilechatTimestampMe.setVisibility(View.GONE);
                    sent_binding.MessageSentIcon.setVisibility(View.GONE);
                    sent_binding.textImagechatMessageMe.setVisibility(View.VISIBLE);
                    sent_binding.textImagechatTimestampMe.setVisibility(View.VISIBLE);
                    sent_binding.MessageFileSentIcon.setVisibility(View.VISIBLE);


                    Glide.with(sent_binding.getRoot().getContext()).load(text.substring(index).trim())
                            .centerCrop()
                            .placeholder(R.drawable.ic_cloud_upload)
                            .error(R.drawable.ic_baseline_lock_24)
                            .into(sent_binding.textImagechatMessageMe);
                    sent_binding.PlayIcon.setVisibility(View.GONE);

                    sent_binding.textImagechatTimestampMe.setText(message.getTimestamp());
                }else {

                    sent_binding.PlayIcon.setVisibility(View.GONE);
                    sent_binding.textImagechatMessageMe.setVisibility(View.GONE);
                    sent_binding.textImagechatTimestampMe.setVisibility(View.GONE);
                    sent_binding.MessageFileSentIcon.setVisibility(View.GONE);

                    sent_binding.textGchatMessageMe.setVisibility(View.VISIBLE);
                    sent_binding.textFilechatTimestampMe.setVisibility(View.VISIBLE);
                    sent_binding.MessageSentIcon.setVisibility(View.VISIBLE);



                    sent_binding.textGchatMessageMe.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                    sent_binding.textGchatMessageMe.setText(text.substring(2,index));
                    sent_binding.textFilechatTimestampMe.setText(message.getTimestamp());
                }


                try {
                    sent_binding.ConvStartFileLastDate.setText(ChangeDateFormat(getReadableDateTime(message.getCreatedAt())));
                    sent_binding.ConvStartFileLastDate.setVisibility(View.GONE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }

        }

        static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

            private ClientEncryption clientEndtoEnd_Encryption = new ClientEncryption();
            private final ReceivedMessagesBinding Receive_binding;
            private DBHandler dbHandler;

            ReceivedMessageViewHolder(ReceivedMessagesBinding receivedMessagesBinding) {
                super(receivedMessagesBinding.getRoot());
                Receive_binding = receivedMessagesBinding;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            void setData(Message message) throws InvalidKeySpecException, NoSuchAlgorithmException {
                PreferenceManager preferenceManager = new PreferenceManager(Receive_binding.getRoot().getContext());
                dbHandler = new DBHandler(Receive_binding.getRoot().getContext());

                byte[] publicKeyByteServer = Base64.decode(message.getSKey(), Base64.NO_WRAP);
                KeyFactory keyFactory = null;
                try {
                    keyFactory = KeyFactory.getInstance("RSA");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                PublicKey publicKeyServer = null;
                try {
                    publicKeyServer = (PublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByteServer));
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }


                 ArrayList<Keys> keysArrayList = new ArrayList<>();
                String priKey= null;
                keysArrayList = dbHandler.readKeys();
                //System.out.println(keysArrayList.get(0).getUser_UID().toString());
                for (int i = 0; i < keysArrayList.size(); i ++) {
                    if (keysArrayList.get(i).getUser_UID().equals(preferenceManager.getString(Constants.KEY_USERID))){
                        priKey = keysArrayList.get(i).getKeyValue().toString();
                    }
                }

                //GET PRIVATE KEY FROM sqllite
                byte[] privateKeyByteServer = Base64.decode(priKey, Base64.NO_WRAP);
                KeyFactory keyFactory1 = null;
                try {
                    keyFactory1 = KeyFactory.getInstance("RSA");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                PrivateKey privateKeyServer = null;
                try {
                    privateKeyServer = (PrivateKey) keyFactory1.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByteServer));
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }

                //RSA DECRYPTION

                //Message
                byte[] decodedBytes = null;
                try {
                    Cipher c = Cipher.getInstance("RSA");
                    c.init(Cipher.DECRYPT_MODE, privateKeyServer);
                    decodedBytes = c.doFinal(Base64.decode(message.getContent().substring(0,344), Base64.NO_WRAP));
                } catch (Exception e) {
                    Log.e("Crypto", "RSA decryption");
                }

                //Digest
                byte[] decodedBytesDigest = null;
                try {
                    Cipher a = Cipher.getInstance("RSA");
                    a.init(Cipher.DECRYPT_MODE, publicKeyServer);
                    decodedBytesDigest = a.doFinal(Base64.decode(message.getMessageDigest(), Base64.NO_WRAP));
                } catch (Exception e) {
                    Log.e("Crypto", e.getMessage());
                }

                if (decodedBytes != null && decodedBytesDigest != null) {

                    String plainmsg = new String(decodedBytes);
                    String Digest = new String(decodedBytesDigest);

                    boolean matched = clientEndtoEnd_Encryption.validatePassword(plainmsg,Digest);

                    if(matched){
                        Receive_binding.textGchatMessageOther.setText(plainmsg);
                    }

                }


                Receive_binding.textGchatTimestampOther.setText(message.getTimestamp());
                try {
                    Receive_binding.ConvStartLastDate1.setText(ChangeDateFormat(getReadableDateTime(message.getCreatedAt())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


            @RequiresApi(api = Build.VERSION_CODES.O)
            void setData_withoutdate(Message message) throws InvalidKeySpecException, NoSuchAlgorithmException {
                PreferenceManager preferenceManager = new PreferenceManager(Receive_binding.getRoot().getContext());
                dbHandler = new DBHandler(Receive_binding.getRoot().getContext());

                byte[] publicKeyByteServer = Base64.decode(message.getSKey(), Base64.NO_WRAP);
                KeyFactory keyFactory = null;
                try {
                    keyFactory = KeyFactory.getInstance("RSA");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                PublicKey publicKeyServer = null;
                try {
                    publicKeyServer = (PublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByteServer));
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }


                ArrayList<Keys> keysArrayList = new ArrayList<>();
                String priKey= null;
                keysArrayList = dbHandler.readKeys();
                //System.out.println(keysArrayList.get(0).getUser_UID().toString());
                for (int i = 0; i < keysArrayList.size(); i ++) {
                    if (keysArrayList.get(i).getUser_UID().equals(preferenceManager.getString(Constants.KEY_USERID))){
                        priKey = keysArrayList.get(i).getKeyValue().toString();
                    }
                }

                //GET PRIVATE KEY FROM sqllite
                byte[] privateKeyByteServer = Base64.decode(priKey, Base64.NO_WRAP);
                KeyFactory keyFactory1 = null;
                try {
                    keyFactory1 = KeyFactory.getInstance("RSA");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                PrivateKey privateKeyServer = null;
                try {
                    privateKeyServer = (PrivateKey) keyFactory1.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByteServer));
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }

                //RSA DECRYPTION

                //Message
                byte[] decodedBytes = null;
                try {
                    Cipher c = Cipher.getInstance("RSA");
                    c.init(Cipher.DECRYPT_MODE, privateKeyServer);
                    decodedBytes = c.doFinal(Base64.decode(message.getContent().substring(0,344), Base64.NO_WRAP));
                } catch (Exception e) {
                    Log.e("Crypto", "RSA decryption");
                }

                //Digest
                byte[] decodedBytesDigest = null;
                try {
                    Cipher a = Cipher.getInstance("RSA");
                    a.init(Cipher.DECRYPT_MODE, publicKeyServer);
                    decodedBytesDigest = a.doFinal(Base64.decode(message.getMessageDigest(), Base64.NO_WRAP));
                } catch (Exception e) {
                    Log.e("Crypto", e.getMessage());
                }

                if (decodedBytes != null && decodedBytesDigest != null) {

                    String plainmsg = new String(decodedBytes);
                    String Digest = new String(decodedBytesDigest);

                    boolean matched = clientEndtoEnd_Encryption.validatePassword(plainmsg,Digest);
                    if(matched){
                        Receive_binding.textGchatMessageOther.setText(plainmsg);
                    }

                }

                Receive_binding.textGchatTimestampOther.setText(message.getTimestamp());
                try {
                    Receive_binding.ConvStartLastDate1.setText(ChangeDateFormat(getReadableDateTime(message.getCreatedAt())));
                    Receive_binding.ConvStartLastDate1.setVisibility(View.GONE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }

        }


        static class ReceivedFileMessageViewHolder extends RecyclerView.ViewHolder {

            private final ReceivedFilemessagesBinding Receive_binding;

            ReceivedFileMessageViewHolder(ReceivedFilemessagesBinding receivedMessagesBinding) {
                super(receivedMessagesBinding.getRoot());
                Receive_binding = receivedMessagesBinding;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            void setData(Message message) {

                byte[] data = Base64.decode(message.getContent(), Base64.NO_WRAP);
                String text = new String(data, StandardCharsets.UTF_8);
                int index = text.indexOf("https");

                if(text.contains("Vi")){

                    Receive_binding.downloadFileBtn.setVisibility(View.GONE);
                    Receive_binding.textGchatMessageOther.setVisibility(View.GONE);
                    Receive_binding.textFilechatTimestampOther.setVisibility(View.GONE);

                    Receive_binding.DownloadFileBtn.setVisibility(View.VISIBLE);
                    Receive_binding.textImagechatMessageOther.setVisibility(View.VISIBLE);
                    Receive_binding.textImagechatTimestampOther.setVisibility(View.VISIBLE);


                    Glide.with(Receive_binding.getRoot().getContext()).load(text.substring(index).trim())
                            .centerCrop()
                            .placeholder(R.drawable.ic_cloud_upload)
                            .error(R.drawable.ic_baseline_lock_24)
                            .into(Receive_binding.textImagechatMessageOther);

                    Receive_binding.PlayIcon.setVisibility(View.VISIBLE);

                    Receive_binding.textImagechatTimestampOther.setText(message.getTimestamp());

                }else if (text.contains("Im")){

                    Receive_binding.downloadFileBtn.setVisibility(View.GONE);
                    Receive_binding.textGchatMessageOther.setVisibility(View.GONE);
                    Receive_binding.textFilechatTimestampOther.setVisibility(View.GONE);

                    Receive_binding.textImagechatMessageOther.setVisibility(View.VISIBLE);
                    Receive_binding.textImagechatTimestampOther.setVisibility(View.VISIBLE);
                    Receive_binding.DownloadFileBtn.setVisibility(View.VISIBLE);


                    Glide.with(Receive_binding.getRoot().getContext()).load(text.substring(index).trim())
                            .centerCrop()
                            .placeholder(R.drawable.ic_cloud_upload)
                            .error(R.drawable.ic_baseline_lock_24)
                            .into(Receive_binding.textImagechatMessageOther);
                    Receive_binding.PlayIcon.setVisibility(View.GONE);

                    Receive_binding.textImagechatTimestampOther.setText(message.getTimestamp());
                }else {

                    Receive_binding.PlayIcon.setVisibility(View.GONE);
                    Receive_binding.textImagechatMessageOther.setVisibility(View.GONE);
                    Receive_binding.textImagechatTimestampOther.setVisibility(View.GONE);
                    Receive_binding.DownloadFileBtn.setVisibility(View.GONE);

                    Receive_binding.textGchatMessageOther.setVisibility(View.VISIBLE);
                    Receive_binding.textFilechatTimestampOther.setVisibility(View.VISIBLE);
                    Receive_binding.downloadFileBtn.setVisibility(View.VISIBLE);


                    //int index = text.indexOf("https");
                    Receive_binding.textGchatMessageOther.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                    Receive_binding.textGchatMessageOther.setText(text.substring(2,index));
                    Receive_binding.textFilechatTimestampOther.setText(message.getTimestamp());
                }

                try {
                    Receive_binding.ConvStartLastDate1.setText(ChangeDateFormat(getReadableDateTime(message.getCreatedAt())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                Receive_binding.DownloadFileBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //int Clicked_position =  holder.getBindingAdapterPosition();

                        File direct = new File(Environment.getExternalStorageDirectory()
                                + "/Workyzo_files");

                        if (!direct.exists()) {
                            direct.mkdirs();
                        }

                        DownloadManager manager = (DownloadManager) Receive_binding.DownloadFileBtn.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                        int index = text.indexOf("https");
                        Uri uri = Uri.parse(text.substring(2,index));
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setDescription("Downloading a file...");
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setTitle("Image");
                        request.setVisibleInDownloadsUi(true);
                        request.allowScanningByMediaScanner();
                        request.setDestinationInExternalPublicDir("/Workyzo_files", "Chat");
                        long reference = manager.enqueue(request);
                    }
                });
            }


            @RequiresApi(api = Build.VERSION_CODES.O)
            void setData_withoutdate(Message message) {

                byte[] data = Base64.decode(message.getContent(), Base64.NO_WRAP);
                String text = new String(data, StandardCharsets.UTF_8);
                int index = text.indexOf("https");

                if(text.contains("Vi")){

                    Receive_binding.downloadFileBtn.setVisibility(View.GONE);
                    Receive_binding.textGchatMessageOther.setVisibility(View.GONE);
                    Receive_binding.textFilechatTimestampOther.setVisibility(View.GONE);

                    Receive_binding.DownloadFileBtn.setVisibility(View.VISIBLE);
                    Receive_binding.textImagechatMessageOther.setVisibility(View.VISIBLE);
                    Receive_binding.textImagechatTimestampOther.setVisibility(View.VISIBLE);

                    Glide.with(Receive_binding.getRoot().getContext()).load(text.substring(index).trim())
                            .centerCrop()
                            .placeholder(R.drawable.ic_cloud_upload)
                            .error(R.drawable.ic_baseline_lock_24)
                            .into(Receive_binding.textImagechatMessageOther);

                    Receive_binding.PlayIcon.setVisibility(View.VISIBLE);

                    Receive_binding.textImagechatTimestampOther.setText(message.getTimestamp());

                }else if (text.contains("Im")){

                    Receive_binding.downloadFileBtn.setVisibility(View.GONE);
                    Receive_binding.textGchatMessageOther.setVisibility(View.GONE);
                    Receive_binding.textFilechatTimestampOther.setVisibility(View.GONE);

                    Receive_binding.textImagechatMessageOther.setVisibility(View.VISIBLE);
                    Receive_binding.textImagechatTimestampOther.setVisibility(View.VISIBLE);
                    Receive_binding.DownloadFileBtn.setVisibility(View.VISIBLE);

                    Glide.with(Receive_binding.getRoot().getContext()).load(text.substring(index).trim())
                            .centerCrop()
                            .placeholder(R.drawable.ic_cloud_upload)
                            .error(R.drawable.ic_baseline_lock_24)
                            .into(Receive_binding.textImagechatMessageOther);
                    Receive_binding.PlayIcon.setVisibility(View.GONE);

                    Receive_binding.textImagechatTimestampOther.setText(message.getTimestamp());
                }else {

                    Receive_binding.PlayIcon.setVisibility(View.GONE);
                    Receive_binding.textImagechatMessageOther.setVisibility(View.GONE);
                    Receive_binding.textImagechatTimestampOther.setVisibility(View.GONE);
                    Receive_binding.DownloadFileBtn.setVisibility(View.GONE);

                    Receive_binding.textGchatMessageOther.setVisibility(View.VISIBLE);
                    Receive_binding.textFilechatTimestampOther.setVisibility(View.VISIBLE);
                    Receive_binding.downloadFileBtn.setVisibility(View.VISIBLE);

                    //int index = text.indexOf("https");
                    Receive_binding.textGchatMessageOther.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                    Receive_binding.textGchatMessageOther.setText(text.substring(2,index));
                    Receive_binding.textFilechatTimestampOther.setText(message.getTimestamp());
                }

                try {
                    Receive_binding.ConvStartLastDate1.setText(ChangeDateFormat(getReadableDateTime(message.getCreatedAt())));
                    Receive_binding.ConvStartLastDate1.setVisibility(View.GONE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Receive_binding.DownloadFileBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //int Clicked_position =  holder.getBindingAdapterPosition();

                        File direct = new File(Environment.getExternalStorageDirectory()
                                + "/Workyzo_files");

                        if (!direct.exists()) {
                            direct.mkdirs();
                        }

                        DownloadManager manager = (DownloadManager) Receive_binding.DownloadFileBtn.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                        int index = text.indexOf("https");
                        Uri uri = Uri.parse(text.substring(2,index));
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setDescription("Downloading a file...");
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setTitle("Image");
                        request.setVisibleInDownloadsUi(true);
                        request.allowScanningByMediaScanner();
                        request.setDestinationInExternalPublicDir("/Workyzo_files", "Chat");
                        long reference = manager.enqueue(request);
                    }
                });
            }

        }
    }







