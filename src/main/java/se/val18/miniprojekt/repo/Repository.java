package se.val18.miniprojekt.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.val18.miniprojekt.repo.Domain;

import javax.sql.DataSource;
import java.sql.*;
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

            int returnValue = 0;

            if (rs.next()) {
                returnValue = rs.getInt("id");
            } else returnValue = -1;

            conn.close();

            return returnValue;

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
                    "SELECT do.Name, COUNT(*) as count, do.Color as color " +
                            "FROM dbo.HITS as hi " +
                            "INNER JOIN dbo.SEARCH as se " +
                            " " +
                            "ON hi.Word_ID = se.ID " +
                            "INNER JOIN dbo.DOMAIN as do " +
                            "ON hi.Domain_ID = do.ID " +
                            "WHERE Word_ID = ? " +
                            "GROUP BY do.Name, do.color";
            var ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            var rs = ps.executeQuery();


            while (rs.next()) {
                countNames.add(new CountName(
                        rs.getString("name"),
                        rs.getInt("count"),
                        rs.getString("color")));
            }
            conn.close();
            return countNames;
        } catch (SQLException e) {
            e.printStackTrace();
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
                    "SELECT id, name, url, path, color from dbo.domain";
            var ps = conn.prepareStatement(query);
            var rs = ps.executeQuery();
            while (rs.next()) {
                domains.add(new Domain(rs.getString("url"),
                        rs.getString("name"),
                        rs.getString("path"),
                        rs.getString("color"),
                        rs.getInt("id"))
                );
            }
            return domains;
        } catch (SQLException e) {
            System.err.println("Something went wrong when fetching domains!");
            return null;
        }
    }
    public List<Search> getAllSearch() {
        var searches = new ArrayList<Search>();
        try {
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();

            String query =
                    "SELECT id, word from dbo.search";
            var ps = conn.prepareStatement(query);
            var rs = ps.executeQuery();


            while (rs.next()) {
                searches.add(new Search(rs.getInt("id"),
                        rs.getString("word")
                ));
            }

            conn.close();
            return searches;
        } catch (SQLException e) {
            System.err.println("Something went wrong when fetching search!");
            return null;
        }
    }
}

