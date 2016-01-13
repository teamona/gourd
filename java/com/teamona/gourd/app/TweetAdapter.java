package com.teamona.gourd.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.image.SmartImageView;
import twitter4j.Status;

public class TweetAdapter  extends android.widget.ArrayAdapter<twitter4j.Status> {

    private Context mContext;
    private android.view.LayoutInflater mInflater;

    public TweetAdapter(Context context){
        super(context, android.R.layout.simple_list_item_1);
        mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_tweet, null);
        }
        Status item = getItem(position);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(item.getUser().getName());
        TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
        screenName.setText("@" + item.getUser().getScreenName());
        TextView text = (TextView) convertView.findViewById(R.id.text);
        text.setText(item.getText());
        SmartImageView icon = (SmartImageView) convertView.findViewById(R.id.icon);
        icon.setImageUrl(item.getUser().getProfileImageURL());

        SmartImageView favButton = (SmartImageView) convertView.findViewById(R.id.fav);
        SmartImageView rtButton = (SmartImageView) convertView.findViewById(R.id.rt);
        SmartImageView repButton = (SmartImageView) convertView.findViewById(R.id.rep);
        final Status status = getItem(position);

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.fav:

                        AsyncTaskExample task = new AsyncTaskExample(mContext);
                        task.execute(status.getId());/* Status ID を渡す */

                        Toast.makeText(mContext, "ふぁぼりました", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });


        rtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                switch (view.getId()){
                    case R.id.rt:

                        AsyncTaskRT taskRT = new AsyncTaskRT(mContext);
                        taskRT.execute(status.getId());/* Status ID を渡す */

                        Toast.makeText(mContext, "RTしました", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        repButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                switch (view.getId()){
                    case R.id.rep:


                        Intent intent1 = new Intent(mContext, TweetActivity.class); //TweetActivityに遷移
                        Bundle bundle = new Bundle();
                        bundle.putLong("in_reply_to",status.getId());   //in_reply_toを鍵としてstatusIDを引き出せるようにする。
                        bundle.putString("target",status.getUser().getScreenName());
                        intent1.putExtras(bundle);                      //intentにbundleを付加
                        mContext.startActivity(intent1);                //TweetActivityに遷移

                        //Toast.makeText(mContext, "リプライしました", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return convertView;
    }
}
