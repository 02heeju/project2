package com.example.project2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ContactFragment contactFragment;
    private GalleryFragment galleryFragment;
    private Tab3Fragment tab3Fragment;

    String user_name;
    String user_email;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        user_email = intent.getStringExtra("user_email");

        Log.d("Main activity onCreate intent-passed user name",user_name);
        // Log.d("Main activity onCreate intent-passed user email",user_email);

        tabLayout = findViewById(R.id.tab_layout);

        // Tab Layout 밑에 inflate 해줄, 3개의 Fragment 객체를 생성.
        contactFragment = new ContactFragment(user_name, user_email);
        galleryFragment = new GalleryFragment();
        tab3Fragment = new Tab3Fragment();

        // ViewPager 레이아웃에, Fragment 를 표시할건데, tabLayout 이랑 연결해줘서, swipe 로 fragment 를 전환 할 수 있게해준다.
        viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);

        // 각 fragment 의 layout 을, 자바 객체로 만들어줄 adapter 를 생성한다.
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);

        // Fragment 의 Title 은 어디에 사용되는지 모르겠다.
        viewPagerAdapter.addFragment(contactFragment,"contact");
        viewPagerAdapter.addFragment(galleryFragment,"gallery");
        viewPagerAdapter.addFragment(tab3Fragment,"tab3");
        viewPager.setAdapter(viewPagerAdapter);

        TextView back_to_login_button = findViewById(R.id.back_to_login_button);
        back_to_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                // finish 하기전에 set result 를 하면, login activity 에서 Main Activity 를 부른 것이기 때문에,
                // Login activity : onActivity Result() 함수가 실행이 된다. 거기서 RESULT_OK 값을 받아서 필요한 행동을 할 수 있다.
                // intent.putExtra("result", "success"); // intent 는 필요한데, put extra는 할 필요 없다.
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        // 내가 만든 함수. viewPagerAdapter 은 Main activity 의 onCreate 에서 불린다.
        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        // Extends
        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            // super class 에 list 가 이미 있는 것도 같다.
            // return super.getPageTitle(position);
            return fragmentTitle.get(position);
        }
    }
}