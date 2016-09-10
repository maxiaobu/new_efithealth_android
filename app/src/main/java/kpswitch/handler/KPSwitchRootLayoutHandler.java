/*
 * Copyright (C) 2015-2016 Jacksgong(blog.dreamtobe.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kpswitch.handler;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import kpswitch.IPanelConflictLayout;
import kpswitch.util.StatusBarHeightUtil;

/**
 * Created by Jacksgong on 3/30/16.
 *
 * @see cn.dreamtobe.kpswitch.widget.KPSwitchRootFrameLayout
 * @see cn.dreamtobe.kpswitch.widget.KPSwitchRootLinearLayout
 * @see cn.dreamtobe.kpswitch.widget.KPSwitchRootRelativeLayout
 */
public class KPSwitchRootLayoutHandler {
    private final static String TAG = "KPSRootLayoutHandler";

    private int mOldHeight = -1;
    private final View mTargetRootView;

    private final int mStatusBarHeight;

    public KPSwitchRootLayoutHandler(final View rootView) {
        this.mTargetRootView = rootView;
        this.mStatusBarHeight = StatusBarHeightUtil.getStatusBarHeight(rootView.getContext());
    }

    public void handleBeforeMeasure(final int width, final int height) {
        // 鐢卞綋鍓嶅竷灞�琚敭鐩樻尋鍘嬶紝鑾风煡锛岀敱浜庨敭鐩樼殑娲诲姩锛屽鑷村竷灞�灏嗚鍙戠敓鍙樺寲銆�

        Log.d(TAG, "onMeasure, width: " + width + " height: " + height);
        if (height < 0) {
            return;
        }

        if (mOldHeight < 0) {
            mOldHeight = height;
            return;
        }

        final int offset = mOldHeight - height;

        if (offset == 0) {
            Log.d(TAG, "" + offset + " == 0 break;");
            return;
        }

        if (Math.abs(offset) == mStatusBarHeight) {
            Log.w(TAG, String.format("offset just equal statusBar height %d", offset));
            // 鏋佹湁鍙兘鏄� 鐩稿鏈〉闈㈢殑浜岀骇椤甸潰鐨勪富棰樻槸鍏ㄥ睆&鏄�忔槑锛屼絾鏄湰椤甸潰涓嶆槸鍏ㄥ睆锛屽洜姝や細鏈塻tatus bar鐨勫竷灞�鍙樺寲宸紓锛岃繘琛岃皟杩�
            // 鏋佹湁鍙兘鏄� 璇ュ竷灞�閲囩敤浜嗛�忔槑鐨勮儗鏅�(windowIsTranslucent=true)锛岃�岃儗鍚庣殑甯冨眬`full screen`涓篺alse锛�
            // 鍥犳鏈夊彲鑳界涓�娆＄粯鍒舵椂娌℃湁attach涓妔tatus bar锛岃�岀浜屾status bar attach涓婂幓锛屽鑷翠簡杩欎釜鍙樺寲銆�
            return;
        }

        mOldHeight = height;
        final IPanelConflictLayout panel = getPanelLayout(mTargetRootView);

        if (panel == null) {
            Log.w(TAG, "can't find the valid panel conflict layout, give up!");
            return;
        }

        // 妫�娴嬪埌鐪熸鐨� 鐢变簬閿洏鏀惰捣瑙﹀彂浜嗘湰娆＄殑甯冨眬鍙樺寲

        if (offset > 0) {
            //閿洏寮硅捣 (offset > 0锛岄珮搴﹀彉灏�)
            panel.handleHide();
        } else if (panel.isKeyboardShowing()) {
            // 1. 鎬诲緱鏉ヨ锛屽湪鐩戝惉鍒伴敭鐩樺凡缁忔樉绀虹殑鍓嶆彁涓嬶紝閿洏鏀跺洖鎵嶆槸鏈夋晥鏈夋剰涔夌殑銆�
            // 2. 淇鍦ˋndroid L涓嬩娇鐢╒7.Theme.AppCompat涓婚锛岃繘鍏ctivity锛岄粯璁ゅ脊璧烽潰鏉縝ug锛�
            // 绗�2鐐圭殑bug鍑虹幇鍘熷洜:鍦ˋndroid L涓嬩娇鐢╒7.Theme.AppCompat涓婚锛屽苟涓斾笉浣跨敤绯荤粺鐨凙ctionBar/ToolBar锛孷7.Theme.AppCompat涓婚,杩樻槸浼氬厛榛樿缁樺埗涓�甯ч粯璁ctionBar锛岀劧鍚庡啀灏嗕粬鍘绘帀锛堢暐鏃犺锛�
            //閿洏鏀跺洖 (offset < 0锛岄珮搴﹀彉澶�)
            if (panel.isVisible()) {
                // the panel is showing/will showing
                panel.handleShow();
            }
        }
    }

    private IPanelConflictLayout mPanelLayout;

    private IPanelConflictLayout getPanelLayout(final View view) {
        if (mPanelLayout != null) {
            return mPanelLayout;
        }

        if (view instanceof IPanelConflictLayout) {
            mPanelLayout = (IPanelConflictLayout) view;
            return mPanelLayout;
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                IPanelConflictLayout v = getPanelLayout(((ViewGroup) view).getChildAt(i));
                if (v != null) {
                    mPanelLayout = v;
                    return mPanelLayout;
                }
            }
        }

        return null;
    }
}
