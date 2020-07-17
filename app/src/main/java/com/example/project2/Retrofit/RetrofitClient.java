package com.example.project2.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;

    public static Retrofit getInstance(){
        if(instance == null)
            instance = new Retrofit.Builder()
                    .baseUrl("http://192.249.19.243:0180") // In emulator, localhost will changed to 10.0.2.2 컴퓨터 local host랑 값이 다른가??
                    /*
                    WRONG hosts
                    //.baseUrl("http://localhost:3000") // In emulator, localhost will changed to 10.0.2.2 컴퓨터 local host랑 값이 다른가??
                    //.baseUrl("192.168.0.80:3000")
                    //.baseUrl("http://10.0.2.2:3000/")
                    */
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return instance;
    }

}
