package com.example.gourd.app;

/**
 * Created by ruzeya on 2014/07/21.
 */
        import android.content.Context;
        import twitter4j.StallWarning;
        import twitter4j.Status;
        import twitter4j.StatusDeletionNotice;
        import twitter4j.StatusListener;
        import twitter4j.TwitterStream;
        import twitter4j.TwitterStreamFactory;
        import twitter4j.auth.AccessToken;
        import twitter4j.conf.Configuration;
        import twitter4j.conf.ConfigurationBuilder;


public class UserStream {

    private String mConsumerKey, mConsumerSecret, mAccessToken, mAccessTokenSecret;
    private TweetAdapter mAdapter;

    class MyStatusListener implements StatusListener {

        public void onStatus(final Status status) {
            System.out.println("Status: " + status.getText());

            new UiHandler() {
                @Override
                public void run() {             //実行内容
                    mAdapter.insert(status, 0);
                    mAdapter.notifyDataSetChanged();
                }
            }.post();       //実行
        }

        public void onDeletionNotice(StatusDeletionNotice sdn) {
            System.out.println("onDeletionNotice.");
        }

        public void onTrackLimitationNotice(int i) {
            System.out.println("onTrackLimitationNotice.(" + i + ")");
        }

        public void onScrubGeo(long lat, long lng) {
            System.out.println("onScrubGeo.(" + lat + ", " + lng + ")");
        }

        public void onException(Exception e) {
            e.printStackTrace();
            System.out.println("onException.");
        }

        @Override
        public void onStallWarning(StallWarning arg0) {
            ;
        }
    }

    public UserStream(Context context) {
        // Contextとは：Androidの色々（strings.xmlに書かれた文字とか設定ファイルとか）を読み書きするのに必要

        // コンシューマキーとシークレット：strings.xmlから読み込む
        mConsumerKey = context.getString(R.string.twitter_consumer_key);
        mConsumerSecret = context.getString(R.string.twitter_consumer_secret);

        // アクセストークンとシークレット：TwitterUtilsを使って設定ファイルから読み込む

        // 変数 hasAccessToken には「設定ファイルにトークンが書いてあるか否か」が入る
        // （設定ファイルがないのに読み込もうとしてエラー、なんてことを防ぐため）
        boolean hasAccessToken = TwitterUtils.hasAccessToken(context);
        if (hasAccessToken) {
            AccessToken token = TwitterUtils.loadAccessToken(context);
            mAccessToken = token.getToken();
            mAccessTokenSecret = token.getTokenSecret();
        }
    }

    public void setAdapter(TweetAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void start() {

        // Configuration とは：UserStreamに接続するために必要な情報
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(mConsumerKey);
        builder.setOAuthConsumerSecret(mConsumerSecret);
        builder.setOAuthAccessToken(mAccessToken);
        builder.setOAuthAccessTokenSecret(mAccessTokenSecret);

        Configuration configuration = builder.build();

        TwitterStream twStream = new TwitterStreamFactory(configuration).getInstance();
        twStream.addListener(new MyStatusListener());

        // twitterStream.user() で UserStream に接続する
        twStream.user();
    }
}
