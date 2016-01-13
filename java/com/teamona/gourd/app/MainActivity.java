package com.teamona.gourd.app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TweetAdapter mAdapter;
    private Twitter mTwitter;
    private ListView mListView;
    private UserStream mUST;
    private ChangeBackground mChangeBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView=(ListView) findViewById(R.id.listview);

        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this,TwitterOAuthActivity.class);
            startActivity(intent);                              /*startActivityは別ので新しいActivityを立ち上げる。画面移動なんかが正にそれ！*/
            finish();
        }else {
            mAdapter = new TweetAdapter(this);
            mListView.setAdapter(mAdapter);

            mTwitter = TwitterUtils.getTwitterInstance(this);
            reloadTimeLine();

            mUST = new UserStream(this);
            mUST.setAdapter(mAdapter);
            mUST.start();
        }

        // ChangeBackgroundを初期化する
        ImageView imageView = (ImageView) findViewById(R.id.imageViewBackground);
        mChangeBackground = new ChangeBackground(this, imageView);

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
                    mAdapter.clear();
                    for (twitter4j.Status status : result) {
                        mAdapter.add(status);
                    }
                    mListView.setSelection(0);
                } else {
                    showToast("タイムラインの取得に失敗しました。。。");
                }
            }

        };
        task.execute();
    }

    private void showToast(String text) {
       android.widget.Toast.makeText(this, text, android.widget.Toast.LENGTH_SHORT).show();
    }


}

