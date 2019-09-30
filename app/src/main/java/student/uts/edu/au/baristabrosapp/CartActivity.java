package student.uts.edu.au.baristabrosapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //declare variables
    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;
    private List<ImageUpload> listCart;
    private ListView listView;
    private TextView totalPrice;
    private ItemsList itemsList;
    private Button purchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        NavigationView navView = findViewById(R.id.nav_view);
        totalPrice = findViewById(R.id.tvTotal);
        navView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        purchase = findViewById(R.id.btnPurchase);
        listView = findViewById(R.id.lvCart);

        //Display category list
        listCart = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent();
                intent.putExtra("picture", listCart.get(position).imageUrl);
                intent.putExtra("title", listCart.get(position).title);
                intent.putExtra("price", listCart.get(position).price);
                intent.putExtra("description", listCart.get(position).desc);
                intent.putExtra("category", listCart.get(position).category);
                intent.putExtra("uploadId", listCart.get(position).uploadId);
                intent.putExtra("sellerId", listCart.get(position).sellerId);
                intent.putExtra("sellTime", listCart.get(position).sellTime);

                intent.setClass(CartActivity.this, ItemActivity.class);
                startActivity(intent);
            }
        });


        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Change when needed
                if (calculateTotalPrice(listCart) > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("price", calculateTotalPrice(listCart));
                    intent.setClass(CartActivity.this, PaymentActivity.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(CartActivity.this, "No items added", Toast.LENGTH_SHORT).show();

                }
            }
        });

        DatabaseReference DrCartData = firebaseDatabase.child("users").child(user.getUid()).child("Cart");

        //get category listing
        DrCartData.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listCart = new ArrayList<>();

                for (DataSnapshot listing : dataSnapshot.getChildren()) {

                    ImageUpload imageUpload = listing.getValue(ImageUpload.class);
                    listCart.add(imageUpload);

                }

                itemsList = new ItemsList(CartActivity.this, R.layout.listview_layout, listCart);

                totalPrice.setText(String.format(Locale.getDefault(), "Total: $%.2f", calculateTotalPrice(listCart)));

                listView.setAdapter(itemsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            drawerLayout.closeDrawer(GravityCompat.START);
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
            //super.onBackPressed();
            //back button goes to homepage
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }
    }

    private double calculateTotalPrice(List<ImageUpload> listCart) {
        double total = 0.0;
        for (ImageUpload item : listCart) {
            total += item.price;
        }
        return total;
    }
}
