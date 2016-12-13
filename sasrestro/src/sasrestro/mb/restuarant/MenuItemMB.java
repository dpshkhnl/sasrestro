package sasrestro.mb.restuarant;




import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import sasrestro.misc.AbstractMB;
import sasrestro.model.restaurant.MenuItemModel;
import sasrestro.sessionejb.restaurant.MenuItemEJB;

@SessionScoped
@ManagedBean(name="menuItemMB")
public class MenuItemMB extends AbstractMB implements Serializable {
	
	@EJB
	MenuItemEJB menuItemEJB;
	
	private static final long serialVersionUID = 1L;
	
	private MenuItemModel menuItemModel ;
	private String itemName ;
	private List<MenuItemModel> lstMenuItem ;
	private StreamedContent foodImage = null;

	public MenuItemModel getMenuItemModel() {
		if (menuItemModel == null)
			menuItemModel = new MenuItemModel();
		return menuItemModel;
	}

	public void setMenuItemModel(MenuItemModel menuItemModel) {
		this.menuItemModel = menuItemModel;
	}
	
	public void save()
	{
		if (menuItemModel.getItemId() != 0)
		{
			menuItemEJB.update(menuItemModel);
			displayInfoMessageToUser("Update Successfull");
			menuItemModel = null;
		}
		else
		{
			menuItemEJB.save(menuItemModel);
			displayInfoMessageToUser("Save Successfull");
			menuItemModel = null;
		}
		
	}
	
	public void loadForNew()
	{
		menuItemModel = null;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("menuItemDia.show();");
		
	}
	
	public void loadForEdit()
	{
		getMenuItemModel();
		menuItemModel = menuItemEJB.find(menuItemModel.getItemId());
		try {
			if(menuItemModel.getImage() != null)
			getFoodImage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("menuItemDia.show();");
		
	}
	
	public void delete()
	{
		menuItemEJB.delete(menuItemModel.getItemId(), MenuItemModel.class );
		displayInfoMessageToUser("Delete Successfull!");
		menuItemModel = null;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public List<MenuItemModel> getLstMenuItem() {
		if (lstMenuItem== null)
			lstMenuItem = new ArrayList<>();
		lstMenuItem = menuItemEJB.findAll();
		return lstMenuItem;
	}

	public void setLstMenuItem(List<MenuItemModel> lstMenuItem) {
		this.lstMenuItem = lstMenuItem;
	}
	
	public void upload(FileUploadEvent event) {  
		try {
			if (validateImage(event.getFile()))
			{
				//initialize student image from the uploaded image file using image/jpeg content type
				foodImage = new DefaultStreamedContent(event.getFile().getInputstream(), "image/jpeg");
				/*BufferedImage resizedImage = new BufferedImage(150, 150, "image/jpeg");
				Graphics2D g = resizedImage.createGraphics();
				g.drawImage(foodImage, 0, 0, 150, 150, null);
				g.dispose();*/
				getMenuItemModel().setImage((event.getFile().getContents()));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	} 
	
	public boolean validateImage(UploadedFile file)
	{
		if (!(file.getContentType().startsWith("image"))) {
			displayErrorMessageToUser("File is not an Image file");
			return false;
		}
		long fileByte = file.getContents().length;
		if(fileByte >102400 ){
			displayErrorMessageToUser("File size is too big (must be at most 100KB)\n");
			return false;
		}
		return true;
	}
	public void setNullMemberImage() {
		foodImage = new DefaultStreamedContent();
		getMenuItemModel().setImage(null);
		try {
			foodImage = getDefaultImage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private DefaultStreamedContent getDefaultImage() throws FileNotFoundException
	{
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		InputStream input  = externalContext.getResourceAsStream("/resources/images/noimage.jpg");
		return new DefaultStreamedContent(input);   
	}
	
	public StreamedContent getFoodImage() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
			foodImage = new DefaultStreamedContent();
		}
		else
		{
			if (menuItemModel != null)
			if(menuItemModel.getImage() != null) {
				foodImage = new DefaultStreamedContent(new ByteArrayInputStream(menuItemModel.getImage(), 0, menuItemModel.getImage().length));
			}	
			else
			{
				foodImage = getDefaultImage();
			}
		}
		return foodImage;
	}

	public void setFoodImage(StreamedContent foodImage) {
		this.foodImage = foodImage;
	}

}
