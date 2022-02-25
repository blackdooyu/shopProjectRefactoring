package shop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shop.helloshop.web.argumentresolver.LoginAr;
import shop.helloshop.web.interceptor.LoginCheckInterceptor;
import shop.helloshop.web.interceptor.ManagerCheckInterceptor;
import shop.helloshop.web.interceptor.MemberUpdateInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginAr());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/member/update/**","/item/**","/shopCart/delete","/order/**","/manager/**")
                .excludePathPatterns("/item/view/**","/item/list/**");

        registry.addInterceptor(new ManagerCheckInterceptor())
                .order(2)
                .addPathPatterns("/manager/**","/item/**")
                .excludePathPatterns("/item/view/**","/item/list/**");

        registry.addInterceptor(new MemberUpdateInterceptor())
                .order(3)
                .addPathPatterns("/member/update");
    }
}
