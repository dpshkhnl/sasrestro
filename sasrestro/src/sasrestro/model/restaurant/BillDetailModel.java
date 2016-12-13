package sasrestro.model.restaurant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="tbl_bill_detail")
public class BillDetailModel implements Serializable {

	private static final long serialVersionUID = 1L; 
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bill_detail_id")
	private int billDetailId;
	
	@OneToOne
	@JoinColumn(name="bill_id")
	private BillModel billId;
	
	@OneToOne
	@JoinColumn(name ="item_id")
	private MenuItemModel itemId;
	
	@Column(name="particulars")
	private String particulars;
	
	public int getBillDetailId() {
		return billDetailId;
	}

	public void setBillDetailId(int billDetailId) {
		this.billDetailId = billDetailId;
	}

	public BillModel getBillId() {
		return billId;
	}

	public void setBillId(BillModel billId) {
		this.billId = billId;
	}

	public MenuItemModel getItemId() {
		return itemId;
	}

	public void setItemId(MenuItemModel itemId) {
		this.itemId = itemId;
	}

	public String getParticulars() {
		return particulars;
	}

	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Column(name="quantity")
	private double quantity;
	
	@Column(name="amount")
	private double amount;
	


}
