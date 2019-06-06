package Util;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectlocator.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Transaction;

public class RecyclerViewAdapterTransactions extends RecyclerView.Adapter<RecyclerViewHoldersTransactions>{

    private List<Transaction> itemList;
    private Context context;

    public RecyclerViewAdapterTransactions(Context context, List<Transaction> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHoldersTransactions onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transaction, null);
        RecyclerViewHoldersTransactions rcv = new RecyclerViewHoldersTransactions(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHoldersTransactions holder, int position) {
        holder.transactionName.setText("Transaction to " + itemList.get(position).getHouseOwner() + "");
        holder.transactionOwnerName.setText("Owner:" + itemList.get(position).getHouseOwner() + "");
        holder.transactionRenteeName.setText("Rentee: " + itemList.get(position).getRentee() + "");
        holder.transactionStatus.setText("Status: " + itemList.get(position).getStatus() + "");
        holder.transactionId.setText(itemList.get(position).getId() + "");

        SharedPreferences sharedpreferences =context.getSharedPreferences("user", Context.MODE_PRIVATE);
        if(sharedpreferences.getString("userType",null).equalsIgnoreCase("rentee"))
            holder.transactionacceptButton.setText("Confirm Acceptance");
        else
            holder.transactionacceptButton.setText("Accept");

        //Picasso.with(context).load("http://192.168.1.11:8080/resources/images/ama.png").resize(200,250).into(holder.houseImage);
        //Picasso.with(context).load("http://192.168.1.11:8080/resources/images/ama.png").into(holder.houseImage);
        //Log.i("path","http://192.168.1.11:8080" + itemList.get(position));
        //Picasso.with(context).load("http://192.168.1.11:8080" + itemList.get(position)).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.houseImage);
        //Picasso.with(context).load("http://192.168.0.137:8080" + itemList.get(position)).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.houseImage);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}