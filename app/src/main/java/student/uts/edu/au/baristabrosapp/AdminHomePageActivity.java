package student.uts.edu.au.baristabrosapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminHomePageActivity extends AppCompatActivity {

    //declare variables
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;

    private Button viewAll;
    private Button viewByCategory;
    private Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        viewAll = findViewById(R.id.btnAllListings);
        viewByCategory = findViewById(R.id.btnByCategory);
        signOut = findViewById(R.id.btnSignOut);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                Intent intent = new Intent(AdminHomePageActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);

            }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("category", "View All Listings");
                intent.setClass(AdminHomePageActivity.this, AdminViewActivity.class);
                startActivity(intent);

            }
        });

        viewByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(AdminHomePageActivity.this, AdminCategoryActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        //Back button goes to home screen instead of previous activity
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
