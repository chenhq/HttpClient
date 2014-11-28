package com.github.chenhq.httpclient.service;

import android.util.Log;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by think on 14-11-10.
 */
public class UserServiceImpl implements UserService {

    private static final String TAG = "UserServiceImpl";

    @Override
    public void userLogin(String username, String password) throws Exception {

        Log.i(TAG, username);
        Log.i(TAG, password);

        //Thread.sleep(3000);

        HttpClient client = new DefaultHttpClient();
        String uri = "http://www.baidu.com";
        HttpGet get = new HttpGet(uri);
        ResponseHandler<String> responseHandle = new BasicResponseHandler();

        /**
         * proxy setting
         */

//        HttpHost proxy = new HttpHost("127.0.0.1", 8000);
//        client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);

        String responseBody = client.execute(get, responseHandle);
        Log.i(TAG, responseBody);
//
//        if (username.equals("tom") && password.equals("123")) {
//            Log.i(TAG,"Username and Password all right!");
//
//        } else {
//            throw new ServiceRulesException("UserName or Password Error!");
//        }


    }
}
