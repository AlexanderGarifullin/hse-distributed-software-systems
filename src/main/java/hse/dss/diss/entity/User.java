package hse.dss.diss.entity;

import lombok.Builder;
import lombok.Data;

/**
 * User represents a user entity in the system.
 * It contains details such as the user ID and login.
 */
@Builder
@Data
public class User {
    private Long id;
    private String login;
}
