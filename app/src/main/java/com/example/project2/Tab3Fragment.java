package com.example.project2;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Tab3Fragment extends Fragment {

    // 로그인 액티비티로부터 넘겨받은 로그인 정보
    String user_name;
    String user_email;

    // 레트로핏 사용을 위한 변수...
    Retrofit retrofitClient  = RetrofitClient.getInstance();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService = retrofitClient.create(IMyService.class);

    private ListView listView;
    private MenuAdapter adapter;
    Button add_menu_button;
    Button delete_menu_button;

    BottomSheetBehavior bottomSheetBehavior;
    BottomSheetDialog bottomSheetDialog;
    View bottom_sheet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view =  (ViewGroup) inflater.inflate(R.layout.tab3_layout,container,false);

        Log.e("Tab3","onCreateView");
        adapter = new MenuAdapter();
        listView = view.findViewById(R.id.menu_list_view);


//        ImageButton refresh_button = (ImageButton) view.findViewById(R.id.refresh_button);
//        refresh_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // iMyService.registerUser() 함수는 Observable<String> 을 리턴한다.
//                Log.e("메뉴 불러오기","on click");
//
//                compositeDisposable.add(iMyService.load_menu()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(String response) throws Exception {
//                                // 기본적으로 register 한다고, 로그인이 바로 되는건 아닌데,
//                                // 여기에서 loginUser 를 불러주면, 할 수 있긴 하겠다.
//                                // response 를 보고, 성패를 알 수 있다.
//                                Log.d("refresh","메뉴 로딩 시작");
//                                Toast.makeText(getActivity(), response , Toast.LENGTH_SHORT).show();
//
//                                adapter.items.clear();
//
//                                JSONArray jsonArray = new JSONArray(response);
//                                for (int i=0;i<jsonArray.length();i++){
//                                    Log.e("one element" ,jsonArray.get(i).toString());
//
//                                    String owner_name = jsonArray.getJSONObject(i).getString("owner_name");
//                                    String place= jsonArray.getJSONObject(i).getString("place");
//                                    String number_of_people= jsonArray.getJSONObject(i).getString("number");
//                                    String time= jsonArray.getJSONObject(i).getString("time");
//                                    String comment= jsonArray.getJSONObject(i).getString("comment");
//                                    String member= jsonArray.getJSONObject(i).getString("member");
//
//                                    Log.d("check",owner_name);
//                                    Log.d("check",place);
//                                    Log.d("check",number_of_people);
//                                    Log.d("check",time);
//                                    Log.d("check",comment);
//                                    Log.d("check",member);
//
//                                    /*
//                                    예외 처리를 해줘야 하나? 위의 Log.d 를 보고 나서 생각해보자
//                                    if(name.equals("null") || email.equals("null")) {
//                                    }else{
//                                        adapter.addItem(new contact_list_item(name, email));
//                                        Log.e("end",jsonArray.getJSONObject(i).getString("name")+ " " + jsonArray.getJSONObject(i).getString("phone"));
//                                    }
//                                    */
//
//                                   adapter.addItem(new menu_list_item(owner_name, place, number_of_people, time, comment, member));
//
//                                }
//
//                                listView.setAdapter(adapter);
//                                Log.d("refresh","메뉴 로딩, 어댑터 설정까지 완료");
//
//                            }
//                        }));
//            }
//        });

        // 메뉴 추가
        add_menu_button = view.findViewById(R.id.add_button);
        add_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // CREATE new account 를 누르면, 보이는 창을 설정하는 부분이다.
                final View add_menu_layout = LayoutInflater.from(getActivity())
                        .inflate(R.layout.add_menu,null);

                new MaterialStyledDialog.Builder(getActivity())
                        .setIcon(R.drawable.mascot)
                        .setTitle("먹고 싶은 메뉴 추가")
                        .setDescription("자세하게 적어주세요")
                        .setCustomView(add_menu_layout)
                        .setNegativeText("좀더 생각해볼래")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // 새로 계정만드는 창에서 CANCEL 을 누르면, 대화상자를 종료한다.
                                dialog.dismiss();
                            }
                        })

                        // 새로 계정을 만드는 창에서도, 취소를 할수 있고, 필드 입력후 생성을 누를 수도 있다.
                        .setPositiveText("이거다 !!")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                MaterialEditText editText_place = (MaterialEditText) add_menu_layout.findViewById(R.id.add_menu_place);
                                MaterialEditText editText_time = (MaterialEditText) add_menu_layout.findViewById(R.id.add_menu_time);
                                MaterialEditText editText_max_number = (MaterialEditText) add_menu_layout.findViewById(R.id.add_menu_number);
                                MaterialEditText editText_comment = (MaterialEditText) add_menu_layout.findViewById(R.id.add_menu_comment);

                                String place = editText_place.getText().toString();
                                String time = editText_time.getText().toString();
                                String max_number = editText_max_number.getText().toString();
                                String comment = editText_comment.getText().toString();

                                // 빈 항목 확인
                                if(checkEmpty(place))
                                {
                                    Toast.makeText(getActivity(), "음식(음식점)정보는 필수 입력 항목입니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(checkEmpty(time))
                                {
                                    Toast.makeText(getActivity(), "주문 시간 정보는 필수 입력 항목입니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(checkEmpty(max_number))
                                {
                                    Toast.makeText(getActivity(), "모집 인원수를 입력해주십시오.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if(checkEmpty(comment))
                                {
                                    Toast.makeText(getActivity(), "하고 싶은 말을 입력해주십시오.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // 올바른 입력인지 확인.
                                if(CheckNumber(max_number) == false)
                                {
                                    Toast.makeText(getActivity(), "모집 인원수는 적어도.. 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(Integer.parseInt(max_number) > 40)
                                {
                                    Toast.makeText(getActivity(), "사람수가 많네요. 대량주문은 곤란합니다.", Toast.LENGTH_SHORT).show();
                                }

                                // owner name 은 지금 유지하고 있는 user_name 으로 채워질것이고,
                                add_menu_to_cloud(
                                        place,
                                        max_number,
                                        time,
                                        comment);

                                Log.e("add_menu_button","finished");
                            }
                            // 모든 설정을 완료한 후, Dialog(대화상자) 를 띄우는 부분.
                        }).show();
            }
        });

//        // 메뉴 삭제
//        delete_menu_button = view.findViewById(R.id.delete_menu_button);
//        delete_menu_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                delete_menu_from_cloud();
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                menu_list_item item = (menu_list_item) adapter.getItem(position);

                String[] num_people = item.number_of_people.split("=");
                String[] members = item.member.split("=");

                int current_num_people = Integer.parseInt(num_people[0]);
                int maximum_num_people = Integer.parseInt(num_people[1]);

                TextView result_titleview = view.findViewById(R.id.result_title);
                TextView result_sub_titleview = view.findViewById(R.id.result_sub_title);
                ImageView result_imageview = view.findViewById(R.id.result_image);
                // TextView menu_directionview = view.findViewById(R.id.menu_direction);
                TextView buttonCloseview = view.findViewById(R.id.buttonClose);

                buttonCloseview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // dismiss 같은걸 해야할 것 같은데.
                    }
                });

                if(current_num_people < maximum_num_people)
                {
                    result_titleview.setText("현재 참여한 사람");
                    result_imageview.setImageResource(R.drawable.people);
                    result_sub_titleview.setText(item.member.replace('=','\n'));
                    result_sub_titleview.setTextSize(20);
                    result_sub_titleview.setTextColor(0);
                }
                else
                {
                    for (int i=0;i<members.length;i++)
                    {
                        String name = members[i].split("/")[0];
                        String role = members[i].split("/")[1];
                        if(name == user_name)
                        {
                            if(role.equals("d"))
                            {
                                // Im driver
                                result_imageview.setImageResource(R.drawable.driver);
                                result_sub_titleview.setText(user_name + " 님은 “버스 드라이버”입니다.\n 메뉴를 신중하게 골라주십시오.");
                            }else if(role.equals("p"))
                            {
                                result_imageview.setImageResource(R.drawable.payer);
                                result_sub_titleview.setText(user_name + " 님은 “총대”입니다.\n 카드를 준비해 주십시오.");
                            }else
                            {
                                result_imageview.setImageResource(R.drawable.payer);
                                result_sub_titleview.setText(user_name + " 님은 “승객”입니다.\n 남은 음식은 그래도 치워주는 매너.");

                            }
                        }

                    }
                }




            }
        });



        // view may be overlapped here.
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        compositeDisposable.add(iMyService.load_menu()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        // 기본적으로 register 한다고, 로그인이 바로 되는건 아닌데,
                        // 여기에서 loginUser 를 불러주면, 할 수 있긴 하겠다.
                        // response 를 보고, 성패를 알 수 있다.
                        Log.d("refresh","메뉴 로딩 시작");

                        // Toast.makeText(getActivity(), response , Toast.LENGTH_SHORT).show();

                        adapter.items.clear();

                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0;i<jsonArray.length();i++){
                            Log.e("one element" ,jsonArray.get(i).toString());

                            String owner_name = jsonArray.getJSONObject(i).getString("owner_name");
                            String place= jsonArray.getJSONObject(i).getString("place");
                            String number_of_people= jsonArray.getJSONObject(i).getString("number");
                            String time= jsonArray.getJSONObject(i).getString("time");
                            String comment= jsonArray.getJSONObject(i).getString("comment");
                            String member= jsonArray.getJSONObject(i).getString("member");

                            Log.d("check",owner_name);
                            Log.d("check",place);
                            Log.d("check",number_of_people);
                            Log.d("check",time);
                            Log.d("check",comment);
                            Log.d("check",member);

                                    /*
                                    예외 처리를 해줘야 하나? 위의 Log.d 를 보고 나서 생각해보자
                                    if(name.equals("null") || email.equals("null")) {
                                    }else{
                                        adapter.addItem(new contact_list_item(name, email));
                                        Log.e("end",jsonArray.getJSONObject(i).getString("name")+ " " + jsonArray.getJSONObject(i).getString("phone"));
                                    }
                                    */

                            adapter.addItem(new menu_list_item(owner_name, place, number_of_people, time, comment, member));

                        }

                        listView.setAdapter(adapter);
                        Log.d("refresh","메뉴 로딩, 어댑터 설정까지 완료");

                    }
                }));
    }

    private void delete_menu_from_cloud () {

        Log.e("add_menu","함수에 들어왔다.");
        compositeDisposable.add(iMyService.delete_menu(user_name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        // 기본적으로 register 한다고, 로그인이 바로 되는건 아닌데,
                        // 여기에서 loginUser 를 불러주면, 할 수 있긴 하겠다.
                        // response 를 보고, 성패를 알 수 있다.
                        if(response.equals("\"delete success\""))
                        {
                            Log.d("delete menu","메뉴 삭제 완료");
                        }
                        else
                        {
                            Log.d("delete menu","없는 메뉴 삭제 요청");
                        }
                        Toast.makeText(getActivity(), response , Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    // 메뉴추가하는 myServcice 랑, onclick 사이 함수.
    private void add_menu_to_cloud (String place, String number, String time, String comment) {
        Log.e("add_menu","함수에 들어왔다.");
        compositeDisposable.add(iMyService.add_menu(user_name,place,number,time,comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        // 기본적으로 register 한다고, 로그인이 바로 되는건 아닌데,
                        // 여기에서 loginUser 를 불러주면, 할 수 있긴 하겠다.
                        // response 를 보고, 성패를 알 수 있다.
                        Log.d("add_menu","메뉴 추가 완료");
                        Log.d("add_menu", response);
                        Toast.makeText(getActivity(), response , Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    public class MenuAdapter extends BaseAdapter {

        ArrayList<menu_list_item> items = new ArrayList();

        public void addItem(menu_list_item item) { items.add(item); }

        @Override
        public int getCount() { return items.size(); }

        @Override
        public Object getItem(int position) { return items.get(position); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            Log.d("getView",Integer.toString(position) + " 번째 리스트 뷰 아이템 생성");

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.menu_list_item, null);

            menu_list_item item = items.get(position);

            TextView menuView = view.findViewById(R.id.menu);
            // TextView partyView = view.findViewById(R.id.party);
            TextView timeView = view.findViewById(R.id.time);
            TextView owner_nameView = view.findViewById(R.id.owner_name);
            TextView commentView = view.findViewById(R.id.comment);
            Button number_of_peopleView = view.findViewById(R.id.number_of_people);

            owner_nameView.setText(item.owner_name);
            menuView.setText(item.place);
            timeView.setText(item.time);
            commentView.setText(item.comment);

            // 기본적으로는 띄워주고 덮어주자.
            // item 값은 그대로 두자
            number_of_peopleView.setText(item.number_of_people.replace('=','/'));

            String number = item.number_of_people;
            int current_num_people = Integer.parseInt(number.split("=")[0]);
            int max_num_people = Integer.parseInt(number.split("=")[1]);

            if(current_num_people == max_num_people)
                number_of_peopleView.setText("결과 확인");

            return view;
        }

    }

    // CONSTRUCTOR
    public Tab3Fragment(String user_name, String user_email) {
        this.user_name = user_name;
        this.user_email = user_email;
    }


    // UTILITY
    private boolean checkEmpty(String comment) {
        if(TextUtils.isEmpty(comment))
            return true;
        else
            return false;
    }

    public boolean CheckNumber(String str){
        char check;

        if(str.equals(""))
        {
            //문자열이 공백인지 확인
            return false;
        }

        for(int i = 0; i<str.length(); i++){
            check = str.charAt(i);
            if( check < 48 || check > 58)
            {
                //해당 char값이 숫자가 아닐 경우
                return false;
            }

        }
        return true;
    }

}
