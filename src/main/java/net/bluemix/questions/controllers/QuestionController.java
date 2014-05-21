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

import java.util.HashMap;
import java.util.Map;

import net.bluemix.questions.models.Question;
import net.bluemix.questions.models.Reply;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;

@RestController
@RequestMapping("/api/admin/questions")
public class QuestionController {
  private static final String FROM = "From";
  private static final String TO = "To";
  private static final String BODY = "Body";
  private static final Logger LOG = LoggerFactory.getLogger(QuestionController.class.getName());

  @Autowired
  private QuestionRepo repo;

  @Autowired
  private MailSender mailSender;

  @Autowired
  private TwilioRestClient twilioClient;

  @RequestMapping(value="{id}", method=RequestMethod.DELETE)
  public void delete(@PathVariable Long id) {
    repo.delete(id);
  }

  @RequestMapping(value="{id}", method=RequestMethod.PUT)
  public Question update(@RequestBody Question domain, @PathVariable Long id) {
    Question question = repo.findOne(id);
    question.setAnswered(domain.isAnswered());
    question.setContent(domain.getContent());
    question.setEmail(domain.getEmail());
    question.setNumber(domain.getNumber());
    return repo.save(question);
  }

  @RequestMapping(method=RequestMethod.GET)
  public Iterable<Question> list() {
    return repo.findAll();
  }

  @RequestMapping(value="{id}/reply", method=RequestMethod.POST)
  public void reply(@PathVariable Long id, @RequestBody Reply reply) {
    Question q = repo.findOne(id);
    q.setAnswered(true);
    repo.save(q);
    if(q.getEmail() != null) {
      SimpleMailMessage msg = new SimpleMailMessage();
      msg.setTo(q.getEmail());
      msg.setFrom("noreply@sessionquestions.ng.bluemix.net");
      msg.setSubject("Thanks For Submitting Your Question!");
      msg.setText(reply.getContent());
      mailSender.send(msg);
    }
    if(q.getNumber() != null) {
      Account acct = twilioClient.getAccount();
      SmsFactory smsFactory = acct.getSmsFactory();
      Map<String,String> params = new HashMap<String,String>();
      params.put(FROM, q.getSession().getNumber());
      params.put(TO, q.getNumber());
      params.put(BODY, reply.getContent());
      try {
        smsFactory.create(params);
      }
      catch (TwilioRestException e) {
        LOG.warn("Error sending SMS reply.", e);
      }
    }
  }
}