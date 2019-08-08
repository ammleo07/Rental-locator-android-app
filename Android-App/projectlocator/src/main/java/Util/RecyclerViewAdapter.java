package Util;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectlocator.R;

import java.util.List;

import Model.House;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders>{

    private List<House> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<House> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_house, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.houseName.setText("House Name: " + itemList.get(position).getHouseName());
        holder.houseType.setText("House Type: " + itemList.get(position).getHouseType());
        holder.housePrice.setText("Monthly Rental : Php " + Math.round(itemList.get(position).getMonthlyFee()));
        holder.houseAddress.setText("Address: " + itemList.get(position).getFullAddress());
        holder.houseId.setText(itemList.get(position).getId() + "");
        holder.boardertype.setText("Boarder Type[Male/Female]:" + itemList.get(position).getBoarderType());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}