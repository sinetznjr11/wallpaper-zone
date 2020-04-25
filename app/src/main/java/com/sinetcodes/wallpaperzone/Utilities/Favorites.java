package com.sinetcodes.wallpaperzone.Utilities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.PhotoView.PhotoViewActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Favorites {
    Context mContext;
    Photos mPhoto;
    FirebaseAuth mAuth;
    DatabaseReference favRef;
    OnAddFavoriteListener mListener;
    private static final String TAG = "Favourites";
    String date;
    public Favorites(Context context, Photos photo) {
        mContext = context;
        mPhoto = photo;
        mAuth = FirebaseAuth.getInstance();
        favRef = FirebaseDatabase.getInstance().getReference().child("favourites");
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        date = df.format(Calendar.getInstance().getTime());
    }


    public void addToFavorites() {
        if (mAuth.getCurrentUser() != null) {
            //check if photo is already in favorites in RTDB
            DatabaseReference photoRef = favRef.child(mAuth.getUid()).child(mPhoto.getId());
            photoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        mListener.onError("Photo already added to favorites.");
                    }else{
                        addFavoriteToDatabase();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            mListener.onError("Firebase authorization failed.");
        }
    }

    public void addOnCompleteListener(OnAddFavoriteListener favoriteListener) {
        this.mListener = favoriteListener;
    }

    private void addFavoriteToDatabase() {
        favRef
                .child(mAuth.getUid())
                .child(mPhoto.getId())
                .setValue(mPhoto)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Successfully added to favorites.");
                            mListener.onComplete("Successfully added to favorites.");
                        } else {
                            mListener.onError(task.getException().toString());
                        }

                    }
                });
    }

    public interface OnAddFavoriteListener {
        void onComplete(String message);

        void onError(String error);
    }

}
