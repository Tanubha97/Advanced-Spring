package com.spring.advanced.controller;

import com.google.gson.Gson;
import com.spring.advanced.model.Question;
import com.spring.advanced.service.SurveyService;
import net.minidev.json.JSONValue;
import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = SurveyController.class)
public class SurveyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // Mock @Autowired
    @MockBean
    private SurveyService surveyService;

    @Test
    public void retrieveDetailsForQuestion() throws Exception {
        Question mockQuestion = new Question("Question1",
                "Largest Country in the World", "Russia", Arrays.asList(
                "India", "Russia", "United States", "China"));

        when(surveyService.retrieveQuestion(anyString(), anyString())).thenReturn(mockQuestion);

        //building the url to be tested
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/surveys/Survey1/questions/Question1").accept(MediaType.APPLICATION_JSON);//checking format

        //calling the service through mockMvc
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"id\":\"Question1\",\"description\":\"Largest Country in the World\"," +
                "\"correctAnswer\":\"Russia\"}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        // Assert
    }
    @Test
    public void retrieveSurveyQuestions() throws Exception {
        List<Question> mockList = Arrays.asList(
                new Question("Question1", "First Alphabet", "A", Arrays.asList(
                        "A", "B", "C", "D")));
               // new Question("Question2", "Last Alphabet", "Z", Arrays.asList("A", "X", "Y", "Z")));

        when(surveyService.retrieveQuestions(anyString())).thenReturn(mockList);

        MvcResult result = mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get("/surveys/Survey1/questions").accept(
                                MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Question question = new Question();
        question.setId("Question1");
        question.setDescription("First Alphabet");
        question.setCorrectAnswer("A");
        List<String> option = new ArrayList<>();
        option.add("A");
        option.add("B");
        option.add("C");
        option.add("D");
        question.setOptions(option);
        Gson gson = new Gson();
        JSONAssert.assertEquals(gson.toJson(Collections.singletonList(question), List.class), result.getResponse()
                .getContentAsString(), false);

        List<Question> questions = new ArrayList<>();
        questions.add(question);
    }

    @Test
    public void createSurveyQuestion() throws Exception {
        Question mockQuestion = new Question("1", "Smallest Number", "1",
                Arrays.asList("1", "2", "3", "4"));

        String questionJson = "{\"description\":\"Smallest Number\",\"correctAnswer\":\"1\",\"options\"" +
                ":[\"1\",\"2\",\"3\",\"4\"]}";
        //surveyService.addQuestion to respond back with mockQuestion
        Mockito.when(
                surveyService.addQuestion(Mockito.anyString(), Mockito
                        .any(Question.class))).thenReturn(mockQuestion);

        //Send question as body to /surveys/Survey1/questions
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/surveys/Survey1/questions")
                .accept(MediaType.APPLICATION_JSON).content(questionJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        assertEquals("http://localhost/surveys/Survey1/questions/1", response
                .getHeader(HttpHeaders.LOCATION));

    }
}
