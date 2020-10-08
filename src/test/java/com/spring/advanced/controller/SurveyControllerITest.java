package com.spring.advanced.controller;

//Integration Test for our service
import com.spring.advanced.Application;
import com.spring.advanced.model.Question;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.html.parser.Entity;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

//Initialize and launch spring boot application using @RunWith and telling it to use a random port for it.
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyControllerITest extends Object {
    public SurveyControllerITest() {
    }

    @LocalServerPort  //Initialize and launch spring boot application
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Before
    public void  before(){
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public  void testJsonAssert() throws JSONException {
        JSONAssert.assertEquals("{id:1}", "{id:1,name:Raju}", false);

    }
  /*  @Test
    public  void  testToGetSurveyQuestion1() throws JSONException {
      //  fail("Not yet implemented");
        String url = "http://localhost:" + port
                + "/surveys/Survey1/questions/Question1";
        TestRestTemplate restTemplate = new TestRestTemplate(); //Use to invoke rest url.
        //String output = restTemplate.getForObject(url, String.class);
        HttpHeaders headers = new HttpHeaders();    //Http headers
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));   // Accept application/JSON into the headers such
                                                                        // that it cane be send to the rest Template.
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        //System.out.print("RESULT"+ responseEntity.getBody());
        //assertTrue(responseEntity.getBody().contains("\"id\":\"Question1\""));

        String expected = "{id:Question1,description:Largest Country in the World,correctAnswer:Russia}";

        JSONAssert.assertEquals(expected, responseEntity.getBody(), false);

    }*/
  @Test
  public void testRetrieveSurveyQuestion() throws JSONException {

      HttpEntity<String> entity = new HttpEntity<String>(null, headers);

      ResponseEntity<String> response = restTemplate.exchange(createUrlWithPort
                      ("/surveys/Survey1/questions/Question1"),
              HttpMethod.GET, entity, String.class);

      String expected = "{id:Question1,correctAnswer:Russia}";

    //  String expected = "{\"id\":\"Question1\",\"description\":\"" +
     //         "Largest Country in the World\",\"correctAnswer\":\"Russia\"}";

      JSONAssert.assertEquals(expected, response.getBody(), false);
  }
    @Test
    public void retrieveAllSurveyQuestions() throws Exception {

        ResponseEntity<List<Question>> response = restTemplate.exchange(createUrlWithPort
                        ("/surveys/Survey1/questions"),
                HttpMethod.GET, new HttpEntity<String>(null, headers),
                new ParameterizedTypeReference<List<Question>>() {
                });

        Question sampleQuestion = new Question("Question1",
                "Largest Country in the World", "Russia", Arrays.asList(
                "India", "Russia", "United States", "China"));

        assertTrue(response.getBody().contains(sampleQuestion));
    }

    private String createUrlWithPort(String retrieveAllQuestionsUrl) {
        return "http://localhost:" + port + retrieveAllQuestionsUrl;
    }

    @Test
    public void addQuestion() {

        Question question = new Question("DOESNTMATTER", "Question1", "Russia",
                Arrays.asList("India", "Russia", "United States", "China"));

        HttpEntity entity = new HttpEntity<Question>(question, headers);

        ResponseEntity<String> response = restTemplate.exchange(createUrlWithPort
                        ("/surveys/Survey1/questions"),
                HttpMethod.POST, entity, String.class);

        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

        assertTrue(actual.contains("/surveys/Survey1/questions/"));

    }


}
    //RESULT{"id":"Question1","description":"Largest Country in the World","correctAnswer":"Russia",
// "options":["India","Russia","United States","China"]}2020-10-07 10:38:30.246  INFO 4148 --- [extShutdownHook]
// o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'