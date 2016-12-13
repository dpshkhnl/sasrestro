package sasrestro.mb.restuarant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import sasrestro.misc.AbstractMB;
import sasrestro.model.restaurant.TableModel;
import sasrestro.sessionejb.restaurant.TableEJB;

@ViewScoped
@ManagedBean(name="tableMB")
public class TableMB extends AbstractMB implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	TableEJB tableEJB;
	
	private TableModel tableModel ;
	
	private List<TableModel> lstTableModel ;

	public TableModel getTableModel() {
		if (tableModel == null)
			tableModel = new TableModel();
		return tableModel;
	}

	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}

	public List<TableModel> getLstTableModel() {
		if (lstTableModel == null)
			lstTableModel = new ArrayList<>();
		lstTableModel = tableEJB.findAll();
		return lstTableModel;
	}

	public void setLstTableModel(List<TableModel> lstTableModel) {
		this.lstTableModel = lstTableModel;
	}
	
	public void save()
	{
		if (tableModel.getTableId() != 0)
		{
			tableEJB.update(tableModel);
			displayInfoMessageToUser("Update Successfull");
			tableModel = null;
		}
		else
		{
			tableEJB.save(tableModel);
			displayInfoMessageToUser("Save Successfull");
			tableModel = null;
		}
		
	}
	
	public void loadForNew()
	{
		tableModel = null;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("tableDia.show();");
		
	}
	
	public void loadForEdit()
	{
		getTableModel();
		tableModel = tableEJB.find(tableModel.getTableId());
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("tableDia.show();");
		
	}
	
	public void delete()
	{
		tableEJB.delete(tableModel.getTableId(), TableModel.class );
		displayInfoMessageToUser("Delete Successfull!");
		tableModel = null;
	}

}
