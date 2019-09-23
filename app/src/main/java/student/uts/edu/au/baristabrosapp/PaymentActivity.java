package student.uts.edu.au.baristabrosapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class PaymentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;
    CardForm cardForm;
    private double price;
    private TextView totalPrice;
    Button buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        price = intent.getDoubleExtra("price", 0.00);
        setContentView(R.layout.activity_payment);

        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        cardForm = findViewById(R.id.credit_card);
        totalPrice = findViewById(R.id.tvTotal);
        totalPrice.setText(String.format(Locale.getDefault(), "Total: $%.2f", price));
        buy = findViewById(R.id.pay_now);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .setup(PaymentActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardForm.isValid()) {
                    new AlertDialog.Builder(PaymentActivity.this)
                            .setTitle("Confirm")
                            .setMessage(String.format(Locale.getDefault(), "Purchase $%.2f?", price))
                            .setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DatabaseReference cartItems = firebaseDatabase.child("users").child(user.getUid()).child("Cart");

                                    cartItems.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                ImageUpload imageUpload = new ImageUpload();
                                                imageUpload.setCategory(ds.getValue(ImageUpload.class).getCategory());
                                                imageUpload.setDesc(ds.getValue(ImageUpload.class).getDesc());
                                                imageUpload.setImageUrl(ds.getValue(ImageUpload.class).getImageUrl());
                                                imageUpload.setPrice(ds.getValue(ImageUpload.class).getPrice());
                                                imageUpload.setSellerId(ds.getValue(ImageUpload.class).getSellerId());
                                                imageUpload.setTitle(ds.getValue(ImageUpload.class).getTitle());
                                                imageUpload.setUploadId(ds.getValue(ImageUpload.class).getUploadId());

                                                firebaseDatabase.child("category").child(imageUpload.getCategory()).child(imageUpload.getUploadId()).removeValue();
                                                firebaseDatabase.child("users").child(imageUpload.getSellerId()).child("Sell Current")
                                                        .child(imageUpload.getUploadId()).removeValue();

                                                firebaseDatabase.child("users").child(imageUpload.getSellerId()).child("Sell History")
                                                        .child(imageUpload.getUploadId()).child("buyerId").setValue(user.getUid());

                                                firebaseDatabase.child("users").child(user.getUid()).child("Buy History")
                                                        .child(imageUpload.getUploadId()).setValue(imageUpload);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    // Remove cart
                                    firebaseDatabase.child("users").child(user.getUid()).child("Cart").removeValue();

                                    Toast.makeText(PaymentActivity.this, "Thank You for Your Purchase", Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(new Intent(PaymentActivity.this, HomePageActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                } else {
                    //TODO add error message
                    Toast.makeText(PaymentActivity.this, "Please fill all sections", Toast.LENGTH_LONG).show();

                    if (cardForm.getCardNumber().isEmpty() || cardForm.getExpirationDateEditText().toString().isEmpty() || cardForm.getCvv().isEmpty()){
                        Toast.makeText(PaymentActivity.this,"Input required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //read user's name from database
        //change side menu name depending on user
        if (firebaseDatabase.child("users").child(user.getUid()).child("name") != null) {

            DatabaseReference DrUserName = firebaseDatabase.child("users").child(user.getUid()).child("name");
            View v = LayoutInflater.from(this).inflate(R.layout.navbar_header_home_page,null);
            navView.addHeaderView(v);
            final TextView tvName = (TextView) v.findViewById(R.id.nav_header_textView);


            DrUserName.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (dataSnapshot.getValue(String.class) == null) {
                        tvName.setText("Chris P. Bacon");
                    } else {
                        tvName.setText(dataSnapshot.getValue(String.class));
                    }

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
            super.onBackPressed();
        }
    }

}
