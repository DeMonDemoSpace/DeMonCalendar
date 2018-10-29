package com.demon.calendar.util;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.Scroller;

/**
 * @author DeMon
 * @date 2018/10/29
 * @description
 */
public class ScrollUtil {

    private static int top;
    private static boolean customScrollToBottom = false;

    /**
     * 判断上一次滑动改变周月日历是向下滑还是向上滑 向下滑表示切换为月日历模式 向上滑表示切换为周日历模式
     *
     * @return boolean 是否是在向下滑动。(true: 已经收缩; false: 已经打开）
     */
    public static boolean isScrollToBottom() {
        return customScrollToBottom;
    }

    /**
     * 设置上一次滑动改变周月日历是向下滑还是向上滑 向下滑表示切换为月日历模式 向上滑表示切换为周日历模式
     *
     * @return void
     */
    public static void setScrollToBottom(boolean customScrollToBottom) {
        ScrollUtil.customScrollToBottom = customScrollToBottom;
    }

    /**
     * 通过scrollTo方法完成协调布局的滑动，其中主要使用了ViewCompat.postOnAnimation
     *
     * @param parent   协调布局parent
     * @param child    协调布局协调滑动的child
     * @param y        滑动目标位置y轴数值
     * @param duration 滑动执行时间
     * @return void
     */
    public static void scrollTo(final CoordinatorLayout parent, final RecyclerView child, final int y, int duration) {
        final Scroller scroller = new Scroller(parent.getContext());
        scroller.startScroll(0, top, 0, y - top, duration);   //设置scroller的滚动偏移量
        ViewCompat.postOnAnimation(child, new Runnable() {
            @Override
            public void run() {
                //返回值为boolean，true说明滚动尚未完成，false说明滚动已经完成。
                // 这是一个很重要的方法，通常放在View.computeScroll()中，用来判断是否滚动是否结束。
                if (scroller.computeScrollOffset()) {
                    int delta = scroller.getCurrY() - child.getTop();
                    child.offsetTopAndBottom(delta);
                    saveTop(child.getTop());
                    parent.dispatchDependentViewsChanged(child);
                    ViewCompat.postOnAnimation(child, this);
                }
            }
        });
    }

    public static void saveTop(int y) {
        top = y;
    }

    public static int loadTop() {
        return top;
    }
}
