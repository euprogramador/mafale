angular.module('mafale', ['mafaleServices']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/home', {templateUrl: 'resources/partials/home.html',   controller: HomeController}).
      when('/listagemTipoCliente/:pagina', {templateUrl: 'resources/partials/listagemTipoCliente.html',   controller: ListagemTipoClienteController}).
      when('/edicaoTipoCliente/:id', {templateUrl: 'resources/partials/edicaoTipoCliente.html',   controller: EdicaoTipoClienteController}).
      otherwise({redirectTo: '/home'});
}]);

