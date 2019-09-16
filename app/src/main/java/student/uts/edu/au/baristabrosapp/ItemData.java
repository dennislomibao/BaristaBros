package student.uts.edu.au.baristabrosapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemData {

    String title;
    String desc;
    String price;
    String category;
    Integer imgId;

    public ItemData(String title, String price, String desc, Integer imgId, String category) {
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.imgId = imgId;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getDesc() {
        return desc;
    }

    public String getCategory() {
        return category;
    }

    public Integer getImgId() {
        return imgId;
    }
}
