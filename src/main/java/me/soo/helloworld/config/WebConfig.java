package me.soo.helloworld.config;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.util.CheckLoginInterceptor;
import me.soo.helloworld.util.CurrentUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CurrentUserArgumentResolver currentUserArgumentResolver;

    private final CheckLoginInterceptor checkLoginInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger**", "/webjars/springfox-swagger-ui/**");
    }
}
