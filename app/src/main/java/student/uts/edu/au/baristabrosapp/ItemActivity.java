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
    private Button btnWishlist;
    private Button btnCart;

    private String imageUrl;
    private String title;
    private Double price;
    private String description;
    private String category;
    private String uploadId;


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

        imageView = (ImageView) findViewById(R.id.imageView);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewPrice = (TextView) findViewById(R.id.textViewPrice);
        textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        btnWishlist = (Button) findViewById(R.id.btnWishlist);
        btnCart = (Button) findViewById(R.id.btnCart);

        //firebase initialise
        firebaseAuth = firebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        //set objects
        Picasso.with(this).load(Uri.parse(imageUrl)).into(imageView);
        textViewTitle.setText(title);
        textViewPrice.setText("$" + df2.format(price));
        textViewCategory.setText(category);
        textViewDescription.setText(description);


        DatabaseReference DrWishlist = firebaseDatabase.child("users").child(user.getUid()).child("Wishlist");
        DrWishlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check if item is already in wishlist
                if (dataSnapshot.hasChild(uploadId)) {

                    btnWishlist.setText("Remove from Wishlist");

                } else {

                    btnWishlist.setText("Add to Wishlist");

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
                            title, description, imageUrl, category, price, uploadId
                    );

                    firebaseDatabase.child("users").child(user.getUid()).child("Wishlist").child(uploadId).setValue(upload);

                    Toast.makeText(ItemActivity.this, "Item Added to Wishlist", Toast.LENGTH_SHORT).show();
                    btnWishlist.setText("Remove from Wishlist");

                } else {

                    firebaseDatabase.child("users").child(user.getUid()).child("Wishlist").child(uploadId).removeValue();
                    Toast.makeText(ItemActivity.this, "Item Removed from Wishlist", Toast.LENGTH_SHORT).show();
                    btnWishlist.setText("Add to Wishlist");

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
