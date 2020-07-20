package com.example.project2.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IMyService {

    // 계정 로그인
    @POST("account/login")
    @FormUrlEncoded
    Observable<String> login(
            @Field("email") String email,
            @Field("password") String password);

    // 계정 추가
    @POST("account/create")
    @FormUrlEncoded
    Observable<String> create_account(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name);

    // 사용자 이름에 해당하는 컬렉션에서, 모든 연락처 정보를 가져온다.
    @POST("contacts/contacts_list")
    @FormUrlEncoded
    Observable<String> get_cloud_contact(
            @Field("name") String name);

    // 연락처앱에서 긁어와서 만든 json 스트링을 보내서, db에 추가되도록
    @POST("contacts/phone_to_cloud")
    @FormUrlEncoded
    Observable<String> phone_to_cloud(
            @Field("name") String name,
            @Field("contents") String contents);

    // Cloud to Phone

    // 연락처앱에서 긁어와서 만든 json 스트링을 보내서, db에 추가되도록
    @POST("contacts/add_contact_entry")
    @FormUrlEncoded
    Observable<String> add_entry(
            @Field("owner_name") String owner_name,
            @Field("name") String name,
            @Field("phone") String phone_number);

    // delete contact entry on cloud

    // Tab 3
    // 메뉴 추가
    @POST("contacts/add_menu")
    @FormUrlEncoded
    Observable<String> add_menu(
            @Field("owner_name") String owner_name,
            @Field("place") String place,
            @Field("number") String number,
            @Field("time") String time,
            @Field("member") String member);

    @GET("contacts/menu_list")
    @FormUrlEncoded
    Observable<String> menu_list();

    @POST("contacts/join")
    @FormUrlEncoded
    Observable<String> join(
            @Field("owner_name") String owner_name,
            @Field("name") String name);

}
