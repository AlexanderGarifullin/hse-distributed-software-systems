package hse.dss.diss.repository.storage;

import hse.dss.diss.entity.Task;

import java.util.List;
import java.util.stream.Collectors;

/**
 * An in-memory storage for Task entities, extending HashMapStorage.
 * Provides additional functionality to find tasks by user ID.
 */
public class TaskInMemoryStorage extends HashMapStorage<Task, Long> {

    /**
     * Retrieves a list of tasks assigned to a specific user.
     *
     * @param userId The ID of the user whose tasks should be retrieved.
     * @return A list of tasks belonging to the specified user.
     */
    public List<Task> findByUserId(Long userId) {
        return storage.values().stream()
                .filter(task -> task.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
