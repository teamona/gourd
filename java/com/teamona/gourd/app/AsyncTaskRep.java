package com.teamona.gourd.app;

import android.content.Context;
import android.os.AsyncTask;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


/**
 * Created by teamona on 15/12/16.
 */
public class AsyncTaskRep extends AsyncTask<Long,Void, Boolean> {
    private Context mContext;

    public AsyncTaskRep(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(Long... longs) {
        long id = longs[0];

        try {
            Twitter twitterRep = TwitterFactory.getSingleton();
            twitterRep.updateStatus(new StatusUpdate("@宛先スクリーン名 メッセージ").inReplyToStatusId(id));
            return true;
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            // うまく行ったときの処理
        } else {
            // 残念
        }
    }
}
