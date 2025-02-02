package hse.dss.diss.exception;

/**
 * EntityAlreadyExistsException is thrown when an entity already exists in the system.
 */
public class EntityAlreadyExistsException extends RuntimeException {
  public EntityAlreadyExistsException(String message) {
    super(message);
  }
}
