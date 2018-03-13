package controller;

import java.awt.Desktop;

//import org.apache.tomcat.util.http.fileupload.*;
//import org.apache.tomcat.util.http.fileupload.disk.*;
//import org.apache.tomcat.util.http.fileupload.servlet.*;

//import java.util.Iterator;
//import java.util.List;
import java.io.*;
import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.FileItemFactory;
//import org.apache.commons.fileupload.FileUploadException;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import dao.ConvalidacaoDAO;
import dao.DisciplinaDAO;
import dao.ProjetoDAO;
import model.Convalidacao;
import model.Disciplina;
import model.Grade;
import model.PPC;

@WebServlet("/upload")
@MultipartConfig
public class FileController extends HttpServlet {
	
	private String nome = "";
	private int ra = 0;
	private String sigla = "";
	private int matriz = 0;
	private String curso = "";
	private int cursado_bct = 0;
	private int cred_obrigatorio_bct = 0;
	private float p_bct = 0.0f;
	private int cursado_obrigatorio_curso = 0;
	private int cred_obrigatorio_curso = 0;
	private float p_curso = 0.0f;
	private int cursado_limitado = 0;
	private int cred_limitado_curso = 0;
	private float p_limitado = 0.0f;
	private int cursado_livre = 0;
	private int cred_curso_livre = 0;
	private float p_livre = 0.0f;
	private int cred_total_curso = 0;
	private int cursado_total = 0;
	private float p_total = 0.0f;
	private List<Disciplina> obrigatoria_bct;
	private List<Grade> obrigatoria_curso;
	private List<Disciplina> limitadas;
	private List<Disciplina> livres;
	private List<Disciplina> obrigatorias;
	private List<Grade> grade_ppc;
	private List<Grade> grade_bct;
	private List<Disciplina> cursadas;
	private List<Disciplina> nao_encontradas;
	private List<Disciplina> nao_catalogada;
	private List<String> pendentes;
	private List<Convalidacao> convalidacoes;
	private String path;
	private String fileName;
	private List<Integer> projetos;
	private List<Disciplina> nao_cursadas_bct;
	private List<Disciplina> nao_cursadas_ppc;
	private String curso_nome;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		nome = req.getParameter("nome");
		ra = Integer.parseInt(req.getParameter("ra"));
		sigla = req.getParameter("curso");
		//Determina qual curso será feita a contagem
		curso = determinaCurso(sigla);
		curso_nome = curso;
		System.out.println("ano: "+req.getParameter("ano"));
		matriz = converteAno(Integer.parseInt(req.getParameter("ano")));
		//matriz = Integer.parseInt(req.getParameter("matriz"));
		String opcao = req.getParameter("opcao");
		System.out.println("opcao: "+opcao);
		//Buscar a lista de matérias da matriz do curso
		ProjetoDAO p_dao = new ProjetoDAO();
		int p_id = p_dao.buscaProjeto(matriz, curso);
		System.out.println("cod: "+p_id);
		grade_ppc = p_dao.buscaGrade(p_id);
		System.out.println("grade: "+grade_ppc.size());
		int i;
		cred_obrigatorio_curso = 0;
		int cred_apenas_curso = 0;
		cred_limitado_curso = 0;
		cred_curso_livre = 0;
		PPC projeto = p_dao.retornaProjeto(p_id);
		cred_obrigatorio_curso = projeto.getCred_obrigatorias();
		cred_limitado_curso = projeto.getCred_limitadas();
		cred_curso_livre = projeto.getCred_livres();
		cred_apenas_curso = cred_obrigatorio_curso + cred_limitado_curso + cred_curso_livre;
		
		//Método para determinar qual matriz do BC&T usar
		List<PPC> bct = p_dao.listaProjetos();
		int ultimo_projeto = 0;
		int cod_ppc = 0;
		cred_obrigatorio_bct = 0;
		for(i=0;i<bct.size();i++){
			if(ultimo_projeto < bct.get(i).getMatriz() && matriz >= bct.get(i).getMatriz()){
				ultimo_projeto = bct.get(i).getMatriz();
				cod_ppc = bct.get(i).getCod_ppc();
				cred_obrigatorio_bct = bct.get(i).getCred_obrigatorias();
			}
		}
		grade_bct = p_dao.buscaGrade(cod_ppc);
		System.out.println("bct "+ultimo_projeto+": "+grade_bct.size()+" total: "+cred_obrigatorio_bct);
		
		gravarArquivo(req);

	    cursadas = lerHistorico(path,fileName);
	    System.out.println("pendentes: "+pendentes.size());
	    nao_encontradas = new ArrayList<>();
		obrigatorias = new ArrayList<>();
		limitadas = new ArrayList<>();
		obrigatoria_bct = new ArrayList<>();
		//int cred_obrigatorio = 0;
		cursado_bct = 0;
		cursado_obrigatorio_curso = 0;
		cursado_limitado= 0;
		boolean encontrada;
		boolean bd = false;
		boolean es = false;
		boolean pe = false;
		for(Disciplina cursada : cursadas){
			verificarPPC(cursada);
			//Tratando casos especiais devido a mudanças nas disciplinas
			if(cursada.getNome().equals("BANCO DE DADOS") && cursada.getP() == 0){
				bd = true;
			} else if(cursada.getNome().equals("Banco de Dados") && cursada.getP() == 0){
				bd = true;
			} else if(cursada.getNome().equals("ENGENHARIA DE SOFTWARE") && cursada.getP() == 0){
				es = true;
			} else if(cursada.getNome().equals("Engenharia de Software") && cursada.getP() == 0){
				es = true;
			} else if(cursada.getNome().equals("Programação Estruturada")){
				pe = true;
			}
		}
		System.out.println("Não Encontradas: "+nao_encontradas.size());
		
		//Verificar as convalidações nas disciplinas que não foram encontradas 
		ConvalidacaoDAO c_dao = new ConvalidacaoDAO();
		DisciplinaDAO d_dao = new DisciplinaDAO();
		convalidacoes = new ArrayList<>();
		livres = new ArrayList<>();
		if(!nao_encontradas.isEmpty()){
			buscaConvalidacoes(p_id, cod_ppc);
			System.out.println("Convalidadas: "+convalidacoes.size());
			boolean convalidado;
			for(Convalidacao convalidacao: convalidacoes){
				verificaConvalidacao(convalidacao);
			}
		}
		
		//Contar o restante como livre
		System.out.println("Livres: "+livres.size());
		cursado_livre = 0;
		for(Disciplina livre : livres){
			cursado_livre += livre.getT() + livre.getP();
		}
				
		//Exibir os resultados da contagem
		//Faz a soma de todos os créditos contados e as porcentagens de cada tipo de crédito
		System.out.println("Obrigatórios: "+cursado_obrigatorio_curso + " + " + cursado_bct);
		System.out.println("Limitados: "+cursado_limitado);
		System.out.println("Livres: "+cursado_livre);
		System.out.println("Não Catalogadas: "+nao_catalogada.size());
		for(Disciplina catalogada : nao_catalogada){
			System.out.println("nc: "+catalogada.getCod_disciplina()+" "+catalogada.getNome());
		}
		
		req.setAttribute("msg", "<h1>Relatório da Contagem de Créditos</h1><br>");

		//Cálculo das porcentagens de créditos do curso
		cursado_total = 0;
		int excedente = 0;
		int reduzidos = 0;
		
		//Verifica se alguma disciplina foi cursada com redução de créditos
		if(matriz < 2015){
			if(bd){
				reduzidos += 2;
			}
			if(es){
				reduzidos += 2;
			}
			if(pe){
				reduzidos = 0;
			}
		}
		//Verifica as quantidades de obrigatórias, limitadas e livres antes de fazer as porcentagens
		int compensados = 0;
		if(cursado_limitado > cred_limitado_curso){
			excedente = cursado_limitado - cred_limitado_curso;
			cursado_limitado = cred_limitado_curso;
			if(reduzidos > 0 && excedente >= reduzidos){
				excedente -= reduzidos;
				compensados = reduzidos;
			}
			cursado_livre += excedente;
		}
		if(compensados > 0){
			cursado_obrigatorio_curso += compensados;
		}
		if(cursado_obrigatorio_curso > cred_obrigatorio_curso){
			cursado_obrigatorio_curso = cred_obrigatorio_curso;
		}
		if(cursado_livre > cred_curso_livre){
			cursado_livre = cred_curso_livre;
			//cursado_total = cursado_obrigatorio_curso + cursado_bct + cursado_limitado+ cred_curso_livre;
		} /*else{
			//cursado_total = cursado_obrigatorio_curso + cursado_bct + cursado_limitado+ cursado_livre;
		}*/	
		
		//Cálculos das porcentagens do curso
		cursado_total = cursado_obrigatorio_curso + cursado_bct + cursado_limitado+ cursado_livre;
		cred_total_curso = cred_apenas_curso + cred_obrigatorio_bct;
		p_bct = (float) (100*cursado_bct)/cred_obrigatorio_bct;
		p_curso = (float) (100*cursado_obrigatorio_curso)/cred_obrigatorio_curso;
		p_total = (float) (100*cursado_total)/cred_total_curso;
		p_limitado = (float) (100*cursado_limitado)/cred_limitado_curso;
		p_livre = (float) (100*cursado_livre)/cred_curso_livre;
		
		//verifica se faltam disciplinas para serem cursadas
		nao_cursadas_bct = new ArrayList<>();
		nao_cursadas_ppc = new ArrayList<>();
		if(p_bct < 100){
			buscaFaltantesBCT();
		}
		if(p_curso < 100){
			buscaFaltantesPPC();
		}
			
		//Página de resposta para exibir o relatório
		req.setAttribute("sigla", sigla);
		req.setAttribute("total_bct", cred_obrigatorio_bct);
		req.setAttribute("curso_obrigatorio", cred_obrigatorio_curso);
		req.setAttribute("total_curso", cred_total_curso);
		req.setAttribute("total_limitada", cred_limitado_curso);
		req.setAttribute("total_livre", cred_curso_livre);
		req.setAttribute("cursada_bct", cursado_bct);
		req.setAttribute("cursada_curso", cursado_obrigatorio_curso);
		req.setAttribute("cursada_total", cursado_total);
		req.setAttribute("cursada_limitada", cursado_limitado);
		req.setAttribute("cursada_livre", cursado_livre);
		req.setAttribute("p_bct", p_bct+"%");
		req.setAttribute("p_curso", p_curso+"%");
		req.setAttribute("p_total", p_total+"%");
		req.setAttribute("p_limitada", p_limitado+"%");
		req.setAttribute("p_livre", p_livre+"%");
		req.setAttribute("bct", obrigatoria_bct);
		req.setAttribute("curso", obrigatorias);
		req.setAttribute("limitadas", limitadas);
		req.setAttribute("livres", livres);
		req.setAttribute("nao_catalogadas", nao_catalogada);

		//Método para escolher a forma de exibição do relatório
		if(opcao.equals("abrir")){
			RequestDispatcher rd = req.getRequestDispatcher("/relatorio.jsp");
			rd.forward(req, resp);
		} else if(opcao.equals("baixar")){
			//Envio da resposta e montagem do pdf
			geraPDF();
			String name = nome+"_"+ra+".pdf";
			name = name.replaceAll(" ", "_");
			String serverHomeDir = System.getenv("CATALINA_HOME");
			String reportDestination = "C:\\Users\\Erick\\Documents\\eclipse\\"+name;
			//String reportDestination = name;//Caminho do servidor Tomcat
			//Diretório do Servidor para baixar o relatório: /home/erickaugusto/Arquivos/ 
			FileInputStream fis = new FileInputStream(new File(reportDestination));
			org.apache.commons.io.IOUtils.copy(fis, resp.getOutputStream());
			resp.setContentType("aplication/pdf");
			resp.setHeader("Content-Disposition", "attachment; filename=" + name);
			resp.flushBuffer();
		} else{
			//Página de erro caso algo não esteja certo
			RequestDispatcher rd = req.getRequestDispatcher("/error.jsp");
			rd.forward(req, resp);
		}
	}

	//Método para verificar qual curso será usado para a contagem de créditos
	public String determinaCurso(String sigla){
		String curso = "";
		projetos = new ArrayList<>();
		switch (sigla){
		case "BCC":
			curso = "Bacharelado em Ciência da Computação";
			projetos.add(2007);
			projetos.add(2009);
			projetos.add(2010);
			projetos.add(2015);
			break;
		case "BMAT":
			curso = "Bacharelado em Matemática";
			projetos.add(2010);
			projetos.add(2012);
			projetos.add(2017);
			break;
		case "LMAT":
			curso = "Licenciatura em Matemática";
			projetos.add(2010);
			break;
		case "BNC":
			curso = "Bacharelado em Neurociência";
			projetos.add(2010);
			projetos.add(2015);
			break;
		}
		return curso;
	}
	
	//Método para converter a linha selecionada após a escolha do curso em um valor referente ao ano do projeto
	public int converteAno(int index){
		int ano = 0;
		ano = projetos.get(index);
		return ano;
	}
	
	//Verifica o nome do arquivo (método padrão que existia na documentação para upload de arquivos)
	private String getFileName(final Part part) {
	    //final String partHeader = part.getHeader("content-disposition");
	    //LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
	    for (String content : part.getHeader("content-disposition").split(";")) {
	        if (content.trim().startsWith("filename")) {
	            return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
	        }
	    }
	    return null;
	}
	
	//Método para ler as informações do histórico do aluno
	public List<Disciplina> lerHistorico(String path, String fileName) throws IOException{
		FileReader arq = null;
		List<Disciplina> cursadas = new ArrayList<>();//Preencher com os dados do arquivo do aluno
		nao_catalogada = new ArrayList<>();
		pendentes = new ArrayList<>();
		try {
			arq = new FileReader(path+""+fileName);
			BufferedReader buffer = new BufferedReader(arq);
			String linha = buffer.readLine();
			linha = buffer.readLine();
			
			//Laço para a leitura das linhas do arquivo
			String[] campos;
			DisciplinaDAO d_dao = new DisciplinaDAO();
			while(linha != null){
				if(!linha.contains("Quadrimestre")){
					linha = linha.replaceAll("\"", "");
					campos = linha.split("\t",-1);
					if(campos.length > 1){
						Disciplina cursada = d_dao.buscaDisciplina(campos[0]);
						if(cursada.getCod_disciplina() != null && (campos[5].equals("Aprovado") || campos[5].equals("Apr.S.Nota") 
								|| campos[5].equals("Disc.Equiv") || campos[5].equals("Aproveitamento")) && !campos[6].equals("Erro!")){
							//Lista para armazenar disciplina aprovadas com D e podem ser refeitas
							if(campos[4].equals("D") && !pendentes.contains(cursada.getNome())){
								pendentes.add(cursada.getNome());
								cursada.setNome(cursada.getNome().toUpperCase());
								cursadas.add(cursada);
							} else if(!pendentes.contains(cursada.getNome())){
								cursada.setNome(cursada.getNome().toUpperCase());
								cursadas.add(cursada);
							}
						} else if(cursada.getCod_disciplina() == null && campos.length > 1){
							if(campos.length > 2 && (campos[5].equals("Aprovado") || campos[5].equals("Apr.S.Nota") 
									|| campos[5].equals("Disc.Equiv") || campos[5].equals("Aproveitamento"))){
								Disciplina d = new Disciplina();
								d.setCod_disciplina(campos[0]);
								d.setNome(campos[1].toUpperCase());
								d.setT(Integer.parseInt(campos[3]));
								d.setP(0);
								d.setI(0);
								nao_catalogada.add(d);
							}
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("cursadas: "+cursadas.size());
		return cursadas;
	}
	
	//Método para ler o arquivo informado pelo usuário
	public void gravarArquivo(HttpServletRequest req) throws IOException{
		//path = "C:\\Users\\Erick\\Documents\\Eclipse Projects\\ContCred\\files\\";//Caminho local 1
		//path = "webapps/upload_files/";//Esse caminho não funciona no servidor Tomcat
		path = "C:\\Users\\Erick\\Documents\\Files\\";//Caminho local 2
		//Caminho usado para salvar os arquivos na máquina que está sendo usada como servidor
		//path = "/home/charles/Documentos/Arquivos_ContCred/";
		//Diretório do Servidor para salvar o arquivo: /home/erickaugusto/ 
		Part filePart = null;
		try {
			filePart = req.getPart("file");
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
	    fileName = getFileName(filePart);
	    System.out.println(fileName);
	    OutputStream out = null;
	    InputStream filecontent = null;
	    try {
	        out = new FileOutputStream(new File(path + fileName));
	        filecontent = filePart.getInputStream();
	        int read = 0;
	        final byte[] bytes = new byte[1024];

	        while ((read = filecontent.read(bytes)) != -1) {
	            out.write(bytes, 0, read);
	        }
	    } catch (FileNotFoundException fne) {
	        /*writer.println("You either did not specify a file to upload or are "
	                + "trying to upload a file to a protected or nonexistent "
	                + "location.");*/
	        //writer.println("<br/> ERROR: " + fne.getMessage());
	        //LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}", new Object[]{fne.getMessage()});
	    } finally {
	        if (out != null) {
	            out.close();
	        }
	        if (filecontent != null) {
	            filecontent.close();
	        }
	        /*if (writer != null) {
	            writer.close();
	        }*/
	        System.out.print("Finalizado");
	    }
	}
	
	//Método para verificar se a disciplina pertence ao ppc
	public void verificarPPC(Disciplina cursada){
		int i;
		boolean encontrada = false;
		for(i=0;i<grade_ppc.size();i++){
			String cod = grade_ppc.get(i).getCod_disciplina();
			if(cod.equals(cursada.getCod_disciplina())){
				encontrada = true;
				//verifica se é obrigatória ou limitada
				String status = grade_ppc.get(i).getStatus();
				if(status.equals("Obrigatória")){
					cursado_obrigatorio_curso += cursada.getT() + cursada.getP();
					obrigatorias.add(cursada);
				} else{
					cursado_limitado+= cursada.getT() + cursada.getP();
					limitadas.add(cursada);
				}
			}
		}
		for(i=0;i<grade_bct.size();i++){
			String cod = grade_bct.get(i).getCod_disciplina();
			if(cod.equals(cursada.getCod_disciplina())){
				encontrada= true;
				//verifica se é obrigatória
				String status = grade_bct.get(i).getStatus();
				if(status.equals("Obrigatória")){
					cursado_bct += cursada.getT() + cursada.getP();
					obrigatoria_bct.add(cursada);
				}
			}
		}
		//Disciplina não encontrada
		if(encontrada == false){
			nao_encontradas.add(cursada);
		}
	}
	
	//Método para verificar se há convalidações no histórico
	public void buscaConvalidacoes(int p_id, int cod_ppc){
		ConvalidacaoDAO c_dao = new ConvalidacaoDAO();
		for(Disciplina nao_encontrada : nao_encontradas){
			Convalidacao c = c_dao.buscaConvalidacao(nao_encontrada.getCod_disciplina(), p_id);
			Convalidacao bct = c_dao.buscaConvalidacao(nao_encontrada.getCod_disciplina(), cod_ppc);
			if(c.getCod_convalidacao() != null){
				convalidacoes.add(c);
			} else if(bct.getCod_convalidacao() != null){
				convalidacoes.add(bct);
			} else{
				//Conta como livre
				livres.add(nao_encontrada);
			}
		}
	}
	
	//Método para verificar se a convalidação pertence ao PPC
	public void verificaConvalidacao(Convalidacao convalidacao){
		System.out.println("convalidada: "+convalidacao.getCod_convalidacao());
		boolean convalidado = false;
		DisciplinaDAO d_dao = new DisciplinaDAO();
		int i;
		String status = "";
		//Verifica na grade do ppc
		for(i=0;i<grade_ppc.size();i++){
			String cod = grade_ppc.get(i).getCod_disciplina();
			if(cod.equals(convalidacao.getCod_convalidacao())){
				convalidado = true;
				//verifica se é obrigatória ou limitada
				status = grade_ppc.get(i).getStatus();
				if(status.equals("Obrigatória")){
					//Buscar a disciplina de origem para adicionar os créditos e colocar na lista
					Disciplina convalidada = d_dao.buscaDisciplina(convalidacao.getCod_disciplina());
					convalidada.setNome(convalidada.getNome().toUpperCase());
					cursado_obrigatorio_curso += convalidada.getT() + convalidada.getP();
					obrigatorias.add(convalidada);
					System.out.println("origem o: "+convalidada.getNome()+" "+convalidada.getT()+" "+convalidada.getP());
				} else{
					//Buscar a disciplina de origem para adicionar os créditos e colocar na lista
					Disciplina convalidada = d_dao.buscaDisciplina(convalidacao.getCod_disciplina());
					convalidada.setNome(convalidada.getNome().toUpperCase());
					cursado_limitado+= convalidada.getT() + convalidada.getP();//d.getT() + d.getP();
					limitadas.add(convalidada);
					System.out.println("origem m: "+convalidada.getNome()+" "+convalidada.getT()+" "+convalidada.getP());
				}
			}
		}
		//Verifica na grade do bct
		for(i=0;i<grade_bct.size();i++){
			String cod = grade_bct.get(i).getCod_disciplina();
			if(cod.equals(convalidacao.getCod_convalidacao())){
				convalidado = true;
				//verifica se é obrigatória
				status = grade_bct.get(i).getStatus();
				if(status.equals("Obrigatória")){
					//Buscar a disciplina de origem para adicionar os créditos e colocar na lista
					Disciplina convalidada = d_dao.buscaDisciplina(convalidacao.getCod_disciplina());
					convalidada.setNome(convalidada.getNome().toUpperCase());
					cursado_bct += convalidada.getT() + convalidada.getP();
					obrigatoria_bct.add(convalidada);
					System.out.println("origem t: "+convalidada.getNome()+" "+convalidada.getT()+" "+convalidada.getP());
				} else{
					convalidado = false;
				}
			}
		}
		//Convalidação não encontrada nos projetos pedagógicos
		if(convalidado == false){
			//Conta os créditos como livres
			//Buscar a disciplina de origem para adicionar os créditos e colocar na lista
			Disciplina convalidada = d_dao.buscaDisciplina(convalidacao.getCod_disciplina());
			convalidada.setNome(convalidada.getNome().toUpperCase());
			livres.add(convalidada);
			System.out.println("origem l: "+convalidada.getNome()+" "+convalidada.getT()+" "+convalidada.getP());
		}
	}
	
	//Método para buscar as disciplinas ainda não cursadas do BC&T
	public void buscaFaltantesBCT(){
		int i, j;
		boolean cursada;
		for(i=0;i<grade_bct.size();i++){
			cursada = false;
			for(j=0;j<obrigatoria_bct.size();j++){
				if(grade_bct.get(i).getCod_disciplina().equals(obrigatoria_bct.get(j).getCod_disciplina())){
					cursada = true;
					j = obrigatoria_bct.size();
				}
			}
			if(cursada == false){
				boolean convalidada = faltanteConvalidada(grade_bct.get(i).getCod_disciplina());
				if(convalidada == false){
					DisciplinaDAO dao = new DisciplinaDAO();
					Disciplina d = dao.buscaDisciplina(grade_bct.get(i).getCod_disciplina());
					System.out.println("faltantes bct: "+d.getNome());
					d.setNome(d.getNome().toUpperCase());
					nao_cursadas_bct.add(d);
				}
			}
		}
	}
	
	//Método para buscar as disciplinas ainda não cursadas do PPC
	public void buscaFaltantesPPC(){
		obrigatoria_curso = new ArrayList<>();
		int i, j, k;
		boolean cursada;
		for(i=0;i<grade_ppc.size();i++){
			if(grade_ppc.get(i).getStatus().equals("Obrigatória")){
				obrigatoria_curso.add(grade_ppc.get(i));
			}
		}
		for(i=0;i<obrigatoria_curso.size();i++){
			cursada = false;
			for(j=0;j<obrigatorias.size();j++){
				if(obrigatoria_curso.get(i).getCod_disciplina().equals(obrigatorias.get(j).getCod_disciplina())){
					cursada = true;
					j = obrigatorias.size();
				}
			}
			if(cursada == false){
				boolean convalidada = false;
				DisciplinaDAO dao = new DisciplinaDAO();
				Disciplina d = dao.buscaDisciplina(obrigatoria_curso.get(i).getCod_disciplina());
				for(k=0;k<convalidacoes.size();k++){
					if(convalidacoes.get(k).getCod_convalidacao().equals(d.getCod_disciplina())){
						convalidada = true;
						//System.out.println("faltantes ppc convalidadas: "+d.getNome());
					}
				}
				if(convalidada == false){
					System.out.println("faltantes ppc: "+d.getNome());
					d.setNome(d.getNome().toUpperCase());
					nao_cursadas_ppc.add(d);
				}
			}
		}
	}
	
	//Método para verificar se a disciplina faltante está entre as convalidadas
	public boolean faltanteConvalidada(String cod_disciplina){
		boolean convalidada = false;
		DisciplinaDAO dao = new DisciplinaDAO();
		Disciplina d = dao.buscaDisciplina(cod_disciplina);
		int i;
		for(i=0;i<convalidacoes.size();i++){
			if(convalidacoes.get(i).getCod_convalidacao().equals(d.getCod_disciplina())){
				convalidada = true;
				i = convalidacoes.size();
				//System.out.println("faltantes ppc convalidadas: "+d.getNome());
			}
		}
		return convalidada;
	}
	
	//Método para gerar o PDF
	public void geraPDF(){
		Document document = new Document(PageSize.A4);
		document.setMargins(10, 10, 50, 50);
		String name = nome+"_"+ra+".pdf";
		name = name.replaceAll(" ", "_");
        try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(name));
			document.open();
	        
	        Font title = FontFactory.getFont(FontFactory.TIMES_BOLD,14,BaseColor.BLACK);
	        Font campos = FontFactory.getFont(FontFactory.TIMES_BOLD,12,BaseColor.WHITE);
	        
	        //Cabeçalho das Tabelas de Disciplinas
	        Paragraph c_1 = new Paragraph("CÓDIGO",campos);
	        Paragraph c_2 = new Paragraph("DISCIPLINA",campos);
	        Paragraph c_3 = new Paragraph("CRÉDITOS",campos);
	        PdfPCell h_1 = new PdfPCell(c_1);
	        PdfPCell h_2 = new PdfPCell(c_2);
	        PdfPCell h_3 = new PdfPCell(c_3);
	        h_1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        h_1.setBackgroundColor(new BaseColor(0,102,0));
	        h_2.setHorizontalAlignment(Element.ALIGN_CENTER);
	        h_2.setBackgroundColor(new BaseColor(0,102,0));
	        h_3.setHorizontalAlignment(Element.ALIGN_CENTER);
	        h_3.setBackgroundColor(new BaseColor(0,102,0));
	        
	        //int i, tp = 0;
	        //Lista de Disciplinas do BC&T
	        PdfPTable bct = preencheTabela(h_1,h_2,h_3,obrigatoria_bct, false);
	        
	        //Lista de Disciplinas do Curso
	        PdfPTable curso = preencheTabela(h_1,h_2,h_3,obrigatorias, false);
	        
	        //Lista de Disciplinas de Opção Limitada
	        PdfPTable limitada = preencheTabela(h_1,h_2,h_3,limitadas, false);
	        
	        //Lista de Disciplinas Livres
	        PdfPTable livre = preencheTabela(h_1,h_2,h_3,livres, false);
	        
	        //Lista de Disciplinas não catalogadas
	        PdfPTable nao_catalogadas = preencheTabela(h_1,h_2,h_3,nao_catalogada, true);
	        
	        //Lista de disciplinas não cursadas do BC&T
	        PdfPTable falta_bct = preencheTabela(h_1,h_2,h_3,nao_cursadas_bct, true);
	        
	        //Lista de disciplinas não cursadas do PPC
	        PdfPTable falta_ppc = preencheTabela(h_1,h_2,h_3,nao_cursadas_ppc, true);

	        //Montando o arquivo
	        PdfPTable relatorio = new PdfPTable(1);
	        
	        Paragraph p = new Paragraph("RELATÓRIO DO SISTEMA DE CONTAGEM DE CRÉDITOS",title);
	        PdfPCell cell = new PdfPCell(p);
	        cell.setBorder(PdfPCell.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        Paragraph r = new Paragraph(" ");
	        PdfPCell row = new PdfPCell(r);
	        row.setRowspan(3);
	        row.setBorder(PdfPCell.NO_BORDER);
	        relatorio.addCell(row);
	        relatorio.addCell(cell);
	        relatorio.addCell(row);
	        document.add(relatorio);

	        //Informações do Aluno
	        PdfPTable aluno = new PdfPTable(2);
	        aluno.setWidths(new int[]{1,5});
	        Paragraph info_nome = new Paragraph("NOME: ",title);
	        Paragraph info_ra = new Paragraph("RA: ",title);
	        Paragraph aluno_nome = new Paragraph(nome,title);
	        Paragraph aluno_ra = new Paragraph(Integer.toString(ra),title);
	        Paragraph info_matriz = new Paragraph("MATRIZ: ",title);
	        Paragraph ano_matriz = new Paragraph(Integer.toString(matriz),title);
	        Paragraph info_curso = new Paragraph("CURSO: ",title);
	        Paragraph nome_curso = new Paragraph(curso_nome,title);
	        PdfPCell info1 = new PdfPCell(info_nome);
	        PdfPCell info2 = new PdfPCell(info_ra);
	        PdfPCell info3 = new PdfPCell(aluno_nome);
	        PdfPCell info4 = new PdfPCell(aluno_ra);
	        PdfPCell info7 = new PdfPCell(info_curso);
	        PdfPCell info8 = new PdfPCell(nome_curso);
	        PdfPCell info5 = new PdfPCell(info_matriz);
	        PdfPCell info6 = new PdfPCell(ano_matriz);
	        info1.setBorder(PdfPCell.NO_BORDER);
	        info2.setBorder(PdfPCell.NO_BORDER);
	        info3.setBorder(PdfPCell.NO_BORDER);
	        info4.setBorder(PdfPCell.NO_BORDER);
	        info7.setBorder(PdfPCell.NO_BORDER);
	        info8.setBorder(PdfPCell.NO_BORDER);
	        info5.setBorder(PdfPCell.NO_BORDER);
	        info6.setBorder(PdfPCell.NO_BORDER);
	        aluno.addCell(info1);
	        aluno.addCell(info3);
	        aluno.addCell(info2);
	        aluno.addCell(info4);
	        aluno.addCell(info7);
	        aluno.addCell(info8);
	        aluno.addCell(info5);
	        aluno.addCell(info6);
	        aluno.addCell(row);
	        aluno.addCell(row);
	        document.add(aluno);
	        
	        PdfPTable porcentagens = new PdfPTable(4);
	        //Cabeçalho
	        Paragraph i_1 = new Paragraph("Tipo de Disciplina",campos);
	        Paragraph i_2 = new Paragraph("Deve Cursar",campos);
	        Paragraph i_3 = new Paragraph("Cursou",campos);
	        Paragraph i_4 = new Paragraph("Porcentagem",campos);
	        PdfPCell pc_1 = new PdfPCell(i_1);
	        PdfPCell pc_2 = new PdfPCell(i_2);
	        PdfPCell pc_3 = new PdfPCell(i_3);
	        PdfPCell pc_4 = new PdfPCell(i_4);
	        pc_1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        pc_1.setBackgroundColor(new BaseColor(0,102,0));
	        pc_2.setHorizontalAlignment(Element.ALIGN_CENTER);
	        pc_2.setBackgroundColor(new BaseColor(0,102,0));
	        pc_3.setHorizontalAlignment(Element.ALIGN_CENTER);
	        pc_3.setBackgroundColor(new BaseColor(0,102,0));
	        pc_4.setHorizontalAlignment(Element.ALIGN_CENTER);
	        pc_4.setBackgroundColor(new BaseColor(0,102,0));
	        porcentagens.addCell(pc_1);
	        porcentagens.addCell(pc_2);
	        porcentagens.addCell(pc_3);
	        porcentagens.addCell(pc_4);
	        //Conteúdo da tabela informativa
	        Paragraph pg_1;
	        Paragraph pg_2;
	        Paragraph pg_3;
	        Paragraph pg_4;
	        PdfPCell cl_1;
	        PdfPCell cl_2;
	        PdfPCell cl_3;
	        PdfPCell cl_4;
	        
	        pg_1 = new Paragraph("Obrigatórias BC&T");
	        cl_1 = new PdfPCell(pg_1);
	        cl_1.setBackgroundColor(new BaseColor(204,255,204));
	        porcentagens.addCell(cl_1);
	        pg_2 = new Paragraph(Integer.toString(cred_obrigatorio_bct));
	        cl_2 = new PdfPCell(pg_2);
	        cl_2.setBackgroundColor(new BaseColor(204,255,204));
	        porcentagens.addCell(cl_2);
	        pg_3 = new Paragraph(Integer.toString(cursado_bct));
	        cl_3 = new PdfPCell(pg_3);
	        cl_3.setBackgroundColor(new BaseColor(204,255,204));
	        porcentagens.addCell(cl_3);
	        pg_4 = new Paragraph(Float.toString(p_bct)+"%");
	        cl_4 = new PdfPCell(pg_4);
	        cl_4.setBackgroundColor(new BaseColor(204,255,204));
	        porcentagens.addCell(cl_4);
	        
	        pg_1 = new Paragraph("Obrigatórias "+sigla);
	        cl_1 = new PdfPCell(pg_1);
	        cl_1.setBackgroundColor(new BaseColor(255,255,153));
	        porcentagens.addCell(cl_1);
	        pg_2 = new Paragraph(Integer.toString(cred_obrigatorio_curso));
	        cl_2 = new PdfPCell(pg_2);
	        cl_2.setBackgroundColor(new BaseColor(255,255,153));
	        porcentagens.addCell(cl_2);
	        pg_3 = new Paragraph(Integer.toString(cursado_obrigatorio_curso));
	        cl_3 = new PdfPCell(pg_3);
	        cl_3.setBackgroundColor(new BaseColor(255,255,153));
	        porcentagens.addCell(cl_3);
	        pg_4 = new Paragraph(Float.toString(p_curso)+"%");
	        cl_4 = new PdfPCell(pg_4);
	        cl_4.setBackgroundColor(new BaseColor(255,255,153));
	        porcentagens.addCell(cl_4);
	        
	        pg_1 = new Paragraph("Limitadas "+sigla);
	        cl_1 = new PdfPCell(pg_1);
	        cl_1.setBackgroundColor(new BaseColor(204,255,204));
	        porcentagens.addCell(cl_1);
	        pg_2 = new Paragraph(Integer.toString(cred_limitado_curso));
	        cl_2 = new PdfPCell(pg_2);
	        cl_2.setBackgroundColor(new BaseColor(204,255,204));
	        porcentagens.addCell(cl_2);
	        pg_3 = new Paragraph(Integer.toString(cursado_limitado));
	        cl_3 = new PdfPCell(pg_3);
	        cl_3.setBackgroundColor(new BaseColor(204,255,204));
	        porcentagens.addCell(cl_3);
	        pg_4 = new Paragraph(Float.toString(p_limitado)+"%");
	        cl_4 = new PdfPCell(pg_4);
	        cl_4.setBackgroundColor(new BaseColor(204,255,204));
	        porcentagens.addCell(cl_4);
	        
	        pg_1 = new Paragraph("Livres "+sigla);
	        cl_1 = new PdfPCell(pg_1);
	        cl_1.setBackgroundColor(new BaseColor(255,255,153));
	        porcentagens.addCell(cl_1);
	        pg_2 = new Paragraph(Integer.toString(cred_curso_livre));
	        cl_2 = new PdfPCell(pg_2);
	        cl_2.setBackgroundColor(new BaseColor(255,255,153));
	        porcentagens.addCell(cl_2);
	        pg_3 = new Paragraph(Integer.toString(cursado_livre));
	        cl_3 = new PdfPCell(pg_3);
	        cl_3.setBackgroundColor(new BaseColor(255,255,153));
	        porcentagens.addCell(cl_3);
	        pg_4 = new Paragraph(Float.toString(p_livre)+"%");
	        cl_4 = new PdfPCell(pg_4);
	        cl_4.setBackgroundColor(new BaseColor(255,255,153));
	        porcentagens.addCell(cl_4);
	        
	        pg_1 = new Paragraph("Total do Curso");
	        cl_1 = new PdfPCell(pg_1);
	        cl_1.setBackgroundColor(new BaseColor(204,255,204));
	        porcentagens.addCell(cl_1);
	        pg_2 = new Paragraph(Integer.toString(cred_total_curso));
	        cl_2 = new PdfPCell(pg_2);
	        cl_2.setBackgroundColor(new BaseColor(204,255,204));
	        porcentagens.addCell(cl_2);
	        pg_3 = new Paragraph(Integer.toString(cursado_total));
	        cl_3 = new PdfPCell(pg_3);
	        cl_3.setBackgroundColor(new BaseColor(204,255,204));
	        porcentagens.addCell(cl_3);
	        pg_4 = new Paragraph(Float.toString(p_total)+"%");
	        cl_4 = new PdfPCell(pg_4);
	        cl_4.setBackgroundColor(new BaseColor(204,255,204));
	        porcentagens.addCell(cl_4);

	        document.add(porcentagens);
	        
	        PdfPTable bct_table = new PdfPTable(1);
	        Paragraph p2 = new Paragraph("DISCIPLINAS OBRIGATÓRIAS DO BC&T",title);
	        PdfPCell cell2 = new PdfPCell(p2);
	        cell2.setBorder(PdfPCell.NO_BORDER);
	        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	        bct_table.addCell(row);
	        bct_table.addCell(cell2);
	        bct_table.addCell(row);
	        document.add(bct_table);
	        document.add(bct);
	        
	        PdfPTable curso_table = new PdfPTable(1);
	        Paragraph p3 = new Paragraph("DISCIPLINAS OBRIGATÓRIAS DO "+sigla,title);
	        PdfPCell cell3 = new PdfPCell(p3);
	        cell3.setBorder(PdfPCell.NO_BORDER);
	        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	        curso_table.addCell(row);
	        curso_table.addCell(cell3);
	        curso_table.addCell(row);
	        document.add(curso_table);
	        document.add(curso);
	        
	        PdfPTable limitadas_table = new PdfPTable(1);
	        Paragraph p4 = new Paragraph("DISCIPLINAS LIMITADAS DO "+sigla,title);
	        PdfPCell cell4 = new PdfPCell(p4);
	        cell4.setBorder(PdfPCell.NO_BORDER);
	        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	        limitadas_table.addCell(row);
	        limitadas_table.addCell(cell4);
	        limitadas_table.addCell(row);
	        document.add(limitadas_table);
	        document.add(limitada);
	        
	        PdfPTable livres_table = new PdfPTable(1);
	        Paragraph p5 = new Paragraph("DISCIPLINAS LIVRES DO "+sigla,title);
	        PdfPCell cell5 = new PdfPCell(p5);
	        cell5.setBorder(PdfPCell.NO_BORDER);
	        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
	        livres_table.addCell(row);
	        livres_table.addCell(cell5);
	        livres_table.addCell(row);
	        document.add(livres_table);
	        document.add(livre);
	        
	        PdfPTable falta_bct_table = new PdfPTable(1);
	        Paragraph p7 = new Paragraph("DISCIPLINAS QUE FALTAM DO BC&T",title);
	        PdfPCell cell7 = new PdfPCell(p7);
	        cell7.setBorder(PdfPCell.NO_BORDER);
	        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
	        falta_bct_table.addCell(row);
	        falta_bct_table.addCell(cell7);
	        falta_bct_table.addCell(row);
	        document.add(falta_bct_table);
	        document.add(falta_bct);
	        
	        PdfPTable falta_ppc_table = new PdfPTable(1);
	        Paragraph p8 = new Paragraph("DISCIPLINAS QUE FALTAM DO "+sigla,title);
	        PdfPCell cell8 = new PdfPCell(p8);
	        cell8.setBorder(PdfPCell.NO_BORDER);
	        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
	        falta_ppc_table.addCell(row);
	        falta_ppc_table.addCell(cell8);
	        falta_ppc_table.addCell(row);
	        document.add(falta_ppc_table);
	        document.add(falta_ppc);
	        
	        PdfPTable calalogo_table = new PdfPTable(1);
	        Paragraph p6 = new Paragraph("DISCIPLINAS NÃO ENCONTRADAS NO BANCO DE DADOS DO SISTEMA",title);
	        PdfPCell cell6 = new PdfPCell(p6);
	        cell6.setBorder(PdfPCell.NO_BORDER);
	        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
	        calalogo_table.addCell(row);
	        calalogo_table.addCell(cell6);
	        calalogo_table.addCell(row);
	        document.add(calalogo_table);
	        document.add(nao_catalogadas);
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
        document.close();
	}

	//Método para preencher as tabelas com as listas e retornar o resultado para o arquivo
	public PdfPTable preencheTabela(PdfPCell h_1, PdfPCell h_2, PdfPCell h_3, List<Disciplina> atual, boolean falta){
		PdfPTable tabela_universal = new PdfPTable(3);
		try {
			tabela_universal.setWidths(new int[]{2,6,2});
			//Cabeçalho
			tabela_universal.addCell(h_1);
			tabela_universal.addCell(h_2);
			tabela_universal.addCell(h_3);
	        //Laço para gerar o conteúdo da tabela
	        int i, tp = 0;
	        Paragraph col1;
	        Paragraph col2;
	        Paragraph col3;
	        PdfPCell linha1;
	        PdfPCell linha2;
	        PdfPCell linha3;
	        for(i=0;i<atual.size();i++){
	        	tp = atual.get(i).getT() + atual.get(i).getP();
	        	if(i%2==0){
	        		col1 = new Paragraph(atual.get(i).getCod_disciplina());
	        		linha1 = new PdfPCell(col1);
	        		col2 = new Paragraph(atual.get(i).getNome());
	        		linha2 = new PdfPCell(col2);
	        		col3 = new Paragraph(Integer.toString(tp));
	        		linha3 = new PdfPCell(col3);
	        		if(falta == true){
	        			linha1.setBackgroundColor(new BaseColor(255,102,102));
		        		linha2.setBackgroundColor(new BaseColor(255,102,102));
		        		linha3.setBackgroundColor(new BaseColor(255,102,102));
	        		} else{
	        			linha1.setBackgroundColor(new BaseColor(204,255,204));
		        		linha2.setBackgroundColor(new BaseColor(204,255,204));
		        		linha3.setBackgroundColor(new BaseColor(204,255,204));
	        		}
	        	} else{
	        		col1 = new Paragraph(atual.get(i).getCod_disciplina());
	        		linha1 = new PdfPCell(col1);
	        		col2 = new Paragraph(atual.get(i).getNome());
	        		linha2 = new PdfPCell(col2);
	        		col3 = new Paragraph(Integer.toString(tp));
	        		linha3 = new PdfPCell(col3);
	        		if(falta == true){
	        			linha1.setBackgroundColor(new BaseColor(255,204,204));
		        		linha2.setBackgroundColor(new BaseColor(255,204,204));
		        		linha3.setBackgroundColor(new BaseColor(255,204,204));
	        		} else{
	        			linha1.setBackgroundColor(new BaseColor(255,255,153));
		        		linha2.setBackgroundColor(new BaseColor(255,255,153));
		        		linha3.setBackgroundColor(new BaseColor(255,255,153));
	        		}
	        	}
	        	tabela_universal.addCell(linha1);
	        	tabela_universal.addCell(linha2);
	        	tabela_universal.addCell(linha3);
	        }
		} catch (DocumentException e) {
			e.printStackTrace();
		}
        return tabela_universal;
	}
}
