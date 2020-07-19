package com.example.project2;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class contact_list_item {

    // 필수 항목
    String name;
    String phone_number;

    public contact_list_item(String name, String phone_number) {
        this.name = name;
        this.phone_number = phone_number;
    }

    /*
    String email_address;
    String website;
    String favored_color;
    String information_public;

    public static List<contact_list_item> getLocalContacts(Context context){

        // ContactsContract.Data 에는 전화번호 하나당, 하나의 relation(document) 가 있다.
        // 중복되는 전화번호가 있는 만큼, website URL 이나, email address 등등 여러가지 정보를 query 를 통해 긁어올 수 있다.

        // 데이터베이스 혹은 content resolver 를 통해 가져온 데이터를 적재할 저장소를 먼저 정의
        List<contact_list_item> data = new ArrayList<>();
        System.out.println(context);

        // 1. Resolver 가져오기(데이터베이스 열어주기)
        // 전화번호부에 이미 만들어져 있는 ContentProvider 를 통해 데이터를 가져올 수 있음
        // 다른 앱에 데이터를 제공할 수 있도록 하고 싶으면 ContentProvider 를 설정
        // 핸드폰 기본 앱들 중 데이터가 존재하는 앱들은 Content Provider 를 갖는다
        // ContentResolver 는 ContentProvider 를 가져오는 통신 수단
        ContentResolver resolver = context.getContentResolver();

        // 2. 전화번호가 저장되어 있는 테이블 주소값(Uri)을 가져오기
        // 3. 테이블에 정의된 칼럼 가져오기
        // ContactsContract.CommonDataKinds.Phone 이 경로에 상수로 칼럼이 정의
        Uri phoneUri = android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = { android.provider.ContactsContract.CommonDataKinds.Phone.CONTACT_ID // 인덱스 값, 중복될 수 있음 -- 한 사람 번호가 여러개인 경우
                ,  android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                ,  android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER
                //        ,  ContactsContract.CommonDataKinds.Phone.
                //
        };
        // System.out.println(projection[0] + " " + projection[1] + " " + projection[2] + " " + projection[3]);
        System.out.println(projection[0] + " " + projection[1] + " " + projection[2]);
        String selectionClause = null;
        String[] selectionArgs = {""};
        String[] sortOrder = null;

        // 4. ContentResolver로 쿼리를 날림 -> resolver 가 provider 에게 쿼리하겠다고 요청
        //Cursor cursor = resolver.query(phoneUri, projection, selectionClause, selectionArgs, String.valueOf(sortOrder));
        Cursor cursor = resolver.query(phoneUri, projection, null, null, null);
        System.out.println("--------------------------------cursor complete----------------------------------------------------------------------------");

        // 4. 커서로 리턴된다. 반복문을 돌면서 cursor 에 담긴 데이터를 하나씩 추출
        // 이 비교가 되는구나.신기하네
        // if (cursor.getCount() != 0) 으로 확인하려고 했는데.
        if(cursor != null){

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // 4.1 이름으로 인덱스를 찾아준다
                    int idIndex = cursor.getColumnIndex(projection[0]); // 이름을 넣어주면 그 칼럼을 가져와준다.
                    int nameIndex = cursor.getColumnIndex(projection[1]);
                    int numberIndex = cursor.getColumnIndex(projection[2]);
                    //int photoIndex = cursor.getColumnIndex(projection[3]);
                    // 4.2 해당 index 를 사용해서 실제 값을 가져온다.
                    String id = cursor.getString(idIndex);
                    String name = cursor.getString(nameIndex);
                    int number = cursor.getInt(numberIndex);
                    //int photoID = cursor.getInt(photoIndex);

                    SingleItem item = new SingleItem(id,name,number);
                    //SingleItem item = new SingleItem(id,name,number,photoID);

                    data.add(item);
                }

                cusrorAdapter = new SimpleCursorAdapter(
                        getActivity(),
                        R.layout.wordlistrow,
                        cursor,
                        wordListColumns,
                        wordListItems,
                );
                wordlist.setAdapter(cursorAdapter);

            }else{
                // nothing found for query
                 * Insert code here to notify the user that the search was unsuccessful. This isn't necessarily
                 * an error. You may want to offer the user the option to insert a new row, or re-type the
                 * search term.

            }
        }else{
            // Some providers return null if an error occurs, others throw an exception

        }
        // 데이터 계열은 반드시 닫아줘야 한다.
        cursor.close();
        return data;
    }
    */

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

    /*
    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFavored_color() {
        return favored_color;
    }

    public void setFavored_color(String favored_color) {
        this.favored_color = favored_color;
    }

    public String getInformation_public() {
        return information_public;
    }

    public void setInformation_public(String information_public) {
        this.information_public = information_public;
    }
    */
}
