package gnb.finseta.backend.rest.config;

import gnb.finseta.backend.logging.IRequestLogger;
import gnb.finseta.backend.rest.interceptors.RequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	IRequestLogger logger;
	@Bean
	public RequestInterceptor requestInterceptor() {
		return new RequestInterceptor(logger);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestInterceptor()).addPathPatterns("/**");
	}
}
