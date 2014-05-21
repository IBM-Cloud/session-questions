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
angular.module('sessionQuestionsApp.session', ['sessionQuestionsApp.services', 'ui.bootstrap'])
.config(['$routeProvider', function ($routeProvider) {
  $routeProvider.when('/createSession/:id?', {
    templateUrl: 'app/sessions/session.html',
    controller: 'AdminSessionController'
  });
}])
.controller('AdminSessionController', ['$scope', '$location', '$routeParams', '$filter', '$modal', '$log', 'AdminSessionService', 'AdminQuestionService', function($scope, $location, $routeParams, $filter, $modal, $log, AdminSessionService, AdminQuestionService) {
  $scope.showEditIcon = false;
  $scope.edit = $routeParams.id != undefined; 
  $scope.filter = false;
  $scope.showDetails = !$scope.edit;
  if($routeParams.id) {
    $scope.session = AdminSessionService.get({id: $routeParams.id}, function(session) {
      session.sessionDate = $filter('date')(session.sessionDate, 'yyyy-MM-dd');
    });
    $scope.questions = AdminSessionService.questions({id: $routeParams.id, questions: 'questions'});
  } else {
    $scope.session = new AdminSessionService();
    $scope.session.active = true;
  }
  
  $scope.save = function() {
    $scope.session.sessionDate = Date.parse($scope.session.sessionDate);
    if($routeParams.id) {
      $scope.session.$update(function() {
        $scope.session.sessionDate = $filter('date')($scope.session.sessionDate, 'yyyy-MM-dd');
      });
    } else {
      $scope.session.$save(function() {
        $location.path('/');
      });
    }
    
    if($scope.edit) {
      $scope.showDetails = false;
    }
  };
  
  $scope.setShowEditIcon = function(show) {
    $scope.showEditIcon = show;
  };
  
  $scope.show = function() {
    $scope.showDetails = true;
  };
  
  $scope.delete = function() {
    $scope.session.$delete(function() {
      $location.path('/');
    });
  };
  
  $scope.deleteQuestion = function(question, index) {
    $scope.questions.splice(index, 1);  
    AdminQuestionService.delete({id:question.id});
  };
  
  $scope.applyFilter = function(filter) {
    $scope.filter = filter;
  }
  
  
  $scope.today = function() {
    $scope.dt = new Date();
  };
  $scope.today();

  $scope.clear = function () {
    $scope.dt = null;
  };

  // Disable weekend selection
  $scope.disabled = function(date, mode) {
    return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
  };

  $scope.toggleMin = function() {
    $scope.minDate = $scope.minDate ? null : new Date();
  };
  $scope.toggleMin();

  $scope.openCalendar = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.opened = true;
  };

  $scope.dateOptions = {
    formatYear: 'yy',
    startingDay: 1
  };
  
  
  

  $scope.open = function (question) {
    var modalInstance = $modal.open({
      templateUrl: 'app/sessions/modal.html',
      controller: 'ModalInstanceCtrl',
      resolve: {
        question: function () {
          return question;
        }
      }
    });

    modalInstance.result.then(function (selectedItem) {
    }, function () {
    });
  };
  
  if($scope.edit) {
    //TODO we also probably want to create topics for when questions are replied to and deleted
    //so we can keep the UI up to date.  Right now this is not the main point of the app so we
    //will leave those for later
    var socket = new SockJS('/api/questions');
    $scope.stompClient = Stomp.over(socket);
    $scope.stompClient.connect({}, function(frame) {
        $scope.stompClient.subscribe('/topic/questions', function(question){
            $scope.$apply(function() {
              $scope.questions.push(angular.fromJson(question.body));
            });
        });
    });
  }
}])

.controller('ModalInstanceCtrl', ['$scope', '$modalInstance', 'question', 'AdminQuestionService', function($scope, $modalInstance, question, AdminQuestionService) {
  $scope.question = question;
  $scope.reply = new AdminQuestionService();

  $scope.sendReply = function () {
    $scope.reply.$reply({id: question.id}, function() {
      question.answered = true;
    });
    $modalInstance.close();
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}]);