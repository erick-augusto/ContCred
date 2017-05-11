<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Gerenciamento de Projetos Pedagógicos</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<style>
    .navbar {
      margin-bottom: 50px;
      border-radius: 0;
    }

     .jumbotron {
      background-color: #008000;
      color: #FFFFFF;
      margin-bottom: 0;
      height: 150px;
    }

    .row.content {
    	height: 500px;
    }

    .fundo{
    	background-color: #CCFFCC;
    }
    
    .menu{
    	background-color: #F2F2F2;
    }
    
    .sidenav {
      background-color: #FFFFFF;
      height: 100%;
    }

    footer {
      background-color: #CCFFCC;
      padding: 10px;
    }
</style>
<script type="text/javascript">
	function determinaCurso(cod_ppc){
		document.getElementById('ppc_id').value = cod_ppc;
	}
</script>
</head>
<body>
<div class="jumbotron">
  <div class="container text-center">
    <h2>Sistema de Contagem de Créditos</h2>
  </div>
</div>

<div class="container-fluid">
  <div class="row content">
    <div class="col-sm-2 sidenav hidden-xs">
      <ul class="nav nav-pills nav-stacked menu row content">
        <li class="fundo"><a href="index.jsp">Home</a></li>
        <li><a href="disciplina.jsp">Disciplina</a></li>
        <li><a href="ppc.jsp">Projeto Pedagógico</a></li>
        <li><a href="convalidacao.jsp">Convalidação</a></li>
        <li><a href="credito.jsp">Contagem de Créditos</a></li>
      </ul><br>
    </div>
    <div class="col-sm-10">
    	<jsp:useBean id="dao" class="dao.ProjetoDAO"/>
		<form class="form-horizontal" role="form" action="controller" method="post">
			<a href="cadastraProjeto.jsp">Cadastrar Novo Projeto</a>
			<h3>Lista de Projetos Cadastrados</h3>
			<table class="table table-striped table-hover">
				<tr>
					<th>Curso</th>
					<th>Matriz</th>
					<th>Créditos Obrigatórios</th>
					<th>Créditos Limitados</th>
					<th>Créditos Livres</th>
					<th>Grade</th>
				</tr>
				<c:forEach var="ppc" items="${dao.retornaProjetos()}">
					<tr>
						<td>${ppc.curso}</td>
						<td>${ppc.matriz}</td>	
						<td>${ppc.cred_obrigatorias}</td>	
						<td>${ppc.cred_limitadas}</td>	
						<td>${ppc.cred_livres}</td>
						<td><input type="submit" class="btn btn-success" value="Visualizar" onClick="determinaCurso(${ppc.cod_ppc})"/></td>
					</tr>
				</c:forEach>
			</table>
			<input type="hidden" id="ppc_id" name="ppc_id" value=""/>
			<input type="hidden" name="opcao" value="VisualizarGrade"/>
		</form>
    </div>
  </div>
</div>
</body>
</html>