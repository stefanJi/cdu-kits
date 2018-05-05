package io.gitHub.JiYang.library.ui.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

public class ScrollBehaivor extends CoordinatorLayout.Behavior<View> {
    /**
     * 因为是在XML中使用app:layout_behavior定义静态的这种行为,
     * 必须实现一个构造函数使布局的效果能够正常工作。
     * 否则 Could not inflate Behavior subclass error messages.
     */
    public ScrollBehaivor(Context context, AttributeSet attrs) {
        super();
    }

    /**
     * 开始滚动时
     */
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                                       @NonNull View directTargetChild, @NonNull View target,
                                       int axes, int type) {
        //如果child可见，设置其不可见
        if (child.getVisibility() == View.VISIBLE) {
            ((FloatingActionButton) child).hide();
        }
        //如果child不可见，设置其可见
        else {
            ((FloatingActionButton) child).show();
        }
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }
}
