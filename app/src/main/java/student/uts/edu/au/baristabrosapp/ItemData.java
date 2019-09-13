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


    public static ArrayList<ItemData> search(String category, String title)
    {
        DatabaseReference ref;
        Query query = null;
        final ArrayList<ItemData> matches = null;
        if(category == null)
        {
            ref = FirebaseDatabase.getInstance().getReference();
        }
        else
        {
            ref = FirebaseDatabase.getInstance().getReference(category);
            query = ref.orderByChild("title").equalTo(title);
        }
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    ItemData i = new ItemData(ds.getValue(ItemData.class).getTitle(),
                            ds.getValue(ItemData.class).getPrice(),
                            ds.getValue(ItemData.class).getDesc(),
                            ds.getValue(ItemData.class).getImgId(),
                            ds.getValue(ItemData.class).getCategory());
                    matches.add(i);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return matches;
    }
}
