function ListagemClienteController($scope, Clientes, $routeParams){
	
	$scope.paginacao = montarPaginador(function(inicio,numRegistros){
		showModal();
		Clientes.query({'inicio':inicio,'numRegistros':numRegistros},function(data){
			$scope.resultado = data;
			$scope.paginacao.montarPaginacao(data);
			hideModal();
		},errorHandler);		
	});
	$routeParams.pagina || 1;
	$scope.paginacao.paginar($routeParams.pagina);

	$scope.remover = function(tipo){
		confirm('Confirma remoção?',function(){
			showModal();
			Clientes.remove({id:tipo.id},function(){
				alert('Cliente removido com sucesso');
				hideModal();
				$scope.paginacao.atualizar();
			},errorHandler);
		});
	};
} 
