package com.r0adkll.slidr.util;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.core.view.ScrollingView;
import androidx.viewpager.widget.ViewPager;

import com.r0adkll.slidr.model.SlidrPosition;

public class ViewHelper {

    public static boolean hasScrollableChildrenUnderPoint(View mView, SlidrPosition direction, int x, int y) {
        View scrollableView = null;
        if (mView instanceof ViewGroup) {
            scrollableView = findScrollableViewContains((ViewGroup) mView, direction, x, y);
        }
        return scrollableView != null;
    }

    private static View findScrollableViewContains(ViewGroup mViewGroup, SlidrPosition direction, int x, int y) {
        for (int i = 0; i < mViewGroup.getChildCount(); i++) {
            View mView = mViewGroup.getChildAt(i);
            if (mView.getVisibility() != View.VISIBLE) {
                continue;
            }
            if (isScrollableView(mView) && canScroll(mView, direction)) {
                return mView;
            }
            if (mView instanceof ViewGroup && isViewUnder(mView, x, y)) {
                int relativeX = x - mViewGroup.getLeft() + mViewGroup.getScrollX();
                int relativeY = y - mViewGroup.getTop() + mViewGroup.getScrollY();
                mView = findScrollableViewContains((ViewGroup) mView, direction, relativeX, relativeY);
                if (mView != null) {
                    return mView;
                }
            }
        }
        return null;
    }

    private static boolean canScroll(View mView, SlidrPosition direction) {
        switch (direction) {
            case LEFT:
                return mView.canScrollHorizontally(-1);
            case RIGHT:
                return mView.canScrollHorizontally(1);
            case TOP:
                return mView.canScrollVertically(-1);
            case BOTTOM:
                return mView.canScrollVertically(1);
            case VERTICAL:
                return mView.canScrollVertically(-1) || mView.canScrollVertically(1);
            case HORIZONTAL:
                return mView.canScrollHorizontally(-1) || mView.canScrollHorizontally(1);
        }
        return false;
    }

    private static boolean isScrollableView(View mView) {
        return mView instanceof ScrollView
                || mView instanceof HorizontalScrollView
                || mView instanceof AbsListView
                || mView instanceof ScrollingView
                || mView instanceof ViewPager
                || mView instanceof WebView;
    }

    private static boolean isViewUnder(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        return x >= view.getLeft() &&
                x < view.getRight() &&
                y >= view.getTop() &&
                y < view.getBottom();
    }
}
