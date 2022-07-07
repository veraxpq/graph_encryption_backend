package graph_encryption;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
@EnableScheduling
@MapperScan("graph_encryption.domain.client")
public class MlApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(MlApplication.class, args);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }
}