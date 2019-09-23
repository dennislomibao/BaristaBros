package student.uts.edu.au.baristabrosapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.view.CardForm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class PaymentMethod extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private FirebaseUser user;
    CardForm cardForm;
    private double price;
    private TextView totalPrice;
    Button buy;
    Button changeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        price = intent.getDoubleExtra("price", 0.00);
        setContentView(R.layout.activity_payment_method);

        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        cardForm = findViewById(R.id.credit_card);
        changeAddress = findViewById(R.id.changeAddress);
        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentMethod.this, ChangeAddress.class));
            }
        });
        totalPrice = findViewById(R.id.tvTotal);
        totalPrice.setText(String.format(Locale.getDefault(), "Total: $%.2f", price));
        buy = findViewById(R.id.pay_now);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .setup(PaymentMethod.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardForm.isValid()) {
                    new AlertDialog.Builder(PaymentMethod.this)
                            .setTitle("Confirm")
                            .setMessage(String.format(Locale.getDefault(), "Purchase $%.2f?", price))
                            .setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Remove cart
                                    firebaseDatabase.child("users").child(user.getUid()).child("Cart").removeValue();

                                    Toast.makeText(PaymentMethod.this, "thank you for your purchase", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(PaymentMethod.this, HomePageActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                } else {

                    if (cardForm.getCardNumber().isEmpty() || cardForm.getExpirationDateEditText().toString().isEmpty() || cardForm.getCvv().isEmpty()){
                        Toast.makeText(PaymentMethod.this,"Input required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
