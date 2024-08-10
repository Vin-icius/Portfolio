package dev.vinicius.portfolio.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    private final AccessFilter accessFilter;

    @Autowired
    public FilterConfiguration(AccessFilter accessFilter) {
        this.accessFilter = accessFilter;
    }

    @Bean
    public FilterRegistrationBean<AccessFilter> registrationBean() {
        FilterRegistrationBean<AccessFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(accessFilter);
        registrationBean.addUrlPatterns("/apis/access/register");
        return registrationBean;
    }
}
