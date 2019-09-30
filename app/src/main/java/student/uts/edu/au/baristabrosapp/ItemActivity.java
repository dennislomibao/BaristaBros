package student.uts.edu.au.baristabrosapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ItemActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    private Button btnWishlist;
    private Button btnCart;

    private String imageUrl;
    private String title;
    private Double price;
    private String description;
    private String category;
    private String uploadId;
    private String sellerId;
    private String sellTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("picture");
        title = intent.getStringExtra("title");
        price = intent.getDoubleExtra("price", 0.00);
        category = intent.getStringExtra("category");
        description = intent.getStringExtra("description");
        uploadId = intent.getStringExtra("uploadId");
        sellerId = intent.getStringExtra("sellerId");
        sellTime = intent.getStringExtra("sellTime");

        imageView = findViewById(R.id.imageView);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewCategory = findViewById(R.id.textViewCategory);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewSeller = findViewById(R.id.textViewSeller);
        textViewSellTime = findViewById(R.id.textViewSellTime);
        btnWishlist = findViewById(R.id.btnWishlist);
        btnCart = findViewById(R.id.btnCart);

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


        DatabaseReference DrWishlist = firebaseDatabase.child("users").child(user.getUid()).child("Wishlist");
        DrWishlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check if item is already in wishlist
                if (dataSnapshot.hasChild(uploadId)) {

                    btnWishlist.setText("Remove from Wishlist");

                } else {

                    DatabaseReference DrCheckUserPosts = firebaseDatabase.child("category").child(category).child(uploadId).child("sellerId");
                    DrCheckUserPosts.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //user can't buy their own items
                            if (dataSnapshot.getValue(String.class).equals(user.getUid())) {

                                btnWishlist.setText("Remove Item");
                                btnCart.setVisibility(View.GONE);

                            } else {

                                btnWishlist.setText("Add to Wishlist");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //add item to wishlist
        btnWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnWishlist.getText() == "Add to Wishlist") {

                    ImageUpload upload = new ImageUpload(
                            title, description, imageUrl, category, price, uploadId, sellerId, sellTime
                    );

                    firebaseDatabase.child("users").child(user.getUid()).child("Wishlist").child(uploadId).setValue(upload);

                    Toast.makeText(ItemActivity.this, "Item Added to Wishlist", Toast.LENGTH_SHORT).show();
                    btnWishlist.setText("Remove from Wishlist");

                } else if (btnWishlist.getText() == "Remove from Wishlist") {

                    firebaseDatabase.child("users").child(user.getUid()).child("Wishlist").child(uploadId).removeValue();
                    Toast.makeText(ItemActivity.this, "Item Removed from Wishlist", Toast.LENGTH_SHORT).show();
                    btnWishlist.setText("Add to Wishlist");

                } else {

                    //user remove their own item from store
                    Toast.makeText(ItemActivity.this, "Item Removed from Store", Toast.LENGTH_SHORT).show();
                    firebaseDatabase.child("category").child(category).child(uploadId).removeValue();
                    firebaseDatabase.child("users").child(user.getUid()).child("Sell Current").child(uploadId).removeValue();
                    firebaseDatabase.child("users").child(user.getUid()).child("Sell History").child(uploadId).removeValue();

                    Intent intent = new Intent(ItemActivity.this, HomePageActivity.class);
                    finish();
                    startActivity(intent);

                }
            }
        });

        DatabaseReference DrCart = firebaseDatabase.child("users").child(user.getUid()).child("Cart");
        DrCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check if item is already in cart
                if (dataSnapshot.hasChild(uploadId)) {

                    btnCart.setText("Remove from Cart");

                    //if item is not in cart already
                } else {

                    btnCart.setText("Add to Cart");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //add item to cart
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnCart.getText() == "Add to Cart") {

                    ImageUpload upload = new ImageUpload(
                            title, description, imageUrl, category, price, uploadId, sellerId, sellTime
                    );

                    firebaseDatabase.child("users").child(user.getUid()).child("Cart").child(uploadId).setValue(upload);

                    Toast.makeText(ItemActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();
                    btnCart.setText("Remove from Cart");

                } else {

                    firebaseDatabase.child("users").child(user.getUid()).child("Cart").child(uploadId).removeValue();
                    Toast.makeText(ItemActivity.this, "Item Removed from Cart", Toast.LENGTH_SHORT).show();
                    btnCart.setText("Add to Cart");

                }

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
