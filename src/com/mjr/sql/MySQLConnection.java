package com.mjr.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection {

    private final Connection connection;

    public MySQLConnection(String url, String user, String password) throws SQLException, ClassNotFoundException {
	Class.forName("com.mysql.jdbc.Driver");
	connection = DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
	return connection;
    }

    public ResultSet executeQuery(String statement) {
	Statement myStmt;
	try {
	    myStmt = connection.createStatement();
	    ResultSet myRs = myStmt.executeQuery(statement);
	    return myRs;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }
}
