'use strict';

angular.module('urlshortenerApp')

    .directive('navigationBar',['UserService','$location',function(UserService,$location){

        return {
            restrict : 'E', //As Element or Attribute
            transclude : false,
            templateUrl : 'views/partials/navBar.html',

            link : function(scope, element, attrs) {


                scope.logged = function(){
                    return UserService.currentlyLogged;
                }

                scope.username = function(){
                    return UserService.username;
                }


                scope.goToProfile = function(){
                    alert("Not implemented!");
                }

                scope.logout = function(){
                    UserService.deleteCurrentToken();
                    $location.path('/login');
                }

                scope.currentPath = function(){
                    return $location.path();
                }

            }
        }
    }]);