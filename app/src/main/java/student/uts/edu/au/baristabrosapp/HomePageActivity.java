package student.uts.edu.au.baristabrosapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //declare variables
    private DrawerLayout drawerLayout;
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
    private Button searchBtn;
    private Button payment;
    private TextView tvGreeting;
    private EditText etSearch;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        searchBtn = findViewById(R.id.btnSearch);
        drawerLayout = findViewById(R.id.drawer_layout);
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
        tvGreeting = findViewById(R.id.textViewGreeting);
        etSearch = findViewById(R.id.etSearch);


        Intent intent = new Intent();


        //category button functions
        catComputersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Computers","");

            }
        });
        catLaptopsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Laptops","");

            }
        });
        catCpuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("CPU","");

            }
        });
        catFansBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Fans and Coolers","");

            }
        });
        catMbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Motherboards","");

            }
        });
        catMemoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Memory","");

            }
        });
        catStorageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Storage","");

            }
        });
        catVcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Video Cards","");

            }
        });
        catCaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Case","");

            }
        });
        catPsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Power Supply","");

            }
        });
        catMonitorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Monitors","");

            }
        });
        catPeriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Peripherals","");

            }
        });
        catOsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Operating Systems","");

            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCategoryActivity("Search", etSearch.getText().toString());

            }
        });


        //read user's name from database
        //change side menu name depending on user
        if (firebaseDatabase.child("users").child(user.getUid()).child("name") != null) {

            DatabaseReference DrUserName = firebaseDatabase.child("users").child(user.getUid()).child("name");
            View v = LayoutInflater.from(this).inflate(R.layout.navbar_header_home_page, null);
            navView.addHeaderView(v);
            final TextView tvName = v.findViewById(R.id.nav_header_textView);


            DrUserName.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (dataSnapshot.getValue(String.class) == null) {
                        tvName.setText("Chris P. Bacon");
                    } else {
                        tvName.setText(dataSnapshot.getValue(String.class));
                        tvGreeting.setText("Good Afternoon " + dataSnapshot.getValue(String.class) + "!");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void startCategoryActivity(String category, String search) {

        Intent intent = new Intent();
        intent.putExtra("category", category);
        intent.putExtra("search", search);
        intent.setClass(HomePageActivity.this, CategoryActivity.class);

        startActivity(intent);
    }

    //Slide out menu options
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_search) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_recommend) {
            intent = new Intent();
            intent.putExtra("category", "Recommended");
            intent.setClass(this, CategoryActivity.class);
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(intent);
            return true;
        }else if (id == R.id.nav_wishlist) {
            intent = new Intent(this, WishlistActivity.class);
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_cart) {
            intent = new Intent(this, CartActivity.class);
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_selling) {
            intent = new Intent(this, SellActivity.class);
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_buy_history) {
            intent = new Intent(this, BuyHistoryActivity.class);
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_sell_history) {
            intent = new Intent(this, SellHistoryActivity.class);
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_settings) {
            intent = new Intent(this, SettingsActivity.class);
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_sign_out) {
            firebaseAuth.signOut();
            intent = new Intent(this, LoginActivity.class);
            drawerLayout.closeDrawer(GravityCompat.START);
            finish();
            startActivity(intent);
            return true;
        }

        return false;
    }

    //Phone back button closes menu rather than app
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //Back button goes to home screen instead of previous activity
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
