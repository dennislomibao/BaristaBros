package student.uts.edu.au.baristabrosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Key;

public class ChangeAddress extends AppCompatActivity {
    private EditText addressLine, suburb, country, state, postcode;
    private Button update;
    private FirebaseDatabase mDataBase;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);


        addressLine = (EditText) findViewById(R.id.etAddress);
        suburb = (EditText) findViewById(R.id.etSuburb);
        country = (EditText) findViewById(R.id.etCountry);
        state = (EditText) findViewById(R.id.etState);
        postcode = (EditText) findViewById(R.id.etPostcode);
        update = (Button) findViewById(R.id.btnUpdate);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(addressLine.getText().toString().isEmpty() ||
                        suburb.getText().toString().isEmpty() ||
                        country.getText().toString().isEmpty()||
                        state.getText().toString().isEmpty() ||
                        postcode.getText().toString().isEmpty()){
                    Toast.makeText(ChangeAddress.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ChangeAddress.this, "Delivery Address updated",Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(ChangeAddress.this, PaymentMethod.class));

            }
        });



    }

}

