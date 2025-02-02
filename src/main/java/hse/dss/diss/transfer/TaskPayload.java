package hse.dss.diss.transfer;

import lombok.Builder;

/**
 * TaskPayload represents the data transferred when creating or updating a task.
 */
@Builder
public record TaskPayload(
        Long taskId,
        String name,
        String statement
) {
}
