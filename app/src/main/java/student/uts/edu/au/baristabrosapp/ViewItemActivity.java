package student.uts.edu.au.baristabrosapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ViewItemActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //declare variables
    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;

    private final DecimalFormat df2 = new DecimalFormat("0.00");

    private ImageView imageView;
    private TextView textViewTitle;
    private TextView textViewPrice;
    private TextView textViewCategory;
    private TextView textViewDescription;
    private TextView textViewSeller;
    private TextView textViewSellTime;
    private TextView textViewBuyer;
    private TextView textViewBuyTime;
    private TextView textViewAddress1;
    private TextView textViewAddress2;

    private String imageUrl;
    private String title;
    private Double price;
    private String description;
    private String category;
    private String uploadId;
    private String sellerId;
    private String sellTime;
    private String buyerId;
    private String audience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("picture");
        title = intent.getStringExtra("title");
        price = intent.getDoubleExtra("price", 0.00);
        category = intent.getStringExtra("category");
        description = intent.getStringExtra("description");
        uploadId = intent.getStringExtra("uploadId");
        sellerId = intent.getStringExtra("sellerId");
        sellTime = intent.getStringExtra("sellTime");
        audience = intent.getStringExtra("audience");

        imageView = findViewById(R.id.imageView);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewCategory = findViewById(R.id.textViewCategory);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewSeller = findViewById(R.id.textViewSeller);
        textViewSellTime = findViewById(R.id.textViewSellTime);
        textViewBuyer = findViewById(R.id.textViewBuyer);
        textViewBuyTime = findViewById(R.id.textViewBuyTime);
        textViewAddress1 = findViewById(R.id.tvAddressLine1);
        textViewAddress2 = findViewById(R.id.tvAddressLine2);

        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);

        //set objects
        Picasso.with(this).load(Uri.parse(imageUrl)).into(imageView);
        textViewTitle.setText(title);
        textViewPrice.setText("$" + df2.format(price));
        textViewCategory.setText(category);
        textViewDescription.setText(description);
        textViewSellTime.setText("Date Posted: " + sellTime);

        DatabaseReference DrSellerName = firebaseDatabase.child("users").child(sellerId).child("name");
        DrSellerName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                textViewSeller.setText("Seller: " + dataSnapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (audience.equals("buyer")) {
            DatabaseReference DrBuyerName = firebaseDatabase.child("users").child(user.getUid()).child("name");
            DrBuyerName.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    textViewBuyer.setText("Buyer: " + dataSnapshot.getValue(String.class));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {

            DatabaseReference dr = firebaseDatabase.child("users").child(user.getUid()).child("Sell History").child(uploadId).child("buyerId");
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    DatabaseReference buyerName = firebaseDatabase.child("users").child(dataSnapshot.getValue(String.class)).child("name");
                    buyerName.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            textViewBuyer.setText("Buyer: " + dataSnapshot.getValue(String.class));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if (audience.equals("buyer")) {

            DatabaseReference DrBuyTime = firebaseDatabase.child("users").child(user.getUid()).child("Buy History").child(uploadId);
            DrBuyTime.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    textViewBuyTime.setText("Date Purchased: " + dataSnapshot.child("buyTime").getValue(String.class));
                    textViewAddress1.setText(dataSnapshot.child("buyerAddress1").getValue(String.class));
                    textViewAddress2.setText(dataSnapshot.child("buyerAddress2").getValue(String.class));


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {

            DatabaseReference DrBuyTime = firebaseDatabase.child("users").child(user.getUid()).child("Sell History").child(uploadId);
            DrBuyTime.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    textViewBuyTime.setText("Date Purchased: " + dataSnapshot.child("buyTime").getValue(String.class));
                    textViewAddress1.setText(dataSnapshot.child("buyerAddress1").getValue(String.class));
                    textViewAddress2.setText(dataSnapshot.child("buyerAddress2").getValue(String.class));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

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
