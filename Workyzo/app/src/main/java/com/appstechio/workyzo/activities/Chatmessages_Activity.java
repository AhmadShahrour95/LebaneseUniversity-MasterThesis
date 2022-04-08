package com.appstechio.workyzo.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;


import com.appstechio.workyzo.R;
import com.appstechio.workyzo.adapters.ChatUsersAdapter;
import com.appstechio.workyzo.models.Message;
import com.appstechio.workyzo.adapters.MessagesAdapter;
import com.appstechio.workyzo.models.User;
import com.appstechio.workyzo.databinding.ActivityChatmessagesBinding;
import com.appstechio.workyzo.interfaces.Display_Toasts;
import com.appstechio.workyzo.network.ApiClient;
import com.appstechio.workyzo.network.ApiService;
import com.appstechio.workyzo.security.ClientEncryption;
import com.appstechio.workyzo.security.DBHandler;
import com.appstechio.workyzo.security.Keys;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
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
import java.util.HashMap;
import java.util.Locale;

import javax.crypto.Cipher;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Chatmessages_Activity extends BaseActivity implements Display_Toasts {


private ClientEncryption clientEncryption = new ClientEncryption();
private User receiverUser;
    private DBHandler dbHandler;
private ArrayList<Message> messageArrayList;
private MessagesAdapter messagesAdapter;
private PreferenceManager preferenceManager;
private FirebaseFirestore database;
private ActivityChatmessagesBinding ChatConversation_layout;
private String conversationId = null;
private Boolean isReceiverAvailable = false;
private Boolean isReceiverSeenMessage = false;
private ActivityResultLauncher<Intent> Openfile_picker;
    private ArrayList<MediaFile> Mediafiles;
    private String encode_sendfile;;
private DocumentReference documentReference;
private  String URL= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatConversation_layout = ActivityChatmessagesBinding.inflate(getLayoutInflater());
        View view = ChatConversation_layout.getRoot();
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(view);
        backbtn_pressed();
        loadReceiverDetails();
        //PopulateMessageBox();
        Populate_Messagebox();
        ListenMessages();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        clickSendMessage();
        ClickSendAttachment();



        Openfile_picker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            preferenceManager = new PreferenceManager(getApplicationContext());
                            Mediafiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);


                            //Do something with files
                            //Get file name
                            for (int i = 0; i<Mediafiles.size();i++){
                                Uri uri = Mediafiles.get(i).getUri();
                                String filename = Mediafiles.get(i).getName();
                                Uri Fileuri =  Mediafiles.get(i).getUri();
                                int type = Mediafiles.get(i).getMediaType();
                                System.out.println(type);

                                    StorageReference Folder = FirebaseStorage.getInstance().getReference().child("Files");
                                    StorageReference File = Folder.child(filename);
                                    File.putFile(Fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            File.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    URL = uri.toString();


                                                    HashMap<String ,Object> message = new HashMap<>();
                                                    message.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USERID));
                                                    message.put(Constants.KEY_RECEIVER_ID,receiverUser.getUserId());
                                                    message.put(Constants.KEY_TIMESTAMP,new Date());
                                                    //message.put(Constants.KEY_MESSAGE, URL);

                                                    if(type == 0){
                                                        String file_byte = "Fi"+filename+URL;
                                                        encode_sendfile = Base64.encodeToString(file_byte.getBytes(), Base64.NO_WRAP);
                                                        message.put(Constants.KEY_MESSAGE,  encode_sendfile);

                                                    }else if (type == 1){
                                                        String file_byte = "Im"+filename+URL;
                                                        encode_sendfile = Base64.encodeToString(file_byte.getBytes(), Base64.NO_WRAP);
                                                        message.put(Constants.KEY_MESSAGE, encode_sendfile);

                                                    }else if (type == 2){
                                                        String file_byte = "Au"+filename+URL;
                                                        encode_sendfile = Base64.encodeToString(file_byte.getBytes(), Base64.NO_WRAP);
                                                        message.put(Constants.KEY_MESSAGE, encode_sendfile);

                                                    }else {
                                                        String file_byte = "Vi"+filename+URL;
                                                        encode_sendfile = Base64.encodeToString(file_byte.getBytes(), Base64.NO_WRAP);
                                                        message.put(Constants.KEY_MESSAGE, encode_sendfile);


                                                    }
                                                    database.collection(Constants.KEY_COLLECTION_CHAT).add(message);

                                                    System.out.println("FILE: "+ encode_sendfile);

                                                    if(conversationId != null){
                                                        updateConversation_File(encode_sendfile,"1");
                                                        //updateConversationSeen(true);
                                                    } else {
                                                        HashMap<String,Object> convers = new HashMap<>();
                                                        convers.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USERID));
                                                        convers.put(Constants.KEY_SENDER_NAME,preferenceManager.getString(Constants.KEY_USERNAME));
                                                        convers.put(Constants.KEY_SENDER_IMAGE,preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
                                                        convers.put(Constants.KEY_RECEIVER_ID,receiverUser.getUserId());
                                                        convers.put(Constants.KEY_RECEIVER_NAME,receiverUser.getUsername());
                                                        convers.put(Constants.KEY_RECEIVER_IMAGE,receiverUser.getProfile_Image());
                                                        convers.put(Constants.KEY_LAST_MESSAGE,encode_sendfile);
                                                        //convers.put(Constants.KEY_LAST_MESSAGE_TYPE,"0");
                                                        convers.put(Constants.KEY_TIMESTAMP,new Date());

                                                        addConversation(convers);
                                                    }
                                                    if(!isReceiverAvailable){
                                                        try {
                                                            JSONArray tokens = new JSONArray();
                                                            tokens.put(receiverUser.getToken());
                                                            JSONObject data1 = new JSONObject();
                                                            data1.put(Constants.KEY_USERID,preferenceManager.getString(Constants.KEY_USERID));
                                                            data1.put(Constants.KEY_USERNAME,preferenceManager.getString(Constants.KEY_USERNAME));
                                                            data1.put(Constants.KEY_FCM_TOKEN,preferenceManager.getString(Constants.KEY_FCM_TOKEN));
                                                            data1.put(Constants.KEY_MESSAGE,preferenceManager.getString(Constants.KEY_MESSAGE));
                                                            data1.put(Constants.NOTIFICATION_TYPE,"chat_message");

                                                            JSONObject body = new JSONObject();
                                                            body.put(Constants.REMOTE_MSG_DATA,data1);
                                                            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);
                                                            SendNotification(body.toString());


                                                        } catch (Exception e){
                                                            showToast(e.getMessage(),1);
                                                        }
                                                    }
                                                    ChatConversation_layout.messageinputtxt.setText(null);
                                                }
                                            });


                                        }
                                    });


                            }


                        }

                    }

                });

    }

    private void MessageInputtxtField_IsEmpty(){
        ChatConversation_layout.messageinputtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(ChatConversation_layout.messageinputtxt.getText().toString().isEmpty()){
                   ChatConversation_layout.SendmessageBtn.setEnabled(false);
                }else{
                    ChatConversation_layout.SendmessageBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private String getReadableDateTime (Date date){
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
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

    //MESSAGES LISTENER
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = messageArrayList.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Message chatmessage = new Message();
                    chatmessage.setSenderID(documentChange.getDocument().getString(Constants.KEY_SENDER_ID));
                    chatmessage.setReceiverID(documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID));
                    chatmessage.setContent(documentChange.getDocument().getString(Constants.KEY_MESSAGE));
                    chatmessage.setTimestamp(getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP)));
                    chatmessage.setCreatedAt(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatmessage.setSKey(documentChange.getDocument().getString("SKey"));
                    chatmessage.setRKey(documentChange.getDocument().getString("RKey"));
                    chatmessage.setMessageDigest(documentChange.getDocument().getString("Message_Digest"));


                    messageArrayList.add(chatmessage);

                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {

            }

            }
            Collections.sort(messageArrayList, (obj1, obj2) -> obj1.getCreatedAt().compareTo(obj2.getCreatedAt()));
            if (count == 0) {
                messagesAdapter.notifyDataSetChanged();
            } else {
                messagesAdapter.notifyItemRangeInserted(messageArrayList.size() , messageArrayList.size());
                ChatConversation_layout.messagesviewRcv.smoothScrollToPosition(messagesAdapter.getItemCount() - 1);

            }
            if(conversationId == null){
                CheckForConversation();
            }
        }
    };


    private void ListenAvailabilityOfReceiver() {
        database.collection(Constants.KEY_COLLECTION_USERS).document(receiverUser.getUserId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }

                        if (value != null && value.exists()) {
                            if (value.getBoolean(Constants.KEY_STATUS)) {
                                isReceiverAvailable = true;
                            } else {
                                isReceiverAvailable = false;
                            }
                            receiverUser.setToken(value.getString(Constants.KEY_FCM_TOKEN));
                        }

                        if (isReceiverAvailable) {
                            ChatConversation_layout.onlinestatusindicatorInsideChat.setVisibility(View.VISIBLE);
                            ChatConversation_layout.textView9.setText("Online");

                        } else {
                            ChatConversation_layout.textView9.setText("Offline");
                            ChatConversation_layout.onlinestatusindicatorInsideChat.setVisibility(View.GONE);
                        }


                    }

                });
    }



    private void ListenMessages(){
        database.collection(Constants.KEY_COLLECTION_CHAT).
                whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USERID)).
                whereEqualTo(Constants.KEY_RECEIVER_ID,receiverUser.getUserId())
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT).
                whereEqualTo(Constants.KEY_SENDER_ID,receiverUser.getUserId())
                .whereEqualTo(Constants.KEY_RECEIVER_ID,preferenceManager.getString(Constants.KEY_USERID))
                .addSnapshotListener(eventListener);
    }



    private void Populate_Messagebox(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        messageArrayList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(
                messageArrayList,preferenceManager.getString(Constants.KEY_USERID)
        );
        ChatConversation_layout.messagesviewRcv.setAdapter(messagesAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private static String getReadableDateTimeTostring(Date date){
        return new SimpleDateFormat("MMMM,dd,yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    void showSendFileDialog(){
        final Dialog dialog = new Dialog(Chatmessages_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.chat_sendfile_dialog);

        Button SendImageandVideo_btn = dialog.findViewById(R.id.SendImageVideo_btn);
        Button SendAudio_btn = dialog.findViewById(R.id.SendAudio_btn);
        Button SendDocument_btn = dialog.findViewById(R.id.SendDocument_btn);


        SendImageandVideo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageandVideoPicker();
                dialog.cancel();
            }
        });

        SendAudio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioPicker();
                dialog.cancel();
            }
        });

        SendDocument_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePicker();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private  void ClickSendAttachment(){
        ChatConversation_layout.SendFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSendFileDialog();

            }
        });

    }

    private void ImageandVideoPicker(){
        Intent intent = new Intent(getApplicationContext(), FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(true)
                .enableImageCapture(true)
                .setShowVideos(true)
                .enableVideoCapture(true)
                .setMaxSelection(10)
                .setSkipZeroSizeFiles(true)
                .build());

        Openfile_picker.launch(intent);
    }


    private void AudioPicker(){
        Intent intent = new Intent(getApplicationContext(), FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowAudios(true)
                .setShowImages(false)
                .setShowVideos(false)
                .setMaxSelection(10)
                .setSkipZeroSizeFiles(true)
                .build());

        Openfile_picker.launch(intent);
    }

    private void FilePicker(){
        Intent intent = new Intent(getApplicationContext(), FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowFiles(true)
                .setShowImages(false)
                .setShowVideos(false)
                .setSuffixes("txt", "pdf", "html", "rtf", "csv", "xml",
                        "zip", "tar", "gz", "rar", "7z","torrent",
                        "doc", "docx", "odt", "ott",
                        "ppt", "pptx", "pps",
                        "xls", "xlsx", "ods", "ots")
                .setMaxSelection(10)
                .setSkipZeroSizeFiles(true)
                .build());

        Openfile_picker.launch(intent);
    }


    //FireStore Chat
    private  void clickSendMessage() {
        ChatConversation_layout.SendmessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               receiverUser.setKey(preferenceManager.getString("RECEIVERKey"));

                //FirebaseFirestore db = FirebaseFirestore.getInstance();
                database.collection(Constants.KEY_COLLECTION_CHAT)
                        .whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USERID))
                        .get()
                        .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful() && task1.getResult() != null &&
                                            task1.getResult().getDocuments().size() > 0) {
                                        for (int x = 0; x < task1.getResult().getDocuments().size(); x++) {
                                            DocumentSnapshot documentSnapshot1 = task1.getResult().getDocuments().get(x);
                                            //Check THE receiver Id
                                            if (documentSnapshot1.getString(Constants.KEY_RECEIVER_ID).equals(receiverUser.getUserId())) {
                                                // Channel is Created between the sender and the receiver

                                                preferenceManager.putString("PUBKEY",documentSnapshot1.getString("SKey"));
                                                break;
                                            }
                                        }
                                    }else{
                                    }
                                });



                if (preferenceManager.getString("PUBKEY") == null){

                    preferenceManager.putString("PUBKEY",preferenceManager.getString("MyKey"));


                    //GET PRIVATE KEY FROM sqllite
                    dbHandler = new DBHandler(getApplicationContext());
                    ArrayList<Keys> keysArrayList = new ArrayList<>();
                    String priKey= null;
                    keysArrayList = dbHandler.readKeys();
                    for (int i = 0; i < keysArrayList.size(); i ++) {
                        if (keysArrayList.get(i).getUser_UID().equals(preferenceManager.getString(Constants.KEY_USERID))){
                            priKey = keysArrayList.get(i).getKeyValue().toString();
                        }
                    }

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


                    //User PUBLIC KEY

                    // ... transport to server
                    byte[] publicKeyByte = Base64.decode(preferenceManager.getString("MyKey"), Base64.NO_WRAP);
                    KeyFactory keyFactory1 = null;
                    try {
                        keyFactory1 = KeyFactory.getInstance("RSA");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    PublicKey MypublicKey = null;
                    try {
                        MypublicKey = (PublicKey) keyFactory1.generatePublic(new X509EncodedKeySpec(publicKeyByte));
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }

                    //Receiver Public Key for Message
                    byte[] publicKeyByteServer = Base64.decode(receiverUser.getKey(), Base64.NO_WRAP);
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


                    //RSA ENCRYPTION

                    //MESSAGE
                    try {
                        byte[] encodedBytes = null;
                        try {
                            Cipher c = Cipher.getInstance("RSA");
                            c.init(Cipher.ENCRYPT_MODE, publicKeyServer);
                            encodedBytes = c.doFinal(ChatConversation_layout.messageinputtxt.getText().toString().getBytes());
                        } catch (Exception e) {
                            Log.e("Crypto", "RSA encryption error");
                        }
                        //for sender message
                        byte[] encodedBytes1 = null;
                        try {
                            Cipher c1 = Cipher.getInstance("RSA");
                            c1.init(Cipher.ENCRYPT_MODE, MypublicKey);
                            encodedBytes1 = c1.doFinal(ChatConversation_layout.messageinputtxt.getText().toString().getBytes());
                        } catch (Exception e) {
                            Log.e("Crypto", "RSA encryption error");
                        }

                       String EncryptedMessage =  Base64.encodeToString(encodedBytes,Base64.NO_WRAP);
                       String EncryptedSentMessage =  Base64.encodeToString(encodedBytes1,Base64.NO_WRAP);

                       preferenceManager.putString("LastMessage",EncryptedMessage+EncryptedSentMessage);


                        //DIGESTMESSAGE
                        byte[] encodedDigestBytes = null;
                        try {
                            Cipher a = Cipher.getInstance("RSA");
                            a.init(Cipher.ENCRYPT_MODE, privateKeyServer);

                            //Hash Message
                            String hash = clientEncryption.HashingMessage(ChatConversation_layout.messageinputtxt.getText().toString());
                            System.out.println("HASH1  "+ hash);

                            //Encrypt Hash
                            encodedDigestBytes = a.doFinal(hash.getBytes());
                            System.out.println("HASHByte  "+ encodedDigestBytes);

                        } catch (Exception e) {
                            Log.e("Crypto", "RSA encryption error");
                        }

                        String Hashedmessage = Base64.encodeToString(encodedDigestBytes,Base64.NO_WRAP);
                        preferenceManager.putString("LastHashedMessage",Hashedmessage);

                        HashMap<String ,Object> message = new HashMap<>();
                        message.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USERID));
                        message.put(Constants.KEY_RECEIVER_ID,receiverUser.getUserId());
                        message.put(Constants.KEY_MESSAGE,EncryptedMessage+EncryptedSentMessage);
                        message.put("Message_Digest",Hashedmessage);
                        message.put(Constants.KEY_TIMESTAMP,new Date());
                        message.put("SKey",preferenceManager.getString("PUBKEY"));
                        message.put("RKey",receiverUser.getKey());

                        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //CHANNEL CREATED BEFORE BETWEEN THE SENDER AND RECEIVER
                }else{

                    System.out.println("CHANNEL CREATED BEFORE");
                    //RSA ENCRYPTION
                    try {
                        byte[] encodedBytes = null;

                        //GET PRIVATE KEY
                        dbHandler = new DBHandler(getApplicationContext());
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

                        //MY Public KEY FROM PREF
                        byte[] publicKeyByteServer = Base64.decode(preferenceManager.getString("PUBKEY"), Base64.NO_WRAP);
                        KeyFactory keyFactory1 = null;
                        try {
                            keyFactory1 = KeyFactory.getInstance("RSA");
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }

                        PublicKey MypublicKeyServer = null;
                        try {
                            MypublicKeyServer = (PublicKey) keyFactory1.generatePublic(new X509EncodedKeySpec(publicKeyByteServer));
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        }

                        //Receiver Public Key
                        byte[] publicKeyByteServer2 = Base64.decode(receiverUser.getKey().toString(), Base64.NO_WRAP);
                        KeyFactory keyFactory2 = null;
                        try {
                            keyFactory2 = KeyFactory.getInstance("RSA");
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        PublicKey publicKeyServer2 = null;
                        try {
                            publicKeyServer2 = (PublicKey) keyFactory2.generatePublic(new X509EncodedKeySpec(publicKeyByteServer2));
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        }

                        //MESSAGE
                        try {
                            Cipher c = Cipher.getInstance("RSA");
                            c.init(Cipher.ENCRYPT_MODE, publicKeyServer2);
                            encodedBytes = c.doFinal(ChatConversation_layout.messageinputtxt.getText().toString().getBytes());
                        } catch (Exception e) {
                            Log.e("Crypto", "RSA encryption error");
                        }

                        //for sender message
                        byte[] encodedBytes1 = null;
                        try {
                            Cipher c1 = Cipher.getInstance("RSA");
                            c1.init(Cipher.ENCRYPT_MODE, MypublicKeyServer);
                            encodedBytes1 = c1.doFinal(ChatConversation_layout.messageinputtxt.getText().toString().getBytes());
                        } catch (Exception e) {
                            Log.e("Crypto", "RSA encryption error");
                        }

                       String EncryptedMessage =  Base64.encodeToString(encodedBytes,Base64.NO_WRAP);
                        String EncryptedSentMessage =  Base64.encodeToString(encodedBytes1,Base64.NO_WRAP);

                        preferenceManager.putString("LastMessage",EncryptedMessage+EncryptedSentMessage);

                        //DIGESTMESSAGE
                        byte[] encodedDigestBytes = null;
                        try {
                            Cipher a = Cipher.getInstance("RSA");
                            a.init(Cipher.ENCRYPT_MODE, privateKeyServer);
                            //Hash Message
                            String hash = clientEncryption.HashingMessage(ChatConversation_layout.messageinputtxt.getText().toString());

                            //Encrypt Hash
                            encodedDigestBytes = a.doFinal(hash.getBytes());


                        } catch (Exception e) {
                            Log.e("Crypto", "RSA encryption error");
                        }

                        String Hashedmessage = Base64.encodeToString(encodedDigestBytes,Base64.NO_WRAP);
                        preferenceManager.putString("LastHashedMessage",Hashedmessage);

                        HashMap<String ,Object> message = new HashMap<>();
                        message.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USERID));
                        message.put(Constants.KEY_RECEIVER_ID,receiverUser.getUserId());
                        message.put(Constants.KEY_MESSAGE,EncryptedMessage+EncryptedSentMessage);
                        message.put("Message_Digest",Hashedmessage);
                        message.put(Constants.KEY_TIMESTAMP,new Date());
                        message.put("SKey",preferenceManager.getString("PUBKEY"));
                        message.put("RKey",receiverUser.getKey());

                        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                String encode = Base64.encodeToString(ChatConversation_layout.messageinputtxt.getText().toString().getBytes(), Base64.NO_WRAP);

                if(conversationId != null){
                  //  updateConversation(preferenceManager.getString("LastMessage"),
                           // preferenceManager.getString("LastHashedMessage"));
                      updateConversation(encode,
                   "0");
                } else {
                    HashMap<String,Object> convers = new HashMap<>();
                    convers.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USERID));
                    convers.put(Constants.KEY_SENDER_NAME,preferenceManager.getString(Constants.KEY_USERNAME));
                    convers.put(Constants.KEY_SENDER_IMAGE,preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
                    convers.put(Constants.KEY_RECEIVER_ID,receiverUser.getUserId());
                    convers.put(Constants.KEY_RECEIVER_NAME,receiverUser.getUsername());
                    convers.put(Constants.KEY_RECEIVER_IMAGE,receiverUser.getProfile_Image());
                    //convers.put("SKey",preferenceManager.getString("PUBKEY"));
                    //convers.put("LastMessage_Digest",preferenceManager.getString("LastHashedMessage"));
                    //convers.put(Constants.KEY_LAST_MESSAGE,preferenceManager.getString("LastMessage"));
                    convers.put(Constants.KEY_LAST_MESSAGE_TYPE,"0");
                    convers.put(Constants.KEY_LAST_MESSAGE,encode);
                    convers.put(Constants.KEY_TIMESTAMP,new Date());
                    addConversation(convers);
                }
                if(!isReceiverAvailable){
                    try {
                        JSONArray tokens = new JSONArray();
                        tokens.put(receiverUser.getToken());
                        JSONObject data = new JSONObject();
                        data.put(Constants.KEY_USERID,preferenceManager.getString(Constants.KEY_USERID));
                        data.put(Constants.KEY_USERNAME,preferenceManager.getString(Constants.KEY_USERNAME));
                        data.put(Constants.KEY_FCM_TOKEN,preferenceManager.getString(Constants.KEY_FCM_TOKEN));
                       // data.put(Constants.KEY_PROFILE_IMAGE,preferenceManager.getString(Constants.KEY_PROFILE_IMAGE));
                        data.put(Constants.NOTIFICATION_TYPE,"chat_message");
                        //data.put(Constants.KEY_MESSAGE,preferenceManager.getString(Constants.KEY_MESSAGE));
                        data.put(Constants.KEY_MESSAGE,ChatConversation_layout.messageinputtxt.getText().toString());

                        JSONObject body = new JSONObject();
                        body.put(Constants.REMOTE_MSG_DATA,data);
                        body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);
                        SendNotification(body.toString());


                    } catch (Exception e){
                        showToast(e.getMessage(),1);
                    }
                }
                ChatConversation_layout.messageinputtxt.setText(null);

            }
        });
    }



    private void SendNotification(String messageBody){
        ApiClient.getClient().create(ApiService.class).sendMessage(
                Constants.getremoteMsgHeaders(),
                messageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    try {
                    if(response.body() != null){
                        JSONObject responseJson = new JSONObject(response.body());
                        JSONArray results = responseJson.getJSONArray("results");
                        if(responseJson.getInt("failure") == 1) {
                            JSONObject error =(JSONObject)  results.get(0);
                            showToast(error.getString("error"),1);
                            return;
                        }
                    }
                } catch (JSONException e) {
                        e.printStackTrace();
                    }
                showToast("Notification sent successfully",1);
                }else{
                    showToast("Error"+response.code(),1);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast(t.getMessage(),1);
            }
        });
    }

    private void loadReceiverDetails(){

        receiverUser =(User) getIntent().getSerializableExtra(Constants.KEY_USER);
        ChatConversation_layout.OtherUserchatUsername.setText(receiverUser.getUsername());
        //CONVERT STRING BASE 64 TO BITMAP
        if( ChatConversation_layout.otheruserchatProfilepic.getDrawable() != null){
            //receiverUser.setProfile_Image(Constants.KEY_PROFILE_IMAGE);
            byte[] decodedString = Base64.decode(receiverUser.getProfile_Image(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ChatConversation_layout.otheruserchatProfilepic.setImageBitmap(decodedByte);
        }else{

        }

        //Get Receiver Key from User
        database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo("Username",receiverUser.getUsername())
                .get()
                .addOnCompleteListener(task -> {
                    // Loading(false);
                    if (task.isSuccessful() && task.getResult() != null) {
                        String Rkey = task.getResult().getDocuments().get(0).get("Key").toString();
                        preferenceManager.putString("RECEIVERKey",Rkey);

                }
                });


    }

    private void addConversation(HashMap<String, Object> conversation){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversation)
                .addOnSuccessListener(documentReference -> conversationId = documentReference.getId());
    }

    private void updateConversation(String msg,String type){
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .document(conversationId);
        documentReference.update(Constants.KEY_LAST_MESSAGE,msg,Constants.KEY_LAST_MESSAGE_TYPE,type,
                Constants.KEY_TIMESTAMP, new Date());
    }

    private void updateConversation_File(String msg,String type){
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .document(conversationId);
        documentReference.update(Constants.KEY_LAST_MESSAGE,msg,Constants.KEY_LAST_MESSAGE_TYPE,type,
                Constants.KEY_TIMESTAMP, new Date());
    }




    private  void backbtn_pressed(){
        ChatConversation_layout.backtochatusersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTaskRoot()) {
                    Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent1);
                    Intent intent2 = new Intent(getApplicationContext(),Home_Activity.class);
                    startActivity(intent2);
                    onBackPressed();
                }else {
                    onBackPressed();
                }

            }
        });
    }

    private void CheckForConversation(){
        if(messageArrayList.size() != 0){
            CheckForConversationRemotely(
                    preferenceManager.getString(Constants.KEY_USERID),receiverUser.getUserId()
            );
            CheckForConversationRemotely(
                    receiverUser.getUserId(),preferenceManager.getString(Constants.KEY_USERID)
            );

        }
    }

    private void CheckForConversationRemotely(String SenderId, String ReceiverId){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID,SenderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID,ReceiverId)
                .get()
                .addOnCompleteListener(conversationOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversationOnCompleteListener = task -> {
        if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversationId = documentSnapshot.getId();
        }
    };



    @Override
    protected void onResume() {
        super.onResume();

        Constants.notificationchat_count = 0;
        ListenAvailabilityOfReceiver();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }




}