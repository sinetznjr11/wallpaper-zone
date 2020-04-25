package com.sinetcodes.wallpaperzone.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.material.card.MaterialCardView;
import com.sinetcodes.wallpaperzone.Ads.AdsUtil;
import com.sinetcodes.wallpaperzone.Ads.MyInterstitialAd;
import com.sinetcodes.wallpaperzone.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestActivity extends AppCompatActivity {


    private static final String TAG = "TestActivity";
    @OnClick(R.id.btnLoad)
    void onLoadClicked(){
       // loadWebsite();
    }

    private void loadWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://html5games.com/").get();
                    String title = doc.title();
                    Elements ul = doc.select("div.main-section > ul");
                    Elements li=ul.select("li");

                    Log.d(TAG, "run: Title "+title);

                    for (Element link : li) {
                        Log.d(TAG, "run: Text => "+link.text());
                        Log.d(TAG, "run: link => "+link.select("a").attr("href"));
                        Log.d(TAG, "run: link => "+link.select("img").attr("src"));



                    }
                } catch (IOException e) {
                    Log.e(TAG, "run: Error"+e.getMessage() );
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d(TAG, "run: "+builder.toString());
                    }
                });
            }
        }).start();
    }

    @BindView(R.id.native_ad_placeholder)
    FrameLayout nativeAdPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        ButterKnife.bind(this);

        final AdLoader adLoader = new AdLoader.Builder(this, AdsUtil.getAdUnit(AdsUtil.SEARCH_FRAGMENT_NATIVE))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        MaterialCardView adView=(MaterialCardView) getLayoutInflater().inflate(R.layout.unified_native_ad_layout,null);

                        populateUnifiedNativeAdView(unifiedNativeAd, adView);
                        nativeAdPlaceholder.removeAllViews();
                        nativeAdPlaceholder.addView(adView);

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd unifiedNativeAd, MaterialCardView cardView) {
        Log.d(TAG, "populateUnifiedNativeAdView: "+unifiedNativeAd.getHeadline());

        UnifiedNativeAdView adView=cardView.findViewById(R.id.native_ad);
        TextView adHeadline=adView.findViewById(R.id.ad_headline);
        adHeadline.setText(unifiedNativeAd.getHeadline());
        adView.setHeadlineView(adHeadline);

        TextView adBody=adView.findViewById(R.id.ad_body);
        adBody.setText(unifiedNativeAd.getBody());
        adView.setBodyView(adBody);

        ImageView adAppIcon=adView.findViewById(R.id.ad_app_icon);
        adAppIcon.setImageDrawable(unifiedNativeAd.getIcon().getDrawable());
        adView.setIconView(adAppIcon);

        MediaView adMediaView=adView.findViewById(R.id.ad_media_view);
        adMediaView.setMediaContent(unifiedNativeAd.getMediaContent());
        adView.setMediaView(adMediaView);


        adView.setNativeAd(unifiedNativeAd);
    }
}
