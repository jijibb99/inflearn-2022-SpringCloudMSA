package sample.b.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.b.controller.dto.Person;
import sample.b.service.ErrTestService;
import sample.b.service.impl.ErrTestServiceImpl;
import skmsa.apiutil.controller.SKMSAController;
import skmsa.apiutil.controller.error.BizException;
import skmsa.apiutil.controller.error.constants.BizErrCode;

import javax.servlet.ServletRequest;

/**
 * Controller에서 발생하는 오류를 일괄적으로 테스트
 * - Test로 이동하면 Swagger로 테스트가 어려워서 여기에 유지함
 */
@Tag(name = "ExceptionTest", description = "Exception")
@RestController
@RequestMapping("/errortest")
public class ErrorTestRestController extends SKMSAController {
    private final ErrTestService myService;

    public ErrorTestRestController(ErrTestServiceImpl myService) {
        this.myService = myService;
    }

    @Tag(name = "ExceptionTest", description = "정상리턴")
    @GetMapping("/hello")
    public String hello() {
        log().debug("@GetMapping(\"/hello\")");
        return "hello";//문자열 반환
    }

    @Tag(name = "ExceptionTest", description = "object 반환")
    @GetMapping("/myData")
    public MyData myData() {
        log().debug(" @GetMapping(\"/myData\")");
        return new MyData("myName");//object 반환
    }

    @Tag(name = "ExceptionTest", description = "일반적인 service호출")
    @GetMapping("/service")
    public String serviceCall() {
        log().debug(" @GetMapping(\"/service\")");
        return myService.serviceMethod();//일반적인 service호출
    }

    @Tag(name = "ExceptionTest", description = "호출한 서비스 모드에서 Exception 발생하고, 별도의 Try~ catch가 없는 Case")
    @GetMapping("/serviceException")
    //GetMapping에서 객체로  입력 받는 경우 @ModelAttribute 사용
    // 입력은 "?age=10&name=serviceException"
    public ResponseEntity<String> serviceException(@ModelAttribute Person person, ServletRequest request) {
        log().error("호출한 서비스 모드에서 Exception 발생하고, 별도의 Try~ catch가 없는 Case");
        request.setAttribute("_IN", person);
        Object obj = myService.serviceExceptionMethod();
        return ResponseEntity.ok().body(obj.toString()); //service에서 예외발생
    }

    @Tag(name = "ExceptionTest", description = "controller에서 예외발생")
    @PostMapping("/controllerException")
    public ResponseEntity<Void> controllerException(@RequestBody Person person, ServletRequest request) {
        request.setAttribute("_IN", person);
        log().debug("controllerException-1 Person:", person);
        throw new NullPointerException();//controller에서 예외발생
    }

    @Tag(name = "ExceptionTest", description = "custom예외 발생")
    @GetMapping("/bizException")
    public String custom(@RequestParam String name, ServletRequest request) {
        request.setAttribute("_IN", name);
        throw new BizException(BizErrCode.H400_INVALID_PARAMETER);//custom예외 발생
    }


    //임의의 데이터 형식
    static class MyData {
        private final String name;

        MyData(String name) {
            this.name = name;
        }
    }
}


