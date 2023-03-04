package sample.b.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sample.b.service.ErrTestService;

@Slf4j
@Service
public class ErrTestServiceImpl implements ErrTestService {
    @Override
    public String serviceMethod() {
        log.info("일반 처리하는 서비스");
        return "serviceMethod";
    }

    @Override
    public String serviceExceptionMethod() {
        log.info("Exception 발생하는 Case");
        if (true) {
            throw new IllegalStateException("오류 태스트");
        }
        return "serviceExceptionMethod";
    }
}
