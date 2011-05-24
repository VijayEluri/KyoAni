package net.hisme.masaki.kyoani.widget;

import net.hisme.masaki.kyoani.R;

public class KyoAniWidget1 extends KyoAniWidget {
    protected final int widget_layout = R.layout.widget_layout_1x1;

    protected int getLayoutId() {
        return R.layout.widget_layout_1x1;
    }
    
    public void log(String message) {
        android.util.Log.d("KyoAni", "[KyoAniWidget1] " + message);
    }
}
