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
angular.module('sessionQuestionsApp.login', ['sessionQuestionsApp.services'])
.config(['$routeProvider', function ($routeProvider) {
  $routeProvider.when('/login', {
    templateUrl: 'app/login/login.html',
    controller: 'LoginController'
  });
}])
.controller('LoginController', ['$scope', '$rootScope', '$location', '$http', '$cookieStore', 'LoginService', function($scope, $rootScope, $location, $http, $cookieStore, LoginService) {
  $scope.error = false;
  $scope.login = function() {
    LoginService.authenticate($.param({username: $scope.username, password: $scope.password}), function(user) {
      $rootScope.user = user;
      $http.defaults.headers.common[ xAuthTokenHeaderName ] = user.token;
      $cookieStore.put('user', user);
      $location.path("/");
    }, function(error) {
      $scope.error = true;
    });
  };
}]);