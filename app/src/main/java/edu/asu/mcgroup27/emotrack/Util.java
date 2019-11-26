package edu.asu.mcgroup27.emotrack;

import android.content.Context;
import android.content.SharedPreferences;

public class Util {
    public static final String biometricSetting = "biometricSetting";

    public static void setBiometric(Context context, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(biometricSetting, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(biometricSetting, value);
        editor.commit();
    }

    public static int getBiometric(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(biometricSetting, Context.MODE_PRIVATE);
        return sharedPref.getInt(biometricSetting, 0);
    }

}
