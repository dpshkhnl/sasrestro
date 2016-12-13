package sasrestro.mb.user;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import sasrestro.misc.AbstractMB;
import sasrestro.model.user.User;
import sasrestro.model.user.UserLoginInfo;
import sasrestro.model.util.DayInOutStatusModel;
import sasrestro.sessionejb.user.LoginInfoEJB;
import sasrestro.sessionejb.user.UserEJB;
import sasrestro.sessionejb.util.DayInOutEJB;
import sasrestro.sessionejb.util.FiscalYrEJB;

/**
 * @author nebula
 *
 */
@ViewScoped
@ManagedBean
public class LoginMB extends AbstractMB implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = UserMB.INJECTION_NAME)
	private UserMB userMB;

	@EJB
	UserEJB userEJB;
	@EJB
	private FiscalYrEJB fiscalYrEJB;

	@EJB
	private DayInOutEJB dayInOutEJB;

	@EJB
	private LoginInfoEJB loginInfoEJB;

	private String email;
	private String password;
	private User currentUser;
	private boolean chkUser;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String login() throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		List<User> userList = userEJB.listAll();
		if (userList == null) {
			// displayErrorMessageToUser("Database is unreachable. Please, Contact your system admin for assistance.");
			return "/dberror.xhtml";
		} else {
			if (userList.size() <= 0) {
				displayInfoMessageToUser("There are no users in the system. Please, Contact your system admin for assistance.");
				return null;
			} else {
				User user = userEJB.isValidLogin(email, password);
				if (user != null && user.getStatus().ordinal() == 1) {
					DayInOutStatusModel dayInStatus = new DayInOutStatusModel();
					dayInStatus = dayInOutEJB.getActiveWorkDay();

					FacesContext context = FacesContext.getCurrentInstance();
					HttpServletRequest request = (HttpServletRequest) context
							.getExternalContext().getRequest();
					request.getSession().setAttribute("user", user);

					if (dayInOutEJB.getActiveWorkDay() == null) {
						userMB.setUser(user);
						RequestContext rc = RequestContext.getCurrentInstance();
						rc.execute("dayInDialog.show()");
						return null;
					} else {

						ServletContext servletContext = request.getSession()
								.getServletContext();
						User att = (User) servletContext.getAttribute("user-"
								+ user.getId());
						if (att == null || att.getId() == 0) {
							String hostname = getUserIpAddr(request);
							System.out.println(hostname);
							servletContext.setAttribute("user-" + user.getId(),
									user);
							servletContext.setAttribute(
									"userIP-" + user.getId(), hostname);
							User attribute = (User) servletContext
									.getAttribute("user-" + user.getId());
							System.out.println(attribute.getName());
						} else {
							if (!chkUser) {
								chkUser = true;
								displayInfoMessageToUser("User is already Logged In in:"
										+ servletContext.getAttribute("userIP-"
												+ user.getId())
										+ " . Please contact System Admin for further assistance");
								return null;
							}
						}
						// request.getSession().setAttribute("user", arg1);
						if (loginInfoEJB.getActiveSessionByUser(user.getId()) != null) {
							UserLoginInfo loginInfo = new UserLoginInfo();
							loginInfo = loginInfoEJB
									.getActiveSessionByUser(user.getId());
							loginInfo.setLogoutTime(new Date());
							loginInfo.setUpdatedBy(user.getId());
							loginInfo.setUpdatedDateAD(new Date());
							loginInfo.setRemarks("Log out during next login");
							loginInfoEJB.update(loginInfo);
						}
						UserLoginInfo loginInfo = new UserLoginInfo();
						loginInfo.setLoginTime(new Date());
						loginInfo.setCreatedBy(user.getId());
						loginInfo.setUserId(user);
						loginInfo.setCreatedDateAD(new Date());
						loginInfo.setRemarks("login");
						loginInfoEJB.save(loginInfo);

						user.setDayInStatus(dayInStatus);
						userMB.setUser(user);

						String page = "/pages/restaurant/main.xhtml?faces-redirect=true";
						return page;

					}
				}
				displayErrorMessageToUser("Check your email/password. May be your account is inactive!");

			}
		}
		return null;
	}

	public void forceLogin() throws NoSuchAlgorithmException,
			InvalidKeySpecException {

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		ServletContext servletContext = request.getSession()
				.getServletContext();
		servletContext.removeAttribute("user-" + userMB.getUser().getId());
		servletContext.removeAttribute("userIP-" + userMB.getUser().getId());
		UserLoginInfo newloginInfo = new UserLoginInfo();
		newloginInfo = loginInfoEJB.getActiveSessionByUser(userMB.getUser()
				.getId());
		newloginInfo.setLogoutTime(new Date());
		newloginInfo.setUpdatedBy(userMB.getUser().getId());
		newloginInfo.setUpdatedDateAD(new Date());
		newloginInfo.setRemarks("Force logout");
		loginInfoEJB.update(newloginInfo);
		displayInfoMessageToUser("User " + userMB.getUser().getEmail()
				+ " has been logged out successfully");
		// login();

	}

	public String getUserIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public void setUserMB(UserMB userMB) {
		this.userMB = userMB;
	}

	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		ServletContext servletContext = request.getSession()
				.getServletContext();

		servletContext.removeAttribute("user-" + userMB.getUser().getId());
		servletContext.removeAttribute("userIP-" + userMB.getUser().getId());

		User user = new User();
		user = userMB.getUser();
		UserLoginInfo loginInfo = new UserLoginInfo();
		loginInfo = loginInfoEJB.getActiveSessionByUser(user.getId());
		loginInfo.setLogoutTime(new Date());
		loginInfo.setUpdatedBy(user.getId());
		loginInfo.setUpdatedDateAD(new Date());
		loginInfo.setRemarks("Session Expire logout");
		loginInfoEJB.update(loginInfo);
		System.out.println("Current View name=" + request.getRemoteUser());
		request.getSession().invalidate();
		return "/login.xhtml?faces-redirect=true";

	}

	public void forceLogOut() {
		User user = new User();
		user = userMB.getUser();
		if (currentUser.getId() == user.getId()) {
			displayErrorMessageToUser("This user cannot be force Log Out");
			return;
		}
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		ServletContext servletContext = request.getSession()
				.getServletContext();

		servletContext.removeAttribute("user-" + currentUser.getId());
		servletContext.removeAttribute("userIP-" + currentUser.getId());
		UserLoginInfo loginInfo = new UserLoginInfo();
		loginInfo = loginInfoEJB.getActiveSessionByUser(currentUser.getId());
		loginInfo.setLogoutTime(new Date());
		loginInfo.setUpdatedBy(currentUser.getId());
		loginInfo.setUpdatedDateAD(new Date());
		loginInfo.setRemarks("Force logout");
		loginInfoEJB.update(loginInfo);
		displayInfoMessageToUser("User " + currentUser.getEmail()
				+ " has been logged out successfully");
	}

	public User getCurrentUser() {
		if (currentUser == null)
			currentUser = new User();
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public boolean isChkUser() {
		return chkUser;
	}

	public void setChkUser(boolean chkUser) {
		this.chkUser = chkUser;
	}

	public void reset() {
		chkUser = false;
	}

}
