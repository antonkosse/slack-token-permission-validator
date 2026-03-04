package org.service.impl;

import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.model.Action;
import org.model.Status;
import org.service.PermissionValidator;

@Slf4j
public class PermissionValidatorImpl implements PermissionValidator {

  @Override
  public Status validatePermissions(List<String> permissions, Action action) {
    if (CollectionUtils.isEmpty(permissions)) {
      log.warn("Permissions are empty");
      return Status.FAIL;
    }

    if (Objects.isNull(action)) {
      log.warn("action is null");
      return Status.FAIL;
    }

    var match = permissions.stream()
        .anyMatch(permission -> Objects.equals(permission.toUpperCase(), action.name()));

    if (match) {
      log.info("User has permissions for provided action: {}", action);
      return Status.SUCCESS;
    }

    return Status.FAIL;
  }
}
