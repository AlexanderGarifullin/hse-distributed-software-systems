package hse.dss.repository;

import hse.dss.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findByTaskId(Long taskId);
}