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
var services = angular.module('sessionQuestionsApp.services', ['ngResource']);

services.factory('LoginService', function($resource) {

  return $resource(':action', {},
      {
        authenticate: {
          method: 'POST',
          params: {'action' : 'authenticate'},
          headers : {'Content-Type': 'application/x-www-form-urlencoded'}
        }
      }
    );
});

services.factory("SessionService", function($resource) {
  return $resource('api/sessions/:id:active/:state:questions', {
    id : "@id",
    active: "@active",
    state: "@state",
    questions: "@questions"
  }, {
    getActiveSessions: {
      method: "GET",
      params: {
        active: "active",
        state: "true"
      },
      isArray: true
    },
    createQuestion: {
      method: "POST",
      params: {
        questions: "questions"
      }
    }
  });
});

services.factory("QuestionService", function($resource) {
  return $resource('api/sessions/:id/questions', {
    id : "@id",
  }, {
    save: {
      method: "POST",
    }
  });
});

services.factory("AdminSessionService", function($resource) {
  return $resource('api/admin/sessions/:id/:questions',{
    id: '@id',
    questions: '@questions'
  },{
    update : {
      method: 'PUT'
    },
    questions: {
      method: 'GET',
      isArray: true
    }
  });
});

services.factory("AdminQuestionService", function($resource) {
  return $resource('api/admin/questions/:id/:reply', {id: '@id', reply: '@reply'}, {
    reply: {
      method: 'POST',
      params: {
        reply: "reply"
      }
    }
  });
});