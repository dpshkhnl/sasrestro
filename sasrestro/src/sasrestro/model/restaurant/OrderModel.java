package sasrestro.model.restaurant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
	
	@Entity
	@Table(name="tbl_order")
	public class OrderModel implements Serializable {

		private static final long serialVersionUID = 1L; 
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "order_id")
		private int orderId;
		
		@OneToOne
		@JoinColumn(name="table_id")
		private TableModel table_id;
		
		@OneToOne
		@JoinColumn(name="item_id")
		private MenuItemModel itemId;
		
		@Column(name="quantity")
		private Double quantity;
		
		@Column(name = "order_time")
		@Temporal(TemporalType.TIMESTAMP)
		private Date orderTime;
		
		@Column(name="status")
		private Integer status;

		public int getOrderId() {
			return orderId;
		}

		public void setOrderId(int orderId) {
			this.orderId = orderId;
		}

		public TableModel getTable_id() {
			return table_id;
		}

		public void setTable_id(TableModel table_id) {
			this.table_id = table_id;
		}

		public MenuItemModel getItemId() {
			return itemId;
		}

		public void setItemId(MenuItemModel itemId) {
			this.itemId = itemId;
		}

		public Double getQuantity() {
			return quantity;
		}

		public void setQuantity(Double quantity) {
			this.quantity = quantity;
		}

		public Date getOrderTime() {
			return orderTime;
		}

		public void setOrderTime(Date orderTime) {
			this.orderTime = orderTime;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		

}
