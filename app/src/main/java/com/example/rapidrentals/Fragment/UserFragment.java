package com.example.rapidrentals.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.rapidrentals.Activity.LoginActivity;
import com.example.rapidrentals.DataModel.User;
import com.example.rapidrentals.DataModel.UserDao;
import com.example.rapidrentals.DataModel.UserDetails;
import com.example.rapidrentals.DataModel.UserDetailsDao;
import com.example.rapidrentals.Helper.GlideApp;
import com.example.rapidrentals.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;


public class UserFragment extends Fragment {

    private TextView fullName, email, phone, text,db;
    private Button logoutBtn;

    private CardView passport,licence,dob,aadhar;
    private Dialog myDialog;
    private ImageView PassView, AadhView, LicView;
    private Uri uri;
    private int u;
    private final int PICK_GALLERY = 101;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private FirebaseUser currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        initComponents(root);

        return root;
    }

    private void initComponents(View root) {
        fullName = root.findViewById(R.id.user_full_name);
        email = root.findViewById(R.id.user_email);
        phone = root.findViewById(R.id.user_phone);
        logoutBtn = root.findViewById(R.id.user_logout_btn);
        passport = root.findViewById(R.id.passport);
        licence = root.findViewById(R.id.licence);
        dob = root.findViewById(R.id.dob);
        db = root.findViewById(R.id.user_dob);
        aadhar = root.findViewById(R.id.aadhar);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogoutClick(view);
            }
        });

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getActivity().getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
            getActivity().finish();
        }


        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.image_popup_layout);
        PassView = myDialog.findViewById(R.id.passView);
        LicView = myDialog.findViewById(R.id.licView);
        AadhView = myDialog.findViewById(R.id.aadhView);
        text = myDialog.findViewById(R.id.popupText);
        StorageReference ref = UserDetails.getStorageReference().child(currentUser.getUid());


        passport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference reff = ref.child("Passport");
                retrieveInfo(PassView,ref.child("Passport").child(UserDetails.getFileName()));
                PassView.setVisibility(View.VISIBLE);
                LicView.setVisibility(View.GONE);
                AadhView.setVisibility(View.GONE);
                myDialog.show();
                PassView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        u = 1;
                        pickGalleryImage(v);
                    }
                });
                text.setText("Passport");
            }
        });
        licence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference reff = ref.child("Licence");
                retrieveInfo(LicView,ref.child("Licence").child(UserDetails.getFileName()));
                PassView.setVisibility(View.GONE);
                LicView.setVisibility(View.VISIBLE);
                AadhView.setVisibility(View.GONE);
                myDialog.show();
                LicView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        u = 2;
                        pickGalleryImage(v);
                    }
                });
                text.setText("Licence");
            }
        });

        aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveInfo(AadhView,ref.child("Aadhar").child(UserDetails.getFileName()));
                PassView.setVisibility(View.GONE);
                LicView.setVisibility(View.GONE);
                AadhView.setVisibility(View.VISIBLE);
                myDialog.show();
                AadhView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        u = 3;
                        pickGalleryImage(v);
                    }
                });
                text.setText("Aadhar");
            }
        });


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                Toast.makeText(getActivity(),"DOB: "+date,Toast.LENGTH_SHORT).show();
                Task<Void> r = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.
                        getUid()).child("birthDate").setValue(date);
            }
        };

        retrieveUserInfo();
    }

    private void retrieveInfo(ImageView v, StorageReference reference) {
        GlideApp.with(this)
                .load(reference)
                .centerCrop()
                .into(v);
    }

    public void pickGalleryImage(View view) {
        ImagePicker.with(this)
                .galleryOnly()
                .crop(16f, 10f)
                .compress(1024)
                .start(PICK_GALLERY);
    }


    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_GALLERY && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            UserDetails userDetails = new UserDetails();
            if (u == 1){
                PassView.setImageURI(uri);
                if (uri != null) {
                    userDetails.uploadPassportImage(uri,currentUser.getUid(), new UserDetailsDao() {
                        @Override
                        public void getBoolean(Boolean result) {
                            if (result) {
                                Toast.makeText(getActivity(), "Image Updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }else if (u == 2){
                LicView.setImageURI(uri);
                if (uri != null) {
                    userDetails.uploadLicenceImage(uri, currentUser.getUid(), new UserDetailsDao() {
                        @Override
                        public void getBoolean(Boolean result) {
                            if (result) {
                                Toast.makeText(getActivity(), "Image Updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            } else if (u==3) {
                AadhView.setImageURI(uri);
                if (uri != null) {
                    userDetails.uploadAadharImage(uri, currentUser.getUid(), new UserDetailsDao() {
                        @Override
                        public void getBoolean(Boolean result) {
                            if (result) {
                                Toast.makeText(getActivity(), "Image Updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            u = 0;
        }

    }

    public void onLogoutClick(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getActivity().getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
        getActivity().startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
    }

    private void retrieveUserInfo() {
        User.getUserById(currentUser.getUid(), new UserDao() {
            @Override
            public void getUser(User user) {
                fullName.setText(user.getFullName());
                email.setText(user.getEmailId());
                phone.setText(user.getPhoneNumber());
            }
        });
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.
                getUid()).child("birthDate");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String d = dataSnapshot.getValue().toString();
                db.setText(d);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}