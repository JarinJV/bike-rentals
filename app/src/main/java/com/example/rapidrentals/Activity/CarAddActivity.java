package com.example.rapidrentals.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.rapidrentals.DataModel.Car;
import com.example.rapidrentals.DataModel.CarDao;
import com.example.rapidrentals.Helper.GlideApp;
import com.example.rapidrentals.R;
import com.example.rapidrentals.Utility.LoadingDialog;
import com.example.rapidrentals.Utility.ProcessManager;
import com.example.rapidrentals.Utility.Validation;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class CarAddActivity extends AppCompatActivity {

    public static final String CAR_OPERATION = "CAR_OPERATION";
    public static final String CAR_ADD = "CAR_ADD";
    public static final String CAR_UPDATE = "CAR_UPDATE";
    public static final String CAR_ID = "CAR_ID";

    private String carOperation;
    private String carId;

    private final int PICK_GALLERY = 101;
    private static final String TAG = "CarAddActivity";
    private TextInputLayout brand, model, type, fuel, year,mileage, reg, rent,kmsDriven,last_layout,garage;
    private AutoCompleteTextView typeAtv, fuelAtv, lastService,garageName;
    private SwitchMaterial carAvailableSwitch;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private AppCompatImageView carImageView;
    private Uri carImageUri;

    private ProcessManager processManager;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_add);

        initComponents();

    }

    private void initComponents() {

        processManager = new ProcessManager(this);

        /*
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
         */

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            carOperation = extras.getString(CAR_OPERATION);
            if (carOperation.equals(CAR_UPDATE)) {
                carId = extras.getString(CAR_ID);
                retrieveCarInformation();
            }
        } else {
            carOperation = "";
        }

        carImageView = findViewById(R.id.imgGallery);
        brand = findViewById(R.id.car_brand_layout);
        model = findViewById(R.id.car_model_layout);
        type = findViewById(R.id.car_type_layout);
        typeAtv = findViewById(R.id.car_type_atv);
        fuel = findViewById(R.id.car_fuel_layout);
        fuelAtv = findViewById(R.id.car_fuel_atv);
        year = findViewById(R.id.car_year_layout);
        reg = findViewById(R.id.car_reg_layout);
        rent = findViewById(R.id.car_rent_layout);
        carAvailableSwitch = findViewById(R.id.car_available_switch);
        kmsDriven = findViewById(R.id.kms_layout);
        lastService = findViewById(R.id.last);
        last_layout = findViewById(R.id.lastLayout);
        garage = findViewById(R.id.garage);
        garageName = findViewById(R.id.garageName);
        mileage = findViewById(R.id.bike_mileage);

        typeAtv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.bike_type)));
        fuelAtv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.bike_fuel)));
        garageName.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.bike_location)));
        lastService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CarAddActivity.this,
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
                lastService.setText(date);
            }
        };
    }

    private void retrieveCarInformation() {
        processManager.incrementProcessCount();
        Car.getCarById(carId, new CarDao() {
            @Override
            public void getCar(Car car) {
                if (car != null) {
                    brand.getEditText().setText(car.getBrand());
                    model.getEditText().setText(car.getModel());
                    mileage.getEditText().setText(String.valueOf(car.getMileage()));
                    typeAtv.setText(car.getType());
                    fuelAtv.setText(car.getFuel());
                    year.getEditText().setText(String.valueOf(car.getYear()));
                    reg.getEditText().setText(car.getRegNumber());
                    rent.getEditText().setText(String.valueOf(car.getRentPerDay()));
                    carAvailableSwitch.setChecked(car.isCarAvailable());
                    kmsDriven.getEditText().setText(String.valueOf(car.getTotalKmsDriven()));
                    lastService.setText(car.getLastServiceDate());
                    garageName.setText(car.getGarage());
                    StorageReference reference = Car.getStorageReference().child(car.getId()).child(Car.getFileName());

                    GlideApp.with(getApplicationContext())
                            .load(reference)
                            .centerCrop()
                            .into(carImageView);

                }
                processManager.decrementProcessCount();
            }
        });
    }

    public void pickGalleryImage(View view) {
        ImagePicker.with(this)
                .galleryOnly()
                .crop(16f, 9f)
                .compress(1024)
                .start(PICK_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_GALLERY && resultCode == RESULT_OK && data != null) {
            carImageUri = data.getData();
            carImageView.setImageURI(carImageUri);
        }

    }

    public void onClickAddCar(View view) {
        processManager.incrementProcessCount();

        // Validation
        if (!validateCarDetails()) {
            Toast.makeText(getApplicationContext(), "Validation Failed", Toast.LENGTH_LONG).show();
            processManager.decrementProcessCount();
            return;
        }

        //Initialize Object
        Car car = new Car();
        car.setId(carOperation.equals(CAR_UPDATE) ? carId : Car.generateCarId());
        car.setBrand(brand.getEditText().getText().toString().trim());
        car.setModel(model.getEditText().getText().toString().trim());
        car.setMileage(Integer.parseInt(mileage.getEditText().getText().toString().trim()));
        car.setFuel(fuelAtv.getText().toString().trim());
        car.setType(typeAtv.getText().toString().trim());
        car.setRegNumber(reg.getEditText().getText().toString().trim());
        car.setLastServiceDate(lastService.getText().toString().trim());
        car.setTotalKmsDriven(Integer.parseInt(kmsDriven.getEditText().getText().toString().trim()));
        car.setYear(Integer.parseInt(year.getEditText().getText().toString().trim()));
        car.setRentPerDay(Integer.parseInt(rent.getEditText().getText().toString().trim()));
        car.setCarAvailable(carAvailableSwitch.isChecked());
        car.setGarage(garageName.getText().toString().trim());
        // Add to Firebase
        car.addCar(new CarDao() {
            @Override
            public void getBoolean(Boolean result) {
                if (result) {
                    Toast.makeText(getApplicationContext(), "Bike Updated", Toast.LENGTH_SHORT).show();
                    if (carImageUri != null) {
                        processManager.incrementProcessCount();
                        car.uploadCarImage(carImageUri, new CarDao() {
                            @Override
                            public void getBoolean(Boolean result) {
                                if (result) {
                                    Toast.makeText(getApplicationContext(), "Image Updated", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                                }
                                processManager.decrementProcessCount();
                            }
                        });
                    } else {
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                }
                processManager.decrementProcessCount();
            }
        });
    }

    public void onClickCancel(View view) {
        onBackPressed();
    }

    private boolean validateCarDetails() {
        return Validation.validateEmpty(brand)
                & Validation.validateEmpty(model)
                & Validation.validateEmpty(mileage)
                & Validation.validateDropDown(type, typeAtv)
                & Validation.validateDropDown(fuel, fuelAtv)
                & Validation.validateEmpty(year)
                & Validation.validateEmpty(reg)
                & Validation.validateEmpty(kmsDriven)
                & Validation.validateEmpty(last_layout)
                & Validation.validateEmpty((rent))
                & Validation.validateDropDown(garage, garageName);
    }

}