/**
 * Copyright 2016-2017 Symphony Integrations - Symphony LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.symphonyoss.integration.webhook.hubspot;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.symphonyoss.integration.json.JsonUtils;
import org.symphonyoss.integration.webhook.WebHookIntegration;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.exception.WebHookParseException;

import java.io.IOException;

@Component
public class HubspotWebHookIntegration extends WebHookIntegration {

  private static final String WEBHOOK_EVENT = "HubSpot Webhook";

  private static final String FORMATTED_MESSAGE = "<b>New Enrollment </b> at %s<br/>%s %s (%s) from %s";

  private static final String PROPERTIES = "properties";

  private static final String VALUE = "value";

  private static final String FORM_NAME = "recent_conversion_event_name";

  private static final String FIRST_NAME = "firstname";

  private static final String LAST_NAME = "lastname";

  private static final String EMAIL = "email";

  private static final String COMPANY = "company";

  @Override
  public String parse(WebHookPayload input) {
    try {
      // Retrieves data from the webhook payload
      JsonNode payload = JsonUtils.readTree(input.getBody());
      JsonNode properties = payload.path(PROPERTIES);
      String formName = properties.path(FORM_NAME).path(VALUE).asText();
      String firstName = properties.path(FIRST_NAME).path(VALUE).asText();
      String lastName = properties.path(LAST_NAME).path(VALUE).asText();
      String email = properties.path(EMAIL).path(VALUE).asText();
      String company = properties.path(COMPANY).path(VALUE).asText();

      // Formats the message
      String formattedMessage = String.format(FORMATTED_MESSAGE, formName, firstName, lastName, email, company);

      // Returns a messageML document
      return super.buildMessageML(formattedMessage, WEBHOOK_EVENT);
    } catch (IOException e) {
      throw new WebHookParseException(getClass().getSimpleName(), "Failed to parse payload", e);
    }
  }

}
