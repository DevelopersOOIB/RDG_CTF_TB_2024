package ctf.spring.flaggetter;

import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
public class Controller {

    @RequestMapping(value = "/")
    public String greeting(Model model) {
        return "home";
    }

}
