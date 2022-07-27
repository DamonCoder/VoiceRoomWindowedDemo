package ltd.maimeng.windowedvoiceroom;

import ltd.maimeng.core.Moe;
import ltd.maimeng.core.MoeApplication;

public class AppApplication extends MoeApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Moe.getInstance().printLog(true);
    }
}
