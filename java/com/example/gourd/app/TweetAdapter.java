package com.example.gourd.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.image.SmartImageView;
import twitter4j.Status;
import twitter4j.Twitter;

/**
 * Created by ruzeya on 2014/07/23.
 */
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

        return convertView;
    }
}
