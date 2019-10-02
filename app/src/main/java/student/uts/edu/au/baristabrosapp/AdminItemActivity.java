package student.uts.edu.au.baristabrosapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class AdminItemActivity extends AppCompatActivity {

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
    private Button btnRemove;

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
        setContentView(R.layout.activity_admin_item);

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
        btnRemove = findViewById(R.id.btnRemove);

        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

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

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //remove item from store
                Toast.makeText(AdminItemActivity.this, "Item Removed", Toast.LENGTH_SHORT).show();
                firebaseDatabase.child("category").child(category).child(uploadId).removeValue();
                firebaseDatabase.child("users").child(user.getUid()).child("Sell Current").child(uploadId).removeValue();
                firebaseDatabase.child("users").child(user.getUid()).child("Sell History").child(uploadId).removeValue();

                Intent intent = new Intent(AdminItemActivity.this, AdminHomePageActivity.class);
                finish();
                startActivity(intent);

            }
        });
    }
}
