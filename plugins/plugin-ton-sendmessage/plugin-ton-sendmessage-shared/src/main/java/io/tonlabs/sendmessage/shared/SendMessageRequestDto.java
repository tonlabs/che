package io.tonlabs.sendmessage.shared;

import org.eclipse.che.dto.shared.DTO;

@DTO
public interface SendMessageRequestDto {
  String getMethod();

  void setMethod(String method);

  String getAbiParamsJson();

  void setAbiParamsJson(String abiParamsJson);
}
