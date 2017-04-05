package com.quartet.repository;

import com.quartet.model.Metric;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
//Test uses a simple in memory database; opted for this for simplicity, but could wire up a test db if one desired
public class MetricRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MetricRepository metricRepository;

    @Test
    public void findValueByDateTime() throws Exception {
        Metric metricToPersist = new Metric("star_wars_view_count", 3000);
        entityManager.persist(metricToPersist);
        long retrievedMetric = metricRepository.findValueByCreatedDateTime(metricToPersist.getCreatedDateTime());
        assertThat(retrievedMetric).isEqualTo(3000);
    }

    @Test
    public void findAggregateValue() throws Exception {
        //persist metrics to db
        Metric metricToPersist1 = new Metric("star_wars_view_count", 1000);
        Metric metricToPersist2 = new Metric("star_wars_view_count", 45);
        Metric metricToPersist3 = new Metric("star_trek_view_count", 5);
        entityManager.persist(metricToPersist1);
        entityManager.persist(metricToPersist2);
        entityManager.persist(metricToPersist3);

        //fetch all star wars metrics, hacked by doing fetch from 1 second ago up until now to pull in all metrics
        long retrievedMetric = metricRepository.findAggregateValueInRangeForMetric("star_wars_view_count",
                DateTime.now().minus(10000),
                DateTime.now());

        assertThat(retrievedMetric).isEqualTo(1045);
    }
}
