package Util;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectlocator.R;

import Model.Transaction;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import Util.Retrofit.RetrofitServiceHouseOwner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewHoldersCostEstimates extends RecyclerView.ViewHolder
        implements View.OnClickListener{

    public TextView routeName,commuteAndCost;


    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public RecyclerViewHoldersCostEstimates(final View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        routeName = itemView.findViewById(R.id.route_name);
        commuteAndCost = itemView.findViewById(R.id.commute_cost);

    }



    @Override
    public void onClick(final View view) {

    }
}