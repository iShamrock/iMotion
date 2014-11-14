package com.iMotion;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iMotion.database.User;

public class Login extends Activity {
    /**
     * Called when the activity is first created.
     */
    public static String USER;
    public static String PASSWORD;
    private EditText user;
    private EditText password;
    private Context context = this;
    boolean login;
    private boolean isGuest = true;
    private boolean isNewUser = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);

//        Button login = (Button) findViewById(R.id.login);
//        login.setOnClickListener(new LoginListener());

        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new SignInListener());

        Button enter = (Button) findViewById(R.id.enter);
        enter.setOnClickListener(new LoginListener());

        Button btnAdd = (Button) findViewById(R.id.add);
        btnAdd.setOnClickListener(new AddUserListener());

    }

    private class RegisterListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Checking your name...", Toast.LENGTH_LONG);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean register = Connect.register(new User(user.getText().toString(), password.getText().toString(), "男"));
                    Looper.prepare();
                    if (register) {
                        Toast.makeText(context, "Welcome!", Toast.LENGTH_SHORT).show();
                        USER = user.getText().toString();
                        PASSWORD = password.getText().toString();
                        Intent intent = new Intent();
                        intent.setClass(context, MapView.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "Oops, bad thing, namesake found.", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                }
            }).start();
        }
    }

    private class AddUserListener implements View.OnClickListener {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View view) {
            Button btnAdd = (Button) findViewById(R.id.add);
            LinearLayout lltPassword = (LinearLayout) findViewById(R.id.lltPassword);
            EditText txtUser = (EditText) findViewById(R.id.user);
            EditText txtPassword = (EditText) findViewById(R.id.password);

            if (isGuest) {
                btnAdd.setBackground(getResources().getDrawable(R.drawable.login_remove));
                lltPassword.setVisibility(0);
                txtUser.setText("");
                isGuest = false;
            } else {
                btnAdd.setBackground(getResources().getDrawable(R.drawable.login_add));
                lltPassword.setVisibility(4);
                txtUser.setText("Guest");
                txtPassword.setText("");
                isGuest = true;
            }
        }
    }


    private class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (isGuest) {
                USER = "Guest";
                Intent intent = new Intent();
                intent.setClass(context, MapView.class);
                startActivity(intent);
                finish();
                return;
            }
            Toast.makeText(context, "Checking your key...", Toast.LENGTH_LONG);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    login = Connect.login(user.getText().toString(), password.getText().toString());
                    Looper.prepare();
                    if (login) {
                        Toast.makeText(context, "Welcome!", Toast.LENGTH_SHORT).show();
                        USER = user.getText().toString();
                        PASSWORD = password.getText().toString();
                        Intent intent = new Intent();
                        intent.setClass(context, MapView.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(context, "Oops, seems that you used a wrong key.", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                }
            }).start();

        }
    }

    private class SignInListener implements View.OnClickListener {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View view) {
            Button register = (Button) findViewById(R.id.register);

            Button enter = (Button) findViewById(R.id.enter);

            if (!isNewUser) {
                register.setBackground(getResources().getDrawable(R.drawable.login_haveaccount));
                enter.setBackground(getResources().getDrawable(R.drawable.login_btnsignin));
                isNewUser = true;
                enter.setOnClickListener(new RegisterListener());
            } else {
                register.setBackground(getResources().getDrawable(R.drawable.login_register));
                enter.setBackground(getResources().getDrawable(R.drawable.login_btnlogin));
                isNewUser = false;
                enter.setOnClickListener(new LoginListener());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mapView = menu.add(Menu.NONE, 0, 0, "MapView");
        MenuItem bubbleAdd = menu.add(Menu.NONE, 1, 1, "BubbleAdd");
        MenuItem discuss = menu.add(Menu.NONE, 2, 2, "Discussion");
        MenuItem emotionBox = menu.add(Menu.NONE, 3, 3, "EmotionBox");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        Intent mapView = new Intent();
        mapView.setClass(Login.this, MapView.class);

        Intent bubbleAdd = new Intent();
        bubbleAdd.putExtra("Location_Geo", "复旦大学");
        bubbleAdd.setClass(Login.this, BubbleAdd.class);

        Intent discuss = new Intent();

        discuss.setClass(Login.this, Discussion.class);

        Intent emotionBox = new Intent();
        emotionBox.setClass(Login.this, EmotionBox.class);

        switch (item.getItemId()) {
            case 0:
                startActivity(mapView);
                break;
            case 1:
                startActivity(bubbleAdd);
                break;
            case 2:
                startActivity(discuss);
                break;
            case 3:
                startActivity(emotionBox);
                break;
        }
        return true;
    }
}
