package com.appstechio.workyzo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appstechio.workyzo.adapters.FreelancerUsersAdapter;
import com.appstechio.workyzo.models.Message;
import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.adapters.ChatUsersAdapter;
import com.appstechio.workyzo.R;
import com.appstechio.workyzo.activities.Chatmessages_Activity;
import com.appstechio.workyzo.adapters.RecentConversationsAdapter;
import com.appstechio.workyzo.security.ClientEncryption;
import com.appstechio.workyzo.security.DBHandler;
import com.appstechio.workyzo.security.Keys;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import com.appstechio.workyzo.databinding.FragmentChatBinding;

import javax.crypto.Cipher;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chat#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class Chat extends Fragment implements ChatUsersAdapter.OnUserchatClickListener, RecentConversationsAdapter.OnUserchatConversationClickListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Chat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Chat.
     */
    // TODO: Rename and change types and number of parameters
    public static Chat newInstance(String param1, String param2) {
        Chat fragment = new Chat();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private PreferenceManager preferenceManager;
    private  ArrayList<Message> conversations = new ArrayList<>();
    private ArrayList<User> users;
    private FirebaseFirestore database;
    ChatUsersAdapter chatUsersAdapter;
    RecentConversationsAdapter recentConversationsAdapter;
    private ActivityResultLauncher<Intent> startchat_activity;
    private FragmentChatBinding binding;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = FragmentChatBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        database = FirebaseFirestore.getInstance();


       /* try {
            getUsers(view);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        users = new ArrayList<>();
        conversations = new ArrayList<>();
        recentConversationsAdapter = new RecentConversationsAdapter(conversations,users,this);
        //DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
       // binding.DisplayUsersConversationRCV.addItemDecoration(itemDecor);
       // binding.DisplayUsersConversationRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
       // binding.DisplayUsersConversationRCV.setAdapter(recentConversationsAdapter);*/

       ListenConversations(view);
        search_for_conversation(view);

        startchat_activity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                        }
                    }
                });



        return view;
    }




    private String getReadableDateTime (Date date){
        return new SimpleDateFormat("MMMM,dd,yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String ChangeDateFormat(String startchatdate) throws ParseException {
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
        SixDaysAgo.add(Calendar.DAY_OF_MONTH, -6);
        // convert Calendar to Date
        Date SixDaysAgoDate = SixDaysAgo.getTime();

        if (date2.compareTo(date1) == 0) {
            LocalTime datestr1 = LocalTime.parse(startchatdate, DateTimeFormatter.ofPattern("MMMM,dd,yyyy - hh:mm a"));
            String newStr1 = datestr1.format(DateTimeFormatter.ofPattern("HH:mm"));
           // SimpleDateFormat format_time = new SimpleDateFormat("HH:mm");
           // String time = format_time.format(newStr1);
            currentdate = newStr1;
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



    //REALTIME DATA LISTENER
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null){
            return ;
        }
        if(value != null) {
            int count = conversations.size();

            for (DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    String senderkey = documentChange.getDocument().getString("SKey");
                    String receiverkey = documentChange.getDocument().getString("RKey");
                    Message message = new Message();
                    message.setSenderID(senderId);
                    message.setReceiverID(receiverId);
                    message.setSKey(senderkey);
                    message.setRKey(receiverkey);
                    message.setType(documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE_TYPE));
                    if (preferenceManager.getString(Constants.KEY_USERID).equals(senderId)) {
                            if (documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE) == null) {
                                Bitmap myLogo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.avatar_man);
                                String encodedimg = encodeImage(myLogo);
                                message.setConversationImage(encodedimg);
                            }else{
                                message.setConversationImage(documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE));
                            }

                        message.setConversationName(documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME));
                        message.setConversationId(documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID));
                        try {
                            message.setTimestamp(ChangeDateFormat(getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP))));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE) == null){
                            Bitmap myLogo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.avatar_man);
                            String encodedimg = encodeImage(myLogo);
                            message.setConversationImage(encodedimg);
                        }else{
                            message.setConversationImage(documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE));
                        }
                        message.setConversationName(documentChange.getDocument().getString(Constants.KEY_SENDER_NAME));
                        message.setConversationId(documentChange.getDocument().getString(Constants.KEY_SENDER_ID));
                        try {
                            message.setTimestamp(ChangeDateFormat(getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP))));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    message.setContent(documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE));
                   // message.setMessageDigest(documentChange.getDocument().getString("LastMessage_Digest"));
                    message.setCreatedAt(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    try {
                        message.setTimestamp(ChangeDateFormat(getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    conversations.add(message);
                }else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for(int i =0;i<conversations.size();i++){
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if(conversations.get(i).getSenderID().equals(senderId) && conversations.get(i).getReceiverID().equals(receiverId)){
                            conversations.get(i).setContent(documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE));
                            conversations.get(i).setCreatedAt(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                           // conversations.get(i).setMessageDigest(documentChange.getDocument().getString("LastMessage_Digest"));
                            //conversations.get(i).setSKey(documentChange.getDocument().getString("SKey"));
                           // conversations.get(i).setRKey(documentChange.getDocument().getString("RKey"));
                            conversations.get(i).setType(documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE_TYPE));
                            try {
                                conversations.get(i).setTimestamp(ChangeDateFormat(getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP))));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }

            Collections.sort(conversations,(obj1 ,obj2) -> obj2.getCreatedAt().compareTo(obj1.getCreatedAt()));
            recentConversationsAdapter.notifyDataSetChanged();
            binding.DisplayUsersConversationRCV.smoothScrollToPosition(0);


        }
    };

    private void ListenConversations(View view){
        preferenceManager = new PreferenceManager(getActivity());
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USERID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
               .whereEqualTo(Constants.KEY_RECEIVER_ID,preferenceManager.getString(Constants.KEY_USERID))
                .addSnapshotListener(eventListener);

    }

    private void search_for_conversation(View view) {

        binding.searchChattxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //binding.resultsRangeLabel.setText(new StringBuilder().append("Search results for").append(" '").append(charSequence).append("'").toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

                recentConversationsAdapter = new RecentConversationsAdapter(conversations,users, Chat.this::onConversationClick);
                binding.DisplayUsersConversationRCV.setAdapter(recentConversationsAdapter);
                if( binding.DisplayUsersConversationRCV.getAdapter().getItemCount() == 0){
                    //do nothing
                }else {
                    filter(editable.toString());
                    binding.DisplayUsersConversationRCV.setAdapter(recentConversationsAdapter);

                    String searchinput = binding.searchChattxt.getText().toString().trim();

                    if (searchinput.isEmpty()) {
                        recentConversationsAdapter = new RecentConversationsAdapter(conversations,users, Chat.this::onConversationClick);
                        binding.DisplayUsersConversationRCV.setAdapter(recentConversationsAdapter);

                    }
                }
            }
        });
    }

    private void filter(String text) {
        ArrayList<Message> filteredFreelancers = new ArrayList<Message>();

        for (int i = 0; i < conversations.size(); i++) {

            if (conversations.get(i).getConversationName().contains(text.toLowerCase())) {
                filteredFreelancers.add(conversations.get(i));
            }
        }
        recentConversationsAdapter.filterList(filteredFreelancers);

    }




    private String encodeImage(Bitmap bitmap){
        int previewwidth = 100;
        int previewHeight = bitmap.getHeight() * previewwidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewwidth,previewHeight,false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte [] bytes = stream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }



    // DISPLAY ALL USERS IN THE DATABASE
    /*public void getUsers(View view) throws ParseException {
       database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        String currentUserID = preferenceManager.getString(Constants.KEY_USERID);

        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    // Loading(false);
                    if (task.isSuccessful() && task.getResult() != null) {
                        users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserID.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.setUsername(queryDocumentSnapshot.getString(Constants.KEY_USERNAME));
                            if (queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE) == null){
                                Bitmap myLogo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.avatar_man);
                                String encodedimg = encodeImage(myLogo);
                                user.setProfile_Image(encodedimg);
                            }else{
                                user.setProfile_Image(queryDocumentSnapshot.getString(Constants.KEY_PROFILE_IMAGE));
                            }
                            user.setUserId(queryDocumentSnapshot.getId());
                            user.setKey(queryDocumentSnapshot.getString("Key"));
                            user.setToken(queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));
                            users.add(user);
                        }

                    }
                    if (users.size() > 0) {

                        ChatUsersAdapter userAdapter = new ChatUsersAdapter(this.getContext(), users, this);
                        DividerItemDecoration itemDecor = new DividerItemDecoration(binding.getRoot().getContext(), DividerItemDecoration.VERTICAL);
                        binding.DisplayUserschatRCV.addItemDecoration(itemDecor);
                        binding.DisplayUserschatRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
                        binding.DisplayUserschatRCV.setAdapter(userAdapter);
                    } else {
                        // Showerrormessage();
                    }
                });
    }*/


    @Override
    public void onchatClick(int position) {
        User user = new User();
        Intent intent = new Intent(getActivity(), Chatmessages_Activity.class);
        //String usernametxt = users.get(position).getUsername();
        user = users.get(position);
        intent.putExtra(Constants.KEY_USER,user);
        //intent.putExtra("usernametitle", usernametxt);
        startchat_activity.launch(intent);

    }
    @Override
    public void onConversationClick(User user) {
        Intent intent = new Intent(getActivity(), Chatmessages_Activity.class);
        //user =users.get(position);
        intent.putExtra(Constants.KEY_USER,user);
        startchat_activity.launch(intent);
    }
/*
    @Override
    public void onConversationClick(User user) {
        Intent intent = new Intent(getActivity(), Chatmessages_Activity.class);
        user =users.get()
        intent.putExtra(Constants.KEY_USER,user);
        startchat_activity.launch(intent);
    }*/

    @Override
    public void onResume() {
        super.onResume();


        //View view = binding.getRoot().getRootView();
        //ListenConversations(view);
        recentConversationsAdapter = new RecentConversationsAdapter(conversations,users,this);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        binding.DisplayUsersConversationRCV.addItemDecoration(itemDecor);
        binding.DisplayUsersConversationRCV.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.DisplayUsersConversationRCV.setAdapter(recentConversationsAdapter);
        Constants.MYJOBS_FLAG = false;
        Constants.PROFILE_FLAG = false;
        Constants.notificationchat_count = 0;


    }


}


