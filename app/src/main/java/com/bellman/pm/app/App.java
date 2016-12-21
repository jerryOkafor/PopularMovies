package com.bellman.pm.app;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Potencio on 12/16/2016. @ 1:10 PM
 * For PopularMovies
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //inti the stetho
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

    }
}
