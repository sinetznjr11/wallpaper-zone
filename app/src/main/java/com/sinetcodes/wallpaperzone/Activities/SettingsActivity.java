package com.sinetcodes.wallpaperzone.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.onesignal.OneSignal;
import com.sinetcodes.wallpaperzone.BuildConfig;
import com.sinetcodes.wallpaperzone.PhotoView.PhotoViewActivity;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.Utilities.AppUtil;
import com.sinetcodes.wallpaperzone.Utilities.FirebaseEventManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity
        implements
        SharedPreferences.OnSharedPreferenceChangeListener
        {

    private static final String TAG = "SettingsActivity";
    @BindView(R.id.toolbar)
    MaterialToolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ButterKnife.bind(this);
        setActionBar();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        setSharedPreference();
    }

    private void setActionBar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void setSharedPreference() {
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case "notification":
                if(sharedPreferences.getBoolean("notification",true)) OneSignal.setSubscription(true);
                else    OneSignal.setSubscription(false);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }


    public static class SettingsFragment extends PreferenceFragmentCompat implements androidx.preference.PreferenceManager.OnPreferenceTreeClickListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            findPreference("app_detail").setSummary(BuildConfig.VERSION_NAME);

        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            switch (preference.getKey()){
                case "share_app":
                    Intent i = new Intent(Intent.ACTION_SEND);
                    new FirebaseEventManager(getContext()).shareAppEvent(AppUtil.getDeviceId(getContext()));
                    i.setType("*/*");
                    i.putExtra(Intent.EXTRA_TEXT, "Hey check this amazing app called Wallpaper Zone.\nYou can download the app here, https://play.google.com/store/apps/details?id=com.sinetcodes.wallpaperzone" );
                    startActivity(Intent.createChooser(i, "Share App"));
                    return true;

                case "report":
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto","sinetpkr@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report: Wallpaper Zone");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Your suggestions here..");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    return true;
            }
            return false;
        }
    }
}