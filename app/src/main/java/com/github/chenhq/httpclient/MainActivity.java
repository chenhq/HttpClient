package com.github.chenhq.httpclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.chenhq.httpclient.service.ServiceRulesException;
import com.github.chenhq.httpclient.service.UserService;
import com.github.chenhq.httpclient.service.UserServiceImpl;
//import com.github.stephanenicolas.loglifecycle.LogLifeCycle;

import java.lang.ref.WeakReference;

//@LogLifeCycle
public class MainActivity extends ActionBarActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private String username;
    private String password;
    private static ProgressDialog prcsDialog;
    private UserService userService = new UserServiceImpl();

    private static class IHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public IHandler(MainActivity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            if (prcsDialog != null) {
                prcsDialog.dismiss();
            }
            int flag = msg.what;
            switch (flag) {
                case 0:
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    ((MainActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case 1:
                    ((MainActivity) mActivity.get()).showTip("Success!!");
                    break;
                default:
                    break;
                    
            }
            // super.handleMessage(msg);
        }
    }

    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private IHandler handler = new IHandler(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();
                Toast.makeText(v.getContext(),
                        "user: " + username + " password: " + password,
                        Toast.LENGTH_SHORT).show();

                /**
                 * input verify
                 */

                /**
                 * loading
                 */

                if (prcsDialog == null) {
                    prcsDialog = new ProgressDialog(MainActivity.this);
                }
                prcsDialog.setTitle("Waiting");
                prcsDialog.setMessage("Login ...");
                prcsDialog.setCancelable(false);
                prcsDialog.show();

                /**
                 * slave thread
                 */
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            userService.userLogin(username, password);
                            handler.sendEmptyMessage(1);
                        } catch (ServiceRulesException e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ErrorMsg", e.getMessage());
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ErrorMsg", "Something error!");
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                });
                thread.start();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
