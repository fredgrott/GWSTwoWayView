/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.shareme.gwstwowayview;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final String ARG_SELECTED_LAYOUT_ID = "selectedLayoutId";

    private final int DEFAULT_LAYOUT = R.layout.layout_list;

    private int mSelectedLayoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
        }

        mSelectedLayoutId = DEFAULT_LAYOUT;
        if (savedInstanceState != null) {
            mSelectedLayoutId = savedInstanceState.getInt(ARG_SELECTED_LAYOUT_ID);
        }

        addLayoutTab(
                actionBar, R.layout.layout_list, R.mipmap.ic_list, "list");
        addLayoutTab(
                actionBar, R.layout.layout_grid, R.mipmap.ic_grid, "grid");
        addLayoutTab(
                actionBar, R.layout.layout_staggered_grid, R.mipmap.ic_staggered, "staggered");
        addLayoutTab(
                actionBar, R.layout.layout_spannable_grid, R.mipmap.ic_spannable, "spannable");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_SELECTED_LAYOUT_ID, mSelectedLayoutId);
    }

    private void addLayoutTab(ActionBar actionBar, int layoutId, int iconId, String tag) {
        ActionBar.Tab tab = actionBar.newTab()
                .setText("")
                .setIcon(iconId)
                .setTabListener(new TabListener(layoutId, tag));
        actionBar.addTab(tab, layoutId == mSelectedLayoutId);
    }

    public class TabListener implements ActionBar.TabListener {
        private LayoutFragment mFragment;
        private final int mLayoutId;
        private final String mTag;

        public TabListener(int layoutId, String tag) {
            mLayoutId = layoutId;
            mTag = tag;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            mFragment = (LayoutFragment) getSupportFragmentManager().findFragmentByTag(mTag);
            if (mFragment == null) {
                mFragment = (LayoutFragment) LayoutFragment.newInstance(mLayoutId);
                ft.add(R.id.content, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }

            mSelectedLayoutId = mFragment.getLayoutId();
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                ft.detach(mFragment);
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
    }
}