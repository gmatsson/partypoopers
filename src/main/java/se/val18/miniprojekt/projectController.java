package se.val18.miniprojekt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import se.val18.miniprojekt.repo.Repository;

import java.util.Calendar;

@Controller
public class projectController {

    @Autowired
    private Repository repo;

    @GetMapping("/")
    public ModelAndView index(){
        return new ModelAndView("index");
    }

    @PostMapping("/search")
    public ModelAndView result(@RequestParam String text){
        return new ModelAndView("index").addObject("words", repo.getCountAndNameForId(repo.getWordIdForString(text)));
    }

    @PostMapping("/words")
    public ModelAndView words(@RequestParam String text){
        return new ModelAndView("index").addObject("words", repo.getCountAndNameForId(repo.getWordIdForString(text)));
    }
}
