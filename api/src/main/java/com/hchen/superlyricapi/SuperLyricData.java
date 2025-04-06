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

import static android.media.MediaMetadata.METADATA_KEY_ALBUM;
import static android.media.MediaMetadata.METADATA_KEY_ARTIST;
import static android.media.MediaMetadata.METADATA_KEY_TITLE;

import android.media.MediaMetadata;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * 歌词数据
 *
 * @author 焕晨HChen
 */
public class SuperLyricData implements Parcelable {
    /**
     * 歌词
     */
    @Nullable
    public String lyric = null;
    /**
     * 音乐软件的包名
     */
    @Nullable
    public String packageName = null;
    /**
     * 自定义的图标
     */
    @Nullable
    public String base64Icon = null;
    /**
     * 每句歌词的持续时间 (毫秒)
     */
    public int delay = 0;
    /**
     * 当前歌曲的 MediaMetadata 数据
     */
    @Nullable
    public MediaMetadata mediaMetadata;
    /**
     * 可以自定义的附加数据
     */
    @Nullable
    public SuperLyricExtra extra;

    public SuperLyricData() {
    }

    protected SuperLyricData(Parcel in) {
        lyric = in.readString();
        packageName = in.readString();
        base64Icon = in.readString();
        delay = in.readInt();
        mediaMetadata = in.readParcelable(MediaMetadata.class.getClassLoader());
        extra = in.readParcelable(SuperLyricExtra.class.getClassLoader());
    }

    /**
     * 获取歌曲的标题
     * <br/>
     * 请注意，可能有些软件会拿此参数传递歌词
     */
    @NonNull
    public String getTitle() {
        if (mediaMetadata == null) return "Unknown";
        return mediaMetadata.getString(METADATA_KEY_TITLE);
    }

    /**
     * 获取歌曲的艺术家
     */
    @NonNull
    public String getArtist() {
        if (mediaMetadata == null) return "Unknown";
        return mediaMetadata.getString(METADATA_KEY_ARTIST);
    }

    /**
     * 获取歌曲的专辑
     */
    @NonNull
    public String getAlbum() {
        if (mediaMetadata == null) return "Unknown";
        return mediaMetadata.getString(METADATA_KEY_ALBUM);
    }

    /**
     * 方便手动封装包裹
     */
    public Parcel marshall() {
        Parcel parcel = Parcel.obtain();
        writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        return parcel;
    }

    @NonNull
    @Override
    public String toString() {
        return "SuperLyricData{" +
            "lyric='" + lyric + '\'' +
            ", packageName='" + packageName + '\'' +
            ", base64Icon='" + base64Icon + '\'' +
            ", delay=" + delay +
            ", mediaMetadata=" + mediaMetadata +
            ", extra=" + extra +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SuperLyricData data)) return false;
        return delay == data.delay && Objects.equals(lyric, data.lyric) &&
            Objects.equals(packageName, data.packageName) &&
            Objects.equals(base64Icon, data.base64Icon) &&
            Objects.equals(mediaMetadata, data.mediaMetadata)
            && Objects.equals(extra, data.extra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lyric, packageName, base64Icon, delay, mediaMetadata, extra);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lyric);
        dest.writeString(packageName);
        dest.writeString(base64Icon);
        dest.writeInt(delay);
        dest.writeParcelable(mediaMetadata, flags);
        dest.writeParcelable(extra, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SuperLyricData> CREATOR = new Creator<SuperLyricData>() {
        @Override
        public SuperLyricData createFromParcel(Parcel in) {
            return new SuperLyricData(in);
        }

        @Override
        public SuperLyricData[] newArray(int size) {
            return new SuperLyricData[size];
        }
    };
}
