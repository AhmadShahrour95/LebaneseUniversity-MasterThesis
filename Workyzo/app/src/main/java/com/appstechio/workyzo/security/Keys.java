package com.appstechio.workyzo.security;




    public class Keys {


        // variable for our id.
        private String id;

        // below line is a variable
        // for course name.
        private String User_UID;
        private String KeyValue;




        // below line we are creating constructor class.
        // inside constructor class we are not passing
        // our id because it is incrementing automatically

        public Keys(String User_UID, String keyValue) {
            this.User_UID = User_UID;
            KeyValue = keyValue;

        }

        public String getUser_UID() {
            return User_UID;
        }

        public void setUser_UID(String user_UID) {
            User_UID = user_UID;
        }

// on below line we are creating
        // getter and setter methods.

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKeyValue() {
            return KeyValue;
        }

        public void setKeyValue(String keyValue) {
            KeyValue = keyValue;
        }


    }

