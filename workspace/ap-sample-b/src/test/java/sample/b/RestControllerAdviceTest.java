package sample.b;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import sample.b.controller.ErrorTestRestController;
import sample.b.controller.dto.Person;
import sample.b.service.impl.ErrTestServiceImpl;
import skmsa.apiutil.controller.error.ExceptionAdvice;


/**
 * 2022-10-17
 * 이 부분이 테스트가 정상적으로 처리되지 않고 있음
 * ==> Controller에서 Exception이 발생하면   "ExceptionHandler" 처리에 의하여 다른것으로 변경되어야 하는데....
 * Exception Handler를 로그 off 했음
 */

@Slf4j
@Import({ErrTestServiceImpl.class, ErrorTestRestController.class, ExceptionAdvice.class})
@SpringBootTest(classes = {SampleBApplication.class})
@SpringBootConfiguration
@ActiveProfiles("local")
public class RestControllerAdviceTest {
    @Autowired
    ErrorTestRestController errorTestRestController;

    @Test
    void aopHelloTest() {
        log.debug("start:   Retry 테스트 ");
        errorTestRestController.hello();
//        assertEquals("Myinno", result);
    }


    @Test
    void aopServiceExceptionTest1() {
        log.debug("start:   Retry 테스트 ");
        Person person = new Person();
        person.setAge(10);
        person.setName("Test");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("127.0.0.1:8080");
        request.setRequestURI("/sampleb");

        Assertions.assertThrows(IllegalStateException.class,
                (Executable) errorTestRestController.serviceException(person, request));
        /* assertEquals("Myinno", result); */
    }

    @Test
    void aopControllerExceptionTest() {
        log.debug("start:   Retry 테스트 ");
        Person person = new Person();
        person.setAge(10);
        person.setName("Test");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("127.0.0.1:8080");
        request.setRequestURI("/sampleb");

        Assertions.assertThrows(NullPointerException.class,
                (Executable) errorTestRestController.controllerException(person, request));    }
}
