package io.tonlabs.ide.sdk;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.ScriptInjector;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TonSdkInitializer {

  @Inject
  public TonSdkInitializer(/*final RequireJsLoader requireJsLoader*/ ) {
    ScriptInjector.fromUrl(GWT.getModuleBaseForStaticFiles() + "ton.js")
        .setWindow(getWindow())
        .inject();
    //    ScriptInjector.fromString(
    //            "debugger;\n"
    //                + "require(['TonSdk'], function(TonSdk) {\n"
    //                + "\t\tconsole.log(TonSdk);\n"
    //                + "\t\twindow.tonSdk = new TonSdk.TonSdk();\n"
    //                + "\t\twindow.tonSdk.init();\n"
    //                + "\t});")
    //        .setWindow(getWindow())
    //        .inject();
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

  public static native JavaScriptObject getWindow() /*-{
    debugger;
    return $wnd;
  }-*/;
}
