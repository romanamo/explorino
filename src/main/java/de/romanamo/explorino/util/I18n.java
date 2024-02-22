package de.romanamo.explorino.util;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class handling internationalization
 * and support for different languages.
 */
public final class I18n {

    private final static String BUNDLE_KEY = "I18n";

    private static ResourceBundle bundle;

    private I18n() {

    }

    /**
     * Gets the {@link Locale}.
     *
     * @return current Locale
     */
    public static Locale getLocale() {
        return Locale.getDefault();
    }

    /**
     * Sets the {@link Locale}.
     *
     * @param locale Locale to set
     */
    public static void setLocale(Locale locale) {
        Locale.setDefault(locale);
    }

    /**
     * Check if {@link Locale} is supported.
     *
     * @param locale locale to check
     * @return true if supported, else false
     */
    public static boolean isSupported(Locale locale) {
        Locale[] availableLocales = Locale.getAvailableLocales();
        return Arrays.asList(availableLocales).contains(locale);
    }

    /**
     * Gets the message from the loaded language bundle.
     *
     * @param key key of the message
     * @return requested message according to {@link Locale}
     */
    public static String getMessage(String key) {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(BUNDLE_KEY);
        }
        return bundle.getString(key);
    }

    /**
     * Gets the message from the loaded language bundle with option to format.
     *
     * @param key      key of the message
     * @param argument arguments to format in
     * @return requested formatted message according to {@link Locale}
     */
    public static String getMessage(String key, Object... argument) {
        return MessageFormat.format(getMessage(key), argument);
    }
}
