package com.example.project2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ContactItemView extends ConstraintLayout {

    TextView name;
    TextView phoneNumber;

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.contact_layout,this,true);

        // list Item is one image and two name, phoneNumber
        name = findViewById(R.id.contact_list_item_name);
        phoneNumber = findViewById(R.id.contact_list_item_phoneNumber);
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(TextView phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    ////////////////////////////////////////
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
