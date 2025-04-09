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
import android.media.session.PlaybackState;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;
import java.util.Optional;

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
    private String lyric = null;
    /**
     * 音乐软件的包名
     * <p>
     * 非常建议您设置包名，这是判断当前播放应用的唯一途径
     */
    @NonNull
    private String packageName = "";
    /**
     * 自定义的图标
     */
    @Nullable
    private String base64Icon = null;
    /**
     * 每句歌词的持续时间 (毫秒)
     */
    private int delay = 0;
    /**
     * 当前歌曲的 MediaMetadata 数据
     */
    @Nullable
    private MediaMetadata mediaMetadata;
    /**
     * 当前的播放状态
     * <p>
     * 一般来说可以放在 stop 动作中设置
     */
    @Nullable
    private PlaybackState playbackState;
    /**
     * 可以自定义的附加数据
     */
    @Nullable
    private SuperLyricExtra extra;

    public SuperLyricData() {
    }

    protected SuperLyricData(Parcel in) {
        lyric = in.readString();
        packageName = Optional.ofNullable(
            in.readString()
        ).orElse("");
        base64Icon = in.readString();
        delay = in.readInt();
        mediaMetadata = in.readParcelable(MediaMetadata.class.getClassLoader());
        playbackState = in.readParcelable(PlaybackState.class.getClassLoader());
        extra = in.readParcelable(SuperLyricExtra.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lyric);
        dest.writeString(packageName);
        dest.writeString(base64Icon);
        dest.writeInt(delay);
        dest.writeParcelable(mediaMetadata, flags);
        dest.writeParcelable(playbackState, flags);
        dest.writeParcelable(extra, flags);
    }

    /**
     * 是否存在 MediaMetadata 信息
     */
    public boolean isExistMediaMetadata() {
        return mediaMetadata != null;
    }

    /**
     * 是否存在 SuperLyricExtra 数据
     */
    public boolean isExistSuperLyricExtra() {
        return extra != null;
    }

    /**
     * 是否存在 base64Icon 数据
     */
    public boolean isExistBase64Icon() {
        return base64Icon != null;
    }

    public SuperLyricData setLyric(@Nullable String lyric) {
        this.lyric = lyric;
        return this;
    }

    public SuperLyricData setPackageName(@NonNull String packageName) {
        this.packageName = packageName;
        return this;
    }

    public SuperLyricData setBase64Icon(@Nullable String base64Icon) {
        this.base64Icon = base64Icon;
        return this;
    }

    public SuperLyricData setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public SuperLyricData setMediaMetadata(@Nullable MediaMetadata mediaMetadata) {
        this.mediaMetadata = mediaMetadata;
        return this;
    }

    public SuperLyricData setPlaybackState(@Nullable PlaybackState playbackState) {
        this.playbackState = playbackState;
        return this;
    }

    public SuperLyricData setExtra(@Nullable SuperLyricExtra extra) {
        this.extra = extra;
        return this;
    }

    @Nullable
    public String getLyric() {
        return lyric;
    }

    @NonNull
    public String getPackageName() {
        return packageName;
    }

    @Nullable
    public String getBase64Icon() {
        return base64Icon;
    }

    public int getDelay() {
        return delay;
    }

    @Nullable
    public MediaMetadata getMediaMetadata() {
        return mediaMetadata;
    }

    @Nullable
    public PlaybackState getPlaybackState() {
        return playbackState;
    }

    @Nullable
    public SuperLyricExtra getExtra() {
        return extra;
    }

    /**
     * 获取歌曲的标题，数据来自 MediaMetadata
     * <br/>
     * 请注意，可能有些软件会拿此参数传递歌词
     */
    @NonNull
    public String getTitle() {
        if (mediaMetadata == null) return "Unknown";
        return mediaMetadata.getString(METADATA_KEY_TITLE);
    }

    /**
     * 获取歌曲的艺术家，数据来自 MediaMetadata
     */
    @NonNull
    public String getArtist() {
        if (mediaMetadata == null) return "Unknown";
        return mediaMetadata.getString(METADATA_KEY_ARTIST);
    }

    /**
     * 获取歌曲的专辑，数据来自 MediaMetadata
     */
    @NonNull
    public String getAlbum() {
        if (mediaMetadata == null) return "Unknown";
        return mediaMetadata.getString(METADATA_KEY_ALBUM);
    }

    /**
     * 方便手动封装包裹
     */
    @NonNull
    public Parcel marshall() {
        Parcel parcel = Parcel.obtain();
        writeToParcel(parcel, 0);
        return parcel;
    }

    /**
     * 解包封装并实例化
     */
    @NonNull
    public static SuperLyricData unmarshall(@NonNull Parcel parcel) {
        parcel.setDataPosition(0);
        return new SuperLyricData(parcel);
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
            ", playbackState=" + playbackState +
            ", extra=" + extra +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SuperLyricData data)) return false;
        return delay == data.delay && Objects.equals(lyric, data.lyric) &&
            Objects.equals(packageName, data.packageName) &&
            Objects.equals(base64Icon, data.base64Icon) &&
            Objects.equals(mediaMetadata, data.mediaMetadata) &&
            Objects.equals(playbackState, data.playbackState) &&
            Objects.equals(extra, data.extra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lyric, packageName, base64Icon, delay, mediaMetadata, playbackState, extra);
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
