package com.noober.wzrytools;

import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.Nullable;

import com.noober.wzrytools.base.BaseAccessibilityService;

public class AssistAccessService extends BaseAccessibilityService {

    private static String TAG2 = "AssistAccessService";

    private static AssistAccessService instance;

    @Nullable
    public static AssistAccessService getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.e(TAG2, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e(TAG2, "onServiceConnected");
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e(TAG2, "event");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG2, "onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG2, "onUnbind");
        return super.onUnbind(intent);
    }
}