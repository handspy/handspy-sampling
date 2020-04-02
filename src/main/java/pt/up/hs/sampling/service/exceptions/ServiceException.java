package pt.up.hs.sampling.service.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import pt.up.hs.sampling.web.rest.errors.ProblemWithMessageException;
import pt.up.hs.sampling.web.rest.errors.ProblemWithoutMessageException;

/**
 * Exception thrown by service.
 */
public class ServiceException extends RuntimeException {

    private String entityName = null;
    private String errorKey = null;
    private Status errorStatus = Status.INTERNAL_SERVER_ERROR;

    public ServiceException() {
    }

    public ServiceException(String entityName, String errorKey, String message) {
        super(message);
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public ServiceException(Status errorStatus, String entityName, String errorKey, String message) {
        super(message);
        this.entityName = entityName;
        this.errorKey = errorKey;
        this.errorStatus = errorStatus;
    }

    public String getEntityName() {
        return entityName;
    }

    public Status getErrorStatus() {
        return errorStatus;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
