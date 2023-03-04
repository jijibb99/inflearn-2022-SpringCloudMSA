package sample.a.common;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import sample.a.SampleAApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = { SampleAApplication.class })
public class CucumberSpingConfiguration {}
