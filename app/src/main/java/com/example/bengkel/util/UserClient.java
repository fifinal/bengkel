package com.example.bengkel.util;



import android.app.Application;

import com.example.bengkel.model.Account;

public class UserClient extends Application {

    private Account account= null;

    public Account getUser() {
        return account;
    }

    public void setUser(Account user) {
        this.account= account;
    }

}
