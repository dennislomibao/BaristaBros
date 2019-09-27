package student.uts.edu.au.baristabrosapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemsList extends ArrayAdapter<ImageUpload> {

    final Context context;
    final int resource;
    List<ImageUpload> data;


    public ItemsList(Context context, int resource, final List<ImageUpload> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.data = objects;


    }

    public void setData(List<ImageUpload> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(ImageUpload upload) {
        data.add(upload);
    }

    static class DataHolder {

        ImageView pic;
        TextView tvTitle;
        TextView tvPrice;
        TextView tvDesc;
        TextView tvCategory;

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        DataHolder dataHolder = null;
        if (convertView == null) {

            LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
            convertView = layoutInflater.inflate(resource, parent, false);
            dataHolder = new DataHolder();
            dataHolder.pic = convertView.findViewById(R.id.imageView_pic);
            dataHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
            dataHolder.tvPrice = convertView.findViewById(R.id.tvPrice);
            dataHolder.tvDesc = convertView.findViewById(R.id.tvDesc);
            dataHolder.tvCategory = convertView.findViewById(R.id.tvCategory);

            convertView.setTag(dataHolder);

        } else {

            dataHolder = (DataHolder) convertView.getTag();


        }


        ImageUpload imageUpload = data.get(position);

        DecimalFormat df2 = new DecimalFormat("0.00");
        String dollarSign = "$";


        //set objects with their corresponding data
        //dataHolder.pic.setImageURI(Uri.parse(imageUpload.imageUrl)/*R.drawable.barista*/);
        final ImageView a = convertView.findViewById(R.id.imageView_pic);
        Picasso.with(context).load(Uri.parse(imageUpload.imageUrl)).networkPolicy(NetworkPolicy.OFFLINE).into(a, new Callback() {
            @Override
            public void onSuccess() {
                Log.v("ImageGen", "Recieved from cashe");
            }

            @Override
            public void onError() {

                Picasso.with(context).load(data.get(position).getImageUrl()).into(a, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.v("ImageGen", "Recieved from internet");
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        });
        dataHolder.tvTitle.setText(imageUpload.title);
        dataHolder.tvPrice.setText(dollarSign + df2.format(imageUpload.price));
        dataHolder.tvDesc.setText(imageUpload.desc);
        dataHolder.tvCategory.setText(imageUpload.category);

        return convertView;

    }
}
