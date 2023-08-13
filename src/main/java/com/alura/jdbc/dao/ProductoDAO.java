package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

public class ProductoDAO {

	//final private Connection con;
	private Connection con;

	public ProductoDAO(Connection con) {
		this.con = con;
	}
	
	public void guardar(Producto producto) {
		/*--no hace falta llamar a la conexcion. solo modificar el atributo Connection como final--*/
		//final Connection con = new ConnectionFactory().recuperaConexion();
		
		//try(con){
		try{
			con.setAutoCommit(false);// con esto configuramos que la conexion no va a tener mas el control de la transaccion 
									//nosotros tomamos el control de la transaccion
									//es necesario luego tener el commit() para que se ejecuten todas las solicitudes
									//**por default es SetUtoCommit(true) y en ese caso no es necesario en commit()
			
			/*Para evitar el fallo por SQL Injection debemos utilizar la interfaz PreparedStatement
			el PreparedStatement trata los parámetros del comando SQL para que caracteres y
			comandos especiales sean tratados como strings.*/
			
			final PreparedStatement statement = con.prepareStatement(
					"INSERT INTO PRODUCTO"
							+ "(nombre, descripcion, cantidad, categoria_id)" 
							+ " VALUES(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);	//el RETURN_GENERATED_KEYS nos da el ID 	
			
			try(statement){				
				ejecutaRegistro(producto, statement);
				con.commit();//con esto aseguramos que todas las transacciones del loop se realizan con exito
							//esto es necesario siempre que tomamos el control de las transacciones con el setAutoCommit en false
			}
		} catch(SQLException e) {
			throw new RuntimeException(e);
//			e.printStackTrace();
//			System.out.println("ROLLBACK de la transaccion");
//			con.rollback();//si por algun motivo algo falla todo vuelve para atras
		}		
	}
	
	private void ejecutaRegistro(Producto producto, PreparedStatement statement)
			throws SQLException {
		statement.setString(1, producto.getNombre());		
		statement.setString(2, producto.getDescripcion());		
		statement.setInt(3, producto.getCantidad());
		statement.setInt(4, producto.getCategoriaId());

		statement.execute();
		
		final ResultSet resultSet = statement.getGeneratedKeys();
		
		try(resultSet){
			while(resultSet.next()) {
				producto.setId(resultSet.getInt(1));
				System.out.println(
						String.format("Fue insertado el producto %s", producto));
			}
		}
	}

	public List<Producto> listar() {

		List<Producto> resultado = new ArrayList<>();		
		
		//final Connection con = new ConnectionFactory().recuperaConexion();
		
		//try(con){
		try{	
			
			final PreparedStatement statement = con.prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");
			
			try(statement){
				statement.execute();
				
				final ResultSet resultSet = statement.getResultSet();
				
				
				try(resultSet){
					while(resultSet.next()) {
						Producto fila = new Producto(resultSet.getInt("ID"),
								resultSet.getString("NOMBRE"),
								resultSet.getString("DESCRIPCION"),
								resultSet.getInt("CANTIDAD"));
						
						resultado.add(fila);	
					}
				}
			}
			//return resultado;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return resultado;		
	}

	public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) {

		//final Connection con = new ConnectionFactory().recuperaConexion();
		
		try{			
			final PreparedStatement statement = con.prepareStatement(
	                "UPDATE PRODUCTO SET "
	                + " NOMBRE = ?, "
	                + " DESCRIPCION = ?,"
	                + " CANTIDAD = ?"
	                + " WHERE ID = ?");

	        try (statement) {
	            statement.setString(1, nombre);
	            statement.setString(2, descripcion);
	            statement.setInt(3, cantidad);
	            statement.setInt(4, id);
	            statement.execute();

	            int updateCount = statement.getUpdateCount();

	            return updateCount;
	        }
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}
	

	public int eliminar(Integer id) {
		//final Connection con = new ConnectionFactory().recuperaConexion();
		
		//try(con){
		try{
			final PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");
			
			try(statement){
				statement.setInt(1, id);
				statement.execute();
				
				int updateCount = statement.getUpdateCount();
		        
		        return updateCount;
			}
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Producto> listar(Integer categoriaId) {
		List<Producto> resultado = new ArrayList<>();		
		
		//final Connection con = new ConnectionFactory().recuperaConexion();
		
		//try(con){
		try{	
			final PreparedStatement statement = con.prepareStatement(
					"SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD "
					+ "FROM PRODUCTO "
					+ "WHERE CATEGORIA_ID = ?");
			
			try(statement){
				statement.setInt(1, categoriaId);
				statement.execute();
				
				final ResultSet resultSet = statement.getResultSet();
				
				
				try(resultSet){
					while(resultSet.next()) {
						Producto fila = new Producto(
								resultSet.getInt("ID"),
								resultSet.getString("NOMBRE"),
								resultSet.getString("DESCRIPCION"),
								resultSet.getInt("CANTIDAD"));
						
						resultado.add(fila);	
					}
				}
			}
			//return resultado;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return resultado;	
	}	
}

/*-------IMPORTANTE 1------*/

/*Para evitar el fallo por SQL Injection debemos utilizar la interfaz PreparedStatement
el PreparedStatement trata los parámetros del comando SQL para que caracteres y
comandos especiales sean tratados como strings.*/

/*-------IMPORTANTE 2 => TRY WITH RESOURCES (utiliza recursos autoCloseable)------*/

/*desde la versión 9 de Java es posible utilizar variables final dentro 
de la declaración de los recursos e implementamos la interfaz autoCloseable*/

/*--desde la versión 7 de Java hay un recurso llamado Try With Resources. 
El Try With Resources nos permite declarar recursos que van a ser utilizados 
en un bloque de try catch con la certeza de que estos recursos van a ser cerrados 
o finalizados automáticamente después de la ejecución del bloque. Un requisito 
para eso es que estos recursos deben implementar la interfaz autoCloseable.--

try(ResultSet resultSet = statement.getGeneratedKeys();){
	while(resultSet.next()) {
		System.out.println(
				String.format(
						"Fue insertado el producto de ID %d", 
						resultSet.getInt(1)));
	}
}*/

/*---antes de java 7 debiamos cerrar las conexiones por medio de comandos--

ResultSet resultSet = statement.getGeneratedKeys();

while(resultSet.next()) {
	System.out.println(
			String.format(
					"Fue insertado el producto de ID %d", 
					resultSet.getInt(1)));
}

resultSet.close();*/
