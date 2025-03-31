package hse.dss.diss.mapper;

import hse.dss.diss.entity.Task;
import hse.dss.diss.transfer.TaskPayload;
import org.springframework.stereotype.Component;


/**
 * TaskMapper is responsible for converting TaskPayload into Task entities.
 */

@Component
public class TaskMapper {

    /**
     * Converts a TaskPayload to a Task entity.
     *
     * @param payload the task payload containing task data
     * @return the corresponding Task entity
     */
    public Task payloadToEntity(TaskPayload payload) {
        return Task.builder()
                .id(payload.taskId())
                .name(payload.name())
                .statement(payload.statement())
                .build();
    }
}
