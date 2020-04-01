package com.cheyrouse.gael.realestatemanagerkt.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import com.cheyrouse.gael.realestatemanagerkt.models.Property;
import org.jetbrains.annotations.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
     * @param dollars <-- to convert dollar to euro
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
     */
    // return today date
    @NotNull
    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat(TEXT_DATE, Locale.FRANCE);
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     */

    // return true if network is connected
    @NotNull
    public static Boolean isInternetAvailable(@NotNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            assert connectivityManager != null;
            network = connectivityManager.getActiveNetwork();
        } else
            return true;
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    public static boolean isLocationEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
//        boolean network_enabled = false;
        try {
            assert lm != null;
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gps_enabled;
    }

    @NotNull
    public static String getStringDate(int year, int dayOfMonth, int monthOfYear) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, monthOfYear, dayOfMonth);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(TEXT_DATE, Locale.getDefault());
        return sdf.format(date);
    }

    public static Long getPropertyId(Context ctx, List<Property> propertiesList) {
        int propId;
        Prefs prefs = Prefs.get(ctx);
        int id = prefs.getLastItemClicked();
        if (id == -2) {
            propId = propertiesList.size();
        } else {
            propId = id + 1;
        }
        return (long) propId;
    }

    public static int getPropertyPosition(Context ctx, List<Property> propertiesList) {
        int propPosition;
        Prefs prefs = Prefs.get(ctx);
        int position = prefs.getLastItemClicked();
        if (position == -2) {
            propPosition = propertiesList.size() - 1;
            prefs.storeLastItemClicked(propPosition);
        } else if (position == -1) {
            propPosition = 0;
        } else {
            propPosition = position;
        }
        return propPosition;
    }
}
