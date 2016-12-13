package sasrestro.mb.restuarant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.primefaces.component.column.Column;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.panel.Panel;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import sasrestro.mb.user.UserMB;
import sasrestro.model.restaurant.OrderModel;
import sasrestro.model.restaurant.TableModel;
import sasrestro.sessionejb.restaurant.OrderItemEJB;
import sasrestro.sessionejb.restaurant.TableEJB;

	@ManagedBean(name = "dashboardBacker")
	@ViewScoped
	public class DashboardBacker {

		public static final int DEFAULT_COLUMN_COUNT = 3;
		private int columnCount = DEFAULT_COLUMN_COUNT;
		
		@ManagedProperty(value = UserMB.INJECTION_NAME)
		private UserMB userMB;

		public UserMB getUserMB() {
			return userMB;
		}

		public void setUserMB(UserMB userMB) {
			this.userMB = userMB;
		}
		
		private Dashboard dashboard;
		
		
		
		@EJB
		OrderItemEJB orderItemEJB;
		
		@EJB
		TableEJB tableEJB;
		
		@PostConstruct
	    public void init() {
	     
			DecimalFormat d = new DecimalFormat("#.##");
			FacesContext fc = FacesContext.getCurrentInstance();
			Application application = fc.getApplication();

			dashboard = (Dashboard) application.createComponent(fc, "org.primefaces.component.Dashboard", "org.primefaces.component.DashboardRenderer");
			dashboard.setId("dashboard");

			DashboardModel model = new DefaultDashboardModel();
			for( int i = 0, n = getColumnCount(); i < n; i++ ) {
				DashboardColumn column = new DefaultDashboardColumn();
				model.addColumn(column);
			}
			dashboard.setModel(model);

			List<OrderModel> lstOrders = new ArrayList<OrderModel>();
			lstOrders = orderItemEJB.getDistinctActiveTable();
			
			
			for( int i = 0, n = lstOrders.size(); i < n; i++ ) {
				TableModel tblModel = new TableModel();
				tblModel = tableEJB.find(lstOrders.get(i).getTable_id().getTableId());
				Panel panel = (Panel) application.createComponent(fc, "org.primefaces.component.Panel", "org.primefaces.component.PanelRenderer");
				panel.setId("measure_" + i);
				panel.setHeader(tblModel.getTableName());
				panel.setClosable(false);
				panel.setToggleable(true);

				getDashboard().getChildren().add(panel);
				refreshChildren(panel,tblModel.getTableId());
				
				double sum = 0;
				List<OrderModel> lstOrder = new ArrayList<OrderModel>();
				lstOrder = orderItemEJB.getOrdersFromActiveTable(tblModel.getTableId());
				//lstOrder = orderItemEJB.findAll();
				for(OrderModel ord: lstOrder)
				{
					double amt= ord.getQuantity() * ord.getItemId().getPrice();
					sum += amt;
				}
				
				DashboardColumn column = model.getColumn(i%getColumnCount());
				column.addWidget(panel.getId());
				
				HtmlOutputText text = new HtmlOutputText();
				text.setValue("<br/>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;"+"Sub Total: &emsp;&emsp;&emsp;&emsp;&emsp;      Rs."+d.format(sum));
				
				double vat = sum*0.10;
				
				HtmlOutputText text1 = new HtmlOutputText();
				text1.setValue("<br/> &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;"+"VAT : &emsp;&emsp; &emsp;&emsp;&emsp;&emsp;      Rs."+d.format(vat));
				
				double TSC = sum*0.10;
				
				HtmlOutputText text2 = new HtmlOutputText();
				text2.setValue("<br/>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;"+"Service Charge: &emsp;&emsp;  Rs. "+d.format(TSC));
				
				double gtot = sum+vat+TSC;
				HtmlOutputText text3 = new HtmlOutputText();
				text3.setValue("<br/>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;"+"Grand Total: &emsp;&emsp;&emsp;         Rs."+d.format(gtot)+" <br/><br/>");
				
			
				
				
			/*	CommandButton button = new CommandButton();
				button.setValue("Print Bill");
				*/
				
				
				
				
				panel.getChildren().add(text);
				panel.getChildren().add(text1);
				panel.getChildren().add(text2);
				panel.getChildren().add(text3);
				//panel.getChildren().add(button);
				
				
				//}
			}
		}

		public Dashboard getDashboard() {
			return dashboard;
		}

		public void setDashboard(Dashboard dashboard) {
			this.dashboard = dashboard;
		}

		public int getColumnCount() {
			return columnCount;
		}

		public void setColumnCount(int columnCount) {
			this.columnCount = columnCount;
		}

		
		private void refreshChildren(Panel panel, int tableid) {
			panel.getChildren().clear();
			
			List<OrderModel> lstOrder = new ArrayList<OrderModel>();
			lstOrder = orderItemEJB.getOrdersFromActiveTable(tableid);
			//lstOrder = orderItemEJB.findAll();
			
			FacesContext fc = FacesContext.getCurrentInstance();
			Application application = fc.getApplication();
			ExpressionFactory ef = application.getExpressionFactory();
			ELContext elc = fc.getELContext();
			
		
			

			DataTable table = (DataTable) application.createComponent(DataTable.COMPONENT_TYPE);
			table.setValue(lstOrder);
			table.setVar("item");
			UIOutput tableTitle = (UIOutput)application.createComponent(UIOutput.COMPONENT_TYPE);
			tableTitle.setValue("Table Title");
			table.getFacets().put("header", tableTitle);
			//Index
			Column indexColumn = (Column) application.createComponent(Column.COMPONENT_TYPE);
			UIOutput indexColumnTitle = (UIOutput)application.createComponent(UIOutput.COMPONENT_TYPE);
			indexColumnTitle.setValue("Sno");
			indexColumn.getFacets().put("header", indexColumnTitle);
//			table.getColumns().add(column);
			table.getChildren().add( indexColumn );
			
			/*Column orderTime = (Column) application.createComponent(Column.COMPONENT_TYPE);
			UIOutput orderTimeTitle = (UIOutput)application.createComponent(UIOutput.COMPONENT_TYPE);
			orderTimeTitle.setValue("Order Time");
			orderTime.getFacets().put("header", orderTimeTitle);
			table.getChildren().add( orderTime );

			ValueExpression orderExp = ef.createValueExpression(elc, "#{item.orderTime}", Object.class);
			HtmlOutputText orderOutput = (HtmlOutputText)application.createComponent( HtmlOutputText.COMPONENT_TYPE );		
			orderOutput.setValueExpression("value", orderExp);
			orderTime.getChildren().add( orderOutput );	*/

			
			
			ValueExpression indexValueExp = ef.createValueExpression(elc, "", Object.class);
			HtmlOutputText indexOutput = (HtmlOutputText)application.createComponent( HtmlOutputText.COMPONENT_TYPE );
			indexOutput.setValueExpression("value", indexValueExp);
			indexColumn.getChildren().add( indexOutput );	
			
			//Name Column
			Column nameColumn = (Column) application.createComponent(Column.COMPONENT_TYPE);
			UIOutput nameColumnTitle = (UIOutput)application.createComponent(UIOutput.COMPONENT_TYPE);
			nameColumnTitle.setValue("Name");
			nameColumn.getFacets().put("header", nameColumnTitle);
			table.getChildren().add( nameColumn );

			ValueExpression nameValueExp = ef.createValueExpression(elc, "#{item.itemId.name}", Object.class);
			HtmlOutputText nameOutput = (HtmlOutputText)application.createComponent( HtmlOutputText.COMPONENT_TYPE );
			nameOutput.setValueExpression("value", nameValueExp);
			nameColumn.getChildren().add( nameOutput );	
			
			Column priceColumn = (Column) application.createComponent(Column.COMPONENT_TYPE);
			UIOutput priceColumnTitle = (UIOutput)application.createComponent(UIOutput.COMPONENT_TYPE);
			priceColumnTitle.setValue("Price");
			priceColumn.getFacets().put("header", priceColumnTitle);
			table.getChildren().add( priceColumn );

			ValueExpression priceValueExp = ef.createValueExpression(elc, "#{item.itemId.price}", Object.class);
			HtmlOutputText priceOutput = (HtmlOutputText)application.createComponent( HtmlOutputText.COMPONENT_TYPE );
			priceOutput.setValueExpression("value", priceValueExp);
			priceColumn.getChildren().add( priceOutput );		

			Column qntyColumn = (Column) application.createComponent(Column.COMPONENT_TYPE);
			UIOutput qntyColumnTitle = (UIOutput)application.createComponent(UIOutput.COMPONENT_TYPE);
			qntyColumnTitle.setValue("Quantity");
			qntyColumn.getFacets().put("header", qntyColumnTitle);
			table.getChildren().add( qntyColumn );

			ValueExpression qntyValueExp = ef.createValueExpression(elc, "#{item.quantity}", Object.class);
			HtmlOutputText qntyOutput = (HtmlOutputText)application.createComponent( HtmlOutputText.COMPONENT_TYPE );
			qntyOutput.setValueExpression("value", qntyValueExp);
			qntyColumn.getChildren().add( qntyOutput );		
			
			Column totColumn = (Column) application.createComponent(Column.COMPONENT_TYPE);
			UIOutput totColumnTitle = (UIOutput)application.createComponent(UIOutput.COMPONENT_TYPE);
			totColumnTitle.setValue("Total");
			totColumn.getFacets().put("header", totColumnTitle);
			table.getChildren().add( totColumn );

			ValueExpression totValueExp = ef.createValueExpression(elc, "#{item.itemId.price * item.quantity}", Object.class);
			HtmlOutputText totOutput = (HtmlOutputText)application.createComponent( HtmlOutputText.COMPONENT_TYPE );
			totOutput.setValueExpression("value", totValueExp);
			totColumn.getChildren().add( totOutput );
			
			

			panel.getChildren().add(table);
		}
		
		
		
		
	}

