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
var xAuthTokenHeaderName = 'x-auth-token';

angular.module('sessionQuestionsApp', ['ngRoute', 'ngCookies', 'sessionQuestionsApp.services','sessionQuestionsApp.login', 
                                       'sessionQuestionsApp.question', 'sessionQuestionsApp.sessions', 'sessionQuestionsApp.session'])
  .config(['$locationProvider', '$httpProvider', function($locationProvider, $httpProvider) {

      $locationProvider.hashPrefix('!');

      /* Intercept http errors */
      var interceptor = function ($rootScope, $q, $location) {

            function success(response) {
                return response;
            }

            function error(response) {

                var status = response.status;
                var config = response.config;
                var method = config.method;
                var url = config.url;

                if (status == 403) {
                  $location.path( "/login" );
                } else {
                  $rootScope.error = method + " on " + url + " failed with status " + status;
                }

                return $q.reject(response);
            }

            return function (promise) {
                return promise.then(success, error);
            };
        };
        $httpProvider.responseInterceptors.push(interceptor);

    } ]

  ).run(function($rootScope, $http, $location, $cookieStore, LoginService) {

    /* Reset error when a new view is loaded */
    $rootScope.$on('$viewContentLoaded', function() {
      delete $rootScope.error;
    });

    $rootScope.hasRole = function(role) {

      if ($rootScope.user === undefined) {
        return false;
      }

      if ($rootScope.user.roles[role] === undefined) {
        return false;
      }

      return $rootScope.user.roles[role];
    };
    
    $rootScope.location = $location;



    $rootScope.logout = function() {
      delete $rootScope.user;
      delete $http.defaults.headers.common[xAuthTokenHeaderName];
      $cookieStore.remove('user');
      $location.path("/sessions");
    };
//
//     /* Try getting valid user from cookie or go to login page */
//    var originalPath = $location.path();
//    $location.path("/login");
//    var user = $cookieStore.get('user');
//    if (user !== undefined) {
//      $rootScope.user = user;
//      $http.defaults.headers.common[xAuthTokenHeaderName] = user.token;
//
//      $location.path(originalPath);
//    }

    var user = $cookieStore.get('user');
    if (user !== undefined) {
      $rootScope.user = user;
      $http.defaults.headers.common[xAuthTokenHeaderName] = user.token;
    }
  });