package com.csc131.deltamedicalteam.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class User implements Parcelable {
    private String documentId;
    private String email;
    private String fName;
    private String lName;
    private String permission;
    private String phone;

    public User() {
        // Default constructor required for Firestore deserialization
    }

    public User(String documentId, String email, String fName, String lName, String permission, String phone) {
        this.documentId = documentId;
        this.email = email;
        this.fName = fName;
        this.lName = lName;
        this.permission = permission;
        this.phone = phone;
    }

    protected User(Parcel in) {
        documentId = in.readString();
        email = in.readString();
        fName = in.readString();
        lName = in.readString();
        permission = in.readString();
        phone = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getDocumentId() {
        return documentId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return fName + " " + lName;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    @NonNull
    @Override
    public String toString() {  return fName + " " + lName;}

    public String getPermission() {
        return permission;
    }

    public String getPhone() {
        return phone;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public static void getUserFromId(String userId, OnSuccessListener<User> onSuccessListener) {
        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the collection where users are stored
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Convert the document snapshot to a User object
                        User user = documentSnapshot.toObject(User.class);
                        // Invoke the success listener with the user object
                        onSuccessListener.onSuccess(user);
                    } else {
                        // Document with the given ID does not exist
                        onSuccessListener.onSuccess(null);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors by returning null
                    onSuccessListener.onSuccess(null);
                });
    }

    // Parcelable methods
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentId);
        dest.writeString(email);
        dest.writeString(fName);
        dest.writeString(lName);
        dest.writeString(permission);
        dest.writeString(phone);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
