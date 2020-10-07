package com.spring.advanced.controller;

import com.spring.advanced.model.Question;
import com.spring.advanced.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/*Create a REST Service for Retrieving all questions for a survey
        Autowire SurveyService
        Create @GetMapping("/surveys/{surveyId}/questions")
Use @PathVariable String surveyId
        http://localhost:8080/surveys/Survey1/questions/*/

@RestController
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @GetMapping("/surveys/{surveyId}/questions")
    public List<Question> retrieveQuestionsForSurvey(@PathVariable String surveyId){
        return surveyService.retrieveQuestions(surveyId);
    }

    @GetMapping("/surveys/{surveyId}/questions/{questionId}")
    public Question retrieveQuestionDetailsForSurvey(@PathVariable String surveyId,
                                                           @PathVariable String questionId) {
        return surveyService.retrieveQuestion(surveyId, questionId);
    }

    // Success - URI of the new resource in Response Header
    // Status - created
    // URI -> /surveys/{surveyId}/questions/{questionId}
    // question.getQuestionId()
    @PostMapping("/surveys/{surveyId}/questions")
    public ResponseEntity<URI> addQuestionForSurvey(@PathVariable String surveyId,
                                                    @RequestBody Question question){
        Question newQuestion = surveyService.addQuestion(surveyId, question);

        if (question == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path
                ("/{id}").buildAndExpand(newQuestion.getId()).toUri();
        return ResponseEntity.created(location).build();

    }

}
