package com.example.admin.googlemapslocation.inject;

import com.example.admin.googlemapslocation.MainActivity;

import dagger.Component;

/**
 * Created by Admin on 8/24/2017.
 */
@Component(modules = MainActivityModule.class)
public interface MainActivityComponent {

    void inject(MainActivity mainActivity);
}
