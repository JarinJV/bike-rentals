package com.example.rapidrentals.DataModel;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BikeDetails {

    private static final String BIKE_DETAILS_DB = "BikeDetails";
    private static final String file1 = "photo1.jpg";
    private static final String file2 = "photo2.jpg";
    private static final String file3 = "photo3.jpg";
    private static final String file4 = "photo4.jpg";

    private static final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(BIKE_DETAILS_DB);
    public BikeDetails() {
    }


    public void uploadImg1(Uri pic1,String id, BikeDetailsDao bikeDetailsDao) {
        storageReference.child(id).child(file1).putFile(pic1).addOnCompleteListener(task -> bikeDetailsDao.getBoolean(task.isSuccessful()));
    }
    public void uploadImg2(Uri pic2,String id, BikeDetailsDao bikeDetailsDao) {
        storageReference.child(id).child(file2).putFile(pic2).addOnCompleteListener(task -> bikeDetailsDao.getBoolean(task.isSuccessful()));
    }
    public void uploadImg3(Uri pic3,String id, BikeDetailsDao bikeDetailsDao) {
        storageReference.child(id).child(file3).putFile(pic3).addOnCompleteListener(task -> bikeDetailsDao.getBoolean(task.isSuccessful()));
    }
    public void uploadImg4(Uri pic4,String id, BikeDetailsDao bikeDetailsDao) {
        storageReference.child(id).child(file4).putFile(pic4).addOnCompleteListener(task -> bikeDetailsDao.getBoolean(task.isSuccessful()));
    }

    public static StorageReference getStorageReference() {
        return storageReference;
    }

    public static String getFile1() {
        return file1;
    }
    public static String getFile2() {
        return file2;
    }
    public static String getFile3() {
        return file3;
    }
    public static String getFile4() {
        return file4;
    }


}