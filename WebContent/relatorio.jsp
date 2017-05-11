<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sucesso</title>
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
    	<jsp:useBean id="dao" class="dao.DisciplinaDAO"/>
    	<jsp:useBean id="download" class="controller.FileController"/>
    	<form role="form" action="upload" method="post">
		${requestScope.msg}
		
		<table class="table table-striped table-hover">
			<tr>
				<th>Tipo de Disciplina</th>
				<th>Deve Cursar</th>
				<th>Cursou</th>
				<th>Procentagem</th>
			</tr>
			<tr>
				<td>Obrigatórias BC&T</td>
				<td>${requestScope.total_bct}</td>
				<td>${requestScope.cursada_bct}</td>
				<td>${requestScope.p_bct}</td>
			</tr>
			<tr>
				<td>Obrigatórias ${requestScope.sigla}</td>
				<td>${requestScope.curso_obrigatorio}</td>
				<td>${requestScope.cursada_curso}</td>
				<td>${requestScope.p_curso}</td>
			</tr>
			<tr>
				<td>Limitadas</td>
				<td>${requestScope.total_limitada}</td>
				<td>${requestScope.cursada_limitada}</td>
				<td>${requestScope.p_limitada}</td>
			</tr>
			<tr>
				<td>Livres</td>
				<td>${requestScope.total_livre}</td>
				<td>${requestScope.cursada_livre}</td>
				<td>${requestScope.p_livre}</td>
			</tr>
			<tr>
				<td>Total do Curso</td>
				<td>${requestScope.total_curso}</td>
				<td>${requestScope.cursada_total}</td>
				<td>${requestScope.p_total}</td>
			</tr>
		</table>

		<h3>Obrigatórias do BC&T</h3>
		<table class="table table-striped table-hover">
			<tr>
				<th>Código</th>
				<th>Disciplina</th>
				<th>Créditos</th>
			</tr>
			<c:forEach var="disciplina" items="${requestScope.bct}">
				<tr>
	 				<td>${disciplina.cod_disciplina}</td>
	 				<td>${disciplina.nome}</td>
	 				<td>${dao.credDisciplina(disciplina.cod_disciplina)}</td>
				</tr>
			</c:forEach>
		</table>

		<h3>Obrigatórias do ${requestScope.sigla}</h3>
		<table class="table table-striped table-hover">
			<tr>
				<th>Código</th>
				<th>Disciplina</th>
				<th>Créditos</th>
			</tr>
			<c:forEach var="disciplina" items="${requestScope.curso}">
				<tr>
	 				<td>${disciplina.cod_disciplina}</td>
	 				<td>${disciplina.nome}</td>
	 				<td>${dao.credDisciplina(disciplina.cod_disciplina)}</td>
				</tr>
			</c:forEach>
		</table>

		<h3>Limitadas do ${requestScope.sigla}</h3>
		<table class="table table-striped table-hover">
			<tr>
				<th>Código</th>
				<th>Disciplina</th>
				<th>Créditos</th>
			</tr>
			<c:forEach var="disciplina" items="${requestScope.limitadas}">
				<tr>
	 				<td>${disciplina.cod_disciplina}</td>
	 				<td>${disciplina.nome}</td>
	 				<td>${dao.credDisciplina(disciplina.cod_disciplina)}</td>
				</tr>
			</c:forEach>
		</table>

		<h3>Livres do ${requestScope.sigla}</h3>
		<table class="table table-striped table-hover">
			<tr>
				<th>Código</th>
				<th>Disciplina</th>
				<th>Créditos</th>
			</tr>
			<c:forEach var="disciplina" items="${requestScope.livres}">
				<tr>
	 				<td>${disciplina.cod_disciplina}</td>
	 				<td>${disciplina.nome}</td>
	 				<td>${dao.credDisciplina(disciplina.cod_disciplina)}</td>
				</tr>
			</c:forEach>
		</table>
		
		<h3>Disciplinas não encontradas no Banco de Dados do Sistema</h3>
		<table class="table table-striped table-hover">
			<tr>
				<th>Código</th>
				<th>Disciplina</th>
				<th>Créditos</th>
			</tr>
			<c:forEach var="disciplina" items="${requestScope.nao_catalogadas}">
				<tr>
	 				<td>${disciplina.cod_disciplina}</td>
	 				<td>${disciplina.nome}</td>
	 				<td>${disciplina.t}</td>
				</tr>
			</c:forEach>
		</table>
		</form>
    </div>
  </div>
</div>
</body>
</html>