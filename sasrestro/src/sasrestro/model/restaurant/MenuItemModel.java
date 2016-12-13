package sasrestro.model.restaurant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
	@Table(name ="tbl_items")
	public class MenuItemModel implements Serializable {

		private static final long serialVersionUID = 1L; 
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "item_id")
		private int itemId;
		
		@Column (name="name")
		private String name;
		
		@Column(name="category")
		private String category;
		
		@Basic(fetch = FetchType.LAZY)
		@Lob
		@Column(name = "image")
		private byte[] image;
		
		
		@Column(name = "price")
		private Double  price;
		
		@Column(name ="unit")
		private Integer unit;
		
		public Integer getUnit() {
			return unit;
		}

		public void setUnit(Integer unit) {
			this.unit = unit;
		}

		@Column(name = "created_date")
		@Temporal(TemporalType.TIMESTAMP)
		private Date createdDate;
		
		@Column(name = "created_by")
		private int createdBy;
		
		@Column(name = "updated_by")
		private int updatedBy;

		@Column(name = "updated_date")
		@Temporal(TemporalType.TIMESTAMP)
		private Date updatedDate;
		
		@Column(name = "update_count")
		private int updateCount;

		public int getItemId() {
			return itemId;
		}

		public void setItemId(int itemId) {
			this.itemId = itemId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public byte[] getImage() {
			return image;
		}

		public void setImage(byte[] image) {
			this.image = image;
		}

		public Double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public int getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(int createdBy) {
			this.createdBy = createdBy;
		}

		public int getUpdatedBy() {
			return updatedBy;
		}

		public void setUpdatedBy(int updatedBy) {
			this.updatedBy = updatedBy;
		}

		public Date getUpdatedDate() {
			return updatedDate;
		}

		public void setUpdatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
		}

		public int getUpdateCount() {
			return updateCount;
		}

		public void setUpdateCount(int updateCount) {
			this.updateCount = updateCount;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}
		


}
