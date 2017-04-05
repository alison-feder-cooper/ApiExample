package com.quartet.web;

import com.quartet.model.InvalidNameException;
import com.quartet.model.Metric;
import com.quartet.repository.MetricRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;


@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private MetricRepository metricRepository;

    //HttpMessageConverter automatically wired up by Spring Boot; converts incoming request
    //JSON to POJO
    @RequestMapping(method = RequestMethod.POST, value = "/metrics")
    public Metric createMetric(@RequestBody CreateMetricRequest request) {
        if (StringUtils.isBlank(request.getName())) {
            throw new InvalidNameException();
        }
        return metricRepository.save(new Metric(request.getName(), request.getValue()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/metrics")
    public Iterable<Metric> getAllMetrics() {
        return metricRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/find_by_created_date_time")
    public long findValueByCreatedDateTime(
            @RequestParam("created_date_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTime) {
        return metricRepository.findValueByCreatedDateTime(createdDateTime);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/find_by_created_date_time")
    public long findAggregateValueInRange(
            @RequestParam("metric_name") String metricName,
            @RequestParam("start_date_time_inclusive") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime startDateTimeInclusive,
            @RequestParam("end_date_time_exclusive") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime endDateTimeExclusive) {
        return metricRepository.findAggregateValueInRangeForMetric(metricName, startDateTimeInclusive, endDateTimeExclusive);
    }
}
