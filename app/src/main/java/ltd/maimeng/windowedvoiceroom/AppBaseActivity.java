package ltd.maimeng.windowedvoiceroom;

import ltd.maimeng.core.ui.BaseActivity;
import ltd.maimeng.windowedvoiceroom.widget.FloatingView;

public abstract class AppBaseActivity extends BaseActivity {

    @Override
    protected void onStart() {
        super.onStart();
        FloatingView.get().attach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FloatingView.get().detach(this);
    }
}
