package by.itacademy.userservice.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyService {

    public void doSomething() {
        log.info("This is an informational message.");
        log.error("This is an error message.");
    }
}
