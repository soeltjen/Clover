/*
 * Clover - 4chan browser https://github.com/Floens/Clover/
 * Copyright (C) 2014  Floens
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.floens.chan.core.settings;

import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;

import org.floens.chan.Chan;
import org.floens.chan.R;
import org.floens.chan.chan.ChanUrls;
import org.floens.chan.ui.adapter.PostsFilter;
import org.floens.chan.ui.cell.PostCellInterface;
import org.floens.chan.utils.AndroidUtils;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class ChanSettings {
    public enum MediaAutoLoadMode {
        // ALways auto load, either wifi or mobile
        ALL("all"),
        // Only auto load if on wifi
        WIFI("wifi"),
        // Never auto load
        NONE("none");

        public String name;

        MediaAutoLoadMode(String name) {
            this.name = name;
        }

        public static MediaAutoLoadMode find(String name) {
            for (MediaAutoLoadMode mode : MediaAutoLoadMode.values()) {
                if (mode.name.equals(name)) {
                    return mode;
                }
            }
            return null;
        }
    }

    private static Proxy proxy;

    private static final StringSetting theme;
    public static final StringSetting fontSize;
    public static final BooleanSetting fontCondensed;
    public static final BooleanSetting openLinkConfirmation;
    public static final BooleanSetting autoRefreshThread;
    //    public static final BooleanSetting imageAutoLoad;
    public static final StringSetting imageAutoLoadNetwork;
    public static final StringSetting videoAutoLoadNetwork;
    public static final BooleanSetting videoOpenExternal;
    public static final BooleanSetting videoErrorIgnore;
    public static final StringSetting boardViewMode;
    public static final StringSetting boardOrder;

    public static final StringSetting postDefaultName;
    public static final BooleanSetting postPinThread;
    public static final BooleanSetting postNewCaptcha;

    public static final BooleanSetting developer;

    public static final StringSetting saveLocation;
    public static final BooleanSetting saveOriginalFilename;
    public static final BooleanSetting shareUrl;
    public static final BooleanSetting networkHttps;
    public static final BooleanSetting forcePhoneLayout;
    public static final BooleanSetting enableReplyFab;
    public static final BooleanSetting anonymize;
    public static final BooleanSetting anonymizeIds;
    public static final BooleanSetting repliesButtonsBottom;
    public static final BooleanSetting confirmExit;
    public static final BooleanSetting tapNoReply;
    public static final BooleanSetting volumeKeysScrolling;
    public static final BooleanSetting postFullDate;
    public static final BooleanSetting postFileInfo;
    public static final BooleanSetting postFilename;
    public static final BooleanSetting neverHideToolbar;
    public static final BooleanSetting controllerSwipeable;

    public static final BooleanSetting watchEnabled;
    public static final BooleanSetting watchCountdown;
    public static final BooleanSetting watchBackground;
    public static final StringSetting watchBackgroundTimeout;
    public static final StringSetting watchNotifyMode;
    public static final StringSetting watchSound;
    public static final StringSetting watchLed;

    public static final StringSetting passToken;
    public static final StringSetting passPin;
    public static final StringSetting passId;

    public static final BooleanSetting historyEnabled;

    public static final IntegerSetting previousVersion;

    public static final BooleanSetting proxyEnabled;
    public static final StringSetting proxyAddress;
    public static final IntegerSetting proxyPort;

    public static final CounterSetting settingsOpenCounter;
    public static final CounterSetting historyOpenCounter;
    public static final CounterSetting replyOpenCounter;
    public static final CounterSetting threadOpenCounter;

    static {
        SharedPreferences p = AndroidUtils.getPreferences();

        theme = new StringSetting(p, "preference_theme", "light");

        boolean tablet = AndroidUtils.getRes().getBoolean(R.bool.is_tablet);

        fontSize = new StringSetting(p, "preference_font", tablet ? "16" : "14");
        fontCondensed = new BooleanSetting(p, "preference_font_condensed", false);
        openLinkConfirmation = new BooleanSetting(p, "preference_open_link_confirmation", true);
        autoRefreshThread = new BooleanSetting(p, "preference_auto_refresh_thread", true);
//        imageAutoLoad = new BooleanSetting(p, "preference_image_auto_load", true);
        imageAutoLoadNetwork = new StringSetting(p, "preference_image_auto_load_network", MediaAutoLoadMode.WIFI.name);
        videoAutoLoadNetwork = new StringSetting(p, "preference_video_auto_load_network", MediaAutoLoadMode.WIFI.name);
        videoOpenExternal = new BooleanSetting(p, "preference_video_external", false);
        videoErrorIgnore = new BooleanSetting(p, "preference_video_error_ignore", false);
        boardViewMode = new StringSetting(p, "preference_board_view_mode", PostCellInterface.PostViewMode.LIST.name); // "list" or "grid"
        boardOrder = new StringSetting(p, "preference_board_order", PostsFilter.Order.BUMP.name);

        postDefaultName = new StringSetting(p, "preference_default_name", "");
        postPinThread = new BooleanSetting(p, "preference_pin_on_post", false);
        postNewCaptcha = new BooleanSetting(p, "preference_new_captcha", false);

        developer = new BooleanSetting(p, "preference_developer", false);

        saveLocation = new StringSetting(p, "preference_image_save_location", Environment.getExternalStorageDirectory() + File.separator + "Clover");
        saveOriginalFilename = new BooleanSetting(p, "preference_image_save_original", false);
        shareUrl = new BooleanSetting(p, "preference_image_share_url", false);
        networkHttps = new BooleanSetting(p, "preference_network_https", true, new Setting.SettingCallback<Boolean>() {
            @Override
            public void onValueChange(Setting setting, Boolean value) {
                ChanUrls.loadScheme(value);
            }
        });
        forcePhoneLayout = new BooleanSetting(p, "preference_force_phone_layout", false);
        enableReplyFab = new BooleanSetting(p, "preference_enable_reply_fab", true);
        anonymize = new BooleanSetting(p, "preference_anonymize", false);
        anonymizeIds = new BooleanSetting(p, "preference_anonymize_ids", false);
        repliesButtonsBottom = new BooleanSetting(p, "preference_buttons_bottom", false);
        confirmExit = new BooleanSetting(p, "preference_confirm_exit", false);
        tapNoReply = new BooleanSetting(p, "preference_tap_no_reply", false);
        volumeKeysScrolling = new BooleanSetting(p, "preference_volume_key_scrolling", false);
        postFullDate = new BooleanSetting(p, "preference_post_full_date", false);
        postFileInfo = new BooleanSetting(p, "preference_post_file_info", true);
        postFilename = new BooleanSetting(p, "preference_post_filename", false);
        neverHideToolbar = new BooleanSetting(p, "preference_never_hide_toolbar", false);
        controllerSwipeable = new BooleanSetting(p, "preference_controller_swipeable", true);

        watchEnabled = new BooleanSetting(p, "preference_watch_enabled", false, new Setting.SettingCallback<Boolean>() {
            @Override
            public void onValueChange(Setting setting, Boolean value) {
                Chan.getWatchManager().onWatchEnabledChanged(value);
            }
        });
        watchCountdown = new BooleanSetting(p, "preference_watch_countdown", false);
        watchBackground = new BooleanSetting(p, "preference_watch_background_enabled", false, new Setting.SettingCallback<Boolean>() {
            @Override
            public void onValueChange(Setting setting, Boolean value) {
                Chan.getWatchManager().onBackgroundWatchingChanged(value);
            }
        });
        watchBackgroundTimeout = new StringSetting(p, "preference_watch_background_timeout", "60");
        watchNotifyMode = new StringSetting(p, "preference_watch_notify_mode", "all");
        watchSound = new StringSetting(p, "preference_watch_sound", "quotes");
        watchLed = new StringSetting(p, "preference_watch_led", "ffffffff");

        passToken = new StringSetting(p, "preference_pass_token", "");
        passPin = new StringSetting(p, "preference_pass_pin", "");
        passId = new StringSetting(p, "preference_pass_id", "");

        historyEnabled = new BooleanSetting(p, "preference_history_enabled", true);

        previousVersion = new IntegerSetting(p, "preference_previous_version", 0);

        proxyEnabled = new BooleanSetting(p, "preference_proxy_enabled", false, new Setting.SettingCallback<Boolean>() {
            @Override
            public void onValueChange(Setting setting, Boolean value) {
                loadProxy();
            }
        });
        proxyAddress = new StringSetting(p, "preference_proxy_address", "", new Setting.SettingCallback<String>() {
            @Override
            public void onValueChange(Setting setting, String value) {
                loadProxy();
            }
        });
        proxyPort = new IntegerSetting(p, "preference_proxy_port", 80, new Setting.SettingCallback<Integer>() {
            @Override
            public void onValueChange(Setting setting, Integer value) {
                loadProxy();
            }
        });
        loadProxy();

        settingsOpenCounter = new CounterSetting(p, "counter_settings_open");
        historyOpenCounter = new CounterSetting(p, "counter_history_open");
        replyOpenCounter = new CounterSetting(p, "counter_reply_open");
        threadOpenCounter = new CounterSetting(p, "counter_thread_open");

        // Old (but possibly still in some users phone)
        // preference_board_view_mode default "list"
        // preference_board_editor_filler default false
        // preference_pass_enabled default false
        // preference_autoplay false
    }

    public static boolean passLoggedIn() {
        return passId.get().length() > 0;
    }

    public static ThemeColor getThemeAndColor() {
        String themeRaw = ChanSettings.theme.get();

        String theme = themeRaw;
        String color = null;
        String accentColor = null;

        String[] splitted = themeRaw.split(",");
        if (splitted.length >= 2) {
            theme = splitted[0];
            color = splitted[1];
            if (splitted.length == 3) {
                accentColor = splitted[2];
            }
        }

        return new ThemeColor(theme, color, accentColor);
    }

    public static void setThemeAndColor(ThemeColor themeColor) {
        if (TextUtils.isEmpty(themeColor.color) || TextUtils.isEmpty(themeColor.accentColor)) {
            throw new IllegalArgumentException();
        }
        ChanSettings.theme.set(themeColor.theme + "," + themeColor.color + "," + themeColor.accentColor);
    }

    /**
     * Returns a {@link Proxy} if a proxy is enabled, <tt>null</tt> otherwise.
     *
     * @return a proxy or null
     */
    public static Proxy getProxy() {
        return proxy;
    }

    private static void loadProxy() {
        if (proxyEnabled.get()) {
            proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(proxyAddress.get(), proxyPort.get()));
        } else {
            proxy = null;
        }
    }

    public static class ThemeColor {
        public String theme;
        public String color;
        public String accentColor;

        public ThemeColor(String theme, String color, String accentColor) {
            this.theme = theme;
            this.color = color;
            this.accentColor = accentColor;
        }
    }
}
