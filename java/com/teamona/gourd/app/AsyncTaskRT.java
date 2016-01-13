package com.teamona.gourd.app;

import android.content.Context;
import android.os.AsyncTask;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by teamona on 2015/10/03.
 */
public class AsyncTaskRT extends AsyncTask<Long, Void, Boolean> {
    private Context mContext;

    public AsyncTaskRT(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(Long... longs) {
        long id = longs[0];

        Twitter twitter = TwitterUtils.getTwitterInstance(mContext);

        try {
            twitter.retweetStatus(id); // ここに「ネットワーク通信」が必要な処理を書く
            return true;
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*private void rtButton_Click(String str){

    }*/
    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {

            // うまく行ったときの処理
        } else {
            // 残念
        }
    }

}
