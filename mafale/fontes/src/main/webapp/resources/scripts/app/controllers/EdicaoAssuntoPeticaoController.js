function EdicaoAssuntoPeticaoController($scope,AssuntosPeticao, $routeParams,$location){

	$scope.assunto = {};
	
	if ($routeParams.id!='novo'){
		showModal();
		AssuntosPeticao.get({id:$routeParams.id},function(data){
			$scope.assunto = data;
			hideModal();
		},errorHandler);
	}
		
	$scope.salvar = function(){
		showModal();
		AssuntosPeticao.save({entidade:$scope.assunto},function(){
			hideModal();
			alert("Assunto de petição salvo com sucesso");
			$location.path('/listagemAssuntoPeticao/1');
		},errorHandler);
	};
}