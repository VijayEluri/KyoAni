package net.hisme.masaki.kyoani.widget;

import net.hisme.masaki.kyoani.R;

public class KyoAniWidget2 extends KyoAniWidget {
    protected final int widget_layout = R.layout.widget_layout_2x2;

    protected int getLayoutId() {
        return R.layout.widget_layout_2x2;
    }

    public void log(String message) {
        android.util.Log.d("KyoAni", "[KyoAniWidget2] " + message);
    }
}
