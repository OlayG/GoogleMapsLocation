package com.example.admin.googlemapslocation.inject;

import com.example.admin.googlemapslocation.receiver.MyStaticReceiver;
import com.example.admin.googlemapslocation.view.MainActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Admin on 8/24/2017.
 */
@Module
public class MainActivityModule {

    @Provides
    MainActivityPresenter providesMainActivityPresenter() {
        return new MainActivityPresenter();
    }
}
