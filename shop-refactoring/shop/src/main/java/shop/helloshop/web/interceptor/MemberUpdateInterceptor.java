package shop.helloshop.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import shop.helloshop.web.dto.SessionKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MemberUpdateInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Object check = session.getAttribute(SessionKey.MEMBER_UPDATE_CHECK);

        if (check == null){
            response.sendRedirect("/member/update/check");
            return false;
        }

        return true;
    }

}
