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

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.mockito.Mock;
import org.symphonyoss.integration.json.JsonUtils;
import org.symphonyoss.integration.model.config.IntegrationInstance;
import org.symphonyoss.integration.webhook.WebHookPayload;

import java.io.IOException;
import java.util.Collections;

public class HubSpotWebHookIntegrationTest {

  private static final String FORM_SUBMISSION_WEB_HOOK_PAYLOAD_JSON =
      "formSubmissionWebHookPayload.json";

  private static final String FORM_SUBMISSION_MESSAGE =
      "<messageML>"
          + "<b>New Enrollment </b> at Symphony Software Foundation Demos Landing Page: Demo Form"
          + "<br/>"
          + "John Smith (john.smith@symphony.com) from Symphony"
          + "</messageML>";

  @Mock
  private IntegrationInstance mockIntegrationInstance;

  private HubSpotWebHookIntegration hubSpotWebHookIntegration = new HubSpotWebHookIntegration();

  @Test
  public void testFormSubmissionMessage() throws IOException {

    ClassLoader classLoader = getClass().getClassLoader();
    JsonNode payload = JsonUtils.readTree(classLoader.getResourceAsStream(FORM_SUBMISSION_WEB_HOOK_PAYLOAD_JSON));

    WebHookPayload webHookPayload = new WebHookPayload(Collections.<String, String>emptyMap(),
        Collections.<String, String>emptyMap(),
        JsonUtils.writeValueAsString(payload));

    String message = hubSpotWebHookIntegration.parse(mockIntegrationInstance, webHookPayload);

    assertEquals(FORM_SUBMISSION_MESSAGE, message);
  }

}
