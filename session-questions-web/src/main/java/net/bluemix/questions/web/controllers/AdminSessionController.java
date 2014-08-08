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
package net.bluemix.questions.web.controllers;

import java.util.Set;

import net.bluemix.questions.data.controllers.SessionRepo;
import net.bluemix.questions.data.models.Question;
import net.bluemix.questions.data.models.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/sessions")
public class AdminSessionController {

  @Autowired
  private SessionRepo repo;
  
  @RequestMapping(value="{id}", method=RequestMethod.DELETE)
  public void delete(@PathVariable Long id) {
    repo.delete(id);
  }
  
  @RequestMapping(method=RequestMethod.POST)
  public Session create(@RequestBody Session domain) {
    return repo.save(domain);
  }
  
  @RequestMapping(value="{id}", method=RequestMethod.PUT)
  public Session update(@RequestBody Session domain, @PathVariable Long id) {
    Session session = repo.findOne(id);
    session.setActive(domain.isActive());
    session.setConference(domain.getConference());
    session.setNumber(domain.getNumber());
    session.setPresenter(domain.getPresenter());
    session.setSessionDate(domain.getSessionDate());
    session.setTitle(domain.getTitle());
    return repo.save(session);
  }
  
  @RequestMapping(value="{id}", method=RequestMethod.GET)
  public Session getSession(@PathVariable Long id) {
    return repo.findOne(id);
  }
  
  @RequestMapping(value="{sessionId}/questions", method=RequestMethod.GET)
  public Set<Question> getQuestions(@PathVariable Long sessionId) {
    return repo.findOne(sessionId).getQuestions();
  }
  
}

