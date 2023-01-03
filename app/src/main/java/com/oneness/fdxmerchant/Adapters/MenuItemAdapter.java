package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.oneness.fdxmerchant.Activity.Order.OrderActivity;
import com.oneness.fdxmerchant.Fragments.BottomSheets.AddItemBottomSheet;
import com.oneness.fdxmerchant.Models.DemoDataModels.MenuItemModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.DeleteItemResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.UpdateResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.AddToCartRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.CartAddResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.CustomCartModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.DeleteCartResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.UpdateCartRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.UpdateCartResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderItemsModels.OrderItemModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.Hold> {

    List<OrderItemModel> mimList;
    List<CustomCartModel> ccList;
    Context context;
    int qty = 0;
    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;
    String cartID = "";


    public MenuItemAdapter(List<OrderItemModel> items, List<CustomCartModel> customCartList, OrderActivity orderActivity) {

        this.context = orderActivity;
        this.mimList = items;
        this.ccList = customCartList;

    }
    //RestaurantDetails restaurantDetails = new RestaurantDetails();
    //RestaurantDetails restaurantDetails;



    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        prefs = new Prefs(context);
        dialogView = new DialogView();
       /* for (int i = 0; i < mimList.size(); i++){
            if (!mimList.get(i).quantity.equals("0")){
                Constants.TAG = Constants.TAG + Integer.parseInt(mimList.get(i).quantity);
                Constants.cartPrice = Constants.cartPrice + Double.parseDouble(mimList.get(i).price);
            }

        }*/

        OrderItemModel mim = mimList.get(position);


        if (mim.quantity.equals("0") && mim.flag == 0){
            holder.addItemLL.setVisibility(View.VISIBLE);
            holder.qtyCountRL.setVisibility(View.GONE);
        }else if(mim.quantity.equals("0") || mim.flag != 0){
            holder.addItemLL.setVisibility(View.GONE);
            holder.qtyCountRL.setVisibility(View.VISIBLE);
            holder.tvQty.setText(String.valueOf(mim.flag));
        }else if (!mim.quantity.equals("0") || mim.flag == 0){

            holder.addItemLL.setVisibility(View.GONE);
            holder.qtyCountRL.setVisibility(View.VISIBLE);
            mim.flag = Integer.parseInt(mim.quantity);
            holder.tvQty.setText(String.valueOf(mim.flag));

        }else {

            holder.addItemLL.setVisibility(View.GONE);
            holder.qtyCountRL.setVisibility(View.VISIBLE);
            mim.flag = Integer.parseInt(mim.quantity);
            holder.tvQty.setText(String.valueOf(mim.flag));

        }

        holder.tvMenuItemName.setText(mim.name);
        holder.tvExtraTxt.setText(mim.description);
        holder.tvPrice.setText("\u20B9 " + mim.price);

       /* if (Integer.parseInt(mim.quantity) > 0){
            mim.flag = Integer.parseInt(mim.quantity);

        }else {
            mim.flag = 0;
        }
        if (mim.flag > 0){
            holder.addItemLL.setVisibility(View.GONE);
            holder.qtyCountRL.setVisibility(View.VISIBLE);
            holder.tvQty.setText(String.valueOf(mim.flag));

        }else{
            holder.addItemLL.setVisibility(View.VISIBLE);
            holder.qtyCountRL.setVisibility(View.GONE);
        }*/

        if (mim.is_veg.equals("0")){
            Glide.with(context).load(R.drawable.non_veg).into(holder.ivItemType);
        }else {
            Glide.with(context).load(R.drawable.veg).into(holder.ivItemType);
        }

        holder.addItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String android_id = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                String img = "";

                if (mim.image != null){
                    if (!mim.image.equals("")){
                        img = "test";
                    }else{
                        img = mim.image;
                    }

                }else{
                    img = "test";
                }

                AddToCartRequestModel addToCartRequestModel = new AddToCartRequestModel(
                        prefs.getData(Constants.SELECTED_USER_ID),
                        android_id,
                        prefs.getData(Constants.REST_ID),
                        mim.id,
                        mim.name,
                        mim.description,
                        img,
                        mim.price,
                        "1",
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

                addToCart(addToCartRequestModel, mim, holder);

                /*AddItemBottomSheet addItemBottomSheet = new AddItemBottomSheet();
                AddItemBottomSheet.itemPrice = mim.price;
                addItemBottomSheet.show(((FragmentActivity)context).getSupportFragmentManager(), "callAddOn");*/


               // restaurantDetails.callAddOn();

            }
        });

        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price = Double.parseDouble(mim.price);
               qty = Integer.parseInt(holder.tvQty.getText().toString());

                String android_id = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                if (mim.cartID.equals("")){
                    for(int i = 0; i < ccList.size(); i++){
                        if (ccList.get(i).product_id.equals(mim.id)){
                            cartID = ccList.get(i).cart_id;
                        }
                    }

                }else {
                    cartID = mim.cartID;
                }

               if (qty > 1){
                   qty = qty - 1;
                   Constants.TAG = Constants.TAG - 1;
                   Constants.cartPrice = Constants.cartPrice - price;
                   holder.tvQty.setText(String.valueOf(qty));
                   String img = "";
                   if (mim.image != null){
                       if (!mim.image.equals("")){
                           img = "test";
                       }else{
                           img = mim.image;
                       }

                   }else{
                       img = "test";
                   }


                   UpdateCartRequestModel updateCartRequestModel = new UpdateCartRequestModel(
                           cartID,
                           android_id,
                           prefs.getData(Constants.REST_ID),
                           mim.id,
                           mim.name,
                           mim.description,
                           img,
                           mim.price,
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

                   updateCart(updateCartRequestModel, mim, "minus");
               }else{
                   Constants.TAG = Constants.TAG - 1;
                   Constants.cartPrice = Constants.cartPrice - price;
                   holder.addItemLL.setVisibility(View.VISIBLE);
                   holder.qtyCountRL.setVisibility(View.GONE);
                   deleteItemFromCart(cartID, mim);
               }
            }
        });

        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String android_id = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                qty = Integer.parseInt(holder.tvQty.getText().toString());
                qty = qty + 1;
                Constants.TAG = Constants.TAG + 1;
                double price = Double.parseDouble(mim.price);
                Constants.cartPrice = Constants.cartPrice + price;
                holder.tvQty.setText(String.valueOf(qty));

                if (mim.cartID.equals("")){
                    for(int i = 0; i < ccList.size(); i++){
                        if (ccList.get(i).product_id.equals(mim.id)){
                            cartID = ccList.get(i).cart_id;
                        }
                    }

                }else {
                    cartID = mim.cartID;
                }

                String img = "";
                if (mim.image != null){
                    if (!mim.image.equals("")){
                        img = "test";
                    }else{
                        img = mim.image;
                    }

                }else{
                    img = "test";
                }

                UpdateCartRequestModel updateCartRequestModel = new UpdateCartRequestModel(
                        cartID,
                        android_id,
                        prefs.getData(Constants.REST_ID),
                        mim.id,
                        mim.name,
                        mim.description,
                        img,
                        mim.price,
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



                updateCart(updateCartRequestModel, mim, "plus");

            }
        });

        if (mim.image != null){
            Glide.with(context).load(mim.image).into(holder.ivFood);

        }else{
            Glide.with(context).load(R.drawable.no_image).into(holder.ivFood);
        }

    }

    private void deleteItemFromCart(String id, OrderItemModel mim) {
        dialogView.showCustomSpinProgress(context);
        manager.service.deleteCartItem(id).enqueue(new Callback<DeleteCartResponseModel>() {
            @Override
            public void onResponse(Call<DeleteCartResponseModel> call, Response<DeleteCartResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    DeleteCartResponseModel dirm = response.body();
                    if (!dirm.error){
                        mim.flag = 0;
                        mim.cartID = "";
                        Toast.makeText(context, "Item removed!", Toast.LENGTH_SHORT).show();
                    }else {

                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<DeleteCartResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private void updateCart(UpdateCartRequestModel updateCartRequestModel, OrderItemModel mim, String from) {
        dialogView.showCustomSpinProgress(context);
        manager.service.updateCartItem(updateCartRequestModel).enqueue(new Callback<UpdateCartResponseModel>() {
            @Override
            public void onResponse(Call<UpdateCartResponseModel> call, Response<UpdateCartResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    UpdateCartResponseModel ucrm = response.body();
                    if (!ucrm.error){
                        //Constants.cartID = ucrm.cart.id;
                        mim.cartID = ucrm.cart.id;
                        if (from.equals("plus")){
                            mim.flag = mim.flag + 1;
                        }else{
                            mim.flag = mim.flag - 1;
                        }
                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<UpdateCartResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();

            }
        });
    }

    private void addToCart(AddToCartRequestModel addToCartRequestModel, OrderItemModel mim, Hold holder) {
        dialogView.showCustomSpinProgress(context);
        manager.service.addItemToCart(addToCartRequestModel).enqueue(new Callback<CartAddResponseModel>() {
            @Override
            public void onResponse(Call<CartAddResponseModel> call, Response<CartAddResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    CartAddResponseModel carm = response.body();
                    if (!carm.error){
                        //Constants.cartID = carm.cart.id;
                        mim.cartID = carm.cart.id;
                        int price = Integer.parseInt(mim.price);
                        Constants.cartPrice = Constants.cartPrice + price;
                        Constants.TAG = Constants.TAG + 1;
                        mim.flag = mim.flag + 1;
                        holder.addItemLL.setVisibility(View.GONE);
                        holder.qtyCountRL.setVisibility(View.VISIBLE);
                    }else {

                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<CartAddResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mimList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvMenuItemName, tvExtraTxt, tvPrice, tvQty;
        LinearLayout addItemLL;
        RelativeLayout qtyCountRL;
        ImageView ivMinus, ivPlus, ivItemType;
        ShapeableImageView ivFood;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvMenuItemName = itemView.findViewById(R.id.tvItemName);
            tvExtraTxt = itemView.findViewById(R.id.tvExtraTxt);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            addItemLL = itemView.findViewById(R.id.addItemLL);
            qtyCountRL = itemView.findViewById(R.id.qtyCountRL);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            tvQty = itemView.findViewById(R.id.tvQty);
            ivFood = itemView.findViewById(R.id.ivFood);
            ivItemType = itemView.findViewById(R.id.ivItemType);

        }
    }
}
