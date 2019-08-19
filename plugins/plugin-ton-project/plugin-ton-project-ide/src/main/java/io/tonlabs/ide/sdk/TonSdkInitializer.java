package io.tonlabs.ide.sdk;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.requirejs.RequireJsLoader;

@Singleton
public class TonSdkInitializer {

  @Inject
  public TonSdkInitializer(final RequireJsLoader requireJsLoader) {
    requireJsLoader.require(
        new Callback<JavaScriptObject[], Throwable>() {
          @Override
          public void onFailure(Throwable reason) {
            Window.alert("TonSdk loading failure: " + reason);
          }

          @Override
          public void onSuccess(JavaScriptObject[] result) {
            Window.alert("TonSdk is loaded, count: " + result.length);
            for (JavaScriptObject obj : result) {
              Window.alert(obj.toString());
            }
          }
        },
        new String[] {"ton"},
        new String[] {"TonSdk"});
  }
}
