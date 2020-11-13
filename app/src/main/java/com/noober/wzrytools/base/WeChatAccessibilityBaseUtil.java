package com.noober.wzrytools.base;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chengxin on 17/10/2017.
 */

public class WeChatAccessibilityBaseUtil {
    private static final String TAG = "WeChatAccessibilityBaseUtil";

    // 设置延时
    public static void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
    }
    // 启动后台服务
//    public static void startAccessibilityService() {
//        Context context = ApplicationKtLike.getContext();
//        if (isAccessibilitySettingsOn()) {
//            context.startService(new Intent(context, WeChatAssistAccessService.class));
//        } else {
//            gotoAccessibilitySetting();
//        }
//    }

    // 判断默认service服务
//    public static boolean isAccessibilitySettingsOn() {
//        return isAccessibilitySettingsOn(getDefaultAccessibilityService());
//    }

    // 根据元素文字查找元素
    public static AccessibilityNodeInfo findViewByText(AccessibilityNodeInfo info, String targetText) {
        AccessibilityNodeInfo note = findViewByText(info, targetText, false);
        if (note == null) {
            note = findViewByText(info, targetText, true);
        }
        return note;
    }

    // 根据元素文字查找元素
    public static AccessibilityNodeInfo findViewByText(AccessibilityNodeInfo info, String targetText, boolean clickable) {
        if (info == null || TextUtils.isEmpty(targetText)) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = info.findAccessibilityNodeInfosByText(targetText);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && (nodeInfo.getText() != null && targetText.equals(nodeInfo.getText().toString()) && nodeInfo.isClickable() == clickable)) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }

    // 根据元素文字查找元素
    public static List<AccessibilityNodeInfo> findViewListByText(AccessibilityNodeInfo info, String targetText) {
        if (info == null || TextUtils.isEmpty(targetText)) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = info.findAccessibilityNodeInfosByText(targetText);
        return nodeInfoList;
    }

    // 根据元素文字查找元素
    public static AccessibilityNodeInfo findViewByContaninsText(AccessibilityNodeInfo info, String targetText, boolean clickable) {
        if (info == null || TextUtils.isEmpty(targetText)) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = info.findAccessibilityNodeInfosByText(targetText);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && (nodeInfo.getText() != null && nodeInfo.getText().toString().contains(targetText) && nodeInfo.isClickable() == clickable)) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }

    // 根据元素id查找元素
    @Nullable
    public static AccessibilityNodeInfo findViewByViewId(AccessibilityNodeInfo info, String targetViewId) {
        if (info == null || TextUtils.isEmpty(targetViewId)) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = info.findAccessibilityNodeInfosByViewId(targetViewId);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            return nodeInfoList.get(0);
        }
        return null;
    }

    // 根据元素id查找元素
    @Nullable
    public static List<AccessibilityNodeInfo> findViewListByViewId(AccessibilityNodeInfo info, String targetViewId) {
        if (info == null || TextUtils.isEmpty(targetViewId)) {
            return null;
        }
        return info.findAccessibilityNodeInfosByViewId(targetViewId);
    }

    // 根据元素介绍信息查找元素 例如: 头像
    public static AccessibilityNodeInfo findViewByDescription(AccessibilityNodeInfo info, String targetDes, boolean clickable) {
        if (info == null || TextUtils.isEmpty(targetDes)) {
            return null;
        }
        for (int i = 0; i < info.getChildCount(); i++) {
            if (info.getChild(i) == null) {
                continue;
            }

            if (info.getChild(i).getChildCount() > 0) {
                if (findViewByDescription(info.getChild(i), targetDes, clickable) == null) {
                    continue;
                } else {
                    return findViewByDescription(info.getChild(i), targetDes, clickable);
                }
            }

            if (info.getChild(i).getContentDescription() != null && targetDes.equals(info.getChild(i).getContentDescription().toString()) && info.getChild(i).isClickable() == clickable) {
                return info.getChild(i);
            }
        }
        return null;
    }

    public static AccessibilityNodeInfo findViewByDescription(AccessibilityNodeInfo info, String targetDes) {
        AccessibilityNodeInfo viewByDescription = findViewByDescription(info, targetDes, false);
        if (viewByDescription == null) {
            viewByDescription = findViewByDescription(info, targetDes, true);
        }
        return viewByDescription;
    }

    // 设置edittext的输入内容
    public static void setEditText(AccessibilityNodeInfo info, String text) {
        if (info == null || TextUtils.isEmpty(text)) {
            return;
        }
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
        info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    }

    // service 的判断
    static boolean isServiceSurvive(AccessibilityService service) {
        try {
            if (service == null || service.getRootInActiveWindow() == null || service.getRootInActiveWindow().getPackageName() == null || service.getPackageName() == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // 通过页面描述判断是否在指定页面
    public static boolean isAtTargetPageByDescription(AccessibilityService service, String targetPageDescription) {
        if (!isServiceSurvive(service) || TextUtils.isEmpty(targetPageDescription)) {
            return false;
        }
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null || TextUtils.isEmpty(nodeInfo.getContentDescription())) {
            return false;
        } else {
            return targetPageDescription.equals(nodeInfo.getContentDescription());
        }
    }

    // 通过页面是否包含指定 view 判断是否在指定页面
    public static boolean isAtTargetPageByViewId(AccessibilityService service, String viewId) {
        if (!isServiceSurvive(service) || TextUtils.isEmpty(viewId)) {
            return false;
        }

        AccessibilityNodeInfo nodeInfo = findViewByViewId(service.getRootInActiveWindow(), viewId);

        if (nodeInfo != null) {
            return true;
        } else {
            return false;
        }
    }

    // 通过 text 定位指定 view, 并触发点击事件, 返回指定 view
    public static AccessibilityNodeInfo clickTargetViewByText(AccessibilityService service, String targetViewText, boolean isClickable) {
        if (!isServiceSurvive(service) || TextUtils.isEmpty(targetViewText)) {
            return null;
        }

        AccessibilityNodeInfo nodeInfo = findViewByText(service.getRootInActiveWindow(), targetViewText, isClickable);
        if (nodeInfo == null) {
            nodeInfo = findViewByText(service.getRootInActiveWindow(), targetViewText, !isClickable);
        }
        if (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            } else {
                if (nodeInfo.getParent().isClickable()) {
                    nodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                } else if (nodeInfo.getParent().getParent() != null) {
                    nodeInfo.getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
        return nodeInfo;
    }

    // 通过控件描述定位指定 view, 并触发点击事件, 返回指定 view
    public static AccessibilityNodeInfo clickTargetViewByDescription(AccessibilityService service, String description, boolean isClickable) {
        if (!isServiceSurvive(service) || TextUtils.isEmpty(description)) {
            return null;
        }

        AccessibilityNodeInfo nodeInfo = findViewByDescription(service.getRootInActiveWindow(), description, isClickable);
        if (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            } else {
                if (nodeInfo.getParent().isClickable()) {
                    nodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                } else {
                    nodeInfo.getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
        return nodeInfo;
    }

    // 通过 view id 定位指定 view, 并触发点击事件, 返回指定 view
    public static AccessibilityNodeInfo clickTargetViewByViewId(AccessibilityService service, String targetViewId) {
        if (!isServiceSurvive(service) || TextUtils.isEmpty(targetViewId)) {
            return null;
        }
        if (isChinese(targetViewId)) {
            return clickTargetViewByText(service, targetViewId, false);
        } else {
            AccessibilityNodeInfo nodeInfo = findViewByViewId(service.getRootInActiveWindow(), targetViewId);
            if (nodeInfo != null) {
                if (nodeInfo.isClickable()) {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                } else {
                    if (nodeInfo.getParent() != null && nodeInfo.getParent().isClickable()) {
                        nodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    } else {
                        if (nodeInfo.getParent() != null && nodeInfo.getParent().getParent() != null) {
                            nodeInfo.getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }
            }
            return nodeInfo;
        }
    }

    public static AccessibilityNodeInfo longClickTargetViewByViewId(AccessibilityService service, String targetViewId) {
        if (!isServiceSurvive(service) || TextUtils.isEmpty(targetViewId)) {
            return null;
        }
        if (isChinese(targetViewId)) {
            return clickTargetViewByText(service, targetViewId, false);
        } else {
            AccessibilityNodeInfo nodeInfo = findViewByViewId(service.getRootInActiveWindow(), targetViewId);
            if (nodeInfo != null) {
                if (nodeInfo.isClickable()) {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                } else {
                    if (nodeInfo.getParent() != null && nodeInfo.getParent().isClickable()) {
                        nodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                    } else {
                        if (nodeInfo.getParent() != null && nodeInfo.getParent().getParent() != null) {
                            nodeInfo.getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                        }
                    }
                }
            }
            return nodeInfo;
        }
    }


    public static boolean clickNodeByPosition(AccessibilityService service, AccessibilityNodeInfo nodeInfo) {
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        return click(service, rect);
    }

    public static boolean longClickNodeByPosition(AccessibilityService service, AccessibilityNodeInfo nodeInfo) {
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        return longClick(service, rect);
    }


    public static boolean longClick(AccessibilityService service, Rect rect) {
        return click(service, rect, 2000L);
    }

    public static boolean click(AccessibilityService service, Rect rect) {
        return click(service, rect, 1L);
    }

    public static boolean click(AccessibilityService service, Rect rect, long time) {
        Log.e("clickPosition", String.format("%s, %s", (rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2));
        Point point = new Point((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(point.x, point.y);
        path.lineTo(point.x, point.y);
        /**
         * 参数path：笔画路径
         * 参数startTime：时间 (以毫秒为单位)，从手势开始到开始笔划的时间，非负数
         * 参数duration：笔划经过路径的持续时间(以毫秒为单位)，非负数
         */
        builder.addStroke(new GestureDescription.StrokeDescription(path, 1L, time));
        GestureDescription gesture = builder.build();
        return service.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        }, null);
    }

    public static void findViewAndClickByPoisition(AccessibilityService service, String id) {
        AccessibilityNodeInfo node = findViewByViewId(service.getRootInActiveWindow(), id);
        if (node != null) {
            clickNodeByPosition(service, node);
            delay(1000);
        }
    }

    public static void findViewAndLongClickByPosition(AccessibilityService service, String id) {
        AccessibilityNodeInfo node = findViewByViewId(service.getRootInActiveWindow(), id);
        if (node != null) {
            longClickNodeByPosition(service, node);
            delay(1000);
        }
    }

    public static void findViewAndClickByTextByPosition(AccessibilityService service, String text) {
        AccessibilityNodeInfo node = findViewByText(service.getRootInActiveWindow(), text);
        if (node != null) {
            clickNodeByPosition(service, node);
            delay(1000);
        }
    }


    public static void findViewAndClick(AccessibilityService service, String id) {
        AccessibilityNodeInfo node = findViewByViewId(service.getRootInActiveWindow(), id);
        if (node != null) {
            clickTargetViewByViewId(service, id);
            delay(1000);
        }
    }

    public static void findViewAndLongClick(AccessibilityService service, String id) {
        AccessibilityNodeInfo node = findViewByViewId(service.getRootInActiveWindow(), id);
        if (node != null) {
            longClickTargetViewByViewId(service, id);
            delay(1000);
        }
    }


    public static void findViewAndInputText(AccessibilityService service, String id, String text) {
        AccessibilityNodeInfo node = findViewByViewId(service.getRootInActiveWindow(), id);
        if (node != null) {
            setEditText(node, text);
            delay(1000);
        }
    }

    public static void findViewAndClickByText(AccessibilityService service, String text) {
        AccessibilityNodeInfo node = findViewByText(service.getRootInActiveWindow(), text);
        if (node != null) {
            AccessibilityNodeInfo nodeInfo = clickTargetViewByText(service, text, true);
            if (nodeInfo == null) {
                clickTargetViewByText(service, text, false);
            }
            delay(1000);
        }
    }

    public static void performClickByNode(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            delay(1000);
        }
    }

    public static void performLongClickByNode(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
            delay(1000);
        }
    }


    public static boolean isChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())
            flg = true;
        return flg;
    }


    public static boolean click(AccessibilityService service, Point point) {
        return click(service, point, 1L);
    }

    public static boolean click(AccessibilityService service, Point valPoint, long time) {
        Log.e("clickPosition", String.format("%s, %s", valPoint.x, valPoint.y));
        Point point = new Point(valPoint.x, valPoint.y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(point.x, point.y);
        path.lineTo(point.x, point.y);
        /**
         * 参数path：笔画路径
         * 参数startTime：时间 (以毫秒为单位)，从手势开始到开始笔划的时间，非负数
         * 参数duration：笔划经过路径的持续时间(以毫秒为单位)，非负数
         */
        builder.addStroke(new GestureDescription.StrokeDescription(path, 1L, time));
        GestureDescription gesture = builder.build();
        return service.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        }, null);
    }

    public interface IMoveSuccessCallBack {
        void callBack();
    }

    public static boolean move(AccessibilityService service, Point startPoint, Point endPoint, IMoveSuccessCallBack callBack) {
        return move(service, startPoint, endPoint, 500L, callBack);
    }

    public static boolean move(AccessibilityService service, Point startPoint, Point endPoint, long time, IMoveSuccessCallBack callBack) {
        Point point = new Point(startPoint.x, startPoint.y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(point.x, point.y);
        path.lineTo(endPoint.x, endPoint.y);

        Log.e("MainActivity moveTo", String.format("%s, %s", point.x, point.y));
        Log.e("MainActivity quadTo", String.format("%s, %s", endPoint.x, endPoint.y));
        /**
         * 参数path：笔画路径
         * 参数startTime：时间 (以毫秒为单位)，从手势开始到开始笔划的时间，非负数
         * 参数duration：笔划经过路径的持续时间(以毫秒为单位)，非负数
         */
        builder.addStroke(new GestureDescription.StrokeDescription(path, 20L, time));
        GestureDescription gesture = builder.build();
        return service.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.e("MainActivity", "onCompleted");
                if (callBack != null) {
                    callBack.callBack();
                }
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.e("MainActivity", "onCancelled");
            }
        }, null);
    }
}
