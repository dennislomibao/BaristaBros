package student.uts.edu.au.baristabrosapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SellActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //declare variables
    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;
    private StorageReference firebaseStorage;


    private static final int PICK_IMAGE_REQUEST = 1;
    private static DecimalFormat df2 = new DecimalFormat("0.00");

    private Button btnChoose;
    private Button btnSubmit;
    private EditText editTextTitle;
    private EditText editTextDesc;
    private EditText editTextPrice;
    private ImageView image;
    private ProgressBar progressBar;
    private Spinner spinner;
    private StorageTask uploadTask;
    private ProgressDialog progressDialog;

    String category;
    String imageReference;
    Double price;

    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);

        btnChoose = findViewById(R.id.btnChoose);
        btnSubmit = findViewById(R.id.btnSubmit);
        image = findViewById(R.id.imageView);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDesc = findViewById(R.id.editTextDesc);
        progressBar = findViewById(R.id.progressBar);
        spinner = findViewById(R.id.spinnerCategory);
        editTextPrice = findViewById(R.id.editTextPrice);

        //category spinner selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //choose image to upload
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        //submit form
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    //buffer
                    Toast.makeText(SellActivity.this, "In Progress...", Toast.LENGTH_SHORT).show();

                } else if (!editTextTitle.getText().toString().trim().equals("") &&
                        !editTextDesc.getText().toString().trim().equals("") &&
                        !editTextPrice.getText().toString().trim().equals("") && !category.trim().equals("")) {

                    if (imageUri != null) {
                        try {

                            //get price and format to 2dp
                            price = Double.parseDouble(editTextPrice.getText().toString().trim());

                            if (price > 0) {

                                uploadData();
                                Toast.makeText(SellActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(SellActivity.this, "Price Invalid", Toast.LENGTH_SHORT).show();

                            }

                        } catch (NumberFormatException e) {

                            Toast.makeText(SellActivity.this, "Price Invalid", Toast.LENGTH_SHORT).show();

                        }

                    } else {

                        Toast.makeText(SellActivity.this, "Image Required", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(SellActivity.this, "Input Required", Toast.LENGTH_SHORT).show();

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

    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //load phone image library
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(image);
        }
    }

    //get image extension
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //upload picture to firebase storage
    private void uploadData() {
        if (imageUri != null) {
            final StorageReference fileReference = firebaseStorage.child("imageUploads")
                    .child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setProgress(0);

                        }
                        //delay upload
                    }, 500);

                    //upload success
                    //progressDialog.dismiss();
                    Task<Uri> downloadUrl = fileReference.getDownloadUrl();
                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            imageReference = uri.toString();
                            String uploadId = firebaseDatabase.push().getKey();

                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm z");

                            Toast.makeText(SellActivity.this, "Posting Successful", Toast.LENGTH_LONG).show();
                            ImageUpload upload =
                                    new ImageUpload(editTextTitle.getText().toString().trim(),
                                            editTextDesc.getText().toString().trim(),
                                            imageReference,
                                            category.trim(),
                                            price,
                                            uploadId,
                                            user.getUid(),
                                            sdf.format(new Date()));

                            firebaseDatabase.child("category").child(category).child(uploadId).setValue(upload);


                            //store user's listing history
                            firebaseDatabase.child("users").child(user.getUid()).child("Sell History").child(uploadId).setValue(upload);
                            firebaseDatabase.child("users").child(user.getUid()).child("Sell Current").child(uploadId).setValue(upload);

                            endTask();

                        }
                    });
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //upload fail
                    //progressDialog.dismiss();
                    Toast.makeText(SellActivity.this, "Posting Unsuccessful", Toast.LENGTH_SHORT).show();

                }

            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    //show progress while image is uploaded
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    if (progress < 20) {
                        progress = 20;
                    }
                    progressBar.setProgress((int) progress);

                }
            });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void endTask() {

        Intent intent = new Intent(this, HomePageActivity.class);
        drawerLayout.closeDrawer(GravityCompat.START);
        finish();
        startActivity(intent);

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
            drawerLayout.closeDrawer(GravityCompat.START);
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
}
