package io.tonlabs.ide.sdk;

import static com.google.gwt.core.client.ScriptInjector.TOP_WINDOW;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.tonlabs.ide.sdk.jso.TonSdkJso;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification.DisplayMode;
import org.eclipse.che.ide.api.notification.StatusNotification.Status;

@Singleton
public class TonSdkInitializer {
  private TonSdkJso tonSdk;

  @Inject private NotificationManager notificationManager;

  public TonSdkInitializer() {
    ScriptInjector.fromUrl(GWT.getModuleBaseForStaticFiles() + "ton.js")
        .setWindow(TOP_WINDOW)
        .setCallback(
            new Callback<Void, Exception>() {
              @Override
              public void onFailure(Exception reason) {
                TonSdkInitializer.this.notificationManager.notify(
                    "TON SDK injection failed! Reload the page.",
                    Status.FAIL,
                    DisplayMode.FLOAT_MODE);
              }

              @Override
              public void onSuccess(Void result) {
                // Nothing to do on success.
              }
            })
        .inject();
  }

  private static native TonSdkJso createTonSdk() /*-{
    return new window.top.frames['ide-application-iframe'].contentWindow.TonSdk.TonSdk();
  }-*/;

  public TonSdkJso getTonSdk() {
    if (this.tonSdk == null) {
      this.tonSdk = createTonSdk();
    }
    return this.tonSdk;
  }
}
