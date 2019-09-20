package student.uts.edu.au.baristabrosapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        Intent intent = getIntent();
        categorySelected = intent.getStringExtra("category");


        //firebase initialise
        firebaseAuth = firebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        listView = (ListView) findViewById(R.id.lvCat);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        tvTitle.setText(categorySelected);

        //Display category list
        catList = new ArrayList<>();
        /*catList.add(new ItemData("Item title 1", "$20", "Item description 1", R.drawable.barista, "Category"));
        catList.add(new ItemData("Item title 2", "$20", "Item description 2", R.drawable.barista, "Category"));
        catList.add(new ItemData("Item title 3", "$20", "Item description 3", R.drawable.barista, "Category"));
        catList.add(new ItemData("Item title 4", "$20", "Item description 4", R.drawable.barista, "Category"));
        catList.add(new ItemData("Item title 5", "$20", "Item description 5", R.drawable.barista, "Category"));
        catList.add(new ItemData("Item title 6", "$20", "Item description 6", R.drawable.barista, "Category"));
        catList.add(new ItemData("Item title 7", "$20", "Item description 7", R.drawable.barista, "Category"));
        catList.add(new ItemData("Item title 8", "$20", "Item description 8", R.drawable.barista, "Category"));

        itemsList = new ItemsList(this, R.layout.listview_layout, catList);

        listView.setAdapter(itemsList);*/

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

                intent.setClass(CategoryActivity.this, ItemActivity.class);
                startActivity(intent);
            }
        });


        DatabaseReference DrCategoryData = firebaseDatabase.child("category").child(categorySelected);

        //get category listing
        /*
        DrCategoryData.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot listing : dataSnapshot.getChildren()) {

                    ImageUpload imageUpload = listing.getValue(ImageUpload.class);
                    catList.add(imageUpload);

                }

                //itemsList = new ItemsList(CategoryActivity.this, R.layout.listview_layout, catList);

                itemsList = new ItemsList(CategoryActivity.this, R.layout.listview_layout, catList );
                listView.setAdapter(itemsList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});*/
        catList = ImageUpload.search(categorySelected,"");
        itemsList = new ItemsList(CategoryActivity.this, R.layout.listview_layout, catList );
        listView.setAdapter(itemsList);

        //Just test for search
        //ArrayList<ImageUpload> test = ImageUpload.search("Computers","Acer");

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

       /* final ArrayList<ImageUpload> matches = new ArrayList<ImageUpload>();
        Query query = DrCategoryData.orderByChild("title").equalTo("Computers");
        DrCategoryData.orderByChild("title").equalTo("Computers");
        DrCategoryData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("Test", "Does this ever get hit?");
                System.out.println("Working");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ImageUpload i = new ImageUpload(ds.getValue(ItemData.class).getTitle(),
                            ds.getValue(ImageUpload.class).getDesc(),
                            ds.getValue(ImageUpload.class).getImageUrl(),
                            ds.getValue(ImageUpload.class).getCategory(),
                            ds.getValue(ImageUpload.class).getPrice(),
                            ds.getValue(ImageUpload.class).getUploadId(),
                            ds.getValue(ImageUpload.class).getSellerId());
                    matches.add(i);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            });*/
       ImageUpload.search("Computers", "Acer");
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
