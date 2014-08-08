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
package net.bluemix.questions.data.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Session {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String title;
  private String presenter;
  private String conference;
  @DateTimeFormat(pattern = "dd/MM/yyyy")
  private Date sessionDate;
  private boolean active;
  private String number;
  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="session")
  private Set<Question> questions;
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getPresenter() {
    return presenter;
  }
  public void setPresenter(String presenter) {
    this.presenter = presenter;
  }

  public String getConference() {
    return conference;
  }
  public void setConference(String conference) {
    this.conference = conference;
  }
  public Date getSessionDate() {
    return sessionDate;
  }
  public void setSessionDate(Date date) {
    this.sessionDate = date;
  }
  public boolean isActive() {
    return active;
  }
  public void setActive(boolean active) {
    this.active = active;
  }
  public String getNumber() {
    return number;
  }
  public void setNumber(String number) {
    this.number = number;
  }
  public Long getId() {
    return id;
  }
  public Set<Question> getQuestions() {
    return questions;
  }
  public void setQuestions(Set<Question> questions) {
    this.questions = questions;
  }
  
  
}

