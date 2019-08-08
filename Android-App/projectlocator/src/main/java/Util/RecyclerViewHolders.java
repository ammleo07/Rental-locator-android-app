package Util;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectlocator.LoginActivity;
import com.example.projectlocator.R;
import com.example.projectlocator.SearchHouseParamActivity;
import com.example.projectlocator.SearchResult2Activity;
import com.example.projectlocator.ViewHouseDetailsActivity;

import java.io.Serializable;
import java.util.List;

import Model.House;
import Model.HouseOwnerForm;
import Model.SearchCriteria;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewHolders extends RecyclerView.ViewHolder
        implements View.OnClickListener{

    public TextView houseId, houseName;
    public TextView houseType, boardertype;
    public TextView housePrice,houseAddress;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        houseName = (TextView)itemView.findViewById(R.id.result_house_name);
        houseType = (TextView)itemView.findViewById(R.id.result_house_type);
        housePrice = (TextView)itemView.findViewById(R.id.result_house_price);
        houseAddress =(TextView)itemView.findViewById(R.id.result_house_address);
        houseId =(TextView)itemView.findViewById(R.id.result_house_id);
        boardertype=(TextView)itemView.findViewById(R.id.result_house_boarder_type);

    }

    @Override
    public void onClick(final View view) {

        Log.i("selected",houseName.getText().toString());
        //Intent intent = new Intent(view.getContext(), ViewHouseDetailsActivity.class);
        //view.getContext().startActivity(intent);

        RetrofitService mService;
        SharedPreferences sharedpreferences =view.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
        mService= ApiUtils.getSOService();
        mService.viewHouseDetails(Integer.parseInt(houseId.getText().toString())).enqueue(new Callback<HouseOwnerForm>() {

            @Override
            public void onResponse(Call<HouseOwnerForm> call, Response<HouseOwnerForm> response) {

                if(response.isSuccessful()) {
                    if(response.body() != null)
                    {
                        Toast.makeText(view.getContext(), "Name:" + response.body().getHouse().getHouseName() , Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(view.getContext(),ViewHouseDetailsActivity.class);
                        intent.putExtra("House", response.body());
                        view.getContext().startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(view.getContext(), "Error has been occured on the server" , Toast.LENGTH_LONG).show();

                    }

                }else {
                    int statusCode  = response.code();
                    Toast.makeText(view.getContext(), "Error has been occured on the server:" + statusCode , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<HouseOwnerForm> call, Throwable t) {
                Toast.makeText(view.getContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

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