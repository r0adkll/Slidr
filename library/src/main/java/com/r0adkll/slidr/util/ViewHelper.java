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

import java.util.Stack;

public class ViewHelper {

    public static boolean hasScrollableChildUnderPoint(View mView, SlidrPosition direction, int x, int y) {
        View scrollableView = findScrollableViewContains(mView, direction, x, y);
        return scrollableView != null;
    }

    private static View findScrollableViewContains(View mView, SlidrPosition direction, int x, int y) {
        if (isScrollableView(mView) && canScroll(mView, direction)) {
            return mView;
        }
        if (!(mView instanceof ViewGroup)) return null;

        ViewGroup mViewGroup = (ViewGroup) mView;
        int relativeX = x - mViewGroup.getLeft() + mViewGroup.getScrollX();
        int relativeY = y - mViewGroup.getTop() + mViewGroup.getScrollY();
        for (int i = 0; i < mViewGroup.getChildCount(); i++) {
            View childView = mViewGroup.getChildAt(i);
            if (childView.getVisibility() != View.VISIBLE || !isViewUnder(childView, relativeX, relativeY)) continue;
            View scrollableView = findScrollableViewContains(childView, direction, relativeX, relativeY);
            if (scrollableView != null) {
                return scrollableView;
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

    private static View findScrollableInIterativeWay(View parent, SlidrPosition direction, int x, int y) {
        Stack<ViewInfo> viewStack = new Stack<>();
        ViewInfo viewInfo = new ViewInfo(parent, x, y);
        while (viewInfo != null) {
            View mView = viewInfo.view;
            if (isScrollableView(mView) && canScroll(mView, direction)) {
                return mView;
            }
            if (mView instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) mView;
                int relativeX = viewInfo.x - viewGroup.getLeft() + viewGroup.getScrollX();
                int relativeY = viewInfo.y - viewGroup.getTop() + viewGroup.getScrollY();
                for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                    View childView = viewGroup.getChildAt(i);
                    if (childView.getVisibility() != View.VISIBLE || !isViewUnder(childView, relativeX, relativeY)) continue;
                    viewStack.push(new ViewInfo(childView, relativeX, relativeY));
                }
            }
            viewInfo = viewStack.isEmpty() ? null : viewStack.pop();
        }
        return null;
    }

    static class ViewInfo {
        View view;
        int x;
        int y;

        public ViewInfo(View view, int x, int y) {
            this.view = view;
            this.x = x;
            this.y = y;
        }
    }
}
