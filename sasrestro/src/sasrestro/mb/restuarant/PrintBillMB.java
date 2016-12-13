package sasrestro.mb.restuarant;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import sasrestro.mb.user.UserMB;
import sasrestro.misc.AbstractMB;
import sasrestro.misc.JrUtils;
import sasrestro.model.restaurant.BillReportModel;
import sasrestro.model.restaurant.OrderModel;
import sasrestro.sessionejb.restaurant.OrderItemEJB;

@ViewScoped
@ManagedBean(name = "printBillMB")
public class PrintBillMB extends AbstractMB implements Serializable {

	/**
	 * 
	 */
	@EJB
	OrderItemEJB orderItemEJB;

	private static final long serialVersionUID = 1L;
	private JasperPrint jasperPrint;
	private HttpServletResponse httpServletResponse;
	private ServletOutputStream servletOutputStream;
	List<OrderModel> lstOrders;
	List<BillReportModel> lstBillItem;
	String customerName;
	int tableId;
	List<OrderModel> lstTable;

	@ManagedProperty(value = UserMB.INJECTION_NAME)
	private UserMB userMB;

	public UserMB getUserMB() {
		return userMB;
	}

	public void setUserMB(UserMB userMB) {
		this.userMB = userMB;
	}

	public void printReceipt() throws IOException, JRException {

		// loadData();
		if (lstBillItem.size() == 0) {
			displayErrorMessageToUser("No Data to Print");
			return;
		}
		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(
				getLstBillItem());
		String reportPath;
		reportPath = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath("/reports/BillReceipt.jasper");

		jasperPrint = JasperFillManager.fillReport(reportPath,
				getReportParameters(), beanCollectionDataSource);
		/*
		 * jasperPrint.setPageHeight(596); jasperPrint.setPageWidth(420);
		 */
		httpServletResponse = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		httpServletResponse.addHeader("Content-disposition",
				"inline; filename=ReceiptNo.pdf");
		servletOutputStream = httpServletResponse.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint,
				servletOutputStream);
		FacesContext.getCurrentInstance().responseComplete();

		lstOrders = orderItemEJB.getOrdersFromActiveTable(tableId);
		for (OrderModel ord : lstOrders) {
			ord.setStatus(2); // Bill cleared
			orderItemEJB.update(ord);
		}
	}

	public void loadData() {
		lstBillItem = null;
		lstOrders = null;
		getLstOrder();
		lstOrders = orderItemEJB.getOrdersFromActiveTable(tableId);

		getLstBillItem();
		for (OrderModel ord : lstOrders) {
			BillReportModel bill = new BillReportModel();
			bill.setItemName(ord.getItemId().getName());
			bill.setItemPrice(ord.getItemId().getPrice());
			bill.setQuantity(ord.getQuantity());
			lstBillItem.add(bill);
		}
	}

	public Map<String, Object> getReportParameters() {
		DecimalFormat df = new DecimalFormat("##.##");
		double sum = 0;
		for (OrderModel ord : lstOrders) {
			double amt = ord.getQuantity() * ord.getItemId().getPrice();
			sum += amt;
		}
		double vat = Double.valueOf(df.format(sum * 0.10));
		double discount = Double.valueOf(df.format(sum * 0.05));
		double TSC = Double.valueOf(df.format(sum * 0.10));
		double gtot = Double.valueOf(df.format(sum + vat + TSC));

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cusName", customerName);
		params.put("Date", userMB.getUser().getDayInStatus().getDayInDateNp());
		params.put("BillNo", "1");
		params.put("BillAmountInWord",
				JrUtils.toWordsMiracle(String.valueOf(gtot)));
		params.put("discount", discount);
		params.put("serviceCharge", TSC);
		params.put("vat", vat);
		params.put("discountPer", 5.0);

		return params;
	}

	public List<OrderModel> getLstOrder() {
		if (lstOrders == null)
			lstOrders = new ArrayList<OrderModel>();
		return lstOrders;
	}

	public void setLstOrder(List<OrderModel> lstOrders) {
		this.lstOrders = lstOrders;
	}

	public List<BillReportModel> getLstBillItem() {
		if (lstBillItem == null)
			lstBillItem = new ArrayList<>();
		return lstBillItem;
	}

	public void setLstBillItem(List<BillReportModel> lstBillItem) {
		this.lstBillItem = lstBillItem;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public List<OrderModel> getLstTable() {
		lstTable = new ArrayList<OrderModel>();
		lstTable = orderItemEJB.getDistinctActiveTable();
		return lstTable;
	}

	public void setLstTable(List<OrderModel> lstTable) {
		this.lstTable = lstTable;
	}

}
