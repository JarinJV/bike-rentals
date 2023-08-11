package com.example.rapidrentals.DataModel;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Car {

    // Database
    private static final String BIKE_DB = "Bikes";
    private static final String fileName = "Bike.jpg";
    private static final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(BIKE_DB);
    private static final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(BIKE_DB);

    // Cars Fields
    private String id;
    private String owner;
    private String brand;
    private String model;
    private String type;
    private String fuel;
    private int year;

    private int mileage;
    private String regNumber;
    private String LastServiceDate;
    private int totalKmsDriven;

    //Rental Fields
    private int rentPerDay;
    private boolean carAvailable;
    private String garage;

    // Constructors
    public Car() {
    }

    public Car(String id, String owner, String brand, String model, int mileage, String type, String fuel, String LastServiceDate, int totalKmsDriven, int year, String regNumber, int rentPerDay, boolean carAvailable, boolean driverAvailable, String garage) {
        this.id = id;
        this.owner = owner;
        this.brand = brand;
        this.model = model;
        this.mileage = mileage;
        this.type = type;
        this.fuel = fuel;
        this.year = year;
        this.regNumber = regNumber;
        this.LastServiceDate = LastServiceDate;
        this.totalKmsDriven = totalKmsDriven;
        this.rentPerDay = rentPerDay;
        this.carAvailable = carAvailable;
        this.garage = garage;
    }

    public static String generateCarId() {
        return databaseReference.push().getKey();
    }

    // Main Functions
    public static void getCarsByOwner(CarDao carDao) {
        databaseReference.addListenerForSingleValueEvent(getCarListListener(carDao));
    }

    public static void getCarsByType(String type, CarDao carDao) {
        databaseReference.orderByChild("type").equalTo(type).addListenerForSingleValueEvent(getCarListListener(carDao));
    }

    public static void getAvailableCars(CarDao carDao) {
        databaseReference.orderByChild("carAvailable").equalTo(true).addListenerForSingleValueEvent(getCarListListener(carDao));
    }

    public static void getCarById(String carId, CarDao carDao) {
        Car car = new Car();
        car.setId(carId);
        car.getCarById(carDao);
    }

    public void getCarById(CarDao carDao) {
        databaseReference.child(id).addListenerForSingleValueEvent(getCarListener(carDao));
    }

    public void addCar(CarDao carDao) {
        databaseReference.child(id).setValue(this).addOnCompleteListener(getBooleanListener(carDao));
    }

    public void deleteCar(CarDao carDao) {
        databaseReference.child(id).removeValue().addOnCompleteListener(getBooleanListener(carDao));
    }

    public void uploadCarImage(Uri carImageUri, CarDao carDao) {
        storageReference.child(id).child(fileName).putFile(carImageUri).addOnCompleteListener(task -> carDao.getBoolean(task.isSuccessful()));
    }

//    public StorageReference getCarImageReference() {
//        return storageReference.child(id).child(fileName);
//    }

    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", mileage=" + mileage +
                ", type='" + type + '\'' +
                ", fuel='" + fuel + '\'' +
                ", year=" + year +
                ", regNumber='" + regNumber + '\'' +
                ", lastServiceDate='"+LastServiceDate+ '\'' +
                ", totalKmsDriven="+ totalKmsDriven +
                ", rentPerDay=" + rentPerDay +
                ", carAvailable=" + carAvailable +
                ", garage=" + garage +
                '}';
    }

    // Listeners
    private static OnCompleteListener<Void> getBooleanListener(CarDao carDao) {
        return new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                carDao.getBoolean(task.isSuccessful());
            }
        };
    }

    private static ValueEventListener getCarListListener(CarDao carDao) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Car> carList = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                    carList.add(snapshot1.getValue(Car.class));
                carDao.getCarList(carList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                carDao.getCarList(null);
            }
        };
    }

    private static ValueEventListener getCarListener(CarDao carDao) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carDao.getCar(snapshot.getValue(Car.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                carDao.getCar(null);
            }
        };
    }

    // Getter and Setter
    public String getBrandModel() {
        return String.format("%s, %s", brand, model);
    }

    public String getYearType() {
        return String.format("%s • %s", year, type);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public int getRentPerDay() {
        return rentPerDay;
    }

    public void setRentPerDay(int rentPerDay) {
        this.rentPerDay = rentPerDay;
    }

    public boolean isCarAvailable() {
        return carAvailable;
    }

    public void setCarAvailable(boolean carAvailable) {
        this.carAvailable = carAvailable;
    }

    public int getTotalKmsDriven() {
        return totalKmsDriven;
    }

    public void setTotalKmsDriven(int totalKmsDriven) {
        this.totalKmsDriven = totalKmsDriven;
    }

    public String getLastServiceDate() {
        return LastServiceDate;
    }

    public void setLastServiceDate(String LastServiceDate) {
        this.LastServiceDate = LastServiceDate;
    }
    public String getGarage() {
        return garage;
    }
    public void setGarage(String garage) {
        this.garage = garage;
    }

    public static StorageReference getStorageReference() {
        return storageReference;
    }

    public static String getFileName() {
        return fileName;
    }

}
