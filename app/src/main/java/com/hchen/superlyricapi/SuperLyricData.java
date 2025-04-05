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

public class SuperLyricData implements Parcelable {
    public String lyric = "";
    public String packageName = "";
    public String base64Icon = "";
    public int delay = 0;
    public MediaMetadata mediaMetadata;
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

    public String getTitle() {
        if (mediaMetadata == null) return "Unknown";
        return mediaMetadata.getString(METADATA_KEY_TITLE);
    }

    public String getArtist() {
        if (mediaMetadata == null) return "Unknown";
        return mediaMetadata.getString(METADATA_KEY_ARTIST);
    }

    public String getAlbum() {
        if (mediaMetadata == null) return "Unknown";
        return mediaMetadata.getString(METADATA_KEY_ALBUM);
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
