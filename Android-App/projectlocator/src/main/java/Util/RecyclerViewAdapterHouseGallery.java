package Util;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectlocator.R;
import com.squareup.picasso.Callback;
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
    public void onBindViewHolder(final RecyclerViewHoldersHouseGallery holder, int position) {
        //holder.houseId.setText(itemList.get(position).getId() + "");
        //Picasso.with(context).load("http://192.168.1.11:8080/resources/images/ama.png").resize(200,250).into(holder.houseImage);
        //Picasso.with(context).load("http://192.168.1.11:8080/resources/images/ama.png").into(holder.houseImage);
        //Log.i("path","http://192.168.1.11:8080" + itemList.get(position));
        final SharedPreferences sharedpreferences =context.getSharedPreferences("user", Context.MODE_PRIVATE);
        //Picasso.with(context).load("http://192.168.1.11:8080" + itemList.get(position)).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.houseImage);
        Log.i("Server Path:","http://" + sharedpreferences.getString("SERVER",null));
        Picasso.with(context).load("http://" + sharedpreferences.getString("SERVER",null) + itemList.get(position)).memoryPolicy(MemoryPolicy.NO_CACHE)
                             .resize(400,300)
                             .centerCrop()
                             .into(holder.houseImage, new Callback() {
                                 @Override
                                 public void onSuccess() {
                                     holder.progressBar.setVisibility(View.GONE);
                                 }

                                 @Override
                                 public void onError() {
                                     Log.i("Error on picasso:","http://" + sharedpreferences.getString("SERVER",null));
                                 }
                             });
        //Picasso.with(context).load("http://192.168.0.137:8080" + itemList.get(position)).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.houseImage);
//        Picasso.with(context).load("http://balangay.site:8080/usr/local/images"  + itemList.get(position)).memoryPolicy(MemoryPolicy.NO_CACHE)
//                .resize(450,600)
//                .centerCrop()
//                .into(holder.houseImage, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        holder.progressBar.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onError() {
//                        Log.i("Error on picasso:","http://" + sharedpreferences.getString("SERVER",null));
//                    }
//                });

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}