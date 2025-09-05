package com.nisum.challenge.application.util;

import java.util.Locale;

public final class RequestLocaleHolder {
    private static final ThreadLocal<Locale> HOLDER = new ThreadLocal<>();

    private RequestLocaleHolder() { }

    public static void set(Locale locale) {
        HOLDER.set(locale);
    }

    public static Locale get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
