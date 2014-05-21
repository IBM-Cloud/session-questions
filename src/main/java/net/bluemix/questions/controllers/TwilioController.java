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
package net.bluemix.questions.controllers;

import net.bluemix.questions.models.Question;
import net.bluemix.questions.models.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/twillio")
public class TwilioController {
  private static final Logger LOG = LoggerFactory.getLogger(TwilioController.class.getName());
  private static final String TOPIC = "/topic/questions";
  @Autowired
  private QuestionRepo questionRepo;
  
  @Autowired
  private SessionRepo sessionRepo;
  
  @Autowired
  private SimpMessagingTemplate template;
  
  @RequestMapping(method=RequestMethod.POST)
  public void createQuestion(@RequestParam("MessageSid") String messageSid, @RequestParam("AccountSid") String accountId,
          @RequestParam("From") String from, @RequestParam("To") String to, @RequestParam("Body") String body) {
    Iterable<Session> sessions = sessionRepo.findSessionByNumber(to);
    Session theSession = null;
    for(Session session : sessions) {
      if(session.isActive()) {
        theSession = session;
      }
    }
    if(theSession != null) {
      Question q = new Question();
      q.setAnswered(false);
      q.setContent(body);
      q.setNumber(from);
      q.setSession(theSession);
      q = questionRepo.save(q);
      this.template.convertAndSend(TOPIC, q);
    } else {
      LOG.warn("Could not find a session for the phone number {1}.", new Object[]{to});
    }
  }
}

