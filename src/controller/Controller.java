package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import acao.Acao;

@WebServlet("/controller")
public class Controller extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String opcao = req.getParameter("opcao");
		//System.out.println("opção: "+opcao);
		String nomeDaClasse = "acao." + opcao;
		
		try {
			Class<?> classe = Class.forName(nomeDaClasse);
			Acao acao = (Acao) classe.newInstance();
			acao.executa(req, resp);
		} catch (Exception e) {
			throw new ServletException("Erro: ",e);
		}
	}
}
