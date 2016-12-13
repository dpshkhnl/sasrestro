package sasrestro.filter;
 
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import sasrestro.model.user.User;
 
public class DefaultUserPagesFilter extends AbstractFilter implements Filter {
 
    public void destroy() {
 
    }
 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        User user = (User) req.getSession(true).getAttribute("user");
 
        if(!user.isUser() && !user.isAdmin()){
            accessDenied(request, response, req);
            return;
        }
 
        chain.doFilter(request, response);
    }
 
    public void init(FilterConfig arg0) throws ServletException {
 
    }
}