package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

// facebook
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

public class login_activity extends AppCompatActivity {

    TextView txt_create_account;
    MaterialEditText edt_login_email,edt_login_password;
    Button btn_login;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    private LoginButton facebook_login_button;
    private LoginCallback mLoginCallback;
    private CallbackManager mCallbackManager;


    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Intent argument 가 있는 이유 : 결과 콜백은 프로세스 및 활동을 다시 만들 때 사용할 수 있어야 하므로
        Log.e("onActivityResult Argument requestCode",Integer.toString(requestCode));
        Log.e("onActivityResult Argument resultCode",Integer.toString(resultCode));
        Log.e("onActivityResult Argument data",data.toString());
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // 로그인 성공, 새로운 액티비티 시작
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            // 현재 액티비티를 종료..하지는 않는게 좋을것 같다?
        }else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // FaceBook Login
        // m CallbackManager 는 로그인 응답을 처리해주는 콜백 관리자이다.
        mCallbackManager = CallbackManager.Factory.create();
        mLoginCallback = new LoginCallback();

        facebook_login_button = (LoginButton) findViewById(R.id.btn_facebook_login);
        facebook_login_button.registerCallback(mCallbackManager, mLoginCallback); // 로그인 응답을 받을경우, m Login Callback 객체의 함수를 실행함! 또 다른 자바파일에 정의되어있음.
        // 버전 낮다고 함. btn_facebook_login.setReadPermissions(Arrays.asList("public_profile", "email"));


        // DB를 이용하여 로그인
        // Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        // 로그인 화면의 이메일, 패스워드 창 xml 로부터 자바 객체 만들기.
        edt_login_email = (MaterialEditText) findViewById(R.id.edit_mail);
        edt_login_password = (MaterialEditText) findViewById(R.id.edit_password);

        // 로그인 버튼 리스너 설정
        btn_login = (Button) findViewById(R.id.button_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(edt_login_email.getText().toString(),
                        edt_login_password.getText().toString());
            }
        });

        // CREATE new account 버튼 리스너 설정
        txt_create_account = (TextView) findViewById(R.id.txt_create_account);
        txt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View register_layout = LayoutInflater.from(login_activity.this)
                        .inflate(R.layout.register_layout, null);

                // CREATE new account 를 누르면, 보이는 창을 설정하는 부분이다.
                new MaterialStyledDialog.Builder(login_activity.this)
                        .setIcon(R.drawable.ic_launcher_background)
                        .setTitle("Registration")
                        .setDescription("Please fill all fields")
                        .setCustomView(register_layout)
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // 새로 계정만드는 창에서 CANCEL 을 누르면, 대화상자를 종료한다.
                                dialog.dismiss();
                            }
                        })

                        // 새로 계정을 만드는 창에서도, 취소를 할수 있고, 필드 입력후 생성을 누를 수도 있다.
                        .setPositiveText("REGISTER")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText edt_register_email = (MaterialEditText) register_layout.findViewById(R.id.register_email);
                                MaterialEditText edt_register_name = (MaterialEditText) register_layout.findViewById(R.id.register_name);
                                MaterialEditText edt_register_password = (MaterialEditText) register_layout.findViewById(R.id.register_password);

                                if(TextUtils.isEmpty(edt_register_email.getText().toString()))
                                {
                                    Toast.makeText(login_activity.this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(TextUtils.isEmpty(edt_register_name.getText().toString()))
                                {
                                    Toast.makeText(login_activity.this, "Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(TextUtils.isEmpty(edt_register_password.getText().toString()))
                                {
                                    Toast.makeText(login_activity.this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                registerUser(
                                        edt_register_email.getText().toString(),
                                        edt_register_name.getText().toString(),
                                        edt_register_password.getText().toString());

                            }
                        // 모든 설정을 완료한 후, Dialog(대화상자) 를 띄우는 부분.
                        }).show();
            }
        });
    }


    // 자원 낭비를 막기 위해 composite Disposable 을 사용한다.
    // disposable 이 일회용이라는 뜻인데, 사용후 바로 free 해주는 기능이 있다.
    private void registerUser(String email, String name, String password) {

        // iMyService.registerUser() 함수는 Observable<String> 을 리턴한다.
        compositeDisposable.add(iMyService.registerUser(email,name,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(login_activity.this, "" + response + "what?", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void loginUser(String email, String password) {
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        compositeDisposable.add(iMyService.loginUser(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        if(response.equals("\"Wrong password\"")){
                            Toast.makeText(login_activity.this, "login fail", Toast.LENGTH_SHORT).show();
                            Toast.makeText(login_activity.this, "try again", Toast.LENGTH_LONG).show();
                        }else if (response.equals("\"Login success\"")){
                            Toast.makeText(login_activity.this, "login success", Toast.LENGTH_LONG).show();

                            // DB 에서 email 을 찾고, 비밀번호를 대조해서, 로그인에 성공!
                            // 새로운 액티비티 실행~
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }));
    }
}