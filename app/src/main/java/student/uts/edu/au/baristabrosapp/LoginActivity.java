package student.uts.edu.au.baristabrosapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private EditText Email;
    private EditText Password;
    private Button Login;
    private Button NewUser;
    private Button ForgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = (EditText) findViewById(R.id.etEmail);
        Password = (EditText) findViewById(R.id.etPassword);
        Login = (Button) findViewById(R.id.btnLogin);
        NewUser = (Button) findViewById(R.id.btnSignUp);
        ForgotPassword = (Button) findViewById(R.id.btnResetPassword);

       NewUser.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               test(v);
           }
       });
    }

    private void validate (String userName, String userPassword){
        if((userName == "Admin") && (userPassword == "12"));
    }
    public void test(View view)
    {
        Intent intent = new Intent(this, RegistrationActivty.class);
        startActivity(intent);
    }
}
