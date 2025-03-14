package nextstep.subway.config;

import java.io.File;
import javax.servlet.Filter;
import nextstep.subway.support.version.SubwayVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    public static final String PREFIX_STATIC_RESOURCES = "/resources";

    @Autowired
    private SubwayVersion version;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 모든 정적 자원에 대해 no-cache, private 설정
        registry.addResourceHandler(PREFIX_STATIC_RESOURCES + File.separator + version.getVersion() + "/static/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCacheControl(CacheControl.noCache().cachePrivate());

        // css : max-age 1년 설정
        registry.addResourceHandler(PREFIX_STATIC_RESOURCES + File.separator + version.getVersion() + "/static/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCachePeriod(60 * 60 * 24 * 365);

        // js : no-cache, private 설정
        registry.addResourceHandler(PREFIX_STATIC_RESOURCES + File.separator + version.getVersion() + "/static/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCacheControl(CacheControl.noCache().cachePrivate());

    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter etagHeaderFilter = new ShallowEtagHeaderFilter();
        registration.setFilter(etagHeaderFilter);
        registration.addUrlPatterns(PREFIX_STATIC_RESOURCES + "/*");
        return registration;
    }
}
