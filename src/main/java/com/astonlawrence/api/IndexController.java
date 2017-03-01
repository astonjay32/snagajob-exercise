package com.astonlawrence.api;

import com.astonlawrence.service.JobApplicationQualifier;
import com.astonlawrence.domain.Question;
import com.astonlawrence.service.QuestionLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionLoader questionLoader;


    @Autowired
    private JobApplicationQualifier jobApplicationQualifier;

    @RequestMapping("/")
    String index(Model model) throws JsonProcessingException {
        List<Question> questions = jobApplicationQualifier.getRequiredQuestions();
        String name = "Aston Lawrence";
        model.addAttribute("questions", questions);

        ObjectMapper objectMapper = new ObjectMapper();
        String questionsJson = objectMapper.writeValueAsString(questions).replaceAll(",", ", ");
        model.addAttribute("questionsJson", questionsJson );

        ModelAndView mav = new ModelAndView("application");


        return "index";
    }

    @RequestMapping(value="/apply", method = RequestMethod.GET)
    public String apply(Model model){
        model.addAttribute("questions", jobApplicationQualifier.getRequiredQuestions());
        return "application";
    }

}
