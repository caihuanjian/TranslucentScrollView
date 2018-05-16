package com.yyh.status.key.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;


public class StatusBarUtils {


    /**
     * @author: HwanJ.Choi
     * @Description: 内容全屏显示，状态栏透明
     */
    public static void setTranslucentStatusBar(Context context) {
        if (!(context instanceof Activity))
            return;
        AppCompatActivity activity = (AppCompatActivity) context;
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        if (Build.VERSION.SDK_INT >= 21) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN   //状态栏浮在activity之上,有LAYOUT的为浮于上方
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    /**
     * @Description:透明状态栏和透明导航栏
     */
    public static void setTranslucentStausBarAndNavigation(Context context) {
        if (!(context instanceof Activity))
            return;
        AppCompatActivity activity = (AppCompatActivity) context;
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        if (Build.VERSION.SDK_INT >= 21) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //状态栏浮于activity上方
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;//导航栏浮于activity上方
            decorView.setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }


    /**
     * @author: HwanJ.Choi
     * @Description: 内容全屏显示，无状态栏，但是在下拉屏幕是会把状态栏显示出来
     */
    public static void setFullScreen(Context context) {
        if (!(context instanceof Activity))
            return;
        AppCompatActivity activity = (AppCompatActivity) context;
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;//状态栏隐藏，而SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN是状态栏浮在activity之上

        decorView.setSystemUiVisibility(option);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    /**
     * @Description:类似全屏游戏时，状态栏和导航栏都隐藏，被呼出透明并且时会自动隐藏
     */
    public static void setGameMode(Context context) {
        if (!(context instanceof Activity))
            return;
        AppCompatActivity activity = (AppCompatActivity) context;
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(option);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

}
