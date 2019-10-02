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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm z");
    private final DecimalFormat df2 = new DecimalFormat("0.00");

    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;

    TextView tvBuyerName;
    private Button updateAddress;
    TextView tvEmail;
    TextView tvAddressLine1;
    TextView tvAddressLine2;
    TextView tvItemsList;
    CardForm cardForm;
    private double price;
    private TextView totalPrice;
    Button buy;

    String addressLine1;
    String addressLine2;

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
        drawerLayout = findViewById(R.id.drawer_layout);


        tvBuyerName = findViewById(R.id.tvBuyerName);
        updateAddress = findViewById(R.id.btnUpdate);
        updateAddress = findViewById(R.id.btnUpdate);
        updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PaymentActivity.this, UpdateAddress.class));
            }
        });
        tvEmail = findViewById(R.id.tvBuyerEmail);
        tvAddressLine1 = findViewById(R.id.tvAddressLine1);
        tvAddressLine2 = findViewById(R.id.tvAddressLine2);
        tvItemsList = findViewById(R.id.tvItemsList);
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
                                                imageUpload.setSellTime(ds.getValue(ImageUpload.class).getSellTime());

                                                String time = sdf.format(new Date());

                                                ViewItem viewItem =
                                                        new ViewItem(imageUpload.getTitle(),
                                                                imageUpload.getDesc(),
                                                                imageUpload.getImageUrl(),
                                                                imageUpload.getCategory(),
                                                                imageUpload.getPrice(),
                                                                imageUpload.getUploadId(),
                                                                imageUpload.getSellerId(),
                                                                imageUpload.getSellTime(),
                                                                user.getUid(),
                                                                time,
                                                                getAddressLine1(),
                                                                getAddressLine2());

                                                firebaseDatabase.child("category").child(imageUpload.getCategory()).child(imageUpload.getUploadId()).removeValue();
                                                firebaseDatabase.child("users").child(imageUpload.getSellerId()).child("Sell Current")
                                                        .child(imageUpload.getUploadId()).removeValue();

                                                firebaseDatabase.child("users").child(imageUpload.getSellerId()).child("Sell History")
                                                        .child(imageUpload.getUploadId()).child("buyerId").setValue(user.getUid());
                                                firebaseDatabase.child("users").child(imageUpload.getSellerId()).child("Sell History")
                                                        .child(imageUpload.getUploadId()).child("buyTime").setValue(time);
                                                firebaseDatabase.child("users").child(imageUpload.getSellerId()).child("Sell History")
                                                        .child(imageUpload.getUploadId()).child("buyerAddress1").setValue(getAddressLine1());
                                                firebaseDatabase.child("users").child(imageUpload.getSellerId()).child("Sell History")
                                                        .child(imageUpload.getUploadId()).child("buyerAddress2").setValue(getAddressLine2());

                                                firebaseDatabase.child("users").child(user.getUid()).child("Buy History")
                                                        .child(imageUpload.getUploadId()).setValue(viewItem);

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

                    if (cardForm.getCardNumber().isEmpty() || cardForm.getExpirationDateEditText().toString().isEmpty() || cardForm.getCvv().isEmpty()) {
                        Toast.makeText(PaymentActivity.this, "Input required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

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

                    int count = 0;
                    String items = "";

                    for (DataSnapshot ds : dataSnapshot.child("Cart").getChildren()) {

                        //new line for each item
                        if (count > 0) {
                            items += "\n\n";
                        }
                        items += ds.child("title").getValue(String.class) +
                                "\n$" + df2.format(ds.child("price").getValue(Double.class));
                        count++;
                    }

                    tvItemsList.setText(items);

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
            super.onBackPressed();
        }
    }
}
