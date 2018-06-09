package com.example.me1438.yoyo3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    //비밀번호 이메일 입력받는 화면
    final int Permission = 123;
    EditText password;
    EditText email;
    Button start;

    MyDatabaseOpenHelper dbHandler;
    Cursor userInfo;
    //다른 class 에서도 쓸 수 있게 static

    static String userPhoneNum = "";  //핸드폰 번호
    static String userPassword;
    static String userEmail;
    static String userIsLocation;
    static String userIsPicture;
    static String userIsAlarm;
    static String userIsSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        dbHandler = new MyDatabaseOpenHelper(this, null, null, 1);
        permission();

        try {
            TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            userPhoneNum = telManager.getLine1Number();
            if (userPhoneNum.startsWith("+82")) {
                userPhoneNum = userPhoneNum.replace("+82", "0");
            }
            Toast.makeText(this, userPhoneNum, Toast.LENGTH_SHORT).show();
        } catch (SecurityException ex) {
            Toast.makeText(this, "예외", Toast.LENGTH_SHORT).show();
        }  //핸드폰 번호 얻기
        userInfo = dbHandler.selectQuery(userPhoneNum);
        if(userInfo.moveToNext()){
            //이미 db에 들어가 있으면
            userPassword = userInfo.getString(1);
            userEmail = userInfo.getString(2);;
            userIsLocation = userInfo.getString(3);
            userIsPicture = userInfo.getString(4);
            userIsAlarm = userInfo.getString(5);
            userIsSensor = userInfo.getString(6);

            Toast.makeText(this, "DB있음", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, ThirdActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "DB없음", Toast.LENGTH_SHORT).show();
        }
        start = (Button) findViewById(R.id.start);
        password = (EditText) findViewById(R.id.password_input);
        email = (EditText) findViewById(R.id.e_mail_input);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() > 0)
                    email.setEnabled(true);
                else
                    email.setEnabled(false);
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() > 0)//한자리라도 입력이 되었으면 활성화
                    start.setEnabled(true);
                else
                    start.setEnabled(false);
            }
        });
    }


    private void permission() {
        if (checkAppPermission(
                new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET})) {

        } else {
            askPermission(new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET}, Permission);
        }
    }

    public void check(View view) {
        String pa = password.getText().toString();
        String em = email.getText().toString();
        //pa랑 em 데이터베이스에 집어넣고 인텐트 시작

        insert(view, pa, em);


        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }

    public void insert(View view, String pw, String email) {
        //일단 권한 허용 된걸로
        //checkPermission.... 함수로 알아내기?
        dbHandler.addProduct(userPhoneNum, pw, email, "1", "1", "1", "1");
    }

    boolean checkAppPermission(String[] requestPermission) {
        boolean[] requestResult = new boolean[requestPermission.length];
        for (int i = 0; i < requestResult.length; i++) {
            requestResult[i] = (ContextCompat.checkSelfPermission(this,
                    requestPermission[i]) == PackageManager.PERMISSION_GRANTED);
            if (!requestResult[i]) {
                return false;
            }
        }
        return true;
    }

    void askPermission(String[] requestPermission, int REQ_PERMISSION) {
        ActivityCompat.requestPermissions(this, requestPermission, REQ_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Permission:
                if (checkAppPermission(permissions)) {
                    // 퍼미션 동의했을 때 할 일 ①
                } else {
                    // 퍼미션 동의하지 않았을 때 할일 ②
                    finish();
                }
                break;
        }
    }
}
