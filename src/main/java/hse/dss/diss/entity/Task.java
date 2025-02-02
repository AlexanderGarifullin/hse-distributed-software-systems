package hse.dss.diss.entity;

import lombok.Builder;
import lombok.Data;


/**
 * Task represents a task entity in the system.
 * It contains details such as task ID, name, statement, and user ID.
 */
@Builder
@Data
public class Task {
    private Long id;
    private String name;
    private String statement;
    private Long userId;
}
