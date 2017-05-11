package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.PPC;
import model.Grade;

public class ProjetoDAO {
	private Connection connection;
	public ProjetoDAO(){
		this.connection = new Conexao().getConexao();
	}
	
	public void fechaConexao() {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void novoProjeto(PPC p){
		String sql = "insert into ppc (cod_ppc,curso,matriz,cred_obrigatorios,cred_limitados,cred_livres) "
				+ "values (?,?,?,?,?,?)";
        try {
            System.out.println("Iniciando Registro no Banco de Dados...");
            PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql);

            int cod_ppc = ultimoProjeto();
            //System.out.println("id: "+cod_ppc);
            stmt.setInt(1, cod_ppc+1);
            stmt.setString(2, p.getCurso());
            stmt.setInt(3, p.getMatriz());
            stmt.setInt(4, p.getCred_obrigatorias());
            stmt.setInt(5, p.getCred_limitadas());
            stmt.setInt(6, p.getCred_livres());

            stmt.execute();
            stmt.close();
            //System.out.println("Registro Realizado com Sucesso!");
            //System.out.println();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}
	
	public int ultimoProjeto(){
		int id = 0;
		String sql = "select max(cod_ppc) from ppc";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				id = rs.getInt(1);
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return id;
	}
	
	public PPC retornaProjeto(int cod_ppc){
		PPC p = new PPC();
		String sql = "select * from ppc where cod_ppc = ?";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, cod_ppc);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				p.setCod_ppc(rs.getInt("cod_ppc"));
				p.setCurso(rs.getString("curso"));
				p.setMatriz(rs.getInt("matriz"));
				p.setCred_obrigatorias(rs.getInt("cred_obrigatorios"));
				p.setCred_limitadas(rs.getInt("cred_limitados"));
				p.setCred_livres(rs.getInt("cred_livres"));
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return p;
	}
	
	//Lista de disciplinas e status para salvar em disciplina_ppc
	public void criarGrade(int cod_ppc, String cod_disciplina, String status){
		String sql = "insert into disciplina_ppc (cod_ppc,cod_disciplina,status) values (?,?,?)";
        try {
            //System.out.println("Iniciando Registro no Banco de Dados...");
            PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql);

            stmt.setInt(1, cod_ppc);
            stmt.setString(2, cod_disciplina);
            stmt.setString(3, status);

            stmt.execute();
            stmt.close();
            //System.out.println("Registro Realizado com Sucesso!");
            //System.out.println();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}
	
	public boolean verificaGrade(int cod_ppc, String cod_disciplina, String status){
		boolean existente = false;
		int ppc = 0;
		String disciplina = "";
		String tipo = "";
		String sql = "select * from disciplina_ppc where cod_ppc = ? and cod_disciplina = ? and status = ?";
        try {
            PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql);

            stmt.setInt(1, cod_ppc);
            stmt.setString(2, cod_disciplina);
            stmt.setString(3, status);

            ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ppc = rs.getInt("cod_ppc");
				disciplina = rs.getString("cod_disciplina");
				tipo = rs.getString("status");
			}
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(ppc != 0 && disciplina != null && tipo != null){
        	existente = true;
        }
		return existente;
	}
	
	public int buscaProjeto(int matriz, String curso){
		String sql = "select * from ppc where curso = ? and matriz = ?";
		int cod = 0;
		PPC p = new PPC();
		try{
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql);

            stmt.setString(1, curso);
            stmt.setInt(2, matriz);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				p.setCod_ppc(rs.getInt("cod_ppc"));
				p.setCurso(rs.getString("curso"));
				p.setMatriz(rs.getInt("matriz"));
				//cod = rs.getInt(1);
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		cod = p.getCod_ppc();
		return cod;
	}
	
	public List<Grade> buscaGrade(int cod_ppc){
		String sql = "select * from disciplina_ppc where cod_ppc = ?";
		List<Grade> grade = new ArrayList<>();
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, cod_ppc);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Grade g = new Grade();
				g.setCod_disciplina(rs.getString("cod_disciplina"));
				g.setStatus(rs.getString("status"));
				grade.add(g);
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return grade;
	}
	
	public List<Grade> gradeOrdenada(int cod_ppc){
		List<Grade> grade = new ArrayList<>();
		String sql = "select p.cod_disciplina, p.status from disciplinas d, disciplina_ppc p where p.cod_ppc = ? and "
				+ "p.cod_disciplina = d.cod order by p.status, d.nome";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, cod_ppc);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Grade g = new Grade();
				g.setCod_disciplina(rs.getString("p.cod_disciplina"));
				g.setStatus(rs.getString("p.status"));
				grade.add(g);
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return grade;
	}
	
	public List<PPC> listaProjetos(){
		List<PPC> projetos = new ArrayList<>();
		String sql = "select * from ppc where curso = ?";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, "Bacharelado em Ciência e Tecnologia");
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				PPC p = new PPC();
				p.setCod_ppc(Integer.parseInt(rs.getString("cod_ppc")));
				p.setCurso(rs.getString("curso"));
				p.setMatriz(Integer.parseInt(rs.getString("matriz")));
				p.setCred_obrigatorias(rs.getInt("cred_obrigatorios"));
				p.setCred_limitadas(rs.getInt("cred_limitados"));
				p.setCred_livres(rs.getInt("cred_livres"));
				projetos.add(p);
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return projetos;
	}
	
	public List<PPC> retornaProjetos(){
		List<PPC> projetos = new ArrayList<>();
		String sql = "select * from ppc order by curso, matriz";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				PPC p = new PPC();
				p.setCod_ppc(Integer.parseInt(rs.getString("cod_ppc")));
				p.setCurso(rs.getString("curso"));
				p.setMatriz(Integer.parseInt(rs.getString("matriz")));
				p.setCred_obrigatorias(rs.getInt("cred_obrigatorios"));
				p.setCred_limitadas(rs.getInt("cred_limitados"));
				p.setCred_livres(rs.getInt("cred_livres"));
				projetos.add(p);
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return projetos;
	}
}
