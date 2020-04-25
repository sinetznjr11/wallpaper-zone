package com.sinetcodes.wallpaperzone.Home;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinetcodes.wallpaperzone.Common.ApiClient;
import com.sinetcodes.wallpaperzone.Common.CommonApiInterface;
import com.sinetcodes.wallpaperzone.Common.ContentType;
import com.sinetcodes.wallpaperzone.POJO.CategoryItem;
import com.sinetcodes.wallpaperzone.POJO.Collection;
import com.sinetcodes.wallpaperzone.POJO.ExploreItem;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.Utilities.SetUpRetrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreModel implements ExploreMVPInterface.model {
    private static final String TAG = "ExploreModel";
    Context context;
    ExploreMVPInterface.presenter presenter;

    List<ExploreItem> exploreItems = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;

    public ExploreModel(Context context, ExplorePresenter explorePresenter) {
        this.context = context;
        this.presenter = explorePresenter;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void askCategory() {

        DatabaseReference categoryRef = firebaseDatabase.getReference().child("category");
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Object> categoryItems = new ArrayList<>();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    CategoryItem categoryItem = categorySnapshot.getValue(CategoryItem.class);
                    categoryItems.add(categoryItem);
                }
                presenter.takeContent(categoryItems, ContentType.CATEGORY);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void askCollections(int page) {
        List<Object> collectionItems = new ArrayList<>();
        ApiClient apiClient = new ApiClient(context);
        CommonApiInterface apiInterface = apiClient.getOkHttpClient().create(CommonApiInterface.class);
        try {
            Call<List<Collection>> call = apiInterface.getCollection(SetUpRetrofit.getUnsplashClientId(), page, 20);
            call.enqueue(new Callback<List<Collection>>() {
                @Override
                public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                    if (response.isSuccessful()) {
                        for (int i = 0; i < response.body().size(); i++) {
                            collectionItems.add(response.body().get(i));
                        }
                        presenter.takeContent(collectionItems, ContentType.COLLECTIONS);
                    } else {
                        presenter.onError(response.code() + ": " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Collection>> call, Throwable t) {
                    presenter.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void askPopular(int page) {
        Log.d(TAG, "askPopular: "+page);
        ApiClient apiClient = new ApiClient(context);
        CommonApiInterface apiInterface = apiClient.getOkHttpClient().create(CommonApiInterface.class);
        Call<List<Photos>> call=apiInterface.getPhotos(SetUpRetrofit.getUnsplashClientId(),page,20);
        call.enqueue(new Callback<List<Photos>>() {
            @Override
            public void onResponse(Call<List<Photos>> call, Response<List<Photos>> response) {
                if(response.isSuccessful()){
                    presenter.takeContent((List<Object>)(List<?>) response.body(),ContentType.POPULAR);
                }
                else{

                }
            }

            @Override
            public void onFailure(Call<List<Photos>> call, Throwable t) {

            }
        });
    }

}
