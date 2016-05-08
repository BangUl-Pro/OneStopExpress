package com.ironfactory.first_express.networks;

/**
 * Created by IronFactory on 2016. 1. 12..
 */
public class RequestListener {

    public interface OnInsertMove {
        void onSuccess();
        void onException();
    }

    public interface OnSignUp {
        void onSuccess(String name, String phoneNum);
        void onException(int code);
    }

    public interface OnLogin {
        void onSuccess(String name, String phoneNum);
        void onException();
    }
}
