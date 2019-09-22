package student.uts.edu.au.baristabrosapp;

import android.app.Activity;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ImageUpload {

    String title;
    String desc;
    String imageUrl;
    String category;
    Double price;
    String uploadId;
    String sellerId;

    public ImageUpload() {

    }

    public ImageUpload(String title, String desc, String imageUrl, String category, Double price, String uploadId, String sellerId) {

        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.category = category;
        this.price = price;
        this.uploadId = uploadId;
        this.sellerId = sellerId;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public static void search(final searchable act, String category, String title)
    {
        DatabaseReference ref;
        Query query;
        final ArrayList<ImageUpload> matches = new ArrayList<ImageUpload>();
        if(category == null)
        {
            ref = FirebaseDatabase.getInstance().getReference();
        }
        else
        {
            ref = FirebaseDatabase.getInstance().getReference().child("category").child(category);

        }
        if(title.equals(""))
        {
            query = ref.orderByChild("title");
        }
        else
        {
           query = ref.orderByChild("title").startAt(title).endAt(title +"\uf8ff");
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ImageUpload i = new ImageUpload();
                        i.setTitle(ds.getValue(ImageUpload.class).getTitle());
                        i.setDesc(ds.getValue(ImageUpload.class).getDesc());
                        i.setImageUrl(ds.getValue(ImageUpload.class).getImageUrl());
                        i.setCategory(ds.getValue(ImageUpload.class).getCategory());
                        i.setPrice(ds.getValue(ImageUpload.class).getPrice());
                        i.setUploadId(ds.getValue(ImageUpload.class).getUploadId());
                        i.setSellerId(ds.getValue(ImageUpload.class).getSellerId());
                        matches.add(i);
                    }
                    act.updateList(matches);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }
}
