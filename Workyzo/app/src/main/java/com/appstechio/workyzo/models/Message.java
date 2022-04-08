package com.appstechio.workyzo.models;

import java.util.Date;

public class Message {

    private String content;
    private String senderID;
    private String receiverID;
    private String Timestamp;
    private Date CreatedAt;
    private String SKey;
    private String RKey;
    private String MessageDigest;
    private  String conversationId,ConversationName,ConversationImage,Type;



    public Message(){

    }

    public Message(String content, String senderID, String receiverID, String timestamp, Date createdAt, String conversationId, String conversationName, String conversationImage) {
        this.content = content;
        this.senderID = senderID;
        this.receiverID = receiverID;
        Timestamp = timestamp;
        CreatedAt = createdAt;
        this.conversationId = conversationId;
        ConversationName = conversationName;
        ConversationImage = conversationImage;
    }


    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSKey() {
        return SKey;
    }

    public void setSKey(String SKey) {
        this.SKey = SKey;
    }

    public String getRKey() {
        return RKey;
    }

    public void setRKey(String RKey) {
        this.RKey = RKey;
    }

    public String getMessageDigest() {
        return MessageDigest;
    }

    public void setMessageDigest(String messageDigest) {
        MessageDigest = messageDigest;
    }

    public String getContent() {
        return content;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public String getConversationName() {
        return ConversationName;
    }

    public String getConversationImage() {
        return ConversationImage;
    }


    public Date getCreatedAt() {
        return CreatedAt;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public void setSenderID(String sender) {
        this.senderID = sender;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }



    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setConversationName(String conversationName) {
        ConversationName = conversationName;
    }
    
    public void setConversationImage(String conversationImage) {
        ConversationImage = conversationImage;
    }
}
