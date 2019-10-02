package student.uts.edu.au.baristabrosapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminCategoryActivity extends AppCompatActivity {

    //declare variables
    private Button catComputersBtn;
    private Button catLaptopsBtn;
    private Button catCpuBtn;
    private Button catFansBtn;
    private Button catMbBtn;
    private Button catMemoryBtn;
    private Button catStorageBtn;
    private Button catVcBtn;
    private Button catCaseBtn;
    private Button catPsBtn;
    private Button catMonitorsBtn;
    private Button catPeriBtn;
    private Button catOsBtn;
    private Button payment;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        catComputersBtn = findViewById(R.id.btnCatComputers);
        catLaptopsBtn = findViewById(R.id.btnCatLaptops);
        catCpuBtn = findViewById(R.id.btnCatCpu);
        catFansBtn = findViewById(R.id.btnCatFans);
        catMbBtn = findViewById(R.id.btnCatMb);
        catMemoryBtn = findViewById(R.id.btnCatMemory);
        catStorageBtn = findViewById(R.id.btnCatStorage);
        catVcBtn = findViewById(R.id.btnCatVc);
        catCaseBtn = findViewById(R.id.btnCatCase);
        catPsBtn = findViewById(R.id.btnCatPs);
        catMonitorsBtn = findViewById(R.id.btnCatMonitors);
        catPeriBtn = findViewById(R.id.btnCatPeri);
        catOsBtn = findViewById(R.id.btnCatOs);


        Intent intent = new Intent();
        //category button functions
        catComputersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Computers");

            }
        });
        catLaptopsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Laptops");

            }
        });
        catCpuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("CPU");

            }
        });
        catFansBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Fans and Coolers");

            }
        });
        catMbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Motherboards");

            }
        });
        catMemoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Memory");

            }
        });
        catStorageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Storage");

            }
        });
        catVcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Video Cards");

            }
        });
        catCaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Case");

            }
        });
        catPsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Power Supply");

            }
        });
        catMonitorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Monitors");

            }
        });
        catPeriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Peripherals");

            }
        });
        catOsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Operating Systems");

            }
        });
    }

    private void startCategoryActivity(String category) {

        Intent intent = new Intent();
        intent.putExtra("category", category);
        intent.setClass(AdminCategoryActivity.this, AdminViewActivity.class);
        startActivity(intent);

    }
}
