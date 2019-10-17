package student.uts.edu.au.baristabrosapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, searchable {


    private static DecimalFormat df2 = new DecimalFormat("0.00");

    //declare variables
    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private StorageReference firebaseStorage;
    private FirebaseUser user;
    private List<ImageUpload> catList;
    private ListView listView;
    private TextView tvTitle;
    private ItemsList itemsList;
    private String categorySelected;
    private String searchTerm;
    private Button searchBtn;
    private EditText etSearch;
    private TextView tvNoContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        Intent intent = getIntent();
        categorySelected = intent.getStringExtra("category");
        searchTerm = intent.getStringExtra("search");
        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);

        listView = findViewById(R.id.lvCat);
        tvTitle = findViewById(R.id.tvTitle);
        searchBtn = findViewById(R.id.btnSearch);
        etSearch = findViewById((R.id.etSearch));
        tvNoContent = findViewById(R.id.tvNotFound);
        tvTitle.setText(categorySelected);

        //Display category list
        catList = new ArrayList<>();

        itemsList = new ItemsList(this, R.layout.listview_layout, catList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent();
                intent.putExtra("picture", catList.get(position).imageUrl);
                intent.putExtra("title", catList.get(position).title);
                intent.putExtra("price", catList.get(position).price);
                intent.putExtra("description", catList.get(position).desc);
                intent.putExtra("category", catList.get(position).category);
                intent.putExtra("uploadId", catList.get(position).uploadId);
                intent.putExtra("sellerId", catList.get(position).sellerId);
                intent.putExtra("sellTime", catList.get(position).sellTime);

                intent.setClass(CategoryActivity.this, ItemActivity.class);
                startActivity(intent);
            }
        });
        if(categorySelected.toLowerCase().equals("search"))
        {
            ImageUpload.search(this, "", searchTerm);
        }
        else if(categorySelected.toLowerCase().equals( "recommended"))
        {
            ImageUpload.search(this, "", "");
        }
        else
        {
            ImageUpload.search(this, categorySelected, "");
        }


        //Just test for search


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
        listView.setAdapter(itemsList);
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
            if (categorySelected.equals("Recommended")) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                intent = new Intent();
                intent.putExtra("category", "Recommended");
                intent.setClass(this, CategoryActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                return true;
            }
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
            //super.onBackPressed();
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void updateList(ArrayList<ImageUpload> imageUploads) {
        if (imageUploads.size() < 1) {
            tvNoContent.setVisibility(View.VISIBLE);
        } else {
            tvNoContent.setVisibility(View.GONE);
        }
        itemsList.clear();

        itemsList.setData(imageUploads);

    }

    public void searchOnClick(View v) {
        if(categorySelected.toLowerCase().equals( "search"))
        {
            ImageUpload.search(this, "", etSearch.getText().toString());
        }
        else
        {
            ImageUpload.search(this, categorySelected, etSearch.getText().toString());
        }
        etSearch.setText("");
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);

    }
}
