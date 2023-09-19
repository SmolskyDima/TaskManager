package by.itacademy.userservice.core.exceptions;

import org.springframework.dao.DataAccessException;

public class FindEntityException extends DataAccessException {


    public FindEntityException(String msg) {
        super(msg);
    }

    public FindEntityException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
