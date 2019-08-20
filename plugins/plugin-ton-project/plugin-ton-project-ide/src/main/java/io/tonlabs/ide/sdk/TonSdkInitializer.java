package io.tonlabs.ide.sdk;

import com.google.gwt.core.client.ScriptInjector;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.requirejs.RequireJsLoader;

@Singleton
public class TonSdkInitializer {

  @Inject
  public TonSdkInitializer(final RequireJsLoader requireJsLoader) {
    ScriptInjector.fromString(
            "require(['TonSdk'], function(TonSdk) {\n"
                + "\t\tconsole.log(TonSdk);\n"
                + "\t\tWindow.tonSdk = new TonSdk.TonSdk();\n"
                + "\t\tWindow.tonSdk.init();\n"
                + "\t});")
        .inject();
    //    requireJsLoader.require(
    //        new Callback<JavaScriptObject[], Throwable>() {
    //          @Override
    //          public void onFailure(Throwable reason) {
    //            Window.alert("TonSdk loading failure: " + reason);
    //          }
    //
    //          @Override
    //          public void onSuccess(JavaScriptObject[] result) {
    //            Window.alert("TonSdk is loaded, count: " + result.length);
    //            for (JavaScriptObject obj : result) {
    //              Window.alert(obj == null ? "null" : obj.toString());
    //            }
    //          }
    //        },
    //        new String[] {"ton"},
    //        new String[] {"TonSdk"});
  }
}
