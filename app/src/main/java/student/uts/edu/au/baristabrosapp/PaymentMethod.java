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

import java.util.Locale;

public class PaymentMethod extends AppCompatActivity {

    CardForm cardForm;
    private double price;
    private TextView totalPrice;
    Button buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        price = intent.getDoubleExtra("price", 0.00);
        setContentView(R.layout.activity_payment_method);

        cardForm = findViewById(R.id.credit_card);
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
                                    //TODO empty cart
                                    Toast.makeText(PaymentMethod.this, "thank you for your purchase", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(PaymentMethod.this, HomePageActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                } else {
                    //TODO add error message
                }
            }
        });
    }
}
