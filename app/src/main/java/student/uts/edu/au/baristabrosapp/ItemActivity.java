package student.uts.edu.au.baristabrosapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ItemActivity extends AppCompatActivity {

    private String imgId;
    private String title;
    private String price;
    private String description;
    private String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();
        imgId = intent.getStringExtra("picture");
        title = intent.getStringExtra("title");
        price = intent.getStringExtra("price");
        category = intent.getStringExtra("category");
        description = intent.getStringExtra("description");

    }

}
