package com.example.project2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ContactFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Below my implementation

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

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.contact_layout,container,false);

        // 로그인 한 사람의 이름이 가장 위에 뜰 수 있도록 하자.
        final TextView user_name_on_top = view.findViewById(R.id.contact_layout_title);
        user_name_on_top.setText(user_name);

        listView = view.findViewById(R.id.contact_list_view);

        // 클라우드에서 불러오기 버튼
        Button load_button = (Button) view.findViewById(R.id.contact_load_from_cloud_button);
        load_button.setOnClickListener(new View.OnClickListener() {
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

        public void addItem(contact_list_item item) {
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            // 필터링을 하면 Item ID랑 position 이랑 다를 수는 있지만 여기서는 아니다.
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            // 인자중에, viewGroup 이랑, convertView 는 어디에 사용되는 애인지 잘 모르겠다.

            View v = convertView;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.contact_list_item_layout, null);

            // 어탭터 클래스 내부의 items 데이터로부터 리스트의
            // 하나의 아이템에 대한 커스텀 뷰를 만들어서 리턴한다.
            // ContactItemView view = new ContactItemView(getActivity());
            Log.e("position",Integer.toString(position));
            contact_list_item item = items.get(position);

            Log.e("what's matter",item.name);
            Log.e("what's matter",item.phone_number);

            TextView name = v.findViewById(R.id.contact_list_item_name);
            TextView phoneNumber = v.findViewById(R.id.contact_list_item_phoneNumber);

            name.setText(item.name);
            phoneNumber.setText(item.phone_number);

            return v;
        }
    }
}
