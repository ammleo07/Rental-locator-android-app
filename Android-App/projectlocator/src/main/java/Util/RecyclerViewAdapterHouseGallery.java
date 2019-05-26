package Util;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectlocator.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.House;

public class RecyclerViewAdapterHouseGallery extends RecyclerView.Adapter<RecyclerViewHoldersHouseGallery>{

    private List<String> itemList;
    private Context context;

    public RecyclerViewAdapterHouseGallery(Context context, List<String> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHoldersHouseGallery onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.house_gallery, null);
        RecyclerViewHoldersHouseGallery rcv = new RecyclerViewHoldersHouseGallery(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHoldersHouseGallery holder, int position) {
        //holder.houseId.setText(itemList.get(position).getId() + "");
        //Picasso.with(context).load("http://192.168.1.11:8080/resources/images/ama.png").resize(200,250).into(holder.houseImage);
        //Picasso.with(context).load("http://192.168.1.11:8080/resources/images/ama.png").into(holder.houseImage);
        Log.i("path","http://192.168.1.11:8080" + itemList.get(position));
        Picasso.with(context).load("http://192.168.1.11:8080" + itemList.get(position)).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.houseImage);
        //Picasso.with(context).load("http://192.168.0.137:8080" + itemList.get(position)).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.houseImage);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}