package org.service;

import java.util.List;

import org.model.Action;
import org.model.Status;

public interface PermissionValidator {
  Status validatePermissions(List<String> permissions, Action action);
}
