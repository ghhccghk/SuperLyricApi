package com.hchen.superlyricapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class SuperLyricTool {
    public static boolean isEnabled = false;
    public static int apiVersion = BuildConfig.API_VERSION;

    public static Bitmap base64ToBitmap(String base64) {
        try {
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Throwable e) {
            return null;
        }
    }

    public static String drawableToBase64(Drawable drawable) {
        if (drawable instanceof AdaptiveIconDrawable adaptiveIconDrawable) {
            return adaptiveIconDrawableBase64(adaptiveIconDrawable);
        }

        if (drawable instanceof BitmapDrawable bitmapDrawable) {
            return bitmapToBase64(bitmapDrawable.getBitmap());
        } else if (drawable instanceof VectorDrawable vectorDrawable) {
            return bitmapToBase64(makeDrawableToBitmap(vectorDrawable));
        }
        return "";
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private static String adaptiveIconDrawableBase64(AdaptiveIconDrawable drawable) {
        Drawable background = drawable.getBackground();
        Drawable foreground = drawable.getForeground();
        if (background != null && foreground != null) {
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{background, foreground});
            Bitmap createBitmap = Bitmap.createBitmap(layerDrawable.getIntrinsicWidth(), layerDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            layerDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            layerDrawable.draw(canvas);
            return bitmapToBase64(createBitmap);
        }
        return "";
    }

    private static Bitmap makeDrawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static void registerSuperLyric(Context context, ISuperLyric.Stub superLyric) {
        Intent intent = new Intent("Super_Lyric");
        Bundle bundle = new Bundle();
        bundle.putBinder("super_lyric_binder", superLyric);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }
}
