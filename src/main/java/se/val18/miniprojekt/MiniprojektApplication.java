package se.val18.miniprojekt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.val18.miniprojekt.repo.Hit;
import se.val18.miniprojekt.repo.Repository;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class MiniprojektApplication implements CommandLineRunner {

    @Autowired
    Repository repo;
    public static void main(String[] args) {
        SpringApplication.run(MiniprojektApplication.class, args);
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


        repo.getAllDomains().forEach(System.out::println);

        repo.getCountAndNameForId(repo.getWordIdForString("Trygghet"))
                .forEach(x-> System.out.println(x.getName() + " " + x.getCount()));

    }
}

