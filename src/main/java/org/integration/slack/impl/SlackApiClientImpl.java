package org.integration.slack.impl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exceptions.SlackApiException;
import org.integration.slack.SlackApiClient;

@Slf4j
@AllArgsConstructor
public class SlackApiClientImpl implements SlackApiClient {
  private static final String SLACK_AUTH_API_URL = "https://slack.com/api/auth.test";
  private static final String AUTHORISATION_HEADER = "Authorization";
  private static final String RESPONSE_HEADER_OAUTH_SCOPES = "x-oauth-scopes";

  //    I guess not closing it can leak resources, but HttpClient becomes closeable only in Java 21
  private final HttpClient client;

  @Override
  public List<String> getScopes(String token) {

    HttpResponse<String> response = null;
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(SLACK_AUTH_API_URL))
          .setHeader(AUTHORISATION_HEADER, "Bearer %s".formatted(token))// Replace with your URL
          .GET()
          .build();
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException e) {
      throw new SlackApiException("Couldn't request slack AUTH API", e);
    } catch (InterruptedException e) {
      log.error("Current thread was interapted", e);
      Thread.currentThread().interrupt();
    }

    if (response != null) {
      log.info("Status Code: {}", response.statusCode());
      log.info("Response Body: {}", response.body());
      var header = response.headers().map().get(RESPONSE_HEADER_OAUTH_SCOPES).get(0);
      return Arrays.asList(header.split(","));
    }
    return Collections.emptyList();
  }
}
