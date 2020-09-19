package com.sinetcodes.wallpaperzone.data.network;

import com.sinetcodes.wallpaperzone.pojo.Collection;
import com.sinetcodes.wallpaperzone.pojo.Photos;
import com.sinetcodes.wallpaperzone.pojo.Results;
import com.sinetcodes.wallpaperzone.data.network.responses.CategoryList;
import com.sinetcodes.wallpaperzone.data.network.responses.WallpaperList;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    //
    @GET("photos")
    Call<List<Photos>> getPhotos(
            @Query("client_id") String clientId,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @GET("photos/{id}")
    Call<Photos> getPhoto(
            @Path(value = "id") String photoId,
            @Query("client_id") String clientId
    );

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

    /**
     * Alpha coders
     */
/*    @GET
    Call<WallpaperList> getWallpaperList(@Url String url);*/

   /* @GET
    Call<CategoryList> getCategoryList(@Url String url);  */

   @GET
   Observable<WallpaperList> getWallpaperList(@Url String url);

    @GET
    Observable<CategoryList> getCategoryList(@Url String url);




}
