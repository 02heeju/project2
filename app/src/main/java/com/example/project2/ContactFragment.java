package com.example.project2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;

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

    public ContactFragment(String user_name, String user_email) {
        Log.d("sequence","Contact Fragment Constructor");
        this.user_name = user_name;
        this.user_email = user_email;
    }

    private ListView listView;
    private ContactFragment.ContactAdapter adapter;

    // DB를 사용하기 위한 변수들
    Retrofit retrofitClient = RetrofitClient.getInstance();

    // 자원 낭비를 막기위한, http 쿼리 한번 던지고 바로 관련된 메모리를 해제하기 위한 객체 선언.
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService = retrofitClient.create(IMyService.class);

    ArrayList<contact_list_item> cloud_items;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 아랫줄 필수.. findViewById를
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.contact_layout,container,false);

        final TextView user_name_on_top = view.findViewById(R.id.contact_layout_title);
        user_name_on_top.setText(user_name);

        listView = view.findViewById(R.id.contact_list_view);

        Button load_from_local_button = view.findViewById(R.id.contact_load_from_local_button);
        load_from_local_button.setOnClickListener(new OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {

                ArrayList<contact_list_item> items = contact_list_item.getLocalContacts(view.getContext());
                Log.e("ContactFragment onCreate",Integer.toString(items.size()) + "개의 아이템이 연락처로부터 로딩 되었습니다");
                Log.e("ContactFragment onCreate",items.toString());

                for (int i=0;i<items.size();i++){
                    adapter.addItem(new contact_list_item(
                            items.get(i).getName(),
                            items.get(i).getPhone_number()));
                }
                listView.setAdapter(adapter);
            }
        });

        // 클라우드에서 불러오기 버튼
        Button load_button = view.findViewById(R.id.contact_load_from_cloud_button);
        load_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(user_name_on_top.getText().toString()))
                {
                    Toast.makeText(getActivity(), "user name be null or empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                compositeDisposable.add(iMyService.get_cloud_contact()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void accept(String response) throws Exception {

                                Log.e("get_cloud_contact result",response);
                                adapter = new ContactAdapter();

                                JSONArray jsonArray = new JSONArray(response);
                                for (int i=0;i<jsonArray.length();i++){
                                    String name = jsonArray.getJSONObject(i).getString("name");
                                    String email = jsonArray.getJSONObject(i).getString("email");

                                    if(name.equals("null") || email.equals("null")) {
                                    }else{
                                        adapter.addItem(new contact_list_item(name, email));
                                        Log.e("end",jsonArray.getJSONObject(i).getString("name")+" "+jsonArray.getJSONObject(i).getString("email"));
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
