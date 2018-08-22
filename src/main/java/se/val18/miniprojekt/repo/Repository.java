package se.val18.miniprojekt.repo;

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

    public void addHitsToDB(List<Hit> hits) throws SQLException {
        try {
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            for (Domain d : domain) {
                int rs = stmt.executeUpdate("UPDATE Domain SET Name = \'" + d.getName()
                + "\', URL= \'" + d.getURL() + "\' WHERE ID= " + d.getId());

            String insert =
                    "insert into dbo.hits (word_id, domain_id, context, createdTimeStamp) Values(?,?,?,?)";
            var ps = conn.prepareStatement(insert);

            for (Hit h : hits) {
                ps.setInt(1, h.getWordId());
                ps.setInt(2, h.getDomainId());
                ps.setString(3, h.getContext());
                ps.setString(4, "2712");
                ps.addBatch();
            }
            ps.executeBatch();


        } catch (SQLException e) {
            throw new SQLException(e);
        }

    }

    public int getWordIdForString(String word) {
        try {
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();

            String query =
                    "select id from dbo.search where word= ? ";
            var ps = conn.prepareStatement(query);
            ps.setString(1, word);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else return -1;

        } catch (SQLException e) {
            try {
                throw new SQLException("Something went wrong when retrieving word: " + word);
            } catch (SQLException e2) {
                e.printStackTrace();
                return -1;

            }
        }
    }


    @SuppressWarnings("Duplicates")
    public List<CountName> getCountAndNameForId(int id) {
        var countNames = new ArrayList<CountName>();
        try {
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();

            String query =
                    "SELECT do.Name, COUNT(*) as count " +
                            "FROM dbo.HITS as hi " +
                            "INNER JOIN dbo.SEARCH as se " +
                            " " +
                            "ON hi.Word_ID = se.ID " +
                            "INNER JOIN dbo.DOMAIN as do " +
                            "ON hi.Domain_ID = do.ID " +
                            "WHERE Word_ID = ? " +
                            "GROUP BY do.Name";
            var ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            var rs = ps.executeQuery();
            while (rs.next()) {
                countNames.add(new CountName(rs.getString("name"), rs.getInt("count")));
            }
            return countNames;
        } catch (SQLException e) {
            System.err.println("Something went wrong when fetching CountNames!");
            return null;
        }
    }



    @SuppressWarnings("Duplicates")
    public List<Domain> getAllDomains() {
        var domains = new ArrayList<Domain>();
        try {
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();

            String query =
                    "SELECT id, name, url from dbo.domain";
            var ps = conn.prepareStatement(query);
            var rs = ps.executeQuery();
            while (rs.next()) {
                domains.add(new Domain(rs.getString("url"),
                        rs.getString("name"),
                        rs.getInt("id"))
                );
            }
            return domains;
        } catch (SQLException e) {
            System.err.println("Something went wrong when fetching domains!");
            return null;
        }
    }
}

