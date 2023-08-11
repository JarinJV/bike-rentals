package com.example.rapidrentals.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rapidrentals.DataModel.BikeDetails;
import com.example.rapidrentals.DataModel.UserDetails;
import com.example.rapidrentals.Helper.GlideApp;
import com.example.rapidrentals.R;
import com.example.rapidrentals.Utility.ProcessManager;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BikePhotos extends AppCompatActivity {

    public static final String BOOKING_ID = "BOOKING_ID";
    private String bookingId;
    private ImageView im1,im2,im3,im4,imageView;

    private Dialog myDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_photos);

        initComponents();
    }

    private void initComponents() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bookingId = extras.getString(BOOKING_ID);
        }
        im1 = findViewById(R.id.pho1);
        im2 = findViewById(R.id.pho2);
        im3 = findViewById(R.id.pho3);
        im4 = findViewById(R.id.pho4);

        StorageReference ref = BikeDetails.getStorageReference();
        retrieveBikeInformation(im1,ref.child(bookingId).child(BikeDetails.getFile1()));
        retrieveBikeInformation(im2,ref.child(bookingId).child(BikeDetails.getFile2()));
        retrieveBikeInformation(im3,ref.child(bookingId).child(BikeDetails.getFile3()));
        retrieveBikeInformation(im4,ref.child(bookingId).child(BikeDetails.getFile4()));

        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.picture_popup_layout);
        imageView = myDialog.findViewById(R.id.image);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Drawable drawable = im1.getDrawable();
                imageView.setImageDrawable(drawable);
                myDialog.show();
            }
        });
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Drawable drawable = im2.getDrawable();
                imageView.setImageDrawable(drawable);
                myDialog.show();
            }
        });
        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Drawable drawable = im3.getDrawable();
                imageView.setImageDrawable(drawable);
                myDialog.show();
            }
        });
        im4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Drawable drawable = im4.getDrawable();
                imageView.setImageDrawable(drawable);
                myDialog.show();
            }
        });
    }

    private void retrieveBikeInformation(ImageView v, StorageReference reference) {
        GlideApp.with(getApplicationContext())
                .load(reference)
                .centerCrop()
                .into(v);
    }


    public void onClickBack(View view) {
        onBackPressed();
    }
}