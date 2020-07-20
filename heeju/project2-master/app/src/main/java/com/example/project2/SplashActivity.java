package com.example.project2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SplashActivity extends Activity {

    int MY_REQUEST_PERMISSIONS = 1234;
    public static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("hamApp Splash", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        ImageView modelView = findViewById(R.id.splash_model);

    }

    @Override
    protected void onResume() {
        Log.d("Splash", "onResume");

        super.onResume();

        if(arePermissionsDenied()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, MY_REQUEST_PERMISSIONS);
        }

        else {
            Log.d("onResume", "no problem");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 다음에 넘어갈 activity 설정 (여기서는 MainActivity.class)
                    Intent intent = new Intent(getApplication(), login_activity.class);

                    intent.putExtra("state", "launch");
                    intent.putExtra("myPermissionCode", MY_REQUEST_PERMISSIONS);
                    intent.putExtra("permissionList", PERMISSIONS);

                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
    }

    // 권한 요청
    public boolean arePermissionsDenied() {

        Log.d("hamApp splash", "arePermissionsDenied");

        for (int i = 0; i < 5; i++) {
            // 5개의 요청사항 중 하나라도 허용하지 않은 상태일 경우
            // fragment에서는 this 대신 getContext()
            if (ContextCompat.checkSelfPermission(this, PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED) {
                Log.d("hamApp Splash", "true");
                return true;
            }
        }
        // 5개 요청사항이 모두 허용된 경우
        Log.d("hamApp Splash", "false");
        return false;
    }

    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    // (requestPermissions 으로) 사용자가 앱 권한 요청에 응답하면
    // 시스템은 앱의 onRequestPermissionsResult() 메서드를 호출하여 사용자 응답을 전달한다.
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {

        Log.d("onRequestPermissionsResult", "");

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_PERMISSIONS && grantResults.length > 0) {
            if(!arePermissionsDenied()) {
                Log.d("hamApp", "permission good");
                // permission이 승인되었다! 유후!
                onResume();
            } else {
                Log.d("hamApp", "permission denied");
                // permission이 거부(denied)...
                // fragment에선 this 대신 getActivity()
                ((ActivityManager) this.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // 계속 묻지 않고 앱종료
                // recreate();
            }
        }
    }
}
