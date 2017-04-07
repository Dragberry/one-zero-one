package org.dragberry.ozo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.dragberry.ozo.admob.AdsController;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.http.HttpClient;
import org.dragberry.ozo.http.HttpTask;
import org.dragberry.ozo.platform.Platform;
import org.dragberry.ozo.platform.User;
import org.dragberry.ozo.platform.UserImpl;

public class AndroidLauncher extends AndroidApplication implements Platform, AdsController, HttpClient {

	private AdView adView;
	private View gameView;

	private InterstitialAd interstitialAd;

	private User user = new UserImpl();

	@Override
	public AdsController getAdsController() {
		return this;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public HttpClient getHttpClient() {
		return this;
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useAccelerometer = false;
		cfg.useCompass = false;

		// Do the stuff that initialize() would do for you
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);


		View gameView = createGameView(cfg);
		layout.addView(gameView);
		AdView admobView = createAdView();
		layout.addView(admobView);

		setContentView(layout);
		startAdvertising(admobView);

		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(getString(R.string.on_exit_banner));

		AdRequest.Builder builder = new AdRequest.Builder();
		AdRequest ad = builder.build();
		interstitialAd.loadAd(ad);
	}

	private AdView createAdView() {
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(getString(R.string.main_menu_banner));
		adView.setId(R.id.ad_view_id); // this is an arbitrary id, allows for relative positioning in createGameView()
		adView.setBackgroundColor(Color.BLACK);
		adView.getHeight();
		adView.setVisibility(View.INVISIBLE);
		return adView;
	}

    private View createGameView(AndroidApplicationConfiguration cfg) {
        gameView = initializeForView(new OneZeroOneGame(this, true), cfg);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        return gameView;
    }

    private void startAdvertising(AdView adView) {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("").build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) adView.resume();
    }

    @Override
    public void onPause() {
        if (adView != null) adView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adView != null) adView.destroy();
        super.onDestroy();
    }

	@Override
	public void showBannerAd() {
		if (isBannerShown()) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					adView.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	@Override
	public void hideBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(View.INVISIBLE);
			}
		});
	}

	@Override
	public void showBannerAdNew() {
		if (isBannerShown()) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					adView.setVisibility(View.VISIBLE);
					startAdvertising(adView);
				}
			});
		}
	}

	@Override
	public boolean isBannerShown() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null) {
			boolean typeMatched = false;
			switch (ni.getType()) {
				case ConnectivityManager.TYPE_MOBILE:
				case ConnectivityManager.TYPE_WIFI:
				case ConnectivityManager.TYPE_WIMAX:
				case ConnectivityManager.TYPE_ETHERNET:
					typeMatched = true;
					break;
			}
			return typeMatched && ni.isConnected() && !ni.isRoaming();
		}
		return false;
	}

	@Override
	public void showInterstitialAd(final Runnable then) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (then != null) {
					interstitialAd.setAdListener(new AdListener() {
						@Override
						public void onAdClosed() {
							Gdx.app.postRunnable(then);
							AdRequest.Builder builder = new AdRequest.Builder();
							AdRequest ad = builder.build();
							interstitialAd.loadAd(ad);
						}
					});
				}
				interstitialAd.show();
			}
		});
	}

	@Override
	public boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null) {
			boolean typeMatched = false;
			switch (ni.getType()) {
				case ConnectivityManager.TYPE_MOBILE:
				case ConnectivityManager.TYPE_WIFI:
				case ConnectivityManager.TYPE_WIMAX:
				case ConnectivityManager.TYPE_ETHERNET:
					typeMatched = true;
					break;
			}
			return typeMatched && ni.isConnected() && !ni.isRoaming();
		}
		return false;
	}

	@Override
	public <T, R>  void executeTask(final HttpTask<T, R> httpTask) {
		if (isConnected()) {
			new HttpRequestTask<>(httpTask).execute();
		}
	}

	@Override
	public void openMarketUrl() {
		Uri marketUri = Uri.parse(Constants.ANDROID_PLAY_MARKET_URL);
		Intent intent = new Intent(Intent.ACTION_VIEW, marketUri);
		getContext().startActivity(intent);
	}

	private class HttpRequestTask<P, R> extends AsyncTask<Void, Void, HttpTask.Result<R>> {

		private final HttpTask<P, R> httpTask;

		public HttpRequestTask(HttpTask<P, R> httpTask) {
			this.httpTask = httpTask;
		}

		@Override
		protected HttpTask.Result<R> doInBackground(Void... params) {
			return httpTask.execute();
		}

		@Override
		protected void onPostExecute(HttpTask.Result<R> result) {
			if (result == null) {
				httpTask.onFail();
			} else if (result.result == null) {
				httpTask.onFail(result.status);
			} else {
				httpTask.onComplete(result.result);
			}
		}

	}
}
