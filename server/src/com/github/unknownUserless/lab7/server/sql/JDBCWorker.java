package com.github.unknownUserless.lab7.server.sql;

import com.github.unknownUserless.lab7.server.sql.mytables.Squads;
import com.github.unknownUserless.lab7.server.sql.mytables.Users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCWorker {

    /*private final String DB_URL = "jdbc:postgresql://pg:5432/studs";
    private final String USER = "s263937";
    private final String PASS = "sms970";*/
    private final String DB_URL = "jdbc:postgresql://localhost:5432/lab7";
    private final String USER = "vlados";
    private final String PASS = "123";
    private static JDBCWorker instance;

    public static JDBCWorker instance(){
        if (instance == null){
            instance = new JDBCWorker();
        }
        return instance;
    }

    private Connection connection;

    private Users users;
    private Squads squads;

    private JDBCWorker() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("The database connection was successful");
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public Users users(){
        if (this.users == null) {
            this.users = new Users(connection);
        }
        return users;
    }

    public Squads squads(){
        if (this.squads == null){
            this.squads = new Squads(connection);
        }
        return this.squads;
    }


}
