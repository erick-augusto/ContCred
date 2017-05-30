package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Disciplina;

public class DisciplinaDAO {
	private Connection connection;
	public DisciplinaDAO(){
		this.connection = new Conexao().getConexao();
	}
	
	public void fechaConexao() {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void insereDisciplina(Disciplina d){
		String sql = "insert into disciplinas (cod,nome,t,p,i) values (?,?,?,?,?)";
        try {
            //System.out.println("Iniciando Registro no Banco de Dados...");
            PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql);

            stmt.setString(1, d.getCod_disciplina());
            stmt.setString(2, d.getNome());
            stmt.setInt(3, d.getT());
            stmt.setInt(4, d.getP());
            stmt.setInt(5, d.getI());

            stmt.execute();
            stmt.close();
            //System.out.println("Registro Realizado com Sucesso!");
            //System.out.println();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}
	
	public Disciplina buscaDisciplina(String cod){
		//System.out.println("Buscando disciplina");
		//System.out.println(cod);
		Disciplina d = new Disciplina();
		String sql = "select * from disciplinas where cod = ?";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, cod);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				d.setCod_disciplina(rs.getString("cod"));
				d.setNome(rs.getString("nome"));
				d.setT(rs.getInt("t"));
				d.setP(rs.getInt("p"));
				d.setI(rs.getInt("i"));
				//System.out.println("d: "+d.getCod_disciplina()+" "+d.getNome()+" "+d.getT()+" "+d.getP()+" "+d.getI());
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return d;
	}
	
	public List<Disciplina> listarDisciplinas(){
		List<Disciplina> disciplinas = new ArrayList<>();
		String sql = "select * from disciplinas order by nome";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Disciplina d = new Disciplina();
				d.setCod_disciplina(rs.getString("cod"));
				d.setNome(rs.getString("nome").toUpperCase());
				d.setT(rs.getInt("t"));
				d.setP(rs.getInt("p"));
				d.setI(rs.getInt("i"));
				disciplinas.add(d);
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return disciplinas;
	}
	
	public int credDisciplina(String cod_disciplina){
		int tp = 0;
		String sql = "select * from disciplinas where cod = ?";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, cod_disciplina);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				tp += rs.getInt("t") + rs.getInt("p");
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return tp;
	}
	
	public String nomeDisciplina(String cod_disciplina){
		String nome = "";
		String sql = "select nome from disciplinas where cod = ?";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, cod_disciplina);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				nome = rs.getString("nome").toUpperCase();
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return nome;
	}
	
	public List<Disciplina> letraDisciplina(String letra){
		List<Disciplina> disciplinas = new ArrayList<>();
		//System.out.println("letra: "+letra);
		String sql = "select * from disciplinas where nome like ? order by nome";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, letra+"%");
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Disciplina d = new Disciplina();
				d.setCod_disciplina(rs.getString("cod"));
				d.setNome(rs.getString("nome").toUpperCase());
				d.setT(rs.getInt("t"));
				d.setP(rs.getInt("p"));
				d.setI(rs.getInt("i"));
				disciplinas.add(d);
			}			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return disciplinas;
	}
}
