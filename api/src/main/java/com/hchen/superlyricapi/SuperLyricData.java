/*
 * This file is part of SuperLyricApi.
 *
 * SuperLyricApi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * SuperLyricApi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SuperLyricApi. If not, see <https://www.gnu.org/licenses/lgpl-2.1>.
 *
 * Copyright (C) 2023–2025 HChenX
 */
package com.hchen.superlyricapi;

import static android.media.MediaMetadata.METADATA_KEY_ALBUM;
import static android.media.MediaMetadata.METADATA_KEY_ARTIST;
import static android.media.MediaMetadata.METADATA_KEY_TITLE;

import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * 歌曲数据
 *
 * @author 焕晨HChen
 */
public class SuperLyricData implements Parcelable {
    /**
     * 歌词
     */
    @NonNull
    private String lyric = "";
    /**
     * 音乐软件的包名
     * <p>
     * 请务必设置包名，这是判断当前播放应用的唯一途径
     */
    @NonNull
    private String packageName = "";
    /**
     * 音乐软件的图标
     * <p>
     * Note：用途有限，要求传递方提供 Base64 样式的 Icon 不太现实
     * <p>
     * 如果愿意您依然可以使用此方法传递 Base64 样式的 Icon
     *
     * @deprecated
     */
    @NonNull
    @Deprecated(since = "1.8")
    private String base64Icon = "";
    /**
     * 当前歌词的持续时间 (毫秒)
     */
    private int delay = 0;
    /**
     * 当前歌曲的 MediaMetadata 数据
     * <p>
     * Note：请注意，MediaMetadata 数据内的 Bitmap 数据已被抹去
     * <p>
     * 因为部分设备传递 MediaMetadata 内 Bitmap 数据时会因为其大小超出 Binder 限制而导致 Binder 破裂
     * <p>
     * 因此 API 主动抹去 MediaMetadata 中 Bitmap 数据以规避 Binder 破裂风险
     */
    @Nullable
    private MediaMetadata mediaMetadata;
    /**
     * 当前的播放状态
     * <p>
     * 建议在播放状态暂停时设置
     */
    @Nullable
    private PlaybackState playbackState;
    /**
     * 自定义附加数据
     */
    @Nullable
    private Bundle extra;
    /**
     * Extra 包中用于存储歌词翻译数据的 Key 值
     * <p>
     * 我们将使用此 Key 存储歌词翻译数据
     */
    private static final String KEY_TRANSLATION = "key_translation";
    /**
     * Extra 包中用于存储逐字歌词数据的 Key 值
     * <p>
     * 我们将使用此 Key 存储逐字歌词数据
     */
    private static final String KEY_ENHANCED_LRC_DATA = "key_enhanced_lrc_data";

    /**
     * Extra 包中用于存储对唱歌词位置数据的 Key 值
     * <p>
     * 我们将使用此 Key 存储对唱歌词位置数据
     */
    private static final String KEY_DUTE = "key_duet";

    public SuperLyricData() {
    }

    /**
     * 是否存在歌词数据
     */
    public boolean isExistLyric() {
        return !lyric.isEmpty();
    }

    /**
     * 是否存在翻译数据
     */
    public boolean isExistTranslation() {
        return extra != null && extra.containsKey(KEY_TRANSLATION);
    }

    /**
     * 是否存在逐字歌词数据
     */
    public boolean isExistEnhancedLRCData() {
        return extra != null && extra.containsKey(KEY_ENHANCED_LRC_DATA);
    }

    /**
     * 是否存在对唱歌词位置数据
     */
    public boolean isExistDute() {
        return extra != null && extra.containsKey(KEY_DUTE);
    }

    /**
     * 是否存在包名数据
     */
    public boolean isExistPackageName() {
        return !packageName.isEmpty();
    }

    /**
     * 是否存在 Delay 数据
     */
    public boolean isExistDelay() {
        return delay > 0;
    }

    /**
     * 是否存在 Base64 Icon 数据
     *
     * @deprecated
     */
    @Deprecated(since = "1.8")
    public boolean isExistBase64Icon() {
        return !base64Icon.isEmpty();
    }

    /**
     * 是否存在 MediaMetadata 数据
     */
    public boolean isExistMediaMetadata() {
        return Objects.nonNull(mediaMetadata);
    }

    /**
     * 是否存在 PlaybackState 数据
     */
    public boolean isExistPlaybackState() {
        return Objects.nonNull(playbackState);
    }

    /**
     * 是否存在附加数据
     */
    public boolean isExistExtra() {
        return Objects.nonNull(extra);
    }

    public SuperLyricData setLyric(@NonNull String lyric) {
        if (Objects.isNull(lyric)) lyric = "";
        this.lyric = lyric;
        return this;
    }

    public SuperLyricData setTranslation(@NonNull String translation) {
        if (Objects.nonNull(translation)) {
            if (this.extra == null) this.extra = new Bundle();
            this.extra.putString(KEY_TRANSLATION, translation);
        }
        return this;
    }
    /**
     * 设置对唱数据
     * @param duet 是否对唱
     */
    public SuperLyricData setDuet(@NonNull Boolean duet) {
        if (Objects.nonNull(duet)) {
            if (this.extra == null) this.extra = new Bundle();
            this.extra.putBoolean(KEY_DUTE, duet);
        }
        return this;
    }

    public SuperLyricData setEnhancedLRCData(@NonNull EnhancedLRCData[] data) {
        if (Objects.nonNull(data)) {
            if (this.extra == null) this.extra = new Bundle();
            this.extra.putParcelableArray(KEY_ENHANCED_LRC_DATA, data);
        }
        return this;
    }

    public SuperLyricData setPackageName(@NonNull String packageName) {
        if (Objects.isNull(packageName)) packageName = "";
        this.packageName = packageName;
        return this;
    }

    @Deprecated(since = "1.8")
    public SuperLyricData setBase64Icon(@NonNull String base64Icon) {
        if (Objects.isNull(base64Icon)) base64Icon = "";
        this.base64Icon = base64Icon;
        return this;
    }

    public SuperLyricData setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public SuperLyricData setMediaMetadata(@NonNull MediaMetadata mediaMetadata) {
        this.mediaMetadata = SuperLyricTool.removeMediaMetadataBitmap(mediaMetadata);
        return this;
    }

    public SuperLyricData setPlaybackState(@NonNull PlaybackState playbackState) {
        this.playbackState = playbackState;
        return this;
    }

    public SuperLyricData setExtra(@NonNull Bundle extra) {
        if (this.extra == null) this.extra = extra;
        else this.extra.putAll(extra);
        return this;
    }

    @NonNull
    public String getLyric() {
        return lyric;
    }

    @Nullable
    public String getTranslation() {
        return extra != null ? extra.getString(KEY_TRANSLATION) : null;
    }
    /**
     * 获取对唱数据
     * @return {@link Boolean}  false 表示左，true 表示右边
     */
    @Nullable
    public Boolean getDute() {
        return extra != null ? extra.getBoolean(KEY_DUTE,false) : null;
    }

    @Nullable
    public EnhancedLRCData[] getEnhancedLRCData() {
        if (extra == null) return null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            return extra.getParcelableArray(KEY_ENHANCED_LRC_DATA, EnhancedLRCData.class);
        else return (EnhancedLRCData[]) extra.getParcelableArray(KEY_ENHANCED_LRC_DATA);
    }

    @NonNull
    public String getPackageName() {
        return packageName;
    }

    @NonNull
    @Deprecated(since = "1.8")
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
    public Bundle getExtra() {
        return extra;
    }

    /**
     * 获取歌曲的标题，数据来自 MediaMetadata
     * <br/>
     * 请注意，蓝牙歌词状态可能使用此参数传递歌词
     */
    @NonNull
    public String getTitle() {
        if (mediaMetadata == null) return "Unknown";

        return Optional.ofNullable(
                mediaMetadata.getString(METADATA_KEY_TITLE)
        ).orElse("Unknown");
    }

    /**
     * 获取歌曲的艺术家，数据来自 MediaMetadata
     */
    @NonNull
    public String getArtist() {
        if (mediaMetadata == null) return "Unknown";

        return Optional.ofNullable(
                mediaMetadata.getString(METADATA_KEY_ARTIST)
        ).orElse("Unknown");
    }

    /**
     * 获取歌曲的专辑，数据来自 MediaMetadata
     */
    @NonNull
    public String getAlbum() {
        if (mediaMetadata == null) return "Unknown";

        return Optional.ofNullable(
                mediaMetadata.getString(METADATA_KEY_ALBUM)
        ).orElse("Unknown");
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
        return delay == data.delay &&
                Objects.equals(lyric, data.lyric) &&
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

    public static final Creator<SuperLyricData> CREATOR = new Creator<SuperLyricData>() {
        @NonNull
        @Override
        public SuperLyricData createFromParcel(Parcel in) {
            return new SuperLyricData(in);
        }

        @NonNull
        @Override
        public SuperLyricData[] newArray(int size) {
            return new SuperLyricData[size];
        }
    };

    private SuperLyricData(@NonNull Parcel in) {
        lyric = Optional.ofNullable(in.readString()).orElse("");
        packageName = Optional.ofNullable(in.readString()).orElse("");
        base64Icon = Optional.ofNullable(in.readString()).orElse("");
        delay = in.readInt();
        mediaMetadata = in.readParcelable(MediaMetadata.class.getClassLoader());
        playbackState = in.readParcelable(PlaybackState.class.getClassLoader());
        extra = in.readBundle(getClass().getClassLoader());
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(lyric);
        dest.writeString(packageName);
        dest.writeString(base64Icon);
        dest.writeInt(delay);
        dest.writeParcelable(mediaMetadata, flags);
        dest.writeParcelable(playbackState, flags);
        dest.writeBundle(extra);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 逐字歌词数据信息
     * <p>
     * 我们将通过此数据包实现对逐字歌词的支持
     */
    public static class EnhancedLRCData implements Parcelable {
        /**
         * 单词
         * <p>
         * 我们将使用此字段传递当前歌词的某个单词数据
         */
        @NonNull
        private String word = "";
        /**
         * 持续时间 (毫秒)
         * <p>
         * 当前单词的持续时间
         */
        private int delay = 0;

        public EnhancedLRCData(@NonNull String word, int delay) {
            if (Objects.isNull(word)) word = "";
            this.word = word;
            this.delay = delay;
        }

        @NonNull
        public String getWord() {
            return word;
        }

        public int getDelay() {
            return delay;
        }

        @NonNull
        @Override
        public String toString() {
            return "EnhancedLRCData{" +
                    "word='" + word + '\'' +
                    ", delay=" + delay +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof EnhancedLRCData that)) return false;
            return delay == that.delay && Objects.equals(word, that.word);
        }

        @Override
        public int hashCode() {
            return Objects.hash(word, delay);
        }

        public static final Creator<EnhancedLRCData> CREATOR = new Creator<EnhancedLRCData>() {
            @NonNull
            @Override
            public EnhancedLRCData createFromParcel(Parcel in) {
                return new EnhancedLRCData(in);
            }

            @NonNull
            @Override
            public EnhancedLRCData[] newArray(int size) {
                return new EnhancedLRCData[size];
            }
        };

        private EnhancedLRCData(@NonNull Parcel in) {
            word = Optional.ofNullable(in.readString()).orElse("");
            delay = in.readInt();
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(word);
            dest.writeInt(delay);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }
}
