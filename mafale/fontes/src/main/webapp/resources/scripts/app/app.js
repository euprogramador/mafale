angular.module('mafale', ['mafaleServices']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/home', {templateUrl: 'resources/partials/home.html',   controller: HomeController}).
      otherwise({redirectTo: '/home'});
}]);