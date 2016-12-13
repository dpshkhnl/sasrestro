package sasrestro.mb.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import sasrestro.mb.user.LoginMB;
import sasrestro.mb.user.UserMB;
import sasrestro.misc.AbstractMB;
import sasrestro.misc.DirectSqlUtils;
import sasrestro.misc.JCalendarFunctions;
import sasrestro.model.user.User;
import sasrestro.model.user.UserLoginInfo;
import sasrestro.model.util.DayInOutStatusModel;
import sasrestro.sessionejb.user.LoginInfoEJB;
import sasrestro.sessionejb.util.DayInOutEJB;

@ViewScoped
@ManagedBean(name = "dayInOutMB")
public class DayInOutMB extends AbstractMB implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = UserMB.INJECTION_NAME)
	private UserMB userMB;

	@EJB
	DayInOutEJB dayInOutEJB;
	@EJB
	private LoginInfoEJB loginInfoEJB;

	public UserMB getUserMB() {
		return userMB;
	}

	public void setUserMB(UserMB userMB) {
		this.userMB = userMB;
	}

	private DayInOutStatusModel dayInOutModel;
	private DayInOutStatusModel previousDayInOutModel;

	private List<User> lstActiveUser;

	Date today;
	String todayNp;
	private boolean warn = false;

	public boolean isWarn() {
		return warn;
	}

	public void setWarn(boolean warn) {
		this.warn = warn;
	}

	public String getTodayNp() {
		return todayNp;
	}

	public void setTodayNp(String todayNp) {
		this.todayNp = todayNp;
	}

	public Date getToday() {
		today = new Date();

		updateDateTodayToBS();
		loadPreviousDay();
		return today;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	private JCalendarFunctions jcal = new JCalendarFunctions();

	public DayInOutStatusModel getDayInOutModel() {
		if (dayInOutModel == null)
			dayInOutModel = new DayInOutStatusModel();
		return dayInOutModel;
	}

	public void setDayInOutModel(DayInOutStatusModel dayInOutModel) {
		this.dayInOutModel = dayInOutModel;
	}

	public void updateDateInToBS() {
		if (dayInOutModel.getDayInDateEn() != null)
			dayInOutModel.setDayInDateNp(jcal
					.GetNepaliDate(new SimpleDateFormat("yyyy-MM-dd")
							.format(dayInOutModel.getDayInDateEn())));

	}

	public void updateDateInToAD() {
		Date d = null;
		if (dayInOutModel.getDayInDateNp() != null
				&& dayInOutModel.getDayInDateNp() != "")

		{
			d = jcal.GetEnglishDate(dayInOutModel.getDayInDateNp());
			dayInOutModel.setDayInDateEn(d);
		}
	}

	public void updateDateTodayToBS() {
		if (today != null)
			setTodayNp(jcal.GetNepaliDate(new SimpleDateFormat("yyyy-MM-dd")
					.format(today)));

	}

	public void updateDateTodayToAD() {
		Date d = null;
		if (todayNp != null && todayNp != "")

		{
			d = jcal.GetEnglishDate(todayNp);
			setToday(d);
		}
	}

	public void saveDayIn() throws IOException {

		if (!(dayInOutModel.getDayInDateEn().after(previousDayInOutModel
				.getTransDateEn()))) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dt = sdf.format(previousDayInOutModel.getTransDateEn());
			displayErrorMessageToUser("Select Date after  " + dt);
			return;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(dayInOutModel.getDayInDateEn());
		// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		// String dayBefore = dateFormat.format(cal.getTime());

		try {
			dayInOutModel.setCreatedBy(userMB.getUser().getId());
			dayInOutModel.setCreatedDateAD(new Date());
			dayInOutModel.setStatus(0);
			dayInOutModel.setTransDateEn(dayInOutModel.getDayInDateEn());
			dayInOutModel.setTransDateNp(dayInOutModel.getDayInDateNp());
			dayInOutEJB.save(dayInOutModel);
			displayInfoMessageToUser("Day in  Successfull");
			userMB.getUser().setDayInStatus(dayInOutModel);

			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context
					.getExternalContext().getRequest();
			request.getSession().setAttribute("user", userMB.getUser());
			ServletContext servletContext = request.getSession()
					.getServletContext();
			LoginMB login = new LoginMB();
			String hostname = login.getUserIpAddr(request);
			servletContext.setAttribute("user-" + userMB.getUser().getId(),
					userMB.getUser());
			servletContext.setAttribute("userIP-" + userMB.getUser().getId(),
					hostname);

			UserLoginInfo loginInfo = new UserLoginInfo();
			loginInfo.setLoginTime(new Date());
			loginInfo.setCreatedBy(userMB.getUser().getId());
			loginInfo.setUserId(userMB.getUser());
			loginInfo.setCreatedDateAD(new Date());
			loginInfo.setRemarks("login");
			loginInfoEJB.save(loginInfo);

			ExternalContext ec = FacesContext.getCurrentInstance()
					.getExternalContext();
			ec.redirect(ec.getRequestContextPath()
					+ "/pages/protected/index.xhtml?faces-redirect=true");
		} catch (Exception ex) {
			displayErrorMessageToUser("Error ");
		}
	}

	public void backUp() {
		// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// Date date = new Date();
		// System.out.println(); // 2014/08/06 15:59:48
		createBackupDayOut("E:\\Backup_Database\\", "sasrestro", userMB
				.getUser().getDayInStatus().getDayInDateNp());
	}

	public void performDayOut() throws IOException {
		if (hasLoggedInuser()) {
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("activeUserWidget.show();");
			displayErrorMessageToUser("All user must be logged out");
			return;
		}
		backUp();
		dayInOutModel = new DayInOutStatusModel();
		dayInOutModel = userMB.getUser().getDayInStatus();
		if (dayInOutModel.getStatus() == 0) {
			setToday(new Date());
			dayInOutModel.setUpdatedBy(userMB.getUser().getId());
			dayInOutModel.setUpdatedDateAD(new Date());
			dayInOutModel.setStatus(1);
			dayInOutModel.setDayOutDateEn(getToday());
			dayInOutModel.setDayOutDateNp(getTodayNp());
			dayInOutEJB.update(dayInOutModel);
			displayInfoMessageToUser("Partial Day Out  Successfully performed! you will be logged Out");
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context
					.getExternalContext().getRequest();
			ServletContext servletContext = request.getSession()
					.getServletContext();

			servletContext.removeAttribute("user-" + userMB.getUser().getId());
			servletContext
					.removeAttribute("userIP-" + userMB.getUser().getId());

			User user = new User();
			user = userMB.getUser();
			UserLoginInfo loginInfo = new UserLoginInfo();
			loginInfo = loginInfoEJB.getActiveSessionByUser(user.getId());
			loginInfo.setLogoutTime(new Date());
			loginInfo.setUpdatedBy(user.getId());
			loginInfo.setUpdatedDateAD(new Date());
			loginInfo.setRemarks("Day End logout");
			loginInfoEJB.update(loginInfo);

			getRequest().getSession().invalidate();

			ExternalContext ec = FacesContext.getCurrentInstance()
					.getExternalContext();
			ec.redirect(ec.getRequestContextPath()
					+ "/pages/public/login.xhtml");
		}

	}

	private HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}

	public void loadPreviousDay() {
		getPreviousDayInOutModel();
		setPreviousDayInOutModel(dayInOutEJB.getPreviousDay());

	}

	public DayInOutStatusModel getPreviousDayInOutModel() {
		if (previousDayInOutModel == null)
			previousDayInOutModel = new DayInOutStatusModel();

		return previousDayInOutModel;
	}

	public void setPreviousDayInOutModel(
			DayInOutStatusModel previousDayInOutModel) {
		this.previousDayInOutModel = previousDayInOutModel;
	}

	private boolean hasLoggedInuser() {
		boolean hasUser = false;
		Long countUser = DirectSqlUtils
				.getSingleResult("SELECT COUNT(*) cnt FROM user_login_info WHERE user_id !="
						+ userMB.getUser().getId()
						+ " AND ISNULL(logout_time) = 0");
		if (countUser > 0)
			hasUser = true;
		return hasUser;
	}

	public List<User> getLstActiveUser() {
		lstActiveUser = new ArrayList<User>();
		List<UserLoginInfo> lstUserLogin = new ArrayList<UserLoginInfo>();
		lstUserLogin = loginInfoEJB.getAllActiveSession();
		for (UserLoginInfo loggedIn : lstUserLogin) {
			User user = new User();
			user = loggedIn.getUserId();
			lstActiveUser.add(user);
		}
		return lstActiveUser;
	}

	public void setLstActiveUser(List<User> lstActiveUser) {
		this.lstActiveUser = lstActiveUser;
	}

	public void resetAll() {
		warn = false;
	}

	public boolean createBackupDayOut(String backUpFileName, String dbSource,
			String dateNow) {
		boolean flag = false;
		/*
		 * check whether the path exists or not. if the path does not exist,
		 * system creates it first and after creation of path the backup will be
		 * created. if the path already exists, system simply creates the backup
		 * in it.
		 */
		String x = backUpFileName
				.substring(0, backUpFileName.lastIndexOf("\\"));
		if (!(new File(x).isDirectory())) {
			new File(x).mkdirs();
		}
		String sqlQry = " BACKUP DATABASE "
				+ dbSource
				+ " TO  "
				+ " DISK = N'"
				+ backUpFileName
				+ dateNow
				+ ".bak' WITH NOFORMAT, NOINIT "
				+ " SELECT Database_Name,"
				+ " CONVERT( SmallDateTime , MAX(Backup_Finish_Date)) as Last_Backup, "
				+ " DATEDIFF(d, MAX(Backup_Finish_Date), Getdate()) as Days_Since_Last"
				+ " FROM MSDB.dbo.BackupSet"
				+ " WHERE Type = 'd' and Database_Name like 'sasrestro'"
				+ " GROUP BY Database_Name ";

		/* try { */
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("sasrestroPU");
		EntityManager em = emf.createEntityManager();
		em.createNativeQuery(sqlQry).getSingleResult();

		// DirectSqlUtils.getQueryExecuted(sqlQry);

		displayInfoMessageToUser("Database back Up created at location "
				+ backUpFileName);

		return flag;

	}

}
