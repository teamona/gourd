package com.teamona.gourd.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by ruzeya on 2014/06/27.
 */
/*FragmentActivityはActivityの中にもう一つの画面を作る
  ページ移動
* SobaChaのデュアルスクリーンなど*/

public class TweetActivity extends Activity {

    private EditText mInputText;
    private Twitter mTwitter;
    private long inReplyTo = -1;
    private String target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        mTwitter = TwitterUtils.getTwitterInstance(this);

        mInputText = (EditText) findViewById(R.id.input_text);



        findViewById(R.id.action_tweet).setOnClickListener(new View.OnClickListener() {
            @Override   //action_tweetはつぶやくボタン
            public void onClick(View v) {
                tweet();
            }
        });

        Intent intent = getIntent();
        if (intent != null){
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                target = bundle.getString("target");
                inReplyTo = bundle.getLong("in_reply_to"); // inReplyTo <= status.getID()
                mInputText.setText("@"+target);
                showToast("成功: "+inReplyTo);
            }

        }
    }

    private void tweet() {
        AsyncTask<StatusUpdate, Void, Boolean> task = new AsyncTask<StatusUpdate, Void, Boolean>() {
            @Override
                protected Boolean doInBackground(StatusUpdate... params) {
                    try {
                            mTwitter.updateStatus(params[0]);//params[0]には本文が入ってる
                            return true;
                    } catch (TwitterException e) {
                        e.printStackTrace();
                        return false;
                    }
                }

            @Override
                protected void onPostExecute(Boolean result) {
                    if (result) {
                        showToast("ツイートが完了しました！");
                        finish();
                    } else {
                        showToast("ツイートに失敗しました。。。");
                    }
            }

        };
        String text = mInputText.getText().toString();
        StatusUpdate statusUpdate = new StatusUpdate(text);
        if (inReplyTo > 0){
            statusUpdate.setInReplyToStatusId(inReplyTo);
        }
        task.execute(statusUpdate);
    }


    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();  /*LENGTH_SHORTは下から出てくるテキストの表示時間*/
    }
}
