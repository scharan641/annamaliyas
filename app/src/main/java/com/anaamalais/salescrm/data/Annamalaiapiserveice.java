package com.anaamalais.salescrm.data;

import com.anaamalais.salescrm.UpdateUser;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;
import static com.anaamalais.salescrm.Utils.Constants.IMAGETOKEN;
import static com.anaamalais.salescrm.Utils.Constants.TESTDRIVEUPDATEIMAGE;
import static com.anaamalais.salescrm.Utils.Constants.TOKEN;
import static com.anaamalais.salescrm.Utils.Constants.UPDATEUSER;
import static com.anaamalais.salescrm.Utils.Constants.UPDATIMAGE;


public interface Annamalaiapiserveice {

    @Multipart
    @POST(UPDATEUSER)
    Call<UpdateUser> updateUserapi(@Header(TOKEN) String token, @Path("FIRST_NAME") String firstName
    , @Path("LAST_NAME") String lastName,@Path("EMAIL") String email ,@Path("PHONE") String phone,
                                   @Part() MultipartBody.Part file );

    @Multipart
    @POST(UPDATIMAGE)
    Call<ImageUpload>updateImageurl(@Header(IMAGETOKEN)String API_TOKEN, @Part() MultipartBody.Part file);

    @Multipart
    @POST(TESTDRIVEUPDATEIMAGE)
    Call<LicenceImage>testdriveImageurl(@Header(IMAGETOKEN)String API_TOKEN, @Part() MultipartBody.Part file);

}
