# API Documentation

This document provides an overview of the available endpoints in the API,
their purposes, and the corresponding HTTP methods.

## TaskController

The `TaskController` manages HTTP requests related to tasks.
It provides endpoints for creating, updating, retrieving, and deleting tasks.

### Endpoints

1. **Get Tasks by User ID**
    - **Endpoint:** `GET /tasks/user/{userId}`
    - **Description:** Retrieves all tasks associated with a specific user.
    - **Parameters:**
        - `userId` (Path): The ID of the user whose tasks are to be fetched.
    - **Returns:** A list of tasks for the specified user.

2. **Get Task by ID**
    - **Endpoint:** `GET /tasks/{taskId}`
    - **Description:** Retrieves a specific task by its ID.
    - **Parameters:**
        - `taskId` (Path): The ID of the task to retrieve.
    - **Returns:** The task with the specified ID.

3. **Create Task**
    - **Endpoint:** `POST /tasks/user/{userId}`
    - **Description:** Creates a new task for a specific user.
    - **Parameters:**
        - `userId` (Path): The ID of the user for whom the task is being created.
        - `task` (Body): The task payload containing task details.
    - **Returns:** The created task.

4. **Update Task**
    - **Endpoint:** `PUT /tasks/{taskId}`
    - **Description:** Updates an existing task.
    - **Parameters:**
        - `taskId` (Path): The ID of the task to update.
        - `task` (Body): The task payload containing updated task details.
    - **Returns:** The updated task.

5. **Delete Task**
    - **Endpoint:** `DELETE /tasks/{taskId}`
    - **Description:** Deletes a task by its ID.
    - **Parameters:**
        - `taskId` (Path): The ID of the task to delete.
    - **Returns:** A response indicating the task was deleted.

## TestController

The `TestController` handles HTTP requests related to tests.
It provides an endpoint for fetching a test related to a specific task.

### Endpoints

1. **Generate Test by Task ID**
    - **Endpoint:** `GET /tests/task/{taskId}`
    - **Description:** Generates a test for a specific task.
    - **Parameters:**
        - `taskId` (Path): The ID of the task for which to generate a test.
    - **Returns:** The generated test as a String.
---

## Storage and Initial Data

- **Storage:** The application uses an **InMemoryStorage** for storing tasks and user data. This means that all data is stored in memory and will be lost when the application is restarted.

- **Starter User:** When the application starts, a default user is created with the following details:
  ```java
  private User createStarterUser() {
      return User.builder()
              .id(1L)
              .login("STARTER")
              .build();
  }
    ```
---

This README provides a concise overview of the available endpoints and their functionalities. 
For more detailed information, refer to the respective controller and service implementations.