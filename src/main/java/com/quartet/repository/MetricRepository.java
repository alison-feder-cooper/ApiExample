package com.quartet.repository;

import com.quartet.model.Metric;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository extends CrudRepository<Metric, Long> {

    //hypothetically possible that multiple metrics created at the same time; return all of them; assuming only one returned per spec
    @Query("SELECT m.value FROM Metric m WHERE m.createdDateTime = ?1")
    Long findValueByCreatedDateTime(DateTime createdDateTime);

    @Query("SELECT SUM(m.value) FROM Metric m WHERE m.name = ?1 AND m.createdDateTime >= ?2 AND m.createdDateTime < ?3")
    Long findAggregateValueInRangeForMetric(String name, DateTime startDateTimeInclusive, DateTime endDateTimeExclusive);
}
