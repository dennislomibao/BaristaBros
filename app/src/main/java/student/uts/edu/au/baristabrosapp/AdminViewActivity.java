package student.uts.edu.au.baristabrosapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdminViewActivity extends AppCompatActivity implements searchable {

    //declare variables
    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private StorageReference firebaseStorage;
    private FirebaseUser user;
    private List<ImageUpload> catList;
    private ListView listView;
    private TextView tvTitle;
    private ItemsList itemsList;
    private String categorySelected;
    private Button searchBtn;
    private EditText etSearch;
    private TextView tvNoContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        Intent intent = getIntent();
        categorySelected = intent.getStringExtra("category");

        //firebase initialise
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        listView = findViewById(R.id.lvCat);
        tvTitle = findViewById(R.id.tvTitle);
        searchBtn = findViewById(R.id.btnSearch);
        etSearch = findViewById((R.id.etSearch));
        tvNoContent = findViewById(R.id.tvNotFound);
        tvTitle.setText("Admin - " + categorySelected);

        //Display category list
        catList = new ArrayList<>();
        itemsList = new ItemsList(this, R.layout.listview_layout, catList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent();
                intent.putExtra("picture", catList.get(position).imageUrl);
                intent.putExtra("title", catList.get(position).title);
                intent.putExtra("price", catList.get(position).price);
                intent.putExtra("description", catList.get(position).desc);
                intent.putExtra("category", catList.get(position).category);
                intent.putExtra("uploadId", catList.get(position).uploadId);
                intent.putExtra("sellerId", catList.get(position).sellerId);
                intent.putExtra("sellTime", catList.get(position).sellTime);

                intent.setClass(AdminViewActivity.this, AdminItemActivity.class);
                startActivity(intent);
            }
        });

        ImageUpload.search(this, "", "");
        listView.setAdapter(itemsList);
    }

    @Override
    public void updateList(ArrayList<ImageUpload> imageUploads) {
        if (imageUploads.size() < 1) {
            tvNoContent.setVisibility(View.VISIBLE);
        } else {
            tvNoContent.setVisibility(View.GONE);
        }
        itemsList.clear();
        itemsList.setData(imageUploads);

    }

    public void searchOnClick(View v) {
        ImageUpload.search(this, "", etSearch.getText().toString());
    }
}
