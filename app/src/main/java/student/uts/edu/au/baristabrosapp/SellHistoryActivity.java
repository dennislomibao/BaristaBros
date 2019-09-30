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

public class SellHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //declare variables
    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;
    private List<ImageUpload> listSellHistory;
    private ListView listView;
    private ItemsList itemsList;

    private Button btnSellCurrent;
    private Button btnSellAll;
    private Boolean viewCurrent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_history);

        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);

        listView = findViewById(R.id.lvSellHistory);

        btnSellCurrent = findViewById(R.id.btnSellCurrent);
        btnSellAll = findViewById(R.id.btnSellAll);

        //Display category list
        listSellHistory = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                final Intent intent = new Intent();
                intent.putExtra("picture", listSellHistory.get(position).imageUrl);
                intent.putExtra("title", listSellHistory.get(position).title);
                intent.putExtra("price", listSellHistory.get(position).price);
                intent.putExtra("description", listSellHistory.get(position).desc);
                intent.putExtra("category", listSellHistory.get(position).category);
                intent.putExtra("uploadId", listSellHistory.get(position).uploadId);
                intent.putExtra("sellerId", listSellHistory.get(position).sellerId);
                intent.putExtra("sellTime", listSellHistory.get(position).sellTime);

                final String uploadId = listSellHistory.get(position).uploadId;

                //if item is already sold, display item as view only
                if (!viewCurrent) {

                    DatabaseReference DrCheckItemExists = firebaseDatabase.child("users").child(user.getUid()).child("Sell Current");

                    DrCheckItemExists.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild(uploadId)) {

                                intent.setClass(SellHistoryActivity.this, ItemActivity.class);
                                startActivity(intent);

                            } else {

                                intent.putExtra("audience", "seller");
                                intent.setClass(SellHistoryActivity.this, ViewItemActivity.class);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {

                    intent.setClass(SellHistoryActivity.this, ItemActivity.class);
                    startActivity(intent);

                }
            }
        });

        getListData("Sell Current");
        btnSellCurrent.setEnabled(false);

        btnSellCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listSellHistory = new ArrayList<>();
                getListData("Sell Current");
                btnSellCurrent.setEnabled(false);
                btnSellAll.setEnabled(true);
                viewCurrent = true;

            }
        });

        btnSellAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listSellHistory = new ArrayList<>();
                getListData("Sell History");
                btnSellCurrent.setEnabled(true);
                btnSellAll.setEnabled(false);
                viewCurrent = false;

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

    private void getListData(String data) {

        DatabaseReference DrSellHistoryData = firebaseDatabase.child("users").child(user.getUid()).child(data);

        //get category listing
        DrSellHistoryData.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listSellHistory = new ArrayList<>();

                for (DataSnapshot listing : dataSnapshot.getChildren()) {

                    ImageUpload imageUpload = listing.getValue(ImageUpload.class);
                    listSellHistory.add(imageUpload);

                }

                itemsList = new ItemsList(SellHistoryActivity.this, R.layout.listview_layout, listSellHistory);

                listView.setAdapter(itemsList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
            drawerLayout.closeDrawer(GravityCompat.START);
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
}
