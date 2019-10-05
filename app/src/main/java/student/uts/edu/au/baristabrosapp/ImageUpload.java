package student.uts.edu.au.baristabrosapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    String sellTime;

    public ImageUpload() {

    }

    public ImageUpload(String title, String desc, String imageUrl, String category, Double price, String uploadId, String sellerId, String sellTime) {

        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.category = category;
        this.price = price;
        this.uploadId = uploadId;
        this.sellerId = sellerId;
        this.sellTime = sellTime;

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

    public String getSellTime() {
        return sellTime;
    }

    public void setSellTime(String sellTime) {
        this.sellTime = sellTime;
    }

    public static void search(final searchable act, final String category, final String title) {
        DatabaseReference ref;
        final ArrayList<ImageUpload> matches = new ArrayList<ImageUpload>();
            ref = FirebaseDatabase.getInstance().getReference().child("category");
            ref.addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot c : dataSnapshot.getChildren()) {
                        for (DataSnapshot listing : c.getChildren()) {

                            ImageUpload i = new ImageUpload();
                            i.setTitle(listing.getValue(ImageUpload.class).getTitle());
                            i.setDesc(listing.getValue(ImageUpload.class).getDesc());
                            i.setImageUrl(listing.getValue(ImageUpload.class).getImageUrl());
                            i.setCategory(listing.getValue(ImageUpload.class).getCategory());
                            i.setPrice(listing.getValue(ImageUpload.class).getPrice());
                            i.setUploadId(listing.getValue(ImageUpload.class).getUploadId());
                            i.setSellerId(listing.getValue(ImageUpload.class).getSellerId());
                            i.setSellTime(listing.getValue(ImageUpload.class).getSellTime());
                            if(i.category.toLowerCase().equals(category.toLowerCase()) || category.toLowerCase().equals(""))
                            {
                                if(i.getTitle().toLowerCase().contains(title.toLowerCase()) || title.toLowerCase().equals(""))
                                {
                                    matches.add(i);
                                }
                            }
                        }


                    }
                    act.updateList(matches);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
}
