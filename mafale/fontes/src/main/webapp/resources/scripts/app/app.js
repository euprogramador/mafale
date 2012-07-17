angular.module('mafale', ['mafaleServices']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/home', {templateUrl: 'resources/partials/home.html',   controller: HomeController}).
      when('/listagemTipoCliente/:pagina', {templateUrl: 'resources/partials/listagemTipoCliente.html',   controller: ListagemTipoClienteController}).
      when('/edicaoTipoCliente/:id', {templateUrl: 'resources/partials/edicaoTipoCliente.html',   controller: EdicaoTipoClienteController}).
      when('/listagemPorteCliente/:pagina', {templateUrl: 'resources/partials/listagemPorteCliente.html',   controller: ListagemPorteClienteController}).
      when('/edicaoPorteCliente/:id', {templateUrl: 'resources/partials/edicaoPorteCliente.html',   controller: EdicaoPorteClienteController}).
      when('/listagemCliente/:pagina', {templateUrl: 'resources/partials/listagemCliente.html',   controller: ListagemClienteController}).
      when('/edicaoCliente/:id', {templateUrl: 'resources/partials/edicaoCliente.html',   controller: EdicaoClienteController}).
      otherwise({redirectTo: '/home'});
}]);
