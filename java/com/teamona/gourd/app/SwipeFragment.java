package com.teamona.gourd.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by teamona on 16/04/19.
 */
//3階層
public class SwipeFragment extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    //親ActivityのonCreateの直後に呼び出される
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AdapterHolder holder = (AdapterHolder) getActivity();
        TweetAdapter adapter = holder.getAdapter(EnumTab.HOME);

        ListView listView = (ListView)getView().findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
