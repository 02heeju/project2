package com.example.project2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

// facebook

public class login_activity extends AppCompatActivity {

    TextView txt_create_account;
    TextView find_password;
    EditText edt_login_email,edt_login_password;
    Button btn_login;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    private String favorite;
    private String theme;
    private String user_email;

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

        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("sequence","onActivityResult");

        // 로그인 성공, 새로운 액티비티 시작
        if (resultCode == RESULT_OK) {
            Log.e("onActivityResult","로그인 시도, 성공한 경우입니당.");

        }else{
            Log.e("onActivityResult","로그인 시도는 했지만, 취소한 경우입니당.");
        }
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
//        edt_login_email = (MaterialEditText) findViewById(R.id.edit_mail);
//        edt_login_password = (MaterialEditText) findViewById(R.id.edit_password);

        edt_login_email = findViewById(R.id.edit_mail);
        edt_login_password = findViewById(R.id.edit_password);

        // 로그인 버튼 리스너 설정
        btn_login = (Button) findViewById(R.id.button_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                loginUser(edt_login_email.getText().toString(),
//                        edt_login_password.getText().toString());
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
                                MaterialEditText edt_register_password = (MaterialEditText) register_layout.findViewById(R.id.register_password);
                                MaterialEditText edt_register_name = (MaterialEditText) register_layout.findViewById(R.id.register_name);
                                Spinner spinner = (Spinner) register_layout.findViewById(R.id.theme_spinner);

                                Log.d("register","email: " + edt_register_email.getText());
                                Log.d("register","pass: " + edt_register_password.getText());
                                Log.d("register","name: " + edt_register_name.getText());

                                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                        R.array.theme, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);

                                final String[] theme = new String[1];

                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        theme[0] = (String) parent.getItemAtPosition(position);
                                        Log.d("theme",theme[0]);
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                if(TextUtils.isEmpty(edt_register_name.getText().toString()))
                                {
                                    Toast.makeText(login_activity.this, "Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(TextUtils.isEmpty(edt_register_email.getText().toString()))
                                {
                                    Toast.makeText(login_activity.this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(TextUtils.isEmpty(edt_register_password.getText().toString()))
                                {
                                    Toast.makeText(login_activity.this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                registerUser(
                                        edt_register_email.getText().toString(),
                                        edt_register_password.getText().toString(),
                                        edt_register_name.getText().toString());

                            }
                        // 모든 설정을 완료한 후, Dialog(대화상자) 를 띄우는 부분.
                        }).show();
            }
        });

        // 비밀번호 찾기
        // CREATE new account 버튼 리스너 설정
        find_password = (TextView) findViewById(R.id.find_password);
        find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View find_password_view = LayoutInflater.from(login_activity.this)
                        .inflate(R.layout.password_find_layout, null);

                // CREATE new account 를 누르면, 보이는 창을 설정하는 부분이다.
                new MaterialStyledDialog.Builder(login_activity.this)
                        .setIcon(R.drawable.ic_password)
                        .setTitle("Registration")
                        .setDescription("Please fill all fields")
                        .setCustomView(find_password_view)
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
                                MaterialEditText find_password_name_text = (MaterialEditText) find_password_view.findViewById(R.id.find_password_name);
                                MaterialEditText find_password_email_text = (MaterialEditText) find_password_view.findViewById(R.id.find_password_email);
                                Log.d("register","name" + find_password_name_text.getText());
                                Log.d("register","email" + find_password_email_text.getText());


                                if(TextUtils.isEmpty(find_password_name_text.getText().toString()))
                                {
                                    Toast.makeText(login_activity.this, "Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(TextUtils.isEmpty(find_password_email_text.getText().toString()))
                                {
                                    Toast.makeText(login_activity.this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                find_password_with_name_email(
                                        find_password_name_text.getText().toString(),
                                        find_password_email_text.getText().toString());

                            }
                            // 모든 설정을 완료한 후, Dialog(대화상자) 를 띄우는 부분.
                        }).show();
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void find_password_with_name_email(String name, String email) {
        Log.d("find_password_with_name_email","entered");

        // iMyService.registerUser() 함수는 Observable<String> 을 리턴한다.
        compositeDisposable.add(iMyService.find_password(name,email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturnItem("find password failed")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        // 기본적으로 register 한다고, 로그인이 바로 되는건 아닌데,
                        // 여기에서 loginUser 를 불러주면, 할 수 있긴 하겠다.
                        Log.d("accept",response);
                        Toast.makeText(login_activity.this, "비밀번호를 찾았습니다", Toast.LENGTH_SHORT).show();
                        Toast.makeText(login_activity.this, response, Toast.LENGTH_LONG).show();
                        // loginUser(email, password);
                    }
                }));

    }

    // 자원 낭비를 막기 위해 composite Disposable 을 사용한다.
    // disposable 이 일회용이라는 뜻인데, 사용후 바로 free 해주는 기능이 있다.
    private void registerUser(final String email, String password, final String name) {

        // iMyService.registerUser() 함수는 Observable<String> 을 리턴한다.
        compositeDisposable.add(iMyService.create_account(email,password,name,favorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturnItem("registerFail")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        // 기본적으로 register 한다고, 로그인이 바로 되는건 아닌데,
                        // 여기에서 loginUser 를 불러주면, 할 수 있긴 하겠다.
                        Log.d("accept",response);
                        Toast.makeText(login_activity.this, response, Toast.LENGTH_SHORT).show();
                        // loginUser(email, password);
                    }
                }));
    }

    private void loginUser(String email, String password) {
        Log.e("loginUser:user()","ENTER");

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

        compositeDisposable.add(iMyService.login(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturnItem("error")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                        String[] login_result = response.split("/");

                        Log.e("response",response);
                        if(login_result[0].equals("\"Login Success")){
                            String name = login_result[1];
                            String theme = login_result[2];
                            Log.e("name",name);
                            Log.e("theme",theme);

                            Intent intent = new Intent(login_activity.this,MainActivity.class);

                            // 이름은 가져와야 하고,
                            // 이메일은 방금 넘겨주면서 그냥 알고있고
                            // theme 은 가져와야 한다.
                            intent.putExtra("user_name",name);
                            intent.putExtra("user_email",user_email);
                            intent.putExtra("theme",theme);

                            startActivity(intent);
                        }else{
                            Toast.makeText(login_activity.this, login_result[0], Toast.LENGTH_SHORT).show();

                        }
                    }
                })

        );
    }

    public class LoginCallback implements FacebookCallback<LoginResult> {

        String FB_name;
        String FB_email;

        // 로그인 성공 시 호출 됩니다. Access Token 발급 성공.
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.e("sequence","onSuccess");
            requestMe(loginResult.getAccessToken());
        }

        // 로그인 창을 닫을 경우, 호출됩니다.
        @Override
        public void onCancel() {
            Log.e("Callback :: ", "onCancel 로그인 성공은 아니고 그냥 닫은 경우입니당");
        }

        // 로그인 실패 시에 호출됩니다.
        @Override
        public void onError(FacebookException error) {
            Log.e("Callback :: ", "onError : " + error.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe(AccessToken token) {
            Log.e("request","1");
            Log.e("AccessToken", token.toString());
            GraphRequest graphRequest = GraphRequest.newMeRequest(token,
                    new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.e("sequence","onCompleted");

                            /*
                            graph API 결과로 받은 object 랑 response 객체 결과 확인.
                            try {
                                Log.e("object",object.getString("name"));
                                Log.e("response",object.getString("email"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            */

                            try {

                                JSONObject jsonObject = (JSONObject) object;
                                FB_name = (String) jsonObject.get("name");
                                FB_email = (String) jsonObject.get("email");

                                Log.e("FB_name",FB_name);
                                Log.e("FB_email",FB_email);

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                intent.putExtra("user_name", FB_name);
                                intent.putExtra("user_email", FB_email);
                                intent.putExtra("user_email", "default"); // 페이스북으로 로그인 할 경우, 기본 theme 제공
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            graphRequest.setParameters(parameters);
            graphRequest.executeAsync();
        }
    }
}