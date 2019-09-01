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
        holder.transactionRenteeName.setText("Rentee:" + itemList.get(position).getRentee() + "");
        holder.transactionStatus.setText("Status: " + itemList.get(position).getStatus() + "");
        holder.transactionId.setText(itemList.get(position).getId() + "");
        holder.ownerTokenId.setText(itemList.get(position).getOwnerTokenId());
        holder.renteeTokenId.setText(itemList.get(position).getRenteeTokenId());
        holder.transactionRenteeContactNumber.setText(itemList.get(position).getRenteeContactNumber());

        SharedPreferences sharedpreferences =context.getSharedPreferences("user", Context.MODE_PRIVATE);
        if(sharedpreferences.getString("userType",null).equalsIgnoreCase("rentee")) {
            holder.transactionacceptButton.setText("Confirm Acceptance");
            if((!itemList.get(position).getStatus().equalsIgnoreCase("Accepted")) || (itemList.get(position).getStatus().equalsIgnoreCase("Sold")))
            {
                holder.transactionacceptButton.setEnabled(false);
            }
        }
        else {
            holder.transactionacceptButton.setText("Accept");

            if(itemList.get(position).getStatus().equalsIgnoreCase("Accepted"))
            {
                holder.transactionacceptButton.setEnabled(false);
            }
        }

        if(itemList.get(position).getStatus().matches("Confirmed|Rejected|Sold"))
            holder.transactionacceptButton.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}