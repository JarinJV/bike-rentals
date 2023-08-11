package com.example.rapidrentals.DataModel;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserDetails {

    private static final String USER_DETAILS_DB = "UserDetails";
    private static final String fileName = "photo.jpg";
    private static final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(USER_DETAILS_DB);

    public UserDetails() {
    }


    public void uploadPassportImage(Uri PassportUri,String id, UserDetailsDao userDetailsDao) {
        storageReference.child(id).child("Passport").child(fileName).putFile(PassportUri).addOnCompleteListener(task -> userDetailsDao.getBoolean(task.isSuccessful()));
    }
    public void uploadLicenceImage(Uri LicenceUri,String id, UserDetailsDao userDetailsDao) {
        storageReference.child(id).child("Licence").child(fileName).putFile(LicenceUri).addOnCompleteListener(task -> userDetailsDao.getBoolean(task.isSuccessful()));
    }
    public void uploadAadharImage(Uri AadharUri,String id, UserDetailsDao userDetailsDao) {
        storageReference.child(id).child("Aadhar").child(fileName).putFile(AadharUri).addOnCompleteListener(task -> userDetailsDao.getBoolean(task.isSuccessful()));
    }
    public static StorageReference getStorageReference() {
        return storageReference;
    }

    public static String getFileName() {
        return fileName;
    }


}