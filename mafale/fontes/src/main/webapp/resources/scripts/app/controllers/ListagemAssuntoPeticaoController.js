function ListagemAssuntoPeticaoController($scope, AssuntosPeticao, $routeParams){
	
	$scope.paginacao = montarPaginador(function(inicio,numRegistros){
		showModal();
		AssuntosPeticao.query({'inicio':inicio,'numRegistros':numRegistros},function(data){
			$scope.resultado = data;
			$scope.paginacao.montarPaginacao(data);
			hideModal();
		},errorHandler);		
	});
	$routeParams.pagina || 1;
	$scope.paginacao.paginar($routeParams.pagina);

	$scope.remover = function(assunto){
		confirm('Confirma remoção?',function(){
			showModal();
			AssuntosPeticao.remove({id:assunto.id},function(){
				alert('Assunto de petição removido com sucesso');
				hideModal();
				$scope.paginacao.atualizar();
			},errorHandler);
		});
	};
	
} 
