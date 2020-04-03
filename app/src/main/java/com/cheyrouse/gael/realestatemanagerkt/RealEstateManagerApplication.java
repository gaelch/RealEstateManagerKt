package com.cheyrouse.gael.realestatemanagerkt;

import android.app.Application;
import android.content.Context;

public class RealEstateManagerApplication extends Application {

    private static Boolean searchCalls = false;
    private static int lastItemClicked = 0;

            @Override
            public void onCreate() {
                super.onCreate();
                sApplication = this;
            }

    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    public static boolean isSearch() { return searchCalls; }

    public static void setSearchCalls(Boolean search) { searchCalls = search; }

    public static int getLastItemClicked() { return lastItemClicked; }

    public static void setLastItemClicked(int item) { lastItemClicked = item; }
}
