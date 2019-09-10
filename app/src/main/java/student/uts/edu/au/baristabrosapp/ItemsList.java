package student.uts.edu.au.baristabrosapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ItemsList extends ArrayAdapter<ItemData> {

    Context context;
    int resource;
    List<ItemData> data = null;

    public ItemsList(Context context, int resource, List<ItemData> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.data = objects;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        DataHolder dataHolder = null;
        if (convertView == null) {

            LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
            convertView = layoutInflater.inflate(resource, parent, false);
            dataHolder = new DataHolder();
            dataHolder.pic = (ImageView) convertView.findViewById(R.id.imageView_pic);
            dataHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            dataHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            dataHolder.tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            dataHolder.tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);

            convertView.setTag(dataHolder);

        } else {

            dataHolder = (DataHolder) convertView.getTag();

        }

        ItemData itemData = data.get(position);
        dataHolder.pic.setImageResource(itemData.imgId);
        dataHolder.tvTitle.setText(itemData.title);
        dataHolder.tvPrice.setText(itemData.price);
        dataHolder.tvDesc.setText(itemData.desc);
        dataHolder.tvCategory.setText(itemData.category);

        return convertView;

    }
}
