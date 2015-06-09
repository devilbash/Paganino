package it.bestapp.paganino.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by marco.compostella on 17/05/2015.
 */
public class MyWebView extends WebView {

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
        return super.onTouchEvent(event);
    }
}
