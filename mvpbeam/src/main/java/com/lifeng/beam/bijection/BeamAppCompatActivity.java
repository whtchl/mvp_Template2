package com.lifeng.beam.bijection;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import com.lifeng.beam.Beam;
import com.lifeng.beam.R;
import com.lifeng.f300.common.utils.StatusBarCompat;


/**
 * This class is an example of how an activity could controls it's presenter.
 * You can inherit from this class or copy/paste this class's code to
 * create your own view implementation.
 *
 */
public abstract class BeamAppCompatActivity<PresenterType extends Presenter> extends AppCompatActivity {
    private ActivityLifeCycleDelegate activityLifeCycleDelegate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /*状态栏 导航栏都沉浸
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }*/
       /*状态栏都沉浸，但是toolbar会向上移动
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            );
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }*/
        //修改状态栏颜色，完成沉浸式。
        StatusBarCompat.compat(this,Color.parseColor("#008000"));
        preCreatePresenter();
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onCreate(savedInstanceState);
        helper.onCreate(savedInstanceState);
    }

    public void preCreatePresenter(){
        activityLifeCycleDelegate = Beam.createActivityLifeCycleDelegate(this);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onPostCreate(savedInstanceState);
        helper.onPostCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onDestroy();
        helper.onDestroyView();
        if (isFinishing())
            helper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onSaveInstanceState(outState);
        helper.onSave(outState);
    }

 /*   @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (activityLifeCycleDelegate!=null)return activityLifeCycleDelegate.dispatchPopulateAccessibilityEvent(event);
        return super.dispatchPopulateAccessibilityEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (activityLifeCycleDelegate!=null)return activityLifeCycleDelegate.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        if (activityLifeCycleDelegate!=null)return activityLifeCycleDelegate.dispatchTrackballEvent(ev);
        return super.dispatchTrackballEvent(ev);
    }*/

    /*
        boolean dispatchPopulateAccessibilityEventSuper(AccessibilityEvent event) {
            return super.dispatchPopulateAccessibilityEvent(event);
        }


        boolean dispatchTouchEventSuper(MotionEvent ev) {
            return super.dispatchTouchEvent(ev);
        }


        boolean dispatchTrackballEventSuper(MotionEvent ev) {
            return super.dispatchTrackballEvent(ev);
        }*/

    @Override
    protected void onResume() {
        super.onResume();
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onResume();
        helper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onPause();
        helper.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onActivityResult(requestCode, resultCode, data);
        helper.onResult(requestCode, resultCode,data);
    }


    public PresenterType getPresenter() {
        return helper.getPresenter();
    }

    private ViewHelper<PresenterType> helper = new ViewHelper<>(this);


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onContentChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onRestart();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onRestoreInstanceState(savedInstanceState);
    }



    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onUserLeaveHint();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onNewIntent(intent);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onTrimMemory(level);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onAttachFragment(fragment);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onResumeFragments();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onStop();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onPostResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (activityLifeCycleDelegate!=null)activityLifeCycleDelegate.onConfigurationChanged(newConfig);
    }
}
