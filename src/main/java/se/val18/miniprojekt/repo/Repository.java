package se.val18.miniprojekt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.val18.miniprojekt.repo.Domain;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//by Gustaf Matsson
//2018-08-21
@Component
public class Repository {

    @Autowired
    DataSource dataSource;

    public void addDomainToDb(List<Domain> domain) throws SQLException {
        try {
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            for (Domain d : domain) {
                int rs = stmt.executeUpdate("UPDATE Domain SET Name = \'" + d.getName()
                + "\', URL= \'" + d.getURL() + "\' WHERE ID= " + d.getId());
            }


        } catch (SQLException e) {
            throw new SQLException(e);
        }

    }
}

