package com.oneness.fdxmerchant.Network;

import com.oneness.fdxmerchant.Models.DemoDataModels.OrderModel;
import com.oneness.fdxmerchant.Models.RestSearchModels.UserAddressModel;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static String BASE_URL = "https://demo91.co.in/dev/fdx/public/api/";
    public static String IMG_BASE_URL = "https://demo91.co.in/dev/fdx/public/";
    //public static String BASE_URL = "https://13.127.50.16/api/";

    //Pref keys
    public static String REST_ID = "REST_ID";
    public static String REST_NAME = "REST_NAME";
    public static String REST_EMAIL = "REST_EMAIL";
    public static String REST_MOBILE = "REST_MOBILE";
    public static String REST_ADR = "REST_ADR";
    public static String REST_LOC = "REST_LOC";
    public static String REST_TAKING = "REST_TAKING";
    public static String REST_OPEN = "REST_OPEN";
    public static String REST_CLOSE = "REST_CLOSE";
    public static String REST_BANNER = "REST_BANNER";
    public static String REST_LOGO = "REST_LOGO";
    public static String REFRESH_TOKEN = "REFRESH_TOKEN";
    public static String CAT_ID = "CAT_ID";
    public static String SELECTED_USER_ID = "SELECTED_USER_ID";
    public static String COUPON_ID = "COUPON_ID";
    public static String ORDER_SIZE = "ORDER_SIZE";


    //Custom variables
    public static int TAG = 0;
    public static double cartPrice = 0;
    public static String cartID = "";
    public static boolean isRevTabSelected = false;
    public static List<OrderModel> orderList = new ArrayList<>();
    public static String isFrom = "";
    public static int isNewOrder = 0;
    public static int orderSize = 0;
    public static boolean isCartEmpty = false;
    public static boolean addressSelected = false;
    public static List<UserAddressModel> userAddressList = new ArrayList<>();
    public static UserAddressModel userAddressModel;
    public static String userName = "";
    public static String userMobile = "";
    public static String userEmail = "";
    public static boolean isNewCouponAdded = false;
    public static boolean isCouponDeleted = false;
    public static boolean isFromCouponEdit = false;
    public static boolean fromAddItem = false;
    public static boolean needToRefresh = false;
    //public static  addressListModel;
}
