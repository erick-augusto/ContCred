package acao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ConvalidacaoDAO;
import dao.DisciplinaDAO;
import dao.ProjetoDAO;
import model.Convalidacao;
import model.Disciplina;
import model.Grade;
import model.PPC;

public class Contagem implements Acao {
	public void executa(HttpServletRequest req, HttpServletResponse resp) throws Exception{
		String nome = req.getParameter("nome");
		int ra = Integer.parseInt(req.getParameter("ra"));
		String sigla = req.getParameter("curso");
		int matriz = Integer.parseInt(req.getParameter("matriz"));
		
		//Determina qual curso será feita a contagem
		String curso = determinaCurso(sigla);
					
		//Buscar a lista de matérias da matriz do curso
		ProjetoDAO p_dao = new ProjetoDAO();
		int p_id = p_dao.buscaProjeto(matriz, curso);
		System.out.println("cod: "+p_id);
		List<Grade> grade_ppc = p_dao.buscaGrade(p_id);
		System.out.println("grade: "+grade_ppc.size());
		int i;
		int curso_obrigatorio = 0;
		int curso_cred = 0;
		int curso_limitado = 0;
		int curso_livre = 0;
		PPC projeto = p_dao.retornaProjeto(p_id);
		curso_obrigatorio = projeto.getCred_obrigatorias();
		curso_limitado = projeto.getCred_limitadas();
		curso_livre = projeto.getCred_livres();
		curso_cred = curso_obrigatorio + curso_limitado + curso_livre;
		
		//Método para determinar qual matriz do BC&T usar
		List<PPC> bct = p_dao.listaProjetos();
		int ultimo_projeto = 0;
		int cod_ppc = 0;
		int bct_total = 0;
		for(i=0;i<bct.size();i++){
			if(ultimo_projeto < bct.get(i).getMatriz() && matriz >= bct.get(i).getMatriz()){
				ultimo_projeto = bct.get(i).getMatriz();
				cod_ppc = bct.get(i).getCod_ppc();
				bct_total = bct.get(i).getCred_obrigatorias();
			}
		}
		List<Grade> grade_bct = p_dao.buscaGrade(cod_ppc);
		System.out.println("bct "+ultimo_projeto+": "+grade_bct.size()+" total: "+bct_total);
		
		/*for(i=0;i<grade_ppc.size();i++){
			System.out.println(grade_ppc.get(i).getCod_disciplina());
		}*/
		
		//Verificar a lista de matérias do histórico do aluno
		//Leitura do arquivo com o histórico do aluno
		//Testar se é possível fazer o split por meio do tab
		FileReader arq = new FileReader("C:\\Users\\Erick\\Documents\\Eclipse Projects\\ContCred\\files\\aluno.csv");
		BufferedReader buffer = new BufferedReader(arq);
		String linha = buffer.readLine();
		linha = buffer.readLine();
		
		//Laço para a leitura das linhas do arquivo
		String[] campos;
		List<Disciplina> cursadas = new ArrayList<>();//Preencher com os dados do arquivo do aluno
		DisciplinaDAO d_dao = new DisciplinaDAO();
		while(linha != null){
			if(!linha.contains("Quadrimestre")){
				linha = linha.replaceAll("\"", "");
				campos = linha.split("\t",-1);
				if(campos.length > 1){
					//System.out.println(campos[0]+" "+campos[1]+" "+campos[2]+" "+campos[3]+" "+campos[4]+" "+campos[5]);
					Disciplina cursada = d_dao.buscaDisciplina(campos[0]);
					
					//cursada.setCod_disciplina(campos[0]);
					//cursada.setNome(campos[1]);
					if(cursada.getCod_disciplina() != null && (campos[5].equals("Aprovado") || campos[5].equals("Apr.S.Nota"))){
						//System.out.println("c: "+cursada.getCod_disciplina()+" "+cursada.getNome()+" "+cursada.getT()+" "+cursada.getP());
						cursadas.add(cursada);
					}
				}
			}
			if(linha.contains("DISCIPLINAS CURSADAS EM MOBILIDADE")){
				linha = null;
				System.out.println("Última linha");
			} else{
				linha = buffer.readLine();
			}
		}
		System.out.println("cursadas: "+cursadas.size());

		List<Disciplina> nao_encontradas = new ArrayList<>();
		List<Disciplina> obrigatorias = new ArrayList<>();
		List<Disciplina> limitadas = new ArrayList<>();
		List<Disciplina> obrigatoria_bct = new ArrayList<>();
		int cred_obrigatorio = 0;
		int cred_obrigatorio_bct = 0;
		int cred_obrigatorio_curso = 0;
		int cred_limitado = 0;
		boolean encontrada;
		for(Disciplina cursada : cursadas){
			encontrada = false;
			//System.out.println("cod para verificar: "+cursada.getCod_disciplina());
			for(i=0;i<grade_ppc.size();i++){
				//System.out.println(grade_ppc.get(i).getCod_disciplina());
				String cod = grade_ppc.get(i).getCod_disciplina();
				if(cod.equals(cursada.getCod_disciplina())){
					encontrada= true;
					//creditos += cursada.getT() + cursada.getP();
					
					//verifica se é obrigatória ou limitada
					String status = grade_ppc.get(i).getStatus();
					if(status.equals("Obrigatória")){
						//cred_obrigatorio += cursada.getT() + cursada.getP();
						cred_obrigatorio_curso += cursada.getT() + cursada.getP();
						obrigatorias.add(cursada);
					} else{
						cred_limitado += cursada.getT() + cursada.getP();
						limitadas.add(cursada);
					}
				}
			}
			for(i=0;i<grade_bct.size();i++){
				String cod = grade_bct.get(i).getCod_disciplina();
				if(cod.equals(cursada.getCod_disciplina())){
					encontrada= true;
					
					//verifica se é obrigatória ou limitada
					String status = grade_bct.get(i).getStatus();
					if(status.equals("Obrigatória")){
						//cred_obrigatorio += cursada.getT() + cursada.getP();
						cred_obrigatorio_bct += cursada.getT() + cursada.getP();
						obrigatoria_bct.add(cursada);
					}
				}
			}
			if(encontrada == false){
				nao_encontradas.add(cursada);
			}
		}
		System.out.println("Não Encontradas: "+nao_encontradas.size());
		
		//Verificar as convalidações se sobrarem disciplinas que não foram encontradas 
		//e não foi atingido 100% das disciplinas obrigatórias
		ConvalidacaoDAO c_dao = new ConvalidacaoDAO();
		List<Convalidacao> convalidacoes = new ArrayList<>();
		List<Disciplina> livres = new ArrayList<>();
		if(!nao_encontradas.isEmpty()){
			//System.out.println("Convalidações");
			for(Disciplina nao_encontrada : nao_encontradas){
				//System.out.println(nao_encontrada.getCod_disciplina()+" "+p_id);
				Convalidacao c = c_dao.buscaConvalidacao(nao_encontrada.getCod_disciplina(), p_id);
				if(c.getCod_convalidacao() != null){
					//System.out.println("c: "+c.getCod_convalidacao());
					convalidacoes.add(c);
				} else{
					//Conta como livre
					//System.out.println("n: "+nao_encontrada.getCod_disciplina()+" "+nao_encontrada.getNome());
					livres.add(nao_encontrada);
				}
			}
			System.out.println("Convalidadas: "+convalidacoes.size());
			boolean convalidado;
			for(Convalidacao convalidacao: convalidacoes){
				convalidado = false;
				for(i=0;i<grade_ppc.size();i++){
					String cod = grade_ppc.get(i).getCod_disciplina();
					if(cod.equals(convalidacao.getCod_convalidacao())){
						convalidado = true;
						//Disciplina d = d_dao.buscaDisciplina(convalidacao.getCod_convalidacao());
						//creditos += d.getT() + d.getP();
						
						//System.out.println("convalidada: "+d.getNome()+" "+d.getT()+" "+d.getP());
						//verifica se é obrigatória ou limitada
						String status = grade_ppc.get(i).getStatus();
						if(status.equals("Obrigatória")){
							//Buscar a disciplina de origem para adicionar os créditos e colocar na lista
							Disciplina convalidada = d_dao.buscaDisciplina(convalidacao.getCod_disciplina());
							//cred_obrigatorio += convalidada.getT() + convalidada.getP();//d.getT() + d.getP();
							cred_obrigatorio_curso += convalidada.getT() + convalidada.getP();
							obrigatorias.add(convalidada);
							System.out.println("origem: "+convalidada.getNome()+" "+convalidada.getT()+" "+convalidada.getP());
						} else{
							//Buscar a disciplina de origem para adicionar os créditos e colocar na lista
							Disciplina convalidada = d_dao.buscaDisciplina(convalidacao.getCod_disciplina());
							cred_limitado += convalidada.getT() + convalidada.getP();//d.getT() + d.getP();
							limitadas.add(convalidada);
							System.out.println("origem: "+convalidada.getNome()+" "+convalidada.getT()+" "+convalidada.getP());
						}
					}
				}
				for(i=0;i<grade_bct.size();i++){
					String cod = grade_bct.get(i).getCod_disciplina();
					if(cod.equals(convalidacao.getCod_convalidacao())){
						convalidado = true;
						//Disciplina d = d_dao.buscaDisciplina(convalidacao.getCod_convalidacao());
						
						//System.out.println("convalidada: "+d.getNome()+" "+d.getT()+" "+d.getP());
						//verifica se é obrigatória
						String status = grade_ppc.get(i).getStatus();
						if(status.equals("Obrigatória")){
							//Buscar a disciplina de origem para adicionar os créditos e colocar na lista
							Disciplina convalidada = d_dao.buscaDisciplina(convalidacao.getCod_disciplina());
							//cred_obrigatorio += convalidada.getT() + convalidada.getP();//d.getT() + d.getP();
							cred_obrigatorio_bct += convalidada.getT() + convalidada.getP();
							obrigatorias.add(convalidada);
							System.out.println("origem: "+convalidada.getNome()+" "+convalidada.getT()+" "+convalidada.getP());
						}
					}
				}
				if(convalidado == false){
					//Conta os créditos como livres
					//Buscar a disciplina de origem para adicionar os créditos e colocar na lista
					Disciplina convalidada = d_dao.buscaDisciplina(convalidacao.getCod_disciplina());
					//Disciplina d = d_dao.buscaDisciplina(convalidacao.getCod_convalidacao());
					livres.add(convalidada);
					System.out.println("origem: "+convalidada.getNome()+" "+convalidada.getT()+" "+convalidada.getP());
				}
			}
		}
		
		//Contar o restante como livre
		System.out.println("Livres: "+livres.size());
		int cred_livre = 0;
		for(Disciplina livre : livres){
			//System.out.println("l: "+livre.getCod_disciplina());
			cred_livre += livre.getT() + livre.getP();
		}
		
		//Exibir os resultados da contagem
		//Faz a soma de todos os créditos contados e as porcentagens de cada tipo de crédito
		System.out.println("Obrigatórios: "+cred_obrigatorio_curso + cred_obrigatorio_bct);
		System.out.println("Limitados: "+cred_limitado);
		System.out.println("Livres: "+cred_livre);
		/*System.out.println("********************OBRIGATÓRIAS BCT********************");
		for(i=0;i<obrigatoria_bct.size();i++){
			System.out.println(obrigatoria_bct.get(i).getCod_disciplina()+" "+obrigatoria_bct.get(i).getNome());
		}
		System.out.println("********************OBRIGATÓRIAS BCC********************");
		for(i=0;i<obrigatorias.size();i++){
			System.out.println(obrigatorias.get(i).getCod_disciplina()+" "+obrigatorias.get(i).getNome());
		}
		System.out.println("********************LIMITADAS********************");
		for(i=0;i<limitadas.size();i++){
			System.out.println(limitadas.get(i).getCod_disciplina()+" "+limitadas.get(i).getNome());
		}
		System.out.println("********************LIVRES********************");
		for(i=0;i<livres.size();i++){
			System.out.println(livres.get(i).getCod_disciplina()+" "+livres.get(i).getNome());
		}*/
		
		req.setAttribute("msg", "<h1>Contagem realizada com sucesso!</h1><br>");
				/*+ "Nome do Aluno: "+nome+" <br>RA: "+ra+" <br>Matriz: "+matriz+""
				+ "Disciplinas Obrigatórias Cursadas BCT: "+obrigatoria_bct.size()+"<br>"
				+ "Disciplinas Obrigatórias Cursadas BCC: "+obrigatorias.size()+" - Créditos: "+cred_obrigatorio+"<br>"
				+ "Disciplinas de Opção Limitada: "+limitadas.size()+" - Créditos: "+cred_limitado+"<br>"
				+ "Disciplinas Livres: "+livres.size()+" - Créditos: "+cred_livre+"<br>");*/
		
		int total = cred_obrigatorio_curso + cred_obrigatorio_bct + cred_limitado + cred_livre;
		int total_curso = curso_cred + bct_total;
		float p_bct = (float) (100*cred_obrigatorio_bct)/bct_total;
		float p_curso = (float) (100*cred_obrigatorio_curso)/curso_obrigatorio;
		float p_total = (float) (100*total)/total_curso;
		float p_limitada = (float) (100*cred_limitado)/curso_limitado;
		float p_livre = (float) (100*cred_livre)/curso_livre;
		
		req.setAttribute("sigla", sigla);
		req.setAttribute("total_bct", bct_total);
		req.setAttribute("curso_obrigatorio", curso_obrigatorio);
		req.setAttribute("total_curso", total_curso);
		req.setAttribute("total_limitada", curso_limitado);
		req.setAttribute("total_livre", curso_livre);
		req.setAttribute("cursada_bct", cred_obrigatorio_bct);
		req.setAttribute("cursada_curso", cred_obrigatorio_curso);
		req.setAttribute("cursada_total", total);
		req.setAttribute("cursada_limitada", cred_limitado);
		req.setAttribute("cursada_livre", cred_livre);
		req.setAttribute("p_bct", p_bct);
		req.setAttribute("p_curso", p_curso);
		req.setAttribute("p_total", p_total);
		req.setAttribute("p_limitada", p_limitada);
		req.setAttribute("p_livre", p_livre);
		req.setAttribute("bct", obrigatoria_bct);
		req.setAttribute("curso", obrigatorias);
		req.setAttribute("limitadas", limitadas);
		req.setAttribute("livres", livres);
		RequestDispatcher rd = req.getRequestDispatcher("/sucesso.jsp");
		rd.forward(req, resp);
	}
	
	public String determinaCurso(String sigla){
		String curso = "";
		switch (sigla){
		case "BCC":
			curso = "Bacharelado em Ciência da Computação";
			break;
		case "BMAT":
			curso = "Bacharelado em Matemática";
			break;
		case "LMAT":
			curso = "Licenciatura em Matemática";
			break;
		case "BNC":
			curso = "Bacharelado em Neurociência";
			break;
		}
		return curso;
	}
}
