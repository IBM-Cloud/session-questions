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
package net.bluemix.questions.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.service.common.AmqpServiceInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSocketMessageBroker
@Profile("cloud")
public class CloudWebSocketConfig extends AbstractWebSocketConfig {
  
  @Autowired
  private AmqpServiceInfo info;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    super.configureMessageBroker(registry);
    registry.enableStompBrokerRelay("/topic", "/queue").
    setRelayHost(info.getHost()).
    setVirtualHost(info.getVirtualHost()).
    setSystemLogin(info.getUserName()).
    setSystemPasscode(info.getPassword()).
    setClientLogin(info.getUserName()).
    setClientPasscode(info.getPassword());
  }

}

