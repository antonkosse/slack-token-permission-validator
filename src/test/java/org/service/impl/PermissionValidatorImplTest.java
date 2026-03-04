package org.service.impl;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.model.Action;
import org.model.Status;

class PermissionValidatorImplTest {

  private final PermissionValidatorImpl service = new PermissionValidatorImpl();

  @Test
  void shouldValidatePermissions() {
    var result = service.validatePermissions(List.of("read", "write", "create"), Action.CREATE);

    assertThat(result).isEqualTo(Status.SUCCESS);
  }

  @Test
  void shouldFailValidationsWhenNoMatch() {
    var result = service.validatePermissions(List.of("read", "write", "create"), Action.INVITE);

    assertThat(result).isEqualTo(Status.FAIL);
  }

  @Test
  void shouldFailValidationsWhenNoPermissionsProvided() {
    var result = service.validatePermissions(List.of(), Action.CREATE);

    assertThat(result).isEqualTo(Status.FAIL);
  }

  @Test
  void shouldFailValidationsWhenNoActionProvided() {
    var result = service.validatePermissions(List.of("read", "write", "create"), null);

    assertThat(result).isEqualTo(Status.FAIL);
  }

}