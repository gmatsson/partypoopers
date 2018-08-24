package se.val18.miniprojekt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import se.val18.miniprojekt.repo.*;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Controller
public class ProjectController {

    @Autowired
    private Repository repo;

    @GetMapping("/")
    public String index(@RequestParam (required = false) List<CountName> parties){
        List <Search> words = repo.getAllSearch();
        System.out.println("index");
        try{
            System.out.println(parties);
        }catch (Exception e){

        }
        return "redirect:/words/Sverige";
    }

    @GetMapping("/words/{word}")
    public ModelAndView result(@PathVariable String word){
        List<CountName> parties = repo.getCountAndNameForId(repo.getWordIdForString(word));
        List <Search> words = repo.getAllSearch();
        System.out.println("result");
        System.out.println(parties);

        double max = 0;

        for (var p : parties) {
            max = Math.max(p.getCount(), max);
        }

        return new ModelAndView("index")
                .addObject("parties", parties)
                .addObject("words", words )
                .addObject("max", max);
    }

    @GetMapping("/quiz")
    public ModelAndView quiz(){
        Hit hits = repo.getRandomContext();
        List<Domain> domains = repo.getAllDomains();
        return new ModelAndView("quiz").addObject("hit", hits).addObject("parties", domains).addObject("parti");
    }

    @GetMapping("/quiz/answer/{domainID}/{questID}")
    public ModelAndView answer(@PathVariable int domainID, @PathVariable int questID){
        List<Domain> domains = repo.getAllDomains();
        String parti = "";
        for (Domain d: domains) {
            if(d.getId()== domainID)
                parti = d.getName();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ModelAndView("quiz").addObject("hit", repo.getRandomContext()).addObject("parties", domains).addObject("parti", parti);
    }


}