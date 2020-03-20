package com.cheyrouse.gael.realestatemanagerkt.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import com.cheyrouse.gael.realestatemanagerkt.models.Address;
import com.cheyrouse.gael.realestatemanagerkt.models.Property;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.TEXT_DATE;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars
     * @return
     */
    public static double convertDollarToEuro(double dollars) {
        return Math.round(dollars * 0.812);
    }

    // Euros to dollars conversion
    public static double convertEuroToDollar(double euros) {
        return Math.round(euros / 0.812);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return
     */
    // return today date
    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat(TEXT_DATE, Locale.FRANCE);
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param context
     * @return
     */
    // return true if network is connected
    public static Boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOWED_GEOLOCATION_ORIGINS);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static String getStringDate(int year, int dayOfMonth, int monthOfYear) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, monthOfYear, dayOfMonth);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(TEXT_DATE, Locale.getDefault());
        return sdf.format(date);
    }


    public static List<View> makeItemList(View itemView, List<View> itemViewList) {
        itemViewList.add(itemView);
        return itemViewList;
    }
}
