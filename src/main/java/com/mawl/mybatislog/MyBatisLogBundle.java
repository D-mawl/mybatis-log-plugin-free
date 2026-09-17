package com.mawl.mybatislog;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

/**
 * Provides localized messages owned by the plugin.
 */
public final class MyBatisLogBundle {
    @NonNls
    private static final String BUNDLE = "messages.MyBatisLogBundle";
    private static final DynamicBundle INSTANCE = new DynamicBundle(MyBatisLogBundle.class, BUNDLE);

    private MyBatisLogBundle() {
    }

    public static @NotNull @Nls String message(
            @NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
            Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }
}
