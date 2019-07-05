/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.plugin.java.languageserver;

import static org.eclipse.che.ide.ext.java.shared.Constants.EXECUTE_CLIENT_COMMAND;
import static org.eclipse.che.ide.ext.java.shared.Constants.EXECUTE_CLIENT_COMMAND_SUBSCRIBE;
import static org.eclipse.che.ide.ext.java.shared.Constants.EXECUTE_CLIENT_COMMAND_UNSUBSCRIBE;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.che.api.core.jsonrpc.commons.RequestHandlerConfigurator;
import org.eclipse.che.api.core.jsonrpc.commons.RequestTransmitter;
import org.eclipse.che.api.core.notification.EventService;
import org.eclipse.che.api.languageserver.server.dto.DtoServerImpls;
import org.eclipse.che.api.languageserver.server.dto.DtoServerImpls.ExecuteCommandParamsDto;
import org.eclipse.lsp4j.ExecuteCommandParams;

/** Transmits 'workspace/executeClientCommand' over the JSON-RPC */
@Singleton
public class ExecuteClientCommandJsonRpcTransmitter {
  private final Set<String> endpointIds = new CopyOnWriteArraySet<>();

  private final RequestTransmitter requestTransmitter;

  @Inject
  public ExecuteClientCommandJsonRpcTransmitter(RequestTransmitter requestTransmitter) {
    this.requestTransmitter = requestTransmitter;
  }

  @Inject
  private void subscribe(EventService eventService, RequestTransmitter requestTransmitter) {
    eventService.subscribe(
        event ->
            endpointIds.forEach(
                endpointId ->
                    requestTransmitter
                        .newRequest()
                        .endpointId(endpointId)
                        .methodName(EXECUTE_CLIENT_COMMAND)
                        .paramsAsDto(new ExecuteCommandParamsDto(event))
                        .sendAndSkipResult()),
        ExecuteCommandParams.class);
  }

  @Inject
  private void configureSubscribeHandler(RequestHandlerConfigurator requestHandler) {
    requestHandler
        .newConfiguration()
        .methodName(EXECUTE_CLIENT_COMMAND_SUBSCRIBE)
        .noParams()
        .noResult()
        .withConsumer(endpointIds::add);
  }

  @Inject
  private void configureUnSubscribeHandler(RequestHandlerConfigurator requestHandler) {
    requestHandler
        .newConfiguration()
        .methodName(EXECUTE_CLIENT_COMMAND_UNSUBSCRIBE)
        .noParams()
        .noResult()
        .withConsumer(endpointIds::remove);
  }

  public CompletableFuture<Object> executeClientCommand(ExecuteCommandParams requestParams) {
    ExecuteCommandParamsDto paramsDto =
        (ExecuteCommandParamsDto) DtoServerImpls.makeDto(requestParams);

    CompletableFuture<Object> result = new CompletableFuture<>();
    for (String endpointId : endpointIds) {
      requestTransmitter
          .newRequest()
          .endpointId(endpointId)
          .methodName(EXECUTE_CLIENT_COMMAND)
          .paramsAsDto(paramsDto)
          .sendAndSkipResult();
    }
    result.complete(null);
    return result;
  }
}
