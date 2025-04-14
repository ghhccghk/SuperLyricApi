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
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

/**
 * 供音乐软件使用的 API
 *
 * @author 焕晨HChen
 */
public class SuperLyricPush {

    private SuperLyricPush() {
    }

    /**
     * 歌曲暂停
     * <p>
     * 主要用于传递暂停播放的应用的包名，当然你也可以传更多参数
     */
    public static void onStop(@NonNull SuperLyricData data) {
    }

    /**
     * 歌曲数据更改
     * <p>
     * 可以发送歌词，参数等数据
     */
    public static void onSuperLyric(@NonNull SuperLyricData data) {
    }

    // ------------------------- 受保护的 API 方法，正常情况请勿使用！ -------------------------------
    // ------------------------------- 非信任 APP 使用无效！---------------------------------------

    /**
     * 注册歌词控制器，注册成功后返回控制器
     * <p>
     * 使用此控制器，你可以向所有接收器发送歌词，或暂停状态
     * <p>
     * 一个软件最多只能注册一个控制器，重复注册返回的控制器相同
     *
     * @param context  上下文
     * @param consumer 回调 （可能永远不会被执行）
     * @throws RuntimeException 绑定失败
     */
    public static void registerSuperLyricController(@NonNull Context context, @NonNull Consumer<ISuperLyric> consumer) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (intent == null) return;
        Bundle bundle = intent.getBundleExtra("super_lyric_info");
        if (bundle == null) return;
        IBinder iBinder = bundle.getBinder("super_lyric_controller");
        if (iBinder == null) return;

        try {
            final Messenger clientMessenger = new Messenger(
                new Handler(msg -> {
                    IBinder binder = msg.getData().getBinder("reply");
                    if (binder == null) return true;
                    consumer.accept(ISuperLyric.Stub.asInterface(binder));
                    return true;
                })
            );

            Messenger messenger = new Messenger(iBinder);
            Message message = Message.obtain();
            message.replyTo = clientMessenger;
            Bundle obj = new Bundle();
            obj.putString("super_lyric_controller_package", context.getPackageName());
            message.setData(obj);
            // message.obj = context.getPackageName(); // 会崩溃
            messenger.send(message);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 注销控制器
     * <p>
     * 非常不建议使用，你没有必要手动取消注册
     * <p>
     * 因为实例会一直存在，并不会因为取消注册而消失
     * <p>
     * 更推荐您重复使用已有的控制器实例
     *
     * @param context 上下文
     * @deprecated
     */
    @Deprecated
    public static void unregisterSuperLyricController(@NonNull Context context) {
        Intent intent = new Intent("Super_Lyric");
        Bundle bundle = new Bundle();
        bundle.putString("super_lyric_un_controller", context.getPackageName());
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }
}
