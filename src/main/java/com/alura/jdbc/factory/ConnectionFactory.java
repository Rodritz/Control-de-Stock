package com.alura.jdbc.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;

public class ConnectionFactory {
	/*PATRON FACTORY PARA TOMAR LAS CONEXIONES DE LA BASE DE DATOS Y EL USO DEL POOLED DE CONEXIONES PARA MEJORAR LA PERFORMANCE*/
	/*opcion avanzada de generar las peticiones de conexion mediante el pool de conexciones*/
	
	//atributo privado
	private DataSource dataSource;
	
	//creamos el constructor
	public ConnectionFactory() {
		ComboPooledDataSource pooledDataSource = new ComboPooledDataSource();
		pooledDataSource.setJdbcUrl("jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTmeZone=UTC");
		pooledDataSource.setUser("root");
		pooledDataSource.setPassword("root");
		pooledDataSource.setMaxPoolSize(10);//seteamos el max de conexiones que se pueden hacer
		
		this.dataSource = pooledDataSource;
	}
	
	//implementacion de recuperaConexion
	public Connection recuperaConexion(){
		try {
			return this.dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}

	/*--opcion 1 para generar las conexiones a la DB--
	public Connection recuperaConexion() throws SQLException {
		return DriverManager.getConnection(
				"jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTmeZone=UTC",
				"root",
				"root");
	}*/

