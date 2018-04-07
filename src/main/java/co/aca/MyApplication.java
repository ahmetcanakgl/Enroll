package co.aca;

import co.aca.config.JwtFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class MyApplication {

    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/api/*");

        return registrationBean;
    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String homepage() {
        return "home";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error() {
        return "error";
    }

    @RequestMapping(value = "/enroll", method = RequestMethod.GET)
    public String enroll() {
        return "enroll";
    }

    @RequestMapping(value = "/tos", method = RequestMethod.GET)
    public String tos() {
        return "tos";
    }

    @RequestMapping(value = "/tos_en", method = RequestMethod.GET)
    public String tosEn() {
        return "tos_en";
    }

    @RequestMapping(value = "/tos_tr", method = RequestMethod.GET)
    public String tosTR() {
        return "tos_tr";
    }

    @RequestMapping(value = "/googleaf6f9dbfec23e3da", method = RequestMethod.GET)
    public String googleVerif() {
        return "googleaf6f9dbfec23e3da";
    }
}            