package com.example.project2.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IMyService {

    // 모든 로그인 정보를 가져온다. 필요한 필드가 없다.
    @POST("account/list")
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
    @GET("contacts/contacts_list")
    Observable<String> get_cloud_contact();

    @POST
    @FormUrlEncoded
    Observable<String> load_contact_app_contacts(
            @Field("name") String name,
            @Field("contents") String contents);

}
