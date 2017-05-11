package acao;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ProjetoDAO;
import model.PPC;

public class CriaPPC implements Acao {
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws Exception{
		//int cod = Integer.parseInt(req.getParameter("cod"));
		String curso = req.getParameter("curso");
		int matriz = Integer.parseInt(req.getParameter("matriz"));
		int obrigatorio = Integer.parseInt(req.getParameter("obrigatorios"));
		int limitado = Integer.parseInt(req.getParameter("limitados"));
		int livre = Integer.parseInt(req.getParameter("livres"));
		
		//System.out.println("curso: "+curso);
		//System.out.println("matriz: "+matriz);
		
		ProjetoDAO dao = new ProjetoDAO();
		int novo = dao.buscaProjeto(matriz, curso);
		
		if(novo > 0){
			req.setAttribute("msg", "Projeto Pedagógico do "+curso+" já está cadastrado no sistema!");
			RequestDispatcher rd = req.getRequestDispatcher("/info.jsp");
			rd.forward(req, resp);
		} else{
			PPC p = new PPC();
			p.setCurso(curso);
			p.setMatriz(matriz);
			p.setCred_obrigatorias(obrigatorio);
			p.setCred_limitadas(limitado);
			p.setCred_livres(livre);
			
			dao.novoProjeto(p);
			
			req.setAttribute("msg", "Projeto Pedagógico do "+curso+" cadastrado com sucesso!");
			RequestDispatcher rd = req.getRequestDispatcher("/info.jsp");
			rd.forward(req, resp);
		}
	}
}
