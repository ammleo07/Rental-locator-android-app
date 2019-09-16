package Util;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectlocator.R;
import com.example.projectlocator.ViewHouseDetailsActivity;

import Model.Comment;
import Model.HouseOwnerForm;
import Model.Transaction;
import Util.Retrofit.ApiUtils;
import Util.Retrofit.RetrofitService;
import Util.Retrofit.RetrofitServiceHouseOwner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewHoldersTransactions extends RecyclerView.ViewHolder
        implements View.OnClickListener{

    public TextView transactionName,transactionOwnerName,transactionRenteeName,transactionRenteeContactNumber;
    public TextView transactionStatus,transactionId, ownerTokenId, renteeTokenId, transactionRenteeSourceOfIncome;
    public Button transactionacceptButton, transactiondeclineButton;


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
        transactiondeclineButton=itemView.findViewById(R.id.decline_btn);
        ownerTokenId=itemView.findViewById(R.id.transaction_owner_token_id);
        renteeTokenId=itemView.findViewById(R.id.transaction_rentee_token_id);
        transactionRenteeContactNumber=itemView.findViewById(R.id.transaction_rentee_contact_number);
        transactionRenteeSourceOfIncome=itemView.findViewById(R.id.transaction_rentee_source_of_income);

        transactionacceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                SharedPreferences sharedpreferences =v.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                if(sharedpreferences.getString("userType",null).equalsIgnoreCase("rentee")) {
                    //Toast.makeText(itemView.getContext(), "Accept Rentee.." , Toast.LENGTH_LONG).show();

                    submitComment(v);

                }
                else
                {
                    sendAcceptToRentee(v);
                    transactionacceptButton.setEnabled(false);
                    transactiondeclineButton.setEnabled(false);
                }
            }
        });

        transactiondeclineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                SharedPreferences sharedpreferences =v.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                if(sharedpreferences.getString("userType",null).equalsIgnoreCase("rentee")) {


                }
                else
                {
                    sendDeclineToRentee(v);
                    transactionacceptButton.setEnabled(false);
                    transactiondeclineButton.setEnabled(false);
                }
            }
        });




    }

    public void sendAcceptToRentee(final View v)
    {
        try {

            SharedPreferences sharedpreferences =v.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            String username=sharedpreferences.getString("username",null);
            Transaction transaction = new Transaction();
            transaction.setId(Integer.parseInt(transactionId.getText().toString()));
            transaction.setHouseOwner(transactionOwnerName.getText().toString().split(":")[1].trim());
            transaction.setOwnerTokenId(ownerTokenId.getText().toString());
            transaction.setRentee(transactionRenteeName.getText().toString().split(":")[1].trim());
            transaction.setRenteeTokenId(renteeTokenId.getText().toString());
            transaction.setSourceOfIncome(transactionRenteeSourceOfIncome.getText().toString().split(":")[1].trim());
            transaction.setRenteeContactNumber(transactionRenteeContactNumber.getText().toString().split(":")[1].trim());
            transaction.setStatus("Accepted");
            transaction.setOrigin("house owner");
            Toast.makeText(v.getContext(), "data: " + transaction.getHouseOwner(), Toast.LENGTH_LONG).show();
            Toast.makeText(v.getContext(), sharedpreferences.getString("username",null) +  " was sending Inquiry..", Toast.LENGTH_LONG).show();
            //SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
            ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
            RetrofitServiceHouseOwner mService= ApiUtils.getHomeOwnerService();
            mService.sendAccepted(transaction).enqueue(new Callback<Transaction>() {

                @Override
                public void onResponse(Call<Transaction> call, Response<Transaction> response) {

                    if(response.isSuccessful() && response.body() != null)
                    {
                        Toast.makeText(v.getContext(), "Accept notification is now sent to rentee", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(v.getContext(), "There is error on sending notification", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<Transaction> call, Throwable t) {
                    Toast.makeText(v.getContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        }
        catch (Exception ex)
        {
            Toast.makeText(v.getContext(), "Error:" + ex.getMessage() , Toast.LENGTH_LONG).show();

        }

    }

    public void sendDeclineToRentee(final View v)
    {
        try {

            SharedPreferences sharedpreferences =v.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            String username=sharedpreferences.getString("username",null);
            Transaction transaction = new Transaction();
            transaction.setId(Integer.parseInt(transactionId.getText().toString()));
            transaction.setHouseOwner(transactionOwnerName.getText().toString().split(":")[1].trim());
            transaction.setOwnerTokenId(ownerTokenId.getText().toString());
            transaction.setRentee(transactionRenteeName.getText().toString().split(":")[1].trim());
            transaction.setRenteeTokenId(renteeTokenId.getText().toString());
            transaction.setRenteeContactNumber(transactionRenteeContactNumber.getText().toString().split(":")[1].trim());
            transaction.setSourceOfIncome(transactionRenteeSourceOfIncome.getText().toString().split(":")[1].trim());
            transaction.setStatus("Declined");
            transaction.setOrigin("house owner");
            Toast.makeText(v.getContext(), "data: " + transaction.getHouseOwner(), Toast.LENGTH_LONG).show();
            Toast.makeText(v.getContext(), sharedpreferences.getString("username",null) +  " was sending Inquiry..", Toast.LENGTH_LONG).show();
            //SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
            ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
            RetrofitServiceHouseOwner mService= ApiUtils.getHomeOwnerService();
            mService.sendAccepted(transaction).enqueue(new Callback<Transaction>() {

                @Override
                public void onResponse(Call<Transaction> call, Response<Transaction> response) {

                    if(response.isSuccessful() && response.body() != null)
                    {
                        Toast.makeText(v.getContext(), "Decline notification is now sent to rentee", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(v.getContext(), "There is error on sending notification", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<Transaction> call, Throwable t) {
                    Toast.makeText(v.getContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        }
        catch (Exception ex)
        {
            Toast.makeText(v.getContext(), "Error:" + ex.getMessage() , Toast.LENGTH_LONG).show();

        }

    }


    public void setStatus(final View v,String status)
    {
        try {

            SharedPreferences sharedpreferences =v.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            String username=sharedpreferences.getString("username",null);
            Transaction transaction = new Transaction();
            transaction.setId(Integer.parseInt(transactionId.getText().toString()));
            transaction.setHouseOwner(transactionOwnerName.getText().toString().split(":")[1].trim());
            transaction.setOwnerTokenId(ownerTokenId.getText().toString());
            transaction.setRentee(transactionRenteeName.getText().toString().split(":")[1].trim());
            transaction.setRenteeTokenId(renteeTokenId.getText().toString());
            transaction.setRenteeContactNumber(transactionRenteeContactNumber.getText().toString());
            transaction.setStatus(status);
            transaction.setOrigin("rentee");
            Toast.makeText(v.getContext(), sharedpreferences.getString("username",null) +  " was confirmed acceptance", Toast.LENGTH_LONG).show();
            //SharedPreferences sharedpreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
            ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
            RetrofitService mService= ApiUtils.getSOService();
            mService.setStatus(transaction).enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if(response.isSuccessful() && response.body().equalsIgnoreCase("success"))
                    {
                        Toast.makeText(v.getContext(), "Transaction is successfully executed", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(v.getContext(), "There is error on accepting confirmation", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(v.getContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        }
        catch (Exception ex)
        {
            Toast.makeText(v.getContext(), "Error:" + ex.getMessage() , Toast.LENGTH_LONG).show();

        }

    }



    public void submitComment(final View v)
    {

        LinearLayout layout = new LinearLayout(v.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setMinimumWidth(100);
        layout.setMinimumWidth(100);

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Send Feedback to Owner");

        final EditText input = new EditText(v.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Your Comment");
        input.setMaxLines(5);
        layout.addView(input);

        final RatingBar rate = new RatingBar(v.getContext());
        rate.setNumStars(5);
        rate.setMax(5);
        layout.addView(rate);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //m_Text = input.getText().toString();
                Comment comment = new Comment();
                comment.setComment(input.getText().toString());
                comment.setHouseOwner(transactionOwnerName.getText().toString().split(":")[1]);
                comment.setRentee(transactionRenteeName.getText().toString().split(":")[1]);
                comment.setTransactionId(Integer.parseInt(transactionId.getText().toString()));
                comment.setRate(Double.parseDouble(rate.getRating()+""));
                saveComment(v,comment);
                setStatus(v, "Sold");
                transactionacceptButton.setEnabled(false);
                //Toast.makeText(v.getContext(), "comment:" + input.getText() + " Rate:" + rate.getRating() , Toast.LENGTH_LONG).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void saveComment(final View v,Comment comment)
    {
        try {

            SharedPreferences sharedpreferences =v.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            String username=sharedpreferences.getString("username",null);
            ApiUtils.BASE_URL="http://" + sharedpreferences.getString("SERVER",null);
            RetrofitService mService= ApiUtils.getSOService();
            mService.saveComment(comment).enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if(response.isSuccessful())
                    {

                    }
                    else
                    {
                        Toast.makeText(v.getContext(), "There is error on saving comment", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(v.getContext(), "Unable to access the server:" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        }
        catch (Exception ex)
        {
            Toast.makeText(v.getContext(), "Error:" + ex.getMessage() , Toast.LENGTH_LONG).show();
        }

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