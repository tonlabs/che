package io.tonlabs.ide.util;

import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.TypedArrays;
import com.google.gwt.typedarrays.shared.Uint8Array;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.google.gwt.xhr.client.XMLHttpRequest.ResponseType;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.js.JsPromiseError;
import org.eclipse.che.api.promises.client.js.Promises;
import org.eclipse.che.ide.api.resources.File;

public final class HttpUtil {
  public static Promise<Uint8Array> getFileContent(File file) {
    return Promises.create(
        (resolve, reject) -> {
          XMLHttpRequest request = XMLHttpRequest.create();

          Console.log(file.getContentUrl());

          request.open("GET", file.getContentUrl());
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
}
