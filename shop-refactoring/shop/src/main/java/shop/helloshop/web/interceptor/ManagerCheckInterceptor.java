package shop.helloshop.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import shop.helloshop.domain.entity.MemberGrade;
import shop.helloshop.web.dto.MemberSessionDto;
import shop.helloshop.web.dto.SessionKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ManagerCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        MemberSessionDto member = (MemberSessionDto) session.getAttribute(SessionKey.LOGIN_MEMBER);

        if (member.getMemberGrade() == MemberGrade.BASIC){
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
