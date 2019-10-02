package student.uts.edu.au.baristabrosapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class UpdateAddress extends AppCompatActivity {

    private EditText addressLine, suburb, country, state, postcode;
    private Button updateAddress;
    private Button settings;
    private Button goBackToPaymentPage;
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);

        addressLine = findViewById(R.id.etAddress);
        settings = findViewById(R.id.btnYea);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateAddress.this, SettingsActivity.class));
            }
        });
        suburb = findViewById(R.id.etSuburb);
        country = findViewById(R.id.etCountry);
        goBackToPaymentPage = findViewById(R.id.btnGoBackToCheckout);
        state = findViewById(R.id.etState);
        postcode = findViewById(R.id.etPostcode);
        updateAddress = findViewById(R.id.btnUpdateAdd);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        DatabaseReference databaseReference = firebaseDatabase.child("users").child(user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                addressLine.setText(dataSnapshot.child("address").getValue(String.class));
                suburb.setText(dataSnapshot.child("suburb").getValue(String.class));
                country.setText(dataSnapshot.child("country").getValue(String.class));
                state.setText(dataSnapshot.child("state").getValue(String.class));
                postcode.setText(dataSnapshot.child("postcode").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateAddress.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    updateDeliveryAddress();
                    Toast.makeText(UpdateAddress.this, "Delivery Address Updated", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(UpdateAddress.this, PaymentActivity.class));

                } else {
                    Toast.makeText(UpdateAddress.this, "Address Format Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goBackToPaymentPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateAddress.this, PaymentActivity.class));
            }
        });
    }

    private Boolean isValid() {
        return (!addressLine.getText().toString().trim().isEmpty()
                && !suburb.getText().toString().trim().isEmpty()
                && !country.getText().toString().trim().isEmpty()
                && !state.getText().toString().trim().isEmpty()
                && !postcode.getText().toString().trim().isEmpty());
    }

    private void updateDeliveryAddress() {
        String AddressLine = addressLine.getText().toString().trim();
        String Suburb = suburb.getText().toString().trim();
        String Country = country.getText().toString().trim();
        String State = state.getText().toString().trim();
        String Postcode = postcode.getText().toString().trim();
        Map<String, Object> data = new HashMap<>();
        data.put("address", AddressLine);
        data.put("suburb", Suburb);
        data.put("country", Country);
        data.put("state", State);
        data.put("postcode", Postcode);

        firebaseDatabase.child("users").child(user.getUid()).updateChildren(data);
    }
}

