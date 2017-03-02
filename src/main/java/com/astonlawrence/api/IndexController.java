package com.astonlawrence.api;

import com.astonlawrence.service.JobApplicationQualifier;
import com.astonlawrence.domain.Question;
import com.astonlawrence.service.QuestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private JobApplicationQualifier jobApplicationQualifier;

    @RequestMapping("/")
    String index(Model model) throws JsonProcessingException {
        List<Question> questions = questionService.getApplicationQuestions();
        String name = "Aston Lawrence";
        model.addAttribute("questions", questions);

        ObjectMapper objectMapper = new ObjectMapper();
        String questionsJson = objectMapper.writeValueAsString(questions).replaceAll(",", ", ");
        model.addAttribute("questionsJson", questionsJson );

        ModelAndView mav = new ModelAndView("application");

        return "index";
    }
}
