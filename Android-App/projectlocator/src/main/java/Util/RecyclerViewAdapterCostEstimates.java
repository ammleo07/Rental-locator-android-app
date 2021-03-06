package Util;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectlocator.R;

import java.util.List;

import Model.Comment;
import Model.CostEstimates;
import Model.Transaction;

public class RecyclerViewAdapterCostEstimates extends RecyclerView.Adapter<RecyclerViewHoldersCostEstimates>{

    private List<Comment> itemList;
    private Context context;

    public RecyclerViewAdapterCostEstimates(Context context, List<Comment> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHoldersCostEstimates onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cost_estimates, null);
        RecyclerViewHoldersCostEstimates rcv = new RecyclerViewHoldersCostEstimates(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHoldersCostEstimates holder, int position) {
        holder.routeName.setText("Customer: " + itemList.get(position).getRentee() + "");
        holder.commuteAndCost.setText("Comments: " + itemList.get(position).getComment() + "");
        holder.houseRating.setEnabled(false);
        holder.houseRating.setRating(Float.parseFloat(itemList.get(position).getRate().toString()));
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}