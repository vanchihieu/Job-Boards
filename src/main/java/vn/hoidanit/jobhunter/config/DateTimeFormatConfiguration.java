package vn.hoidanit.jobhunter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configure the converters to use the ISO format for dates by default.
 */
@Configuration
public class DateTimeFormatConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar(); // DateTimeFormatterRegistrar là một class giúp chúng ta cấu hình format cho các kiểu dữ liệu ngày tháng
        registrar.setUseIsoFormat(true); // setUseIsoFormat(true) để sử dụng định dạng ISO cho ngày tháng
        registrar.registerFormatters(registry);
    }
}
