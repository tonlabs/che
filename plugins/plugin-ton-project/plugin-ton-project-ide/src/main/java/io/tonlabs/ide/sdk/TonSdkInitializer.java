package io.tonlabs.ide.sdk;

import static com.google.gwt.core.client.ScriptInjector.TOP_WINDOW;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.user.client.Window;
import com.google.inject.Singleton;
import io.tonlabs.ide.sdk.jso.TonSdkJso;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.api.promises.client.js.Promises;

@Singleton
public class TonSdkInitializer {
  private TonSdkJso tonSdk;
  //  private Promise<Void> waitForIjection = Promises.create((success, fail) -> {
  //
  //  });

  public TonSdkInitializer() {
    ScriptInjector.fromUrl(GWT.getModuleBaseForStaticFiles() + "ton.js")
        .setWindow(TOP_WINDOW)
        .setCallback(
            new Callback<Void, Exception>() {
              @Override
              public void onFailure(Exception reason) {
                Window.alert("TON SDK injection failed! Reload the page.");
              }

              @Override
              public void onSuccess(Void result) {
                // TODO:
              }
            })
        .inject();
  }

  private static native TonSdkJso createTonSdk() /*-{
    return new window.top.frames['ide-application-iframe'].contentWindow.TonSdk.TonSdk();
  }-*/;

  public Promise<TonSdkJso> getTonSdk() {
    if (this.tonSdk == null) {
      this.tonSdk = createTonSdk();
      return Promises.create(
          (success, fail) ->
              this.tonSdk
                  .initApp()
                  .then(
                      (Void nothing) -> {
                        success.apply(this.tonSdk);
                      })
                  .catchError(
                      (PromiseError error) -> {
                        Window.alert("Ton SDK initialization error: " + error.getMessage());
                      }));
    }

    return Promises.create(
        (success, fail) -> {
          success.apply(this.tonSdk);
        });
  }
}
