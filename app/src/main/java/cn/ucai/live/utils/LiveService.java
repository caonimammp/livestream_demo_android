package cn.ucai.live.utils;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/6/7.
 */

public interface LiveService {
    @GET("live/getAllGifts")
    Call<String> getAllGifts();
    @GET("live/getBalance")
    Call<String> getBalance(@Query("uname") String username);
    @GET("live/givingGifts")
    Call<String> givingGifts(
            @Query("uname") String username,
            @Query("anchor") String anchor,
            @Query("giftId") int giftId,
            @Query("giftNum") int giftNum
    );
    @Multipart
    @POST("register")
    Call<String> register(
            @Query("m_user_name")String username,
            @Query("m_user_nick")String nickname,
            @Query("m_user_password")String password,
            @Part MultipartBody.Part file
    );
}
