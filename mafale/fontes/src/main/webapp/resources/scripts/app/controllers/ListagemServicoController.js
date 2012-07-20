function ListagemServicoController($scope, Servicos, $routeParams){
	
	$scope.filtro = [{alvo:'cliente.razaoSocial|like|string'}];
	
	$scope.paginacao = montarPaginador(function(inicio,numRegistros){
		showModal();
		Servicos.query({'inicio':inicio,'numRegistros':numRegistros,filtro:montarFiltro($scope.filtro)},function(data){
			$scope.resultado = data;
			$scope.paginacao.montarPaginacao(data);
			hideModal(); 
		},errorHandler);		
	});
	
	$routeParams.pagina || 1;
	$scope.paginacao.paginar($routeParams.pagina);

	$scope.remover = function(servico){
		confirm('Confirma remoção?',function(){
			showModal();
			Servicos.remove({id:servico.id},function(){
				alert('Serviço removido com sucesso');
				hideModal();
				$scope.paginacao.atualizar();
			},errorHandler);
		});
	};
	
} 

