package com.example.project2;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;

public class contact_list_item {

    // 필수 항목
    String name;
    String phone_number;
    String image;
    String favored_color;
    String information_public;

    public contact_list_item(String name, String phone_number, String image) {
        this.name = name;
        this.phone_number = phone_number;
        this.image = image;
    }

    public contact_list_item(String name, String phone_number) {
        this.name = name;
        this.phone_number = phone_number;
    }

    public static ArrayList<contact_list_item> getLocalContacts(Context context){

        ArrayList<contact_list_item> data = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();

        //------------------------------cursor 인자 세팅-------------------------------------------------------------------------------------------------------------
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = { ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                ,  ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                ,  ContactsContract.CommonDataKinds.Phone.NUMBER
                ,  ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        };
        String selectionClause = null;
        String[] selectionArgs = {""};
        String[] sortOrder = null;
        //------------------------------cursor 인자 세팅-------------------------------------------------------------------------------------------------------------

        Cursor cursor = resolver.query(phoneUri, projection, null, null, null);

        if(cursor != null){
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    int idIndex = cursor.getColumnIndex(projection[0]);
                    int nameIndex = cursor.getColumnIndex(projection[1]);
                    int numberIndex = cursor.getColumnIndex(projection[2]);
                    int imageIndex = cursor.getColumnIndex(projection[3]);

                    String id = cursor.getString(idIndex);
                    String name = cursor.getString(nameIndex);
                    String number = cursor.getString(numberIndex);
                    String image = cursor.getString(imageIndex);

                    contact_list_item item = new contact_list_item(name,number,image);
                    data.add(item);
                }

            }else{
                // 쿼리했는데, 아무것도 얻지 못한 경우
                // 에러는 아니지만, 사용자에게 다시 타이핑하거나, 추가하라는 문구를 넣을 수 있겠다.
                Toast.makeText(context,"결과가 없습니다",Toast.LENGTH_LONG);
            }
        }else{
            // Some providers return null if an error occurs, others throw an exception

        }
        // 데이터 계열은 반드시 닫아줘야 한다.
        cursor.close();
        return data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFavored_color() { return favored_color; }

    public void setFavored_color(String favored_color) { this.favored_color = favored_color; }

    public String getInformation_public() { return information_public; }

    public void setInformation_public(String information_public) { this.information_public = information_public; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }
}
