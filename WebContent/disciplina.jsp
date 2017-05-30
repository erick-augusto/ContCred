<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Gerenciamento de Disciplinas</title>
<script type="text/javascript">
	function retornaLetra(letra){
		document.getElementById('letra').value = letra;
	}
</script>
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
		<form class="form-horizontal" role="form" action="controller" method="post">
			<a href="cadastraDisciplina.jsp">Cadastrar Nova Disciplina</a>
			<br>
			<input type="hidden" name="letra" id="letra" value="">
			<input type="submit" class="btn btn-success" value="A" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="B" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="C" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="D" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="E" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="F" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="G" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="H" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="I" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="J" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="K" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="L" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="M" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="N" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="O" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="P" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="Q" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="R" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="S" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="T" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="U" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="V" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="W" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="X" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="Y" onClick="return retornaLetra(value)">
			<input type="submit" class="btn btn-success" value="Z" onClick="return retornaLetra(value)">
			<input type="hidden" name="opcao" value="OrdemDisciplinas">
			
			<h3>Lista de Disciplinas Cadastradas</h3>
			<table class="table table-striped table-hover">
				<tr>
					<th>Código</th>
					<th>Nome</th>
					<th>T</th>
					<th>P</th>
					<th>I</th>
					<th></th>
				</tr>
				<c:forEach var="disciplina" items="${dao.listarDisciplinas()}">
					<tr>
						<td>${disciplina.cod_disciplina}</td>
						<td>${disciplina.nome}</td>
						<td>${disciplina.t}</td>	
						<td>${disciplina.p}</td>	
						<td>${disciplina.i}</td>	
						<td></td>
					</tr>
				</c:forEach>
			</table>
		</form>
    </div>
  </div>
</div>	
</body>
</html>
