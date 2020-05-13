package com.sinetcodes.wallpaperzone.PhotoView;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinetcodes.wallpaperzone.Common.ApiClient;
import com.sinetcodes.wallpaperzone.Common.CommonApiInterface;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.Utilities.AppUtil;
import com.sinetcodes.wallpaperzone.Utilities.FirebaseEventManager;
import com.sinetcodes.wallpaperzone.Utilities.SetUpRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoViewModel implements UserPhotosMVPInterface.model{

    private static final String TAG = "PhotoViewModel";
    Context mContext;
    UserPhotosMVPInterface.presenter mPresenter;

    FirebaseAuth mAuth;
    DatabaseReference favRef;

    public PhotoViewModel(Context context, UserPhotosMVPInterface.presenter presenter) {
        mContext = context;
        mPresenter = presenter;

        mAuth=FirebaseAuth.getInstance();
        favRef = FirebaseDatabase.getInstance().getReference().child("favourites");
    }

    @Override
    public void askContent(String userName) {
        ApiClient apiClient = new ApiClient(mContext);
        CommonApiInterface apiInterface=apiClient.getOkHttpClient().create(CommonApiInterface.class);

        try {
            Call<List<Photos>> listCall=apiInterface.getUserPhotos(userName, SetUpRetrofit.getUnsplashClientId(),1,20,"popular","portrait");
            listCall.enqueue(new Callback<List<Photos>>() {
                @Override
                public void onResponse(Call<List<Photos>> call, Response<List<Photos>> response) {
                    if(response.isSuccessful()){
                        mPresenter.takeContent(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Photos>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void isPhotoInFavorites(String photoId) {
        if (mAuth.getCurrentUser() != null) {
            //check if photo is already in favorites in RTDB
            DatabaseReference photoRef = favRef.child(mAuth.getUid()).child(photoId);
            photoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                       mPresenter.isPhotoInFavoritesResponse(true);
                    }else{
                       mPresenter.isPhotoInFavoritesResponse(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Log.e(TAG, "Firebase authorization failed.");
        }

    }

    public void addFavoriteToDatabase(Photos mPhoto) {
        favRef
                .child(mAuth.getUid())
                .child(mPhoto.getId())
                .setValue(mPhoto)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Successfully added to favorites.");
                            mPresenter.showToast("Successfully added to favorites.");
                            new FirebaseEventManager(mContext).addToFavEvent(mPhoto.getUrls().getSmall(), AppUtil.getDeviceId(mContext));
                        } else {
                            mPresenter.showToast(task.getException().toString());
                        }

                    }
                });
    }

    public void removeFavoriteFromDatabase(Photos mPhoto) {
        favRef
                .child(mAuth.getUid())
                .child(mPhoto.getId())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            new FirebaseEventManager(mContext).removeFromFav(mPhoto.getUrls().getSmall(),AppUtil.getDeviceId(mContext));
                            Toast.makeText(mContext, "Removed from favorites.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: Remove from favorites: "+e.getMessage() );
                    }
                });

    }

}

