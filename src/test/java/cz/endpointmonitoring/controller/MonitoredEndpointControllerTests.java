package cz.endpointmonitoring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.endpointmonitoring.model.MonitoredEndpoint;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class MonitoredEndpointControllerTests {
    private static final String MAIN = "/api/monitoredEndpoints/";
    private static final String DEFAULT_URL = "http://www.google.com";
    private static final int DEFAULT_INTERVAL = 10;

    @Autowired
    private MockMvc mvc;

    @Test
    public void testApiWithoutAuthorization() throws Exception {
        int status = mvc.perform(MockMvcRequestBuilders.get(MAIN)).andReturn().getResponse().getStatus();
        assert HttpStatus.UNAUTHORIZED.value() == status;
    }

    @Test
    public void testApiWithWrongAuthorization() throws Exception {
        int status = mvc.perform(MockMvcRequestBuilders.get(MAIN).header("Authorization", "Bearer abc123"))
                .andReturn().getResponse().getStatus();
        assert HttpStatus.UNAUTHORIZED.value() == status;
    }

    @Test
    public void testApiWithCorrectAuthorization() throws Exception {
        int status = mvc.perform(MockMvcRequestBuilders.get(MAIN).header("Authorization", "Bearer 93f39e2f-80de-4033-99ee-249d92736a25"))
                .andReturn().getResponse().getStatus();
        assert HttpStatus.OK.value() == status;
    }

    private MockHttpServletResponse insertValidEndpoint() throws Exception {
        MonitoredEndpoint monitoredEndpoint = new MonitoredEndpoint();
        monitoredEndpoint.setUrl(DEFAULT_URL);
        String requestJson = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(monitoredEndpoint);
        return mvc.perform(MockMvcRequestBuilders.post(MAIN).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer 93f39e2f-80de-4033-99ee-249d92736a25")
                .content(requestJson)).andReturn().getResponse();
    }

    @Test
    public void testInsert() throws Exception {
        MockHttpServletResponse response = insertValidEndpoint();

        assert HttpStatus.OK.value() == response.getStatus();

        JSONObject responseJson = new JSONObject(response.getContentAsString());

        // null is represent as "null"
        // Even if you use .get() instead it still fails on assertNull()
        Assertions.assertNotEquals("null", responseJson.getString("id"));
        Assertions.assertEquals(DEFAULT_URL, responseJson.getString("url"));
        Assertions.assertEquals("null", responseJson.getString("name"));
        Assertions.assertNotEquals("null",responseJson.getString("dateOfCreation"));
        Assertions.assertEquals(DEFAULT_INTERVAL, responseJson.getInt("monitoringInterval"));
    }

    @Test
    public void testDeleteForInsertingUser() throws Exception {
        String id = new JSONObject(insertValidEndpoint().getContentAsString()).getString("id");
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.delete(MAIN + id)
                .header("Authorization", "Bearer 93f39e2f-80de-4033-99ee-249d92736a25")).andReturn().getResponse();

        assert HttpStatus.OK.value() == response.getStatus();
        assert -1 < Integer.parseInt(response.getContentAsString());
    }

    @Test
    public void testDeleteForOtherUser() throws Exception {
        String id = new JSONObject(insertValidEndpoint().getContentAsString()).getString("id");
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.delete(MAIN + id)
                .header("Authorization", "Bearer dcb20f8a-5657-4f1b-9f7f-ce65739b359e")).andReturn().getResponse();

        assert HttpStatus.OK.value() == response.getStatus();
        assert -1 == Integer.parseInt(response.getContentAsString());
    }

    @Test
    public void testUpdate() throws Exception {
        JSONObject responseJson = new JSONObject(insertValidEndpoint().getContentAsString());
        String id = responseJson.getString("id");
        String dateOfCreation = responseJson.getString("dateOfCreation");
        String updatedUrl = "http://www.reddit.com";
        String updatedName = "updated Name";
        int updatedInterval = DEFAULT_INTERVAL + 10;

        MonitoredEndpoint monitoredEndpoint = new MonitoredEndpoint();
        monitoredEndpoint.setId((long) Integer.parseInt(id) + 10);
        monitoredEndpoint.setUrl(updatedUrl);
        monitoredEndpoint.setName(updatedName);
        monitoredEndpoint.setMonitoringInterval(updatedInterval);

        String requestJson = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(monitoredEndpoint);
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.put(MAIN + id).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer 93f39e2f-80de-4033-99ee-249d92736a25")
                .content(requestJson)).andReturn().getResponse();
        System.out.println("respondant " + response.getStatus());
        responseJson = new JSONObject(response.getContentAsString());

        Assertions.assertEquals(id, responseJson.getString("id"));
        Assertions.assertEquals(DEFAULT_URL, responseJson.getString("url"));
        Assertions.assertEquals(updatedName, responseJson.getString("name"));
        Assertions.assertEquals(updatedInterval, responseJson.getInt("monitoringInterval"));
    }

}
