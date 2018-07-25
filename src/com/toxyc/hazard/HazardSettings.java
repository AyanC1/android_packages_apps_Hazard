/*
 * Copyright (C) 2018 ToxycOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.toxyc.hazard;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.SwitchPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.FrameLayout;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.Utils;

import com.toxyc.hazard.about.FancyAboutPage;

import com.toxyc.hazard.tabs.StatusBar;
import com.toxyc.hazard.tabs.Recents;
import com.toxyc.hazard.tabs.Lockscreen;
import com.toxyc.hazard.tabs.System;

import com.toxyc.hazard.fab.fab.FloatingActionButton;
import com.toxyc.hazard.fab.MenuItemView;
import com.toxyc.hazard.fab.OnMenuActionListener;
import com.toxyc.hazard.fab.SpringFloatingActionMenu;

public class HazardSettings extends SettingsPreferenceFragment implements
        View.OnClickListener {

    private static int[] frameAnimRes = new int[]{
            R.mipmap.compose_anim_1,
            R.mipmap.compose_anim_2,
            R.mipmap.compose_anim_3,
            R.mipmap.compose_anim_4,
            R.mipmap.compose_anim_5,
            R.mipmap.compose_anim_6,
            R.mipmap.compose_anim_7,
            R.mipmap.compose_anim_8,
            R.mipmap.compose_anim_9,
            R.mipmap.compose_anim_10,
            R.mipmap.compose_anim_11,
            R.mipmap.compose_anim_12,
            R.mipmap.compose_anim_13,
            R.mipmap.compose_anim_14,
            R.mipmap.compose_anim_15,
            R.mipmap.compose_anim_15,
            R.mipmap.compose_anim_16,
            R.mipmap.compose_anim_17,
            R.mipmap.compose_anim_18,
            R.mipmap.compose_anim_19
    };

    private SpringFloatingActionMenu springFloatingActionMenu;
    private int frameDuration = 20;
    private AnimationDrawable frameAnim;
    private AnimationDrawable frameReverseAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hazard, container, false);
        FancyAboutPage fancyAboutPage = (FancyAboutPage)view. findViewById(R.id.fancyaboutpage);

        createFabFrameAnim();
        createFabReverseFrameAnim();

        final FrameLayout fl = (FrameLayout)view. findViewById(R.id.hazard);

        final FloatingActionButton fab = new FloatingActionButton(this.getActivity());
        fab.setType(FloatingActionButton.TYPE_NORMAL);
        fab.setImageDrawable(frameAnim);
        fab.setColorPressedResId(R.color.colorPrimary);
        fab.setColorNormalResId(R.color.fab);
        fab.setColorRippleResId(R.color.text_color);
        fab.setShadow(true);
        springFloatingActionMenu = new SpringFloatingActionMenu.Builder(this.getActivity())
                .fab(fab)
                .addMenuItem(R.string.status_bar_category, R.color.text_color, R.color.fab_statusbar, R.drawable.fab_statusbar,new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(springFloatingActionMenu.isMenuOpen()) {
                           springFloatingActionMenu.hideMenu();
                        Fragment statusbar = new StatusBar();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.hazard, statusbar);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        fl.setVisibility((fl.getVisibility() == View.VISIBLE)
                          ? View.VISIBLE
                          : View.VISIBLE);
                        }
                    }
                })
                .addMenuItem(R.string.recents_category, R.color.text_color, R.color.fab_recents, R.drawable.fab_recents,new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(springFloatingActionMenu.isMenuOpen()) {
                           springFloatingActionMenu.hideMenu();
                        Fragment recents = new Recents();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.hazard, recents);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        fl.setVisibility((fl.getVisibility() == View.VISIBLE)
                          ? View.VISIBLE
                          : View.VISIBLE);
                        }
                    }
                })
                .addMenuItem(R.string.lockscreen_category, R.color.text_color, R.color.fab_lockscreen, R.drawable.fab_lockscreen,new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(springFloatingActionMenu.isMenuOpen()) {
                           springFloatingActionMenu.hideMenu();
                        Fragment lockscreen = new Lockscreen();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.hazard, lockscreen);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        fl.setVisibility((fl.getVisibility() == View.VISIBLE)
                          ? View.VISIBLE
                          : View.VISIBLE);
                        }
                    }
                })
                .addMenuItem(R.string.system_category, R.color.text_color, R.color.fab_system, R.drawable.fab_system,new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(springFloatingActionMenu.isMenuOpen()) {
                           springFloatingActionMenu.hideMenu();
                        Fragment system = new System();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.hazard, system);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        fl.setVisibility((fl.getVisibility() == View.VISIBLE)
                          ? View.VISIBLE
                          : View.VISIBLE);
                        }
                    }
                })
                .animationType(SpringFloatingActionMenu.ANIMATION_TYPE_TUMBLR)
                .revealColor(R.color.colorPrimary)
                .onMenuActionListner(new OnMenuActionListener() {
                    @Override
                    public void onMenuOpen() {
                        fab.setImageDrawable(frameAnim);
                        frameReverseAnim.stop();
                        frameAnim.start();
                    }

                    @Override
                    public void onMenuClose() {
                        fab.setImageDrawable(frameReverseAnim);
                        frameAnim.stop();
                        frameReverseAnim.start();
                    }
                })
                .build();

        fancyAboutPage.setCover(R.drawable.coverimg);
        fancyAboutPage.addTwitterLink("https://twitter.com/toxycos");
        fancyAboutPage.addGoogleLink("https://plus.google.com/u/0/communities/112431558642001786170");
        fancyAboutPage.addTelegramLink("https://t.me/toxycos");
        fancyAboutPage.addGitHubLink("https://github.com/ToxycOS");

        return view;

    }

    private void createFabFrameAnim() {
        frameAnim = new AnimationDrawable();
        frameAnim.setOneShot(true);
        Resources resources = getResources();
        for (int res : frameAnimRes) {
            frameAnim.addFrame(resources.getDrawable(res), frameDuration);
        }
    }

    private void createFabReverseFrameAnim() {
        frameReverseAnim = new AnimationDrawable();
        frameReverseAnim.setOneShot(true);
        Resources resources = getResources();
        for (int i = frameAnimRes.length - 1; i >= 0; i--) {
            frameReverseAnim.addFrame(resources.getDrawable(frameAnimRes[i]), frameDuration);
        }
    }

    public void onBackPressed() {
        if(springFloatingActionMenu.isMenuOpen())
            springFloatingActionMenu.hideMenu();
    }

    @Override
    public void onClick(View v) {
        Log.d("TAG eg","onclick");
        MenuItemView menuItemView = (MenuItemView) v;
        Toast.makeText(this.getActivity(),menuItemView.getLabelTextView().getText(),Toast.LENGTH_SHORT).show();
        }

    class PagerAdapter extends FragmentPagerAdapter {

        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = new StatusBar();
            frags[1] = new Recents();
            frags[2] = new Lockscreen();
            frags[3] = new System();
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private String[] getTitles() {
        String titleString[];
        titleString = new String[]{
                    getString(R.string.status_bar_category),
                    getString(R.string.recents_category),
                    getString(R.string.lockscreen_category),
                    getString(R.string.system_category)};

        return titleString;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.HAZARD;
    }

}