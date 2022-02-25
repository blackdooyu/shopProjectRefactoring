package shop.helloshop.web.argumentresolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shop.helloshop.web.dto.MemberSessionDto;
import shop.helloshop.web.dto.SessionKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginAr implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean check = parameter.hasParameterAnnotation(Login.class);
        boolean typeCheck = MemberSessionDto.class.isAssignableFrom(parameter.getParameterType());

        return check && typeCheck;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object memberSessionDto = session.getAttribute(SessionKey.LOGIN_MEMBER);

        return memberSessionDto;
    }
}
