/*
 * This file is part of SuperLyricApi.

 * SuperLyricApi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.

 * Copyright (C) 2023-2025 HChenX
 */
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

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;

/**
 * 工具
 *
 * @author 焕晨HChen
 */
public class SuperLyricTool {
    /**
     * API 是否成功启用，供音乐软件检查使用
     */
    public static boolean isEnabled = false;
    /**
     * 当前 API 版本
     */
    public static int apiVersion = BuildConfig.API_VERSION;

    /**
     * Base64 转 Bitmap
     */
    public static Bitmap base64ToBitmap(@NonNull String base64) {
        try {
            if (base64.isEmpty()) return null;

            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Drawable 转 Base64
     */
    public static String drawableToBase64(@NonNull Drawable drawable) {
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

    /**
     * Bitmap 转 Base64
     */
    public static String bitmapToBase64(@NonNull Bitmap bitmap) {
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

    /**
     * 注册 SuperLyric 回调
     */
    public static void registerSuperLyric(@NonNull Context context, @NonNull ISuperLyric.Stub superLyric) {
        registerSuperLyric(context, false, superLyric);
    }

    /**
     * 注册 SuperLyric 回调
     *
     * @param context     上下文信息
     * @param selfControl 是否自己控制歌曲是否暂停和发布 MediaMetadata 信息，否则由系统控制
     * @param superLyric  回调
     */
    public static void registerSuperLyric(@NonNull Context context, boolean selfControl, @NonNull ISuperLyric.Stub superLyric) {
        Intent intent = new Intent("Super_Lyric");
        Bundle bundle = new Bundle();
        bundle.putBoolean("super_lyric_self_control", selfControl);
        bundle.putBinder("super_lyric_binder", superLyric);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }
}
