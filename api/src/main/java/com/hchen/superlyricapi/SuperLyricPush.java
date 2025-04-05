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

import androidx.annotation.NonNull;

/**
 * 供音乐软件使用的 API
 *
 * @author 焕晨HChen
 */
public class SuperLyricPush {
    /**
     * 歌曲暂停
     */
    void onStop() {
    }

    /**
     * 歌曲数据更改
     * <p>
     * 可以发送歌词，参数等数据
     */
    void onSuperLyric(@NonNull SuperLyricData data) {
    }
}
