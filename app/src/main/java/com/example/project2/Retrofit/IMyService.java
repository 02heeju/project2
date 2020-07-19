package com.example.project2.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IMyService {

    // 모든 로그인 정보를 가져온다. 필요한 필드가 없다.
    @GET("account/list")
    @FormUrlEncoded
    Observable<String> get_account_list();

    // 계정 로그인
    @POST("account/login")
    @FormUrlEncoded
    Observable<String> login_with_email_password(
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
    @POST("contacts/load_contact_app_contacts")
    @FormUrlEncoded
    Observable<String> load_contact_app_contacts(
            @Field("name") String name,
            @Field("contents") String contents);

    // 연락처앱에서 긁어와서 만든 json 스트링을 보내서, db에 추가되도록
    @POST("contacts/add_contact_entry")
    @FormUrlEncoded
    Observable<String> add_entry(
            @Field("owner_name") String owner_name,
            @Field("name") String name,
            @Field("phone") String phone_number);

    // 연락처앱에서 긁어와서 만든 json 스트링을 보내서, db에 추가되도록
    @POST("contacts/delete_contact_entry")
    @FormUrlEncoded
    Observable<String> delete_contact_entry(
            @Field("name") String name,
            @Field("target") String target);

}
