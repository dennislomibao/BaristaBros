package student.uts.edu.au.baristabrosapp;

import android.util.Log;

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

    public static ArrayList<ImageUpload> search(String category, String title)
    {
        DatabaseReference ref;
       // Query query = null;
        final ArrayList<ImageUpload> matches = new ArrayList<ImageUpload>();
        if(category == null)
        {
            ref = FirebaseDatabase.getInstance().getReference();
        }
        else
        {
            ref = FirebaseDatabase.getInstance().getReference().child("category").child(category);

        }
        Query query = ref.orderByChild("title").equalTo(title);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("Test","Does this ever get hit?");
                System.out.println("Working");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ImageUpload i = new ImageUpload(ds.getValue(ItemData.class).getTitle(),
                                ds.getValue(ImageUpload.class).getDesc(),
                                ds.getValue(ImageUpload.class).getImageUrl(),
                                ds.getValue(ImageUpload.class).getCategory(),
                                ds.getValue(ImageUpload.class).getPrice(),
                                ds.getValue(ImageUpload.class).getUploadId(),
                                ds.getValue(ImageUpload.class).getSellerId());
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
