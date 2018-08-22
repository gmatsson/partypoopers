package se.val18.miniprojekt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiniprojektApplication implements CommandLineRunner {

    @Autowired
    se.val18.miniprojekt.Repository repo;

    public static void main(String[] args) {
        SpringApplication.run(se.val18.miniprojekt.MiniprojektApplication.class, args);

    }

    @Override
    public void run(String... strings) throws Exception {
        //        Domain dom = new Domain(1, "Mathias", "www.mathiasnaked.com");
        //        List<Domain> list = new ArrayList<>();
        //        list.add(dom);
        //        try {
        //            repo.addDomainToDb(list);
        //        } catch (
        //                SQLException e) {
        //            e.printStackTrace();
        //        }
    }
}

