package sasrestro.sessionejb.restaurant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sasrestro.misc.DirectSqlUtils;
import sasrestro.model.restaurant.OrderModel;
import sasrestro.sessionejb.core.GenericDAO;



@Stateless
@LocalBean
public class OrderItemEJB extends GenericDAO<OrderModel> implements Serializable {

	private static final long serialVersionUID = 1L;

	public OrderItemEJB() {
		super(OrderModel.class);
	
	}
	
	public List<OrderModel> getDistinctActiveTable()
	{
		List<OrderModel> lstOrders = new ArrayList<>();
		String sql = "select * from tbl_order o where o.status <> '2'  GROUP BY table_id";
		
		System.out.println(sql);
		
		lstOrders =  DirectSqlUtils.getListOfResultSets(sql, OrderModel.class);
		
		return lstOrders;
	}
	
	public List<OrderModel> getOrdersFromActiveTable(int tableId)
	{
		List<OrderModel> lstOrders = new ArrayList<>();
		String sql = "select * from tbl_order o where o.status <> '2' and o.table_id = '"+tableId+"'";
		
		//System.out.println(sql);
		lstOrders = DirectSqlUtils.getListOfResultSets(sql, OrderModel.class);
		/*Query query = getEntityManager()
				.createQuery(sql);
		lstOrders =  query.getResultList();*/
		
		return lstOrders;
	}

}
