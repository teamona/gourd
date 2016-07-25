package com.teamona.gourd.app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//1階層
public class MainActivity extends FragmentActivity implements AdapterHolder {

    private Map<EnumTab, TweetAdapter> mAdapterMap;

    private Twitter mTwitter;
    private UserStream mUST;
    private ChangeBackground mChangeBackground;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        mViewPager=(ViewPager) findViewById(R.id.viewPager);

        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this,TwitterOAuthActivity.class);
            startActivity(intent);                              /*startActivityは別ので新しいActivityを立ち上げる。画面移動なんかが正にそれ！*/
            finish();
        }else {
            mAdapterMap = new HashMap<EnumTab, TweetAdapter>();
            mAdapterMap.put(EnumTab.HOME,new TweetAdapter(this));
            mAdapterMap.put(EnumTab.MENTION,new TweetAdapter(this));
            mAdapterMap.put(EnumTab.LIST,new TweetAdapter(this));
            mAdapterMap.put(EnumTab.SEARCH,new TweetAdapter(this));


            mTwitter = TwitterUtils.getTwitterInstance(this);
            reloadTimeLine();

            mUST = new UserStream(this);
            mUST.setAdapter(mAdapterMap.get(EnumTab.HOME));
            mUST.start();
        }

        // ChangeBackgroundを初期化する
        ImageView imageView = (ImageView) findViewById(R.id.imageViewBackground);
        mChangeBackground = new ChangeBackground(this,imageView);

        // Preferencesを初期化する
        Preferences1 pref1 = new Preferences1(this);

        // 設定から画像のパスを読み込む
        String uriString = pref1.getString("background");

        // 読み込んだパスをUri形式に変換する
        Uri uri = mChangeBackground.stringToUri(uriString);

        if (uri != null) {
            // Uriから画像を読み込む
            mChangeBackground.loadFromUri(uri);
        }


        // ViewPagerを初期化する
        initializeViewPager();
    }

    /*ActionBarにメニューを表示*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    /*メニューの選択*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tweet:
                Intent intent1 = new Intent(this, TweetActivity.class);
                startActivity(intent1);
                return true;
            case R.id.menu_background_change:
                //画像の選択とサイズの変更諸々
               mChangeBackground.onButtonClick(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        mChangeBackground.onActivityResult(requestCode,resultCode,data);
    }

    private void reloadTimeLine() {             /*AsyncTaskはインターネットの接続と同時進行で処理するようにする*/
        android.os.AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    return mTwitter.getHomeTimeline();      /*ここでエラーが起きたら*/
                } catch (TwitterException e) {
                    e.printStackTrace();                    /*デバッグ報告*/
                }
                return null;
            }
            @Override
            protected void onPostExecute(List<twitter4j.Status> result) {
                if (result != null) {
                    mAdapterMap.get(EnumTab.HOME).clear();
                    for (twitter4j.Status status : result) {
                        mAdapterMap.get(EnumTab.HOME).add(status);
                    }
                    //mListView.setSelection(0);
                } else {
                    showToast("タイムラインの取得に失敗しました。。。");
                }
            }
        };
        task.execute();
    }

    public void showToast(String text) {
       android.widget.Toast.makeText(this, text, android.widget.Toast.LENGTH_SHORT).show();
    }

    private void initializeViewPager() {
        SwipeAdapter adapter = new SwipeAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
    }

    @Override
    public TweetAdapter getAdapter(EnumTab tab) {
        return mAdapterMap.get(tab);
    }
}

