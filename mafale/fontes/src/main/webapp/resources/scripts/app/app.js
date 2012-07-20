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
      when('/listagemTipoServico/:pagina', {templateUrl: 'resources/partials/listagemTipoServico.html',   controller: ListagemTipoServicoController}).
      when('/edicaoTipoServico/:id', {templateUrl: 'resources/partials/edicaoTipoServico.html',   controller: EdicaoTipoServicoController}).
      when('/listagemAssuntoPeticao/:pagina', {templateUrl: 'resources/partials/listagemAssuntoPeticao.html',   controller: ListagemAssuntoPeticaoController}).
      when('/edicaoAssuntoPeticao/:id', {templateUrl: 'resources/partials/edicaoAssuntoPeticao.html',   controller: EdicaoAssuntoPeticaoController}).
      when('/listagemServico/:pagina', {templateUrl: 'resources/partials/listagemServico.html',   controller: ListagemServicoController}).
      when('/edicaoServico/:id', {templateUrl: 'resources/partials/edicaoServico.html',   controller: EdicaoServicoController}).
      when('/listagemPeticao/:servicoId', {templateUrl: 'resources/partials/listagemPeticao.html',   controller: ListagemPeticaoController}).
      otherwise({redirectTo: '/home'});
}])

//directiva para mascaras
.directive('uiMask', function() {
    return {
        require: 'ngModel',
        
        link: function($scope, element, attrs, controller) {
            var mask;
            
            // Update the placeholder attribute from the mask
            var updatePlaceholder = function() {
                if ( !attrs.placeholder ) {
                    element.attr('placeholder', mask.replace(/9/g, '_'));
                }
            };

            // Render the mask widget after changes to either the model value or the mask
            controller.$render = function() {
                var value = controller.$viewValue || '';
                element.val(value);
                if ( !!mask ) {
                    element.mask(mask);
                }
                updatePlaceholder();
            };
            
            // Keep watch for changes to the mask
            attrs.$observe('uiMask', function(maskValue) {
                mask = maskValue;
                controller.$render();
            });
            
            // Check the validity of the masked value
            controller.$parsers.push(function(value) {
                var isValid;
                isValid = element.data('mask-isvalid');
                controller.$setValidity('mask', isValid);
                if (isValid) {
                    return element.mask();
                } else {
                    return null;
                }
            });
            
            // Update the model value everytime a key is pressed on the mask widget
            element.bind('keyup', function() {
            	$scope.$apply(function() {
            		controller.$setViewValue(element.mask());
                });
            });
            
        }
    };
});
