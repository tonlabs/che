package io.tonlabs.ide.util;

import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.TypedArrays;
import com.google.gwt.typedarrays.shared.Uint8Array;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.google.gwt.xhr.client.XMLHttpRequest.ResponseType;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.js.JsPromiseError;
import org.eclipse.che.api.promises.client.js.Promises;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.resources.File;
import org.eclipse.che.ide.util.PathEncoder;

public final class HttpUtil {
  private static final String PROJECT = "/project";
  private static final String FILE = "/file";

  public static Promise<Uint8Array> getFileContent(AppContext appContext, File file) {
    return Promises.create(
        (resolve, reject) -> {
          XMLHttpRequest request = XMLHttpRequest.create();

          String url = getFileUrl(appContext, file);

          Console.log(url);

          request.open("GET", url);
          request.setResponseType(ResponseType.ArrayBuffer);
          request.setOnReadyStateChange(
              xhr -> {
                if (xhr.getReadyState() == XMLHttpRequest.DONE) {
                  if (xhr.getStatus() == 200) {
                    ArrayBuffer buffer = xhr.getResponseArrayBuffer();
                    Uint8Array array = TypedArrays.createUint8Array(buffer);
                    resolve.apply(array);
                  } else {
                    reject.apply(
                        JsPromiseError.create(
                            "Error downloading file: "
                                + file.getContentUrl()
                                + ". Error: "
                                + xhr.getStatusText()));
                  }
                }
              });
          request.send();
        });
  }

  private static String getFileUrl(AppContext appContext, File file) {
    return appContext.getWsAgentServerApiEndpoint()
        + PROJECT
        + FILE
        + PathEncoder.encodePath(file.getLocation());
  }
}
