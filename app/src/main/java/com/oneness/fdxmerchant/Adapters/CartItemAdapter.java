package com.oneness.fdxmerchant.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.Order.CartActivity;
import com.oneness.fdxmerchant.Activity.Order.OrderActivity;
import com.oneness.fdxmerchant.Models.CartModels.CartDataModel;
import com.oneness.fdxmerchant.Models.DemoDataModels.CartItemModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.DeleteCartResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.UpdateCartRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.UpdateCartResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.Hold> {

    List<CartDataModel> ciList;
    Context context;

    ApiManager manager = new ApiManager();
    Prefs prefs;
    DialogView dialogView;

    public CartItemAdapter(CartActivity cartActivity, List<CartDataModel> cartItemList) {
        this.context = cartActivity;
        this.ciList = cartItemList;
    }


    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, @SuppressLint("RecyclerView") int position) {

        prefs = new Prefs(context.getApplicationContext());
        dialogView = new DialogView();

        CartDataModel cim = ciList.get(position);

        holder.tvItemName.setText(cim.product_name);
        //holder.tvExtraTxt.setText();
        holder.tvExtraTxt.setVisibility(View.GONE);
        holder.tvPrice.setText("\u20B9 " + cim.price);
        holder.tvQty.setText(cim.quantity);

        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String android_id = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                double price = Double.parseDouble(cim.price);
                int q = Integer.parseInt(holder.tvQty.getText().toString());
                if (q > 1){
                    q = q-1;
                    Constants.TAG = Constants.TAG - 1;
                    Constants.cartPrice = Constants.cartPrice - price;
                    holder.tvQty.setText(String.valueOf(q));

                    UpdateCartRequestModel updateCartRequestModel = new UpdateCartRequestModel(
                            cim.id,
                            android_id,
                            prefs.getData(Constants.REST_ID),
                            cim.product_id,
                            cim.product_name,
                            cim.product_description,
                            cim.product_image,
                            cim.price,
                            holder.tvQty.getText().toString(),
                            "0",
                            "",
                            "0",
                            "0",
                            "0",
                            "",
                            "0",
                            "0",
                            "0",
                            "0",
                            "0"
                    );

                    updateCartItem(updateCartRequestModel);

                }else{
                    showDeleteAlertPopup(cim.id, position, cim.restaurant_id, price);
                }

            }
        });

        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String android_id = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                int q = Integer.parseInt(holder.tvQty.getText().toString());
                q = q+1;
                holder.tvQty.setText(String.valueOf(q));
                Constants.TAG = Constants.TAG + 1;
                double price = Double.parseDouble(cim.price);
                Constants.cartPrice = Constants.cartPrice + price;

                UpdateCartRequestModel updateCartRequest = new UpdateCartRequestModel(
                        cim.id,
                        android_id,
                        prefs.getData(Constants.REST_ID),
                        cim.product_id,
                        cim.product_name,
                        cim.product_description,
                        cim.product_image,
                        cim.price,
                        holder.tvQty.getText().toString(),
                        "0",
                        "",
                        "0",
                        "0",
                        "0",
                        "",
                        "0",
                        "0",
                        "0",
                        "0",
                        "0"
                );

                updateCartItem(updateCartRequest);



            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price = Double.parseDouble(cim.price);
                showDeleteAlertPopup(cim.id, position, cim.restaurant_id, price);
            }
        });

        /*holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q = Integer.parseInt(holder.tvQty.getText().toString());
                if (q > 1){
                    q = q-1;
                    holder.tvQty.setText(String.valueOf(q));
                }else{
                    Toast.makeText(context, "Can't reduce!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int q = Integer.parseInt(holder.tvQty.getText().toString());
                q = q+1;
                holder.tvQty.setText(String.valueOf(q));
            }
        });*/



    }

    private void showDeleteAlertPopup(String id, int pos, String restId, double price) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.inflate_custom_alert_dialog, null);

        final AlertDialog alertD = new AlertDialog.Builder(context).create();
        TextView tvHeader=(TextView)promptView.findViewById(R.id.tvHeader);
        tvHeader.setText(context.getResources().getString(R.string.app_name));
        TextView tvMsg=(TextView)promptView.findViewById(R.id.tvMsg);
        tvMsg.setText("Are you sure to remove this item ?");
        Button btnCancel = (Button) promptView.findViewById(R.id.btnCancel);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) promptView.findViewById(R.id.btnOk);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteFromCart(id, pos, restId, price);

                alertD.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD.dismiss();

            }
        });

        alertD.setView(promptView);
        try {
            alertD.show();
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }
    }

    private void deleteFromCart(String id, int pos, String restId, double price) {
        dialogView.showCustomSpinProgress(context);
        manager.service.deleteCartItem(id).enqueue(new Callback<DeleteCartResponseModel>() {
            @Override
            public void onResponse(Call<DeleteCartResponseModel> call, Response<DeleteCartResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    DeleteCartResponseModel deleteCartItemResponseModel = response.body();
                    if (deleteCartItemResponseModel.error != true){
                        // context.startActivity(new Intent(context.getApplicationContext(), CartActivity.class));
                        Toast.makeText(context, "Removed successfully!", Toast.LENGTH_SHORT).show();

                        if (ciList.size() > 1){
                            Constants.TAG = Constants.TAG - 1;
                            Constants.cartPrice = Constants.cartPrice - price;
                            ciList.remove(pos);
                            notifyItemRemoved(pos);
                            Intent i = new Intent(((Activity)context), CartActivity.class);
                            ((Activity)context).finish();
                            ((Activity)context).overridePendingTransition(0, 0);
                            ((Activity)context).startActivity(i);
                            ((Activity)context).overridePendingTransition(0, 0);
                        }else{
                            //ciList.remove(pos);
                            Constants.isCartEmpty = true;
                            //context
                            ((Activity)context).startActivity(new Intent(((Activity)context), OrderActivity.class).putExtra(Constants.REST_ID, restId));
                            ((Activity)context).finish();

                        }

                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteCartResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();
                Toast.makeText(context, "Server failed! Please try again!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateCartItem(UpdateCartRequestModel updateCartRequest) {
        dialogView.showCustomSpinProgress(context);
        manager.service.updateCartItem(updateCartRequest).enqueue(new Callback<UpdateCartResponseModel>() {
            @Override
            public void onResponse(Call<UpdateCartResponseModel> call, Response<UpdateCartResponseModel> response) {
                if (response.isSuccessful()){

                    dialogView.dismissCustomSpinProgress();
                    UpdateCartResponseModel crm = response.body();
                    if (crm.error != true){

                        Intent i = new Intent(((Activity)context), CartActivity.class);
                        ((Activity)context).finish();
                        ((Activity)context).overridePendingTransition(0, 0);
                        ((Activity)context).startActivity(i);
                        ((Activity)context).overridePendingTransition(0, 0);


                    }

                }else{
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(context, "ERROR!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateCartResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });
    }



    @Override
    public int getItemCount() {
        return ciList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvItemName, tvExtraTxt, tvPrice, tvQty;
        TextView tvDelete;
        ImageView ivMinus, ivPlus;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvExtraTxt = itemView.findViewById(R.id.tvExtraTxt);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            tvQty = itemView.findViewById(R.id.tvQty);

        }
    }
}
