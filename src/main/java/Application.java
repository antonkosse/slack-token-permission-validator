import static org.model.Action.ADMIN;

import java.net.http.HttpClient;

import org.integration.slack.SlackApiClient;
import org.integration.slack.impl.SlackApiClientImpl;
import org.service.PermissionValidator;
import org.service.impl.PermissionValidatorImpl;

public class Application {

  public static void main(String[] args) {
    SlackApiClient slackApiClient = new SlackApiClientImpl(HttpClient.newHttpClient());
    PermissionValidator permissionValidator = new PermissionValidatorImpl();

    var scopes = slackApiClient.getScopes("<token>");

    System.out.println(permissionValidator.validatePermissions(scopes, ADMIN));

  }
}