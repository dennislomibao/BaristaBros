package student.uts.edu.au.baristabrosapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //declare variables
    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;
    private Button changeAddress;
    private TextView tvBuyerName;
    private TextView tvEmail;
    private TextView tvAddressLine1;
    private TextView tvAddressLine2;
    private String addressLine1;

    private String addressLine2;
    private Button changeName;
    private Button email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tvBuyerName = findViewById(R.id.tvAccName);
        changeName = findViewById(R.id.btnChName);
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, NewName.class));
            }
        });

        email = findViewById(R.id.btnChEmail);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, Email.class));
            }
        });

        changeAddress = findViewById(R.id.btnChangeAddress);
        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, UpdateAddress.class));
            }
        });





        tvEmail = findViewById(R.id.tvAccEmail);
        tvAddressLine1 = findViewById(R.id.tvAccAddressLine1);
        tvAddressLine2 = findViewById(R.id.tvAccAddressLine2);

        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);

        //read user's name from database
        //change side menu name depending on user
        if (firebaseDatabase.child("users").child(user.getUid()).child("name") != null) {

            DatabaseReference DrUserName = firebaseDatabase.child("users").child(user.getUid());
            View v = LayoutInflater.from(this).inflate(R.layout.navbar_header_home_page, null);
            navView.addHeaderView(v);
            final TextView tvName = v.findViewById(R.id.nav_header_textView);


            DrUserName.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (dataSnapshot.child("name").getValue(String.class) == null) {
                        tvName.setText("Chris P. Bacon");
                    } else {
                        tvName.setText(dataSnapshot.child("name").getValue(String.class));
                        tvBuyerName.setText("Name: " + dataSnapshot.child("name").getValue(String.class));
                    }

                    //Show user's details on payment page
                    tvEmail.setText("Email: " + dataSnapshot.child("email").getValue(String.class));

                    setAddressLine1(dataSnapshot.child("address").getValue(String.class) + ",");
                    setAddressLine2(dataSnapshot.child("suburb").getValue(String.class) + " " +
                            dataSnapshot.child("state").getValue(String.class) + " " +
                            dataSnapshot.child("postcode").getValue(String.class) + ", " +
                            dataSnapshot.child("country").getValue(String.class));

                    tvAddressLine1.setText(getAddressLine1());
                    tvAddressLine2.setText(getAddressLine2());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    //Slide out menu options
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_search) {
            intent = new Intent(this, HomePageActivity.class);
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_recommend) {
            intent = new Intent();
            intent.putExtra("category", "Recommended");
            intent.setClass(this, CategoryActivity.class);
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_wishlist) {
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
            drawerLayout.closeDrawer(GravityCompat.START);
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

    private void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    private String getAddressLine1() {
        return addressLine1;
    }

    private void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    private String getAddressLine2() {
        return addressLine2;
    }

    //Phone back button closes menu rather than app
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            //back button goes to homepage
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }
    }
}
