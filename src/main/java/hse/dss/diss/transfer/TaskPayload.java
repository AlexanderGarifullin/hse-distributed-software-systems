package hse.dss.diss.transfer;

/**
 * TaskPayload represents the data transferred when creating or updating a task.
 */
public record TaskPayload(
        Long taskId,
        String name,
        String statement
) {
}
