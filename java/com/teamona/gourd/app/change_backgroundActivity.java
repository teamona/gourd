package com.teamona.gourd.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by ruzeya on 2014/09/15.
 */
public class change_backgroundActivity extends Activity {

    private Bitmap bitmap;
    private ImageView imageView;
    private int viewWidth, viewHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_background);
        // ウィンドウマネージャのインスタンス取得
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // ディスプレイのインスタンスを生成
        Display display = manager.getDefaultDisplay();

        // 画像サイズの取得
        /*
        Point point = new Point();
        display.getSize(point);
        viewWidth = point.x;
        viewHeight = point.y;
        */
        viewWidth = display.getWidth();
        viewHeight = display.getHeight();

        imageView = (ImageView) findViewById(R.id.imageView1);

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 画像が保存されてるフォルダにアクセス
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                // 起動先アクティビティからデータを返してもらいたい場合
                startActivityForResult(intent, 0);
            }
        });
    }

    // 起動先アクティビティからデータを返してもらう
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 画像を選択した時のみ実行
        if (data != null) {
            Uri uri = data.getData();
            try {
                bitmap = loadImage(uri, viewWidth, viewHeight);
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
        }
    }

    // 取得したURIを用いて画像を読み込む
    private Bitmap loadImage(Uri uri, int viewWidth, int viewHeight) {
        // Uriから画像を読み込みBitmapを作成
        Bitmap originalBitmap = null;
        try {
            originalBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // MediaStoreから回転情報を取得
        final int orientation;
        Cursor cursor = MediaStore.Images.Media.query(getContentResolver(), uri, new String[]{
                MediaStore.Images.ImageColumns.ORIENTATION
        });
        if (cursor != null) {
            cursor.moveToFirst();
            orientation = cursor.getInt(0);
        } else {
            orientation = 0;
        }

        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();

        // 縮小割合を計算
        final float scale;
        if (orientation == 90 || orientation == 270) {
            // 縦向きの画像は半分のサイズに変更
            scale = Math.min(((float) viewWidth / originalHeight) / 2, ((float) viewHeight / originalWidth) / 2);
        } else {
            // 横向きの画像
            scale = Math.min((float) viewWidth / originalWidth, (float) viewHeight / originalHeight);
        }

        // 変換行列の作成
        final Matrix matrix = new Matrix();
        if (orientation != 0) {
            //画像を回転させる
            matrix.postRotate(orientation);
        }
        if (scale < 1.0f) {
            // Bitmapを拡大縮小する
            matrix.postScale(scale, scale);
        }

        // 行列によって変換されたBitmapを返す
        return Bitmap.createBitmap(originalBitmap, 0, 0, originalWidth, originalHeight, matrix,
                true);


    }
}