package com.example.gourd.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.prefs.Preferences;

/**
 * Created by ruzeya on 2014/09/15.
 */
public class ChangeBackground {

    private Bitmap bitmap;
    private ImageView imageView;
    private int viewWidth, viewHeight;
    private Context context;

    public ChangeBackground(Context context, ImageView imageView) {// ウィンドウマネージャのインスタンス取得
        this.context = context;
        this.imageView = imageView;

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // ディスプレイのインスタンスを生成
        Display display = manager.getDefaultDisplay();

        viewWidth = display.getWidth();
        viewHeight = display.getHeight();
    }

    public void onButtonClick(Activity activity) {
        // 画像が保存されてるフォルダにアクセス
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        // 起動先アクティビティからデータを返してもらいたい場合
        activity.startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 画像を選択した時のみ実行
        if (data != null) {
            Uri uri = data.getData();
            try {
                bitmap = loadImage(uri, viewWidth, viewHeight);
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
            Preferences1 prefs1 = new Preferences1(context);
            prefs1.setStringPreference("background", uri.toString());
        }
    }

    public void loadFromUri(Uri uri) {
        // 画像を選択した時のみ実行
        try {
            bitmap = loadImage(uri, viewWidth, viewHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
    }

    public Uri stringToUri(String uriString) {

        // 設定ファイルから読み込んだパスが空でなければ
        if (uriString != null && !uriString.equals("")) {
            // StringをUriに変換する
            return Uri.parse(uriString);
        }
        return null;
    }

    private Bitmap loadImage(Uri uri, int viewWidth, int viewHeight) {
        // Uriから画像を読み込みBitmapを作成
        Bitmap originalBitmap = null;
        try {
            originalBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // MediaStoreから回転情報を取得
        final int orientation;
        Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(), uri, new String[]{
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
