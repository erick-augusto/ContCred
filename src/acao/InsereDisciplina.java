package acao;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DisciplinaDAO;
import model.Disciplina;

public class InsereDisciplina implements Acao {
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws Exception{
		String cod = req.getParameter("codigo");
		String nome = req.getParameter("nome");
		int t = Integer.parseInt(req.getParameter("t"));
		int p = Integer.parseInt(req.getParameter("p"));
		int i = Integer.parseInt(req.getParameter("i"));
		//System.out.println("cod: "+cod);
		//System.out.println("nome: "+nome);
		//System.out.println("t: "+t);
		//System.out.println("p: "+p);
		//System.out.println("i: "+i);

		DisciplinaDAO dao = new DisciplinaDAO();
		Disciplina d = dao.buscaDisciplina(cod);
		if(d.getCod_disciplina() != null){
			req.setAttribute("msg", "Disciplina "+nome+" já está cadastrada no sistema!");
			RequestDispatcher rd = req.getRequestDispatcher("/info.jsp");
			rd.forward(req, resp);
		} else{
			d.setCod_disciplina(cod);
			d.setNome(nome);
			d.setT(t);
			d.setP(p);
			d.setI(i);
			
			dao.insereDisciplina(d);
			
			req.setAttribute("msg", "Disciplina "+nome+" cadastrada com sucesso!");
			RequestDispatcher rd = req.getRequestDispatcher("/info.jsp");
			rd.forward(req, resp);
		}
	}
}
