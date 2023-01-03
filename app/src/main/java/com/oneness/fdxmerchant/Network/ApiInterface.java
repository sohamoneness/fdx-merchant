package com.oneness.fdxmerchant.Network;

import com.oneness.fdxmerchant.Models.CartModels.CartClearResponseModel;
import com.oneness.fdxmerchant.Models.CartModels.CartDataResponseModel;
import com.oneness.fdxmerchant.Models.CategoryManagementModels.CategoryAddRequestModel;
import com.oneness.fdxmerchant.Models.CategoryManagementModels.CategoryAddResponseModel;
import com.oneness.fdxmerchant.Models.CategoryManagementModels.CategoryDeleteResponseModel;
import com.oneness.fdxmerchant.Models.CategoryManagementModels.CategoryUpdateRequestModel;
import com.oneness.fdxmerchant.Models.CategoryManagementModels.CategoryUpdateResponseModel;
import com.oneness.fdxmerchant.Models.CouponModels.CouponDeleteResponseModel;
import com.oneness.fdxmerchant.Models.CouponModels.CouponResponseModel;
import com.oneness.fdxmerchant.Models.CouponModels.CreateCouponRequestModel;
import com.oneness.fdxmerchant.Models.CouponModels.CreateCouponResponseModel;
import com.oneness.fdxmerchant.Models.CouponModels.UpdateCouponRequestModel;
import com.oneness.fdxmerchant.Models.CouponModels.UpdateCouponResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.AddItemRequestModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.AddItemResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryWiseItemResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.DeleteItemResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.ItemImageResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.ItemsResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.UpdateItemRequestModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.UpdateResponseModel;
import com.oneness.fdxmerchant.Models.LoginModels.LoginRequestModel;
import com.oneness.fdxmerchant.Models.LoginModels.LoginResponseModel;
import com.oneness.fdxmerchant.Models.NotificationModels.NotificationResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.AcceptOrderRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.AcceptOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.CanceledOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.AddToCartRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.CartAddResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.DeleteCartResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.UpdateCartRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.UpdateCartResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.DeliveredOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.NewOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.OngoingOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderDetailsResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderItemsModels.OrderItemRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderItemsModels.OrderItemResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.RejectOrderRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.RejectOrderResponseModel;
import com.oneness.fdxmerchant.Models.PlaceOrderModels.PlaceOrderRequest;
import com.oneness.fdxmerchant.Models.PlaceOrderModels.PlaceOrderResponseModel;
import com.oneness.fdxmerchant.Models.ProfileModels.AvailabilityRequestModel;
import com.oneness.fdxmerchant.Models.ProfileModels.AvailabilityResponseModel;
import com.oneness.fdxmerchant.Models.ProfileModels.DeliveryConfigModel;
import com.oneness.fdxmerchant.Models.ProfileModels.DeliveryConfigResponseModel;
import com.oneness.fdxmerchant.Models.ProfileModels.DeliveryConfigUpdateRequestModel;
import com.oneness.fdxmerchant.Models.ProfileModels.DeliveryConfigUpdateResponseModel;
import com.oneness.fdxmerchant.Models.ProfileModels.SettingsUpdateRequestModel;
import com.oneness.fdxmerchant.Models.ProfileModels.SettingsUpdateResponseModel;
import com.oneness.fdxmerchant.Models.ProfileModels.TimingResponseModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportResponseModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseTransactionReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseTransactionReportResponseModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.ItemReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.ItemReportResponseModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.OrderCountResponseModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.PayoutResponseModel;
import com.oneness.fdxmerchant.Models.RestSearchModels.SearchRequestModel;
import com.oneness.fdxmerchant.Models.RestSearchModels.SearchResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDashboardModels.RestaurantDashboardResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.ChangePasswordRequestModel;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.ChangePasswordResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataUpdateResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataUpdateRequest;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("restaurant-login")
    Call<LoginResponseModel> loginWithEmail(@Body LoginRequestModel loginRequestModel);

    @GET("restaurant-wise-new-orders/{id}")
    Call<NewOrderResponseModel> getNewOrders(@Path("id") String id);

    @GET("restaurant-details/{id}")
    Call<RestaurantDataResponseModel> getRestaurantData(@Path("id") String id);

    @GET("restaurant-wise-ongoing-orders/{id}")
    Call<OngoingOrderResponseModel> getOngoingOrders(@Path("id") String id);

    @GET("restaurant-wise-delivered-orders/{id}")
    Call<DeliveredOrderResponseModel> getDeliveredOrders(@Path("id") String id);

    @GET("restaurant-wise-cancelled-orders/{id}")
    Call<CanceledOrderResponseModel> getCancelledOrders(@Path("id") String id);

    @GET("restaurant-order-details/{id}")
    Call<OrderDetailsResponseModel> getOrderDetails(@Path("id") String id);

    @POST("restaurant-accept-order")
    Call<AcceptOrderResponseModel> acceptNewOrder(@Body AcceptOrderRequestModel acceptOrderRequestModel);

    @POST("restaurant-cancel-order")
    Call<RejectOrderResponseModel> rejectNewOrder(@Body RejectOrderRequestModel rejectOrderRequestModel);

    @GET("restaurant-dashboard/{id}")
    Call<RestaurantDashboardResponseModel> getDashboardData(@Path("id") String id);

    @GET("restaurant/notification/list/{id}")
    Call<NotificationResponseModel> getNotifications(@Path("id") String id);

    @GET("restaurant-wise-categories/{id}")
    Call<CategoryResponseModel> getRestaurantWiseCategories(@Path("id") String id);

    @GET("restaurant-wise-items/{id}")
    Call<ItemsResponseModel> getRestaurantWiseItems(@Path("id") String id);

    @POST("restaurant/item/add")
    Call<AddItemResponseModel> addItems(@Body AddItemRequestModel addItemRequestModel);

    @POST("restaurant/item/update")
    Call<UpdateResponseModel> updateItems(@Body UpdateItemRequestModel updateItemRequestModel);

    @GET("restaurant/item/delete/{id}")
    Call<DeleteItemResponseModel> deleteItem(@Path("id") String id);

    @POST("restaurant/user/find")
    Call<SearchResponseModel> findUsers(@Body SearchRequestModel searchRequestModel);

    @POST("restaurant-items-for-order")
    Call<OrderItemResponseModel> getOrderItems(@Body OrderItemRequestModel orderItemRequestModel);

    @POST("restaurant-cart/add")
    Call<CartAddResponseModel> addItemToCart(@Body AddToCartRequestModel addToCartRequestModel);

    @POST("restaurant-cart/update")
    Call<UpdateCartResponseModel> updateCartItem(@Body UpdateCartRequestModel updateCartRequestModel);

    @GET("restaurant-cart/delete/{id}")
    Call<DeleteCartResponseModel> deleteCartItem(@Path("id") String id);

    @GET("restaurant-cart/list/{id}")
    Call<CartDataResponseModel> getCartList(@Path("id") String id);

    @POST("restaurant/order/create")
    Call<PlaceOrderResponseModel> placeOrder(@Body PlaceOrderRequest placeOrderRequest);

    @GET("restaurant-cart/clear/{id}")
    Call<CartClearResponseModel> clearCart(@Path("id") String id);

    @GET("restaurant/offer/list/{id}")
    Call<CouponResponseModel> getCouponsList(@Path("id") String id);

    @POST("restaurant/offer/create")
    Call<CreateCouponResponseModel> addNewOffers(@Body CreateCouponRequestModel createCouponRequestModel);

    @POST("restaurant/offer/update")
    Call<UpdateCouponResponseModel> updateOffers(@Body UpdateCouponRequestModel updateCouponRequestModel);

    @GET("restaurant/offer/delete/{id}")
    Call<CouponDeleteResponseModel> deleteOffer(@Path("id") String id);

    @POST("restaurant/report/date-wise")
    Call<DateWiseOrderReportResponseModel> getDateWiseOrderReport(@Body DateWiseOrderReportRequestModel dateWiseOrderReportRequestModel);

    @POST("restaurant/report/date-wise-transactions")
    Call<DateWiseTransactionReportResponseModel> getDateWiseTransactionReport(@Body DateWiseTransactionReportRequestModel dateWiseTransactionReportRequestModel);

    @POST("restaurant/report/item-wise")
    Call<ItemReportResponseModel> getItemWiseReport(@Body ItemReportRequestModel itemReportRequestModel);

    @Multipart
    @POST("image.php")
    Call<ItemImageResponseModel> uploadItemImage(@Part MultipartBody.Part file);

    @Multipart
    @POST("restaurant.php")
    Call<ItemImageResponseModel> uploadRestaurantProfileImage(@Part MultipartBody.Part file);

    @POST("restaurant/data/update")
    Call<RestaurantDataUpdateResponseModel> updateRestaurantData(@Body RestaurantDataUpdateRequest restaurantDataUpdateRequest);

    @POST("restaurant/password/update")
    Call<ChangePasswordResponseModel> updatePassword(@Body ChangePasswordRequestModel changePasswordRequestModel);

    @POST("restaurant/category/add")
    Call<CategoryAddResponseModel> addCategory(@Body CategoryAddRequestModel categoryAddRequestModel);

    @POST("restaurant/category/update")
    Call<CategoryUpdateResponseModel> updateCategory(@Body CategoryUpdateRequestModel categoryUpdateRequestModel);

    @GET("restaurant/category/delete/{id}")
    Call<CategoryDeleteResponseModel> deleteCategory(@Path("id") String id);

    @GET("restaurant/category/items/{id}")
    Call<CategoryWiseItemResponseModel> getItemsForCategory(@Path("id") String id);

    @POST("restaurant/settings/update")
    Call<SettingsUpdateResponseModel> updateSettings(@Body SettingsUpdateRequestModel settingsUpdateRequestModel);

    @POST("restaurant/timing/update")
    Call<AvailabilityResponseModel> availabilitySettings(@Body AvailabilityRequestModel availabilityRequestModel);

    @GET("restaurant/timing/{id}")
    Call<TimingResponseModel> getTimings(@Path("id") String id);

    @GET("restaurant/delivery/config/{id}")
    Call<DeliveryConfigResponseModel> getDeliveryConfig(@Path("id") String id);

    @POST("restaurant/delivery/config/update")
    Call<DeliveryConfigUpdateResponseModel> updateDeliveryConfig(@Body DeliveryConfigUpdateRequestModel deliveryConfigUpdateRequestModel);

    @GET("restaurant/transactions/{id}")
    Call<PayoutResponseModel> getPayouts(@Path("id") String id);

    @GET("restaurant/monthly/order/count/{id}")
    Call<OrderCountResponseModel> getOrderCount(@Path("id") String id);

    @GET("restaurant/monthly/sales/{id}")
    Call<OrderCountResponseModel> getSaleReport(@Path("id") String id);

}
