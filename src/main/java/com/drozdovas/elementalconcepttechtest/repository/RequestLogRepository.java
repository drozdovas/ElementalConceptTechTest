package com.drozdovas.elementalconcepttechtest.repository;

import com.drozdovas.elementalconcepttechtest.model.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
}
