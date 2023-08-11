package com.example.rapidrentals.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidrentals.DataModel.Booking;
import com.example.rapidrentals.DataModel.Car;
import com.example.rapidrentals.DataModel.CarDao;
import com.example.rapidrentals.DataModel.Location;
import com.example.rapidrentals.DataModel.User;
import com.example.rapidrentals.DataModel.UserDao;
import com.example.rapidrentals.Helper.GlideApp;
import com.example.rapidrentals.R;
import com.example.rapidrentals.Utility.ProcessManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class CarDetailActivity extends AppCompatActivity {

    public static final String CAR_ID = "CAR_ID";

    private String carId;

    private ProcessManager processManager;

    private ImageView image, backBtn;
    private TextInputLayout pickup,drop;
    private AutoCompleteTextView pickup_atv, drop_atv;
    private TextView brand_model, year_type, rent, fuel, transmission, year, location, reg, last, contact;
    private Button boonBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        initComponents();

    }

    private void initComponents() {
        // Hooks
        image = findViewById(R.id.car_image);
        brand_model = findViewById(R.id.car_brand_model);
        year_type = findViewById(R.id.car_year_type);
        rent = findViewById(R.id.car_rent);
        fuel = findViewById(R.id.car_fuel);
        transmission = findViewById(R.id.car_transmission);
        year = findViewById(R.id.year);
        location = findViewById(R.id.car_location);
        reg = findViewById(R.id.car_reg);
        last = findViewById(R.id.last);
        contact = findViewById(R.id.car_contact);
        backBtn = findViewById(R.id.backButton);
        pickup = findViewById(R.id.pickup);
        drop = findViewById(R.id.drop);
        pickup_atv = findViewById(R.id.pickup_atv);
        drop_atv = findViewById(R.id.drop_atv);
        drop_atv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.bike_location)));
        pickup_atv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.bike_location)));


        processManager = new ProcessManager(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            carId = extras.getString(CAR_ID);
        } else {
            Toast.makeText(getApplicationContext(), "Bike Id not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        retrieveCarInformation();

    }

    private void retrieveCarInformation() {
        processManager.incrementProcessCount();
        Car.getCarById(carId, new CarDao() {
            @Override
            public void getCar(Car car) {
                if(car != null) {
                    StorageReference reference = Car.getStorageReference().child(car.getId()).child(Car.getFileName());
                    GlideApp.with(getApplicationContext())
                            .load(reference)
                            .placeholder(R.drawable.preview)
                            .error(R.drawable.preview)
                            .centerCrop()
                            .into(image);
                    brand_model.setText(String.format("%s %s", car.getBrand(), car.getModel()));
                    year_type.setText(String.format("%d • %s", car.getYear(), car.getType()));
                    rent.setText(String.format("Rs.%d", car.getRentPerDay()));
                    fuel.setText(car.getFuel());
                    year.setText(String.format("%d", car.getYear()));
                    reg.setText(car.getRegNumber());
                    location.setText(car.getGarage());
                    transmission.setText(String.format("%d kms/l", car.getMileage()));
                    processManager.incrementProcessCount();
                    last.setText(car.getLastServiceDate());
                    contact.setText("9360276325");
                    processManager.decrementProcessCount();
                } else {
                    Toast.makeText(getApplicationContext(), "Bike not found!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                processManager.decrementProcessCount();
            }
        });
    }

    public void onClickBook(View view) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Garage");
        String p = pickup_atv.getText().toString().trim();
        String d = drop_atv.getText().toString().trim();
        Location loc = new Location(p,d,carId);
        ref.child(carId).setValue(loc);

        Intent intent = new Intent(getApplicationContext(), CarBookActivity.class);
        Bundle extras = new Bundle();
        extras.putString(CarBookActivity.CAR_ID,carId);
        extras.putString(CarBookActivity.BOOK_OPERATION, CarBookActivity.BOOK_ADD);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void onClickBack(View view) {
        onBackPressed();
    }
}