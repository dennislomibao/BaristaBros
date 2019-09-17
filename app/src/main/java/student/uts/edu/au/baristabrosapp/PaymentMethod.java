package student.uts.edu.au.baristabrosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.braintreepayments.cardform.view.CardForm;

import org.w3c.dom.Text;

public class PaymentMethod extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        CardForm cardForm = (CardForm) findViewById(R.id.credit_card);
        //TextView txtDes = (TextView) findViewById(R.id.payment_ammount);
        Button btnPay = (Button)findViewById(R.id.pay_now);

       // txtDes.setText("$1999");
       // btnPay.setText(String.format("Payer %s", txtDes.getText()));

        //CardForm


    }
}
