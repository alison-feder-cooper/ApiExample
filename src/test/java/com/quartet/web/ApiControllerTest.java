package com.quartet.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quartet.model.Metric;
import com.quartet.repository.MetricRepository;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ApiControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private MetricRepository metricRepository;

    @Test
    public void createMetricSucceeds() throws Exception {
        given(metricRepository.save(any(Metric.class)))
                .willReturn(new Metric("doughnut_count", 1005));

        mvc.perform(post("/api/metrics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        new CreateMetricRequest("doughnut_count", 1005))))
                .andExpect(status().is2xxSuccessful())
                //TODO more robust matchers for evaluating the Metric model directly
                .andExpect(content().string(containsString("doughnut_count")))
                .andExpect(content().string(containsString("1005")));
    }

    @Test
    public void createMetricFailsInvalidName() throws Exception {
        mvc.perform(post("/api/metrics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        new CreateMetricRequest("", 100))))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Name must not be blank"));
    }

    @Test
    public void getAllMetrics() throws Exception {
        Set<Metric> metrics = new HashSet<>();
        metrics.add(new Metric("pasta_quality", 555));
        metrics.add(new Metric("pasta_quality", 600));
        metrics.add(new Metric("tomato_sauce_intensity", 14));

        given(metricRepository.findAll())
                .willReturn(metrics);

        mvc.perform(get("/api/metrics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                //TODO more robust matchers for evaluating the Metric model directly / number of models returned
                .andExpect(content().string(containsString("\"name\":\"pasta_quality\",\"value\":555,\"")))
                .andExpect(content().string(containsString("\"name\":\"pasta_quality\",\"value\":600,\"")))
                .andExpect(content().string(containsString("\"name\":\"tomato_sauce_intensity\",\"value\":14,\"")));
    }

    @Test
    public void findByTimeReturnsMetricValue() throws Exception {
        given(metricRepository.findValueByCreatedDateTime(any(DateTime.class)))
                .willReturn(57L);

        mvc.perform(get("/api/value_at_time")
                .contentType(MediaType.APPLICATION_JSON)
                .param("created_date_time", DateTime.now().toString(ISODateTimeFormat.dateTime())))
                .andExpect(status().is2xxSuccessful())
                //TODO more robust utilities to inspect as a Long
                .andExpect(content().string("57"));

    }

    @Test
    public void findByTimeReturnsEmpty() throws Exception {
        given(metricRepository.findValueByCreatedDateTime(any(DateTime.class)))
                .willReturn(null);

        mvc.perform(get("/api/value_at_time")
                .contentType(MediaType.APPLICATION_JSON)
                .param("created_date_time", DateTime.now().toString(ISODateTimeFormat.dateTime())))
                .andExpect(status().is2xxSuccessful())
                //TODO more robust utilities to inspect as a Long
                .andExpect(content().string(""));
    }

    @Test
    public void getAggregateValueInRange() throws Exception {
        given(metricRepository.findAggregateValueInRangeForMetric(anyString(), any(DateTime.class),
                any(DateTime.class)))
                .willReturn(142L);

        mvc.perform(get("/api/aggregate_value")
                .contentType(MediaType.APPLICATION_JSON)
                .param("metric_name", "hot_sauce_power")
                .param("start_date_time_inclusive", DateTime.now().toString(ISODateTimeFormat.dateTime()))
                .param("end_date_time_exclusive", DateTime.now().toString(ISODateTimeFormat.dateTime())))
                .andExpect(status().is2xxSuccessful())
                //TODO more robust utilities to inspect as a Long
                .andExpect(content().string("142"));
    }
}
