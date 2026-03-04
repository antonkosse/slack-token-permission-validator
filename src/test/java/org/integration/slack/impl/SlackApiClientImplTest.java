package org.integration.slack.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import org.exceptions.SlackApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlackApiClientImplTest {

  @Mock
  private HttpClient client;

  @InjectMocks
  private SlackApiClientImpl service;

  @Captor
  private ArgumentCaptor<HttpRequest> httpRequestArgumentCaptor;

  @Mock
  private HttpResponse<String> httpResponse;


  @Test
  void shouldGetScopes() throws IOException, InterruptedException {
    given(client.send(any(), eq(HttpResponse.BodyHandlers.ofString())))
        .willReturn(httpResponse);
    given(httpResponse.statusCode()).willReturn(200);
    given(httpResponse.body()).willReturn("{}");
    given(httpResponse.headers()).willReturn(HttpHeaders.of(
        Map.of("x-oauth-scopes", List.of("read,write")),
        (key, value) -> true
    ));

    var result = service.getScopes("token");

    assertThat(result).hasSize(2)
        .contains("read", "write");
    then(client).should().send(httpRequestArgumentCaptor.capture(), eq(HttpResponse.BodyHandlers.ofString()));

    var request = httpRequestArgumentCaptor.getValue();

    assertThat(request.method()).isEqualTo("GET");
    assertThat(request.headers().map()).hasSize(1);
    assertThat(request.headers().map().get("Authorization")).isNotNull();
    assertThat(request.headers().map().get("Authorization").get(0)).isEqualTo("Bearer token");
  }

  @Test
  void shouldGetScopes_onEmptyResponse() throws IOException, InterruptedException {
    given(client.send(any(), eq(HttpResponse.BodyHandlers.ofString())))
        .willReturn(null);

    var result = service.getScopes("token");

    assertThat(result).isEmpty();
  }

  @Test
  void shouldCatchIOException() throws IOException, InterruptedException {
    given(client.send(any(), eq(HttpResponse.BodyHandlers.ofString())))
        .willThrow(new IOException("test exception"));

    assertThatThrownBy(() -> service.getScopes("token")).isInstanceOf(SlackApiException.class);
  }
}