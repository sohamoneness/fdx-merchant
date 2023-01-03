package com.oneness.fdxmerchant.Models.LoginModels;

public class LoginRequestModel {
    public String mobile = "";
    public String password = "";

    public LoginRequestModel(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }
}
