package com.sinetcodes.wallpaperzone.Common;

import com.sinetcodes.wallpaperzone.POJO.Collection;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.POJO.Results;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommonApiInterface {

    //
    @GET("photos")
    Call<List<Photos>> getPhotos(
            @Query("client_id") String clientId,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @GET("collections")
    Call<List<Collection>> getCollection(
            @Query("client_id") String clientId,
            @Query("page") int page,
            @Query("per_page") int perPage
    );

    @GET("users/{user_name}/photos")
    Call<List<Photos>> getUserPhotos(
            @Path(value = "user_name") String userName,
            @Query("client_id") String clientId,
            @Query("page") int page,
            @Query("per_page") int perPage,
            @Query("order_by") String orderBy,
            @Query("orientation") String orientation
    );

    @GET("search/photos")
    Call<Results> getSearchResults(
            @Query("client_id") String clientId,
            @Query("query") String query,
            @Query("page") int page,
            @Query("per_page") int perPage,
            @Query("order_by") String orderBy,
            @Query("orientation") String orientation
    );

    @GET("photos/random")
    Call<List<Photos>> getRandomPhoto(
            @Query("client_id") String clientId,
            @Query("query") String query,
            @Query("count") int count,
            @Query("orientation") String orientation
            );

    @GET("collections/{collection_id}/photos")
    Call<List<Photos>> getCollectionPhotos(
            @Path(value = "collection_id") int collectionId,
            @Query("client_id") String clientId,
            @Query("page") int page,
            @Query("per_page") int perPage
    );
}
