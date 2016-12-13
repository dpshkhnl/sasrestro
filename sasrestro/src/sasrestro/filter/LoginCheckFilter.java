package sasrestro.filter;
 

 
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import sasrestro.model.user.User;
 
/**
 * Servlet Filter implementation class UserCheckFilter
 */
@WebFilter("/LoginCheckFilter")
public class LoginCheckFilter extends AbstractFilter implements Filter {
    private static List<String> allowedURIs;
 
    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        if(allowedURIs == null){
            allowedURIs = new ArrayList<String>();
            allowedURIs.add(fConfig.getInitParameter("loginActionURI"));
            allowedURIs.add("/sasrestro/");
            allowedURIs.add("/sasrestro/javax.faces.resource/css/main.css.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/fonts/preeti.ttf.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/theme.css.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/primefaces.js.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/primefaces.css.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/jquery/jquery.js.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/javascript/jscodes.js.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/messages/messages.png.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/ui-icons_2e83ff_256x240.png.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/ui-icons_38667f_256x240.png.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/ican_icon.png");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/icon.png.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/favicon.ico.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/clock/clock.css.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/clock/clock.js.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/jquery/jquery-plugins.js.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/primefaces.css.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/primefaces.js.xhtml");
            
            allowedURIs.add("/sasrestro/javax.faces.resource/images/ui-bg_inset-hard_100_fcfdfd_1x100.png.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/ui-bg_glass_45_0078ae_1x400.png.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/ui-bg_gloss-wave_75_2191c0_500x100.png.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/ui-bg_glass_75_79c9ec_1x400.png.xhtml");
            
            allowedURIs.add("/sasrestro/images/ican_icon.png");
            allowedURIs.add("/sasrestro/javax.faces.resource/css/demo.css");
            allowedURIs.add("/sasrestro/javax.faces.resource/css/animate-custom.css");
            allowedURIs.add("/sasrestro/javax.faces.resource/css/style.css");
            
            allowedURIs.add("/sasrestro/pages/public/login.xhtml");
            allowedURIs.add("/sasrestro/resources/css/style.css");
            allowedURIs.add("/sasrestro/resources/css/demo.css");
            allowedURIs.add("/sasrestro/resources/css/animate-custom.css");
            allowedURIs.add("/sasrestro/resources/images/icon.png");
            allowedURIs.add("/sasrestro/resources/images/bg.jpg");
            allowedURIs.add("/sasrestro/resources/images/button.png");
            allowedURIs.add("/sasrestro/images/button.png");
            
            allowedURIs.add("/sasrestro/resources/css/fonts/fontomas-webfont.woff");
            allowedURIs.add("/sasrestro/resources/css/fonts/BebasNeue-webfont.woff");
            allowedURIs.add("/sasrestro/resources/css/fonts/BebasNeue-webfont.ttf");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/button.png.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/images.jpg.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/brook.JPG.xhtml");
            
            allowedURIs.add("/sasrestro/javax.faces.resource/watermark/watermark.css.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/watermark/watermark.js.xhtml");
            allowedURIs.add("/sasrestro/ui-icon-login");
            allowedURIs.add("/sasrestro/javax.faces.resource/css/fonts/fontomas-webfont.ttf");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/ui-bg_glass_55_f8da4e_1x400.png.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/css/fonts/fontomas-webfont.woff");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/bg.jpg"); 
            allowedURIs.add("/sasrestro/javax.faces.resource/images/bg.jpg.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/css/demo.css.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/css/style.css.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/css/animate-custom.css.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/css/fonts/fontomas-webfont.svg");
            allowedURIs.add("/sasrestro/javax.faces.resource/images/button.png.xhtml");
            allowedURIs.add("/sasrestro/javax.faces.resource/jsf.js.xhtml");
            
        }
    }
 
    /**
     * @see Filter#destroy()
     */
    public void destroy() {
    }
 
    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
    	ServletContext servletContext = req.getSession().getServletContext();
 
        if (session.isNew()) {
            doLogin(request, response, req);
            return;
        }
 
        User user = (User) session.getAttribute("user");
        String testValue=req.getRequestURI();
        String checkString=null;
        if(testValue.contains(";"))
        {
        	checkString=testValue.substring(0,req.getRequestURI().indexOf(";"));
        }
        else
        {
        	checkString=testValue;
        }
        if (user == null && !allowedURIs.contains(checkString)) {
            System.out.println("The requested stuff is :"+checkString);
            doLogin(request, response, req);
            return;
        }
        
        if (user != null && user.getDayInStatus() == null && !allowedURIs.contains(checkString)) {
            System.out.println("The requested stuff is :"+checkString);
            doLogin(request, response, req);
            return;
        }
        
        User userAtt = new User();
        if (user!=null){
		userAtt = (User)servletContext.getAttribute("user-"+user.getId());
		if ( userAtt == null  && !allowedURIs.contains(checkString))
		{
			  doLogin(request, response, req);
	            return;
		}
        }
		/* Added by Dipesh 
         * for the page fitering of final exam Roll Code Generation so
         *  that the roll code generation page can only be viewd
        	only after inputting the two user user1 and user2
        */
        User user1 = (User) session.getAttribute("user1");
        User user2 = (User) session.getAttribute("user2");
        if (user1 == null && user2 == null && req.getRequestURI().endsWith("/RollCodeGeneration.xhtml"))
        {
        	accessDenied(request, response, req);
        }
        
        //Upto here
        chain.doFilter(request, response);
    }
}