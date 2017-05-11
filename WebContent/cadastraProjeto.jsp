<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cadastro de Projetos Pedagógicos</title>
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
    	<form role="form" action="controller" method="post">
			<a href="cadastraGrade.jsp">Criar Grade para o PPC</a>
			<h3>Criar Projeto</h3>
			<div class="form-group">
				<label for="curso">Curso:</label>
				<input type="text" name="curso" class="form-control" id="curso"/>
			</div>
			<div class="form-group"></div>
			<div class="form-group">
				<label for="matriz">Matriz:</label>
				<input type="text" name="matriz" class="form-control" id="matriz"/>
			</div>
			<div class="form-group">
				<label for="obrigatorios">Créditos Obrigatórios:</label>
				<input type="text" name="obrigatorios" class="form-control" id="obrigatorios"/>
			</div>
			<div class="form-group">
				<label for="limitados">Créditos Limitados:</label>
				<input type="text" name="limitados" class="form-control" id="limitados"/>
			</div>
			<div class="form-group">
				<label for="livres">Créditos Livres:</label>
				<input type="text" name="livres" class="form-control" id="livres"/>
			</div>
			<input type="submit" class="btn btn-success" value="Cadastrar Projeto">
			<input type="hidden" name="opcao" value="CriaPPC">
		</form>
    </div>
  </div>
</div>
</body>
</html>