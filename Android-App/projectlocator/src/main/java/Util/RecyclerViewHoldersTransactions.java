package Util;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectlocator.R;
import com.example.projectlocator.ViewHouseDetailsActivity;

import Model.HouseOwnerForm;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewHoldersTransactions extends RecyclerView.ViewHolder
        implements View.OnClickListener{

    public TextView transactionName,transactionOwnerName,transactionRenteeName;
    public TextView transactionStatus,transactionId;
    public Button transactionacceptButton;


    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public RecyclerViewHoldersTransactions(final View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        transactionName = itemView.findViewById(R.id.transaction_name);
        transactionOwnerName = itemView.findViewById(R.id.transaction_owner_name);
        transactionRenteeName = itemView.findViewById(R.id.transaction_rentee_name);
        transactionStatus = itemView.findViewById(R.id.transaction_status);
        transactionId = itemView.findViewById(R.id.transaction_id);
        transactionacceptButton=itemView.findViewById(R.id.accept_btn);

        transactionacceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(), "Accept Rentee.." , Toast.LENGTH_LONG).show();
            }
        });



    }

    @Override
    public void onClick(final View view) {

        if(view.getId() == R.id.accept_btn)
        {

        }
//
//        RetrofitService mService;
//        mService= ApiUtils.getSOService();
//        mService.viewHouseDetails(Integer.parseInt(houseId.getText().toString())).enqueue(new Callback<HouseOwnerForm>() {
//
//            @Override
//            public void onResponse(Call<HouseOwnerForm> call, Response<HouseOwnerForm> response) {
//
//                if(response.isSuccessful()) {
//                    if(response.body() != null)
//                    {
//                        Toast.makeText(view.getContext(), "Name:" + response.body().getHouse().getHouseName() , Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(view.getContext(),ViewHouseDetailsActivity.class);
//                        intent.putExtra("House", response.body());
//                        view.getContext().startActivity(intent);
//                    }
//                    else
//                    {
//                        Toast.makeText(view.getContext(), "Error has been occured on the server" , Toast.LENGTH_LONG).show();
//
//                    }
//
//                }else {
//                    int statusCode  = response.code();
//                    Toast.makeText(view.getContext(), "Error has been occured on the server:" + statusCode , Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<HouseOwnerForm> call, Throwable t) {
//                Toast.makeText(view.getContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

//        if (selectedItems.get(getAdapterPosition(), false)) {
//            selectedItems.delete(getAdapterPosition());
//            view.setSelected(false);
//            //Toast.makeText(SearchResult2Activity.c, houseName.getText().toString() , Toast.LENGTH_LONG).show();
//        }
//        else {
//            selectedItems.put(getAdapterPosition(), true);
//            view.setSelected(true);
//        }
    }
}