package com.example.mejner.myrestapi.controller;

import com.example.mejner.myrestapi.exception.QuestionNotFoundException;
import com.example.mejner.myrestapi.model.Question;
import com.example.mejner.myrestapi.service.SurveyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
class SurveyController {
    @Autowired
    private SurveyService surveyService;

    @GetMapping("/surveys/{surveyId}/questions")
    public List<Question> retrieveQuestions(@PathVariable String surveyId) {
        return surveyService.retrieveQuestions(surveyId);
    }

    @GetMapping("/surveys/{surveyId}/questions/{questionId}")
    public Question retrieveSingleQuestion(@PathVariable String surveyId, @PathVariable String questionId) {

        Question question = surveyService.retrieveQuestion(surveyId, questionId);

        if(question == null)
            throw new QuestionNotFoundException("id-" + questionId);

        return question;
    }

    @PostMapping("/surveys/{surveyId}/questions")
    public ResponseEntity<Object> addQuestionToSurvey(@PathVariable String surveyId, @RequestBody Question newQuestion) {
        Question question = surveyService.addQuestion(surveyId, newQuestion);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(question.getId()).toUri();

        if (question == null) {
            return ResponseEntity.noContent().build();
        }


        return ResponseEntity.created(location).build();
    }


}
