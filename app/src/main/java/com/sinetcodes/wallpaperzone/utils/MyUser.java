package com.sinetcodes.wallpaperzone.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MyUser extends AsyncTask<String, String, String> {

    FirebaseAuth mAuth;
    Context mContext;
    private static final String TAG = "CreateDummyUser";
    DatabaseReference usersRef;

    public MyUser(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    @Override
    protected String doInBackground(String... strings) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean isRecordPresent = false;
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        if (childSnapshot.child("device_id").getValue(String.class).equals(AppUtil.getDeviceId(mContext))) {
                            isRecordPresent = true;
                        }
                    }

                    if (isRecordPresent) {
                        signInUser();
                    } else {
                        createNewUser();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else{
            Log.d(TAG, "doInBackground: signed in user_id: "+user.getUid());
        }
        return null;
    }

    private void createNewUser() {
        Log.d(TAG, "createNewUser: ");
        mAuth.createUserWithEmailAndPassword(AppUtil.getDeviceId(mContext) + "@dummy.com", StringsUtil.DUMMY_PASSWORD)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            createUserInDatabase(user);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void createUserInDatabase(FirebaseUser user) {
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("device_id", AppUtil.getDeviceId(mContext));

        usersRef.child(user.getUid())
                .setValue(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: created user in database");
                            new FirebaseEventManager(mContext).dummySignUpEvent(user.getUid(),AppUtil.getDeviceId(mContext));

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }

    private void signInUser() {
        mAuth.signInWithEmailAndPassword(AppUtil.getDeviceId(mContext)+"@dummy.com",StringsUtil.DUMMY_PASSWORD)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: login successful.");
                            new FirebaseEventManager(mContext).dummyLoginEvent(FirebaseAuth.getInstance().getCurrentUser().getUid(),AppUtil.getDeviceId(mContext));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "signInUser: onFailure: "+e.getMessage() );
                    }
                });
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}
