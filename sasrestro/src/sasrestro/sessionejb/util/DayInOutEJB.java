package sasrestro.sessionejb.util;

import java.io.Serializable;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sasrestro.model.util.DayInOutStatusModel;
import sasrestro.sessionejb.core.GenericDAO;
	@Stateless
	@LocalBean

	public class DayInOutEJB extends GenericDAO<DayInOutStatusModel> implements Serializable {

		private static final long serialVersionUID = 1L;

		public DayInOutEJB() {
			super(DayInOutStatusModel.class);
		
		}

	public DayInOutStatusModel getActiveWorkDay()
	{
		return  findOneResult(DayInOutStatusModel.FIND_ACTIVE_WORKING_DATE, null);
	}
	
	public DayInOutStatusModel getPreviousDay()
	{
		List<DayInOutStatusModel> lstDayInOut = new  ArrayList<DayInOutStatusModel>();
		lstDayInOut = findAllWithGivenCondition(DayInOutStatusModel.FIND_PREVIOUS_WORKING_DATE, null);
		return lstDayInOut.get(0);
	}
}
