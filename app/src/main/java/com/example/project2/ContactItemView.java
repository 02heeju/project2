package com.example.project2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ContactItemView extends ConstraintLayout {

    // 여기는 필드에 String 이 아닌, View 가 있다.
    // 어댑터가, getView 에서, 리스트의 한 항목에 대한 view 를 contact_list_item 객체로 부터 생성해서, 화면에 전달한다.

    TextView name;
    TextView phoneNumber;
    ImageView image;

    public ImageView getImage() {
        return image;
    }

    // Image 처리는 나중에 하자.
    // public void setImage(int resId) {
    public void setImage(Drawable drawable) {
        // this.image.setImageBitmap();
        // this.image.setImageResource(resId);
        this.image.setImageDrawable(drawable);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.contact_layout,this,true);

        // list Item is one image and two name, phoneNumber
        name = (TextView) findViewById(R.id.contact_list_item_name);
        phoneNumber = (TextView) findViewById(R.id.contact_list_item_phoneNumber);
        image = (ImageView) findViewById(R.id.contact_list_item_image);
    }

    public TextView getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public TextView getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.name.setText(phoneNumber);
    }

    // adapter 에서,
    // 생성자는, 인자 없는 애만 불린다. 아래 3개의 생성자는 불리지 않는다.
    public ContactItemView(Context context) {
        super(context);
    }

    public ContactItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
