package com.example.project2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ContactFragment extends Fragment {

    // 로그인 액티비티로부터 넘겨받은 로그인 정보
    String user_name;
    String user_email;

    // 레트로핏 사용을 위한 변수...
    Retrofit retrofitClient  = RetrofitClient.getInstance();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService = retrofitClient.create(IMyService.class);

    public ContactFragment(String user_name, String user_email) {
        Log.d("sequence","Contact Fragment Constructor");
        this.user_name = user_name;
        this.user_email = user_email;
    }

    private ListView listView;
    private ContactAdapter adapter;

    // 클라우드에 채우기
    private void fill_cloud(final String name, final String phone_number) {

        // iMyService.registerUser() 함수는 Observable<String> 을 리턴한다.
        Log.e("add_contact","user_name: " + user_name);
        Log.e("add_contact","name: " + name);
        Log.e("add_contact","phone_number: " + phone_number);

        compositeDisposable.add(iMyService.add_entry(user_name,name,phone_number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        // 기본적으로 register 한다고, 로그인이 바로 되는건 아닌데,
                        // 여기에서 loginUser 를 불러주면, 할 수 있긴 하겠다.
                        // response 를 보고, 성패를 알 수 있다.
                        Log.d("add_contact","연락처 추가 완료");
                        Toast.makeText(getActivity(), response , Toast.LENGTH_SHORT).show();
                    }
                }));
    }


    // 자원 낭비를 막기 위해 composite Disposable 을 사용한다.
    // 추가 버튼
    private void add_contact(final String name, final String phone_number) {

        // iMyService.registerUser() 함수는 Observable<String> 을 리턴한다.
        Log.e("add_contact","user_name: " + user_name);
        Log.e("add_contact","name: " + name);
        Log.e("add_contact","phone_number: " + phone_number);

        compositeDisposable.add(iMyService.add_entry(user_name,name,phone_number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        // 기본적으로 register 한다고, 로그인이 바로 되는건 아닌데,
                        // 여기에서 loginUser 를 불러주면, 할 수 있긴 하겠다.
                        // response 를 보고, 성패를 알 수 있다.
                        Log.d("add_contact","연락처 추가 완료");
                        Toast.makeText(getActivity(), response , Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // 아랫줄 필수.. findViewById를
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.contact_layout,container,false);

        final TextView user_name_on_top = view.findViewById(R.id.contact_layout_title);
        user_name_on_top.setText(user_name);

        adapter = new ContactAdapter();
        listView = view.findViewById(R.id.contact_list_view);

        ImageButton buttonShow = view.findViewById(R.id.buttonShow);
        buttonShow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getActivity(),R.style.BottomSheDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(
                                R.layout.layout_bottom_sheet,
                                (LinearLayout) getActivity().findViewById(R.id.bottomSheetContainer)
                        );
                // 컨택트 하나 추가하는 버튼
                // CREATE new account 버튼 리스너 설정
                // Button add_to_cloud_button = view.findViewById(R.id.contact_add);

                LinearLayout add_layout = bottomSheetView.findViewById(R.id.contact_add);
                add_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final View add_layout = LayoutInflater.from(getActivity()) // changed here
                                .inflate(R.layout.add_layout, null);

                        // CREATE new account 를 누르면, 보이는 창을 설정하는 부분이다.
                        new MaterialStyledDialog.Builder(getActivity()) // changed here
                                .setIcon(R.drawable.ic_launcher_background)
                                .setTitle("아는 사람 추가")
                                .setDescription("항목을 채워주세요")
                                .setCustomView(add_layout)
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

                                        MaterialEditText editText_add_name = add_layout.findViewById(R.id.add_name);
                                        MaterialEditText editText_add_phone_number = add_layout.findViewById(R.id.add_phone);

                                        String target_name = editText_add_name.getText().toString();
                                        String target_phone_number = editText_add_phone_number.getText().toString();

                                        Log.d("add", "name: " + target_name);
                                        Log.d("add", "phone number: " + target_phone_number);

                                        if (TextUtils.isEmpty(target_name)) {
                                            Toast.makeText(getActivity(), "Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (TextUtils.isEmpty(target_phone_number)) {
                                            Toast.makeText(getActivity(), "phone number cannot be null or empty", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        add_contact(target_name,target_phone_number);

                                    }
                                    // 모든 설정을 완료한 후, Dialog(대화상자) 를 띄우는 부분.
                                }).show();
                    }
                });

                //bottomSheetView.findViewById(R.id.buttonShow)
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        // 로컬 연락처에서 리스트뷰 채우기
        ImageButton load_from_local_button = view.findViewById(R.id.phone_to_screen);
        load_from_local_button.setOnClickListener(new OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {

                adapter.items.clear();

                ArrayList<contact_list_item> items = contact_list_item.getLocalContacts(view.getContext());
                Log.e("ContactFragment onCreate",Integer.toString(items.size()) + "개의 아이템이 연락처로부터 로딩 되었습니다");
                Log.e("ContactFragment onCreate",items.toString());

                for (int i=0;i<items.size();i++){
                    Log.e("ContactFragment onCreate",items.get(i).toString());
                    adapter.addItem(new contact_list_item(
                            items.get(i).getName(),
                            items.get(i).getPhone_number()));
                }
                listView.setAdapter(adapter);
            }
        });

        // Cloud 내용들로 화면 띄우는 것.
        ImageButton load_button = view.findViewById(R.id.cloud_to_screen);
        load_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(user_name_on_top.getText().toString()))
                {
                    Toast.makeText(getActivity(), "user name be null or empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                compositeDisposable.add(iMyService.get_cloud_contact(user_name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void accept(String response) throws Exception {

                                Log.e("get_cloud_contact result",response);
                                adapter.items.clear();

                                JSONArray jsonArray = new JSONArray(response);
                                for (int i=0;i<jsonArray.length();i++){
                                    Log.e("one element" ,jsonArray.get(i).toString());
                                    String name = jsonArray.getJSONObject(i).getString("name");
                                    String email = jsonArray.getJSONObject(i).getString("phone");

                                    if(name.equals("null") || email.equals("null")) {
                                    }else{
                                        adapter.addItem(new contact_list_item(name, email));
                                        Log.e("end",jsonArray.getJSONObject(i).getString("name")+ " " + jsonArray.getJSONObject(i).getString("phone"));
                                    }

                                }
                                listView.setAdapter(adapter);
                                Log.e("adapter","FIN");

                            }
                        }));
            }
        });

        // 처음에는 연락처에 있는 정보들을 긁어와서 보여준다.
        // load 클릭 시에는 클라우드에 있는 정보들을 긁어와서 보여줘야 할 것.
        return view;
    }

    // 로그인 후에, 이름을 주면, 이름과 관련된 collection 에 들어가서 모든 document 를 긁어오는 행위를 한다.



    public class ContactAdapter extends BaseAdapter {

        ArrayList<contact_list_item> items = new ArrayList();

        public void addItem(contact_list_item item) { items.add(item); }

        @Override
        public int getCount() { return items.size(); }

        @Override
        public Object getItem(int position) { return items.get(position); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            Log.d("getView",Integer.toString(position) + " 번째 리스트 뷰 아이템 생성");

            // convertView 나 viewGroup 이나 사용되지 않는다.
            // position 으로, 리스트의 position 번째 아이템의 정보들을 가지고,
            // 뷰를 구성해주고, 화면에 추가하고 리턴해줄것이다.

            // 현재 화면의 inflater 에 contact_list_item_layout 을 추가하여 만든 새로운 view 를 리턴할 것이다.

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.contact_list_item_layout, null);

            contact_list_item item = items.get(position);

            TextView name = view.findViewById(R.id.contact_list_item_name);
            TextView phoneNumber = view.findViewById(R.id.contact_list_item_phoneNumber);

            name.setText(item.name);
            phoneNumber.setText(item.phone_number);

            return view;
        }

    }


}
