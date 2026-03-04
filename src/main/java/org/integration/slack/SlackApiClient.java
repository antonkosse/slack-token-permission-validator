package org.integration.slack;

import java.util.List;

public interface SlackApiClient {
  List<String> getScopes(String token);
}
