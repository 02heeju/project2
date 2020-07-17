package com.example.project2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ContactFragment extends Fragment {

    private ListView listView;
    private ContactFragment.ContactAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void load_from_DB(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.contact_layout,container,false);

        listView = (ListView) view.findViewById(R.id.contact_list_view);

        adapter = new ContactAdapter();


        return view;
    }

    public class ContactAdapter extends BaseAdapter {

        ArrayList<contact_list_item> items = new ArrayList<contact_list_item>();

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

            // 어탭터 클래스 내부의 items 데이터로부터 리스트의
            // 하나의 아이템에 대한 커스텀 뷰를 만들어서 리턴한다.
            ContactItemView view = new ContactItemView(getActivity());
            contact_list_item item = items.get(position);

            view.setName(item.getName());
            view.setPhoneNumber(item.getPhone_number());
            // view.setImage(item.getPhotoURI());

            return view;
        }
    }
}
