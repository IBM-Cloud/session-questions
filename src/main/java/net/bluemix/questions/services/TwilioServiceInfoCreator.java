/*
 * Copyright IBM Corp. 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.bluemix.questions.services;

import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;


public class TwilioServiceInfoCreator extends CloudFoundryServiceInfoCreator<TwilioServiceInfo> {

  public TwilioServiceInfoCreator() {
    super(new Tags(), "twillio");
  }

  @Override
  public boolean accept(Map<String, Object> serviceData) {
    String name = (String)serviceData.get("name");
    return "twilio-questions".equals(name);
  }

  @Override
  public TwilioServiceInfo createServiceInfo(Map<String, Object> serviceData) {
    Map<String, Object> credentials = (Map<String, Object>) serviceData.get("credentials");
    
    String id = (String)serviceData.get("name");
    String url = (String)credentials.get("url");
    String accountId = (String)credentials.get("accountSID");
    String authToken = (String)credentials.get("authToken");
    return new TwilioServiceInfo(id, accountId, authToken, url);
  }


}

