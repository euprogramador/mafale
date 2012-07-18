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
}])

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



//
//
//
//function (value) { this.$viewValue = value; 
//// change to dirty 
//if (this.$pristine) { this.$dirty = true; this.$pristine = false;
//$element.removeClass(PRISTINE_CLASS).addClass(DIRTY_CLASS); parentForm.$setDirty(); } 
//forEach(this.$parsers, function(fn) { value = fn(value); }); if (this.$modelValue !== value) 
//{ this.$modelValue = value; 
//ngModelSet($scope, value); 
//forEach(this.$viewChangeListeners, function(listener) { try { listener(); }
//catch(e) { $exceptionHandler(e); } }) 
//} }
//}