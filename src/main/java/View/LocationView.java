package View;

import Controller.BagController;
import Controller.LocationController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;

public class LocationView {
    private static volatile LocationView locationView = null;

    private LocationController locationcontroller = null;
    private BagController bagcontroller = null;
    private double position_x, position_y;
    
    @FXML
    private AnchorPane page; 

    @FXML
    private ImageView imageView;
    private HBox buttonBox;
    
    @FXML
	private MenuItem menu_bag;
	
    @FXML
    private BagView bagViewController;
    
    
    private ItemView itemViewController;
    
    @FXML
    private void initialize() {
    	bagViewController.injectMainController(this);
    	itemViewController = new ItemView(this);
    }
    

    public void setLocationController(LocationController controller){this.locationcontroller = controller;}
    public LocationController getLocationController(){return locationcontroller;}

    public void setBagController(BagController controller){this.bagcontroller = controller;}
    public BagController getBagController(){return bagcontroller;}


    public BagView getBagViewController() {
		return bagViewController;
	}

	public void setBagViewController(BagView bagViewController) {
		this.bagViewController = bagViewController;
	}

	public ItemView getItemViewController() {
		return itemViewController;
	}

	public void setItemViewController(ItemView itemViewController) {
		this.itemViewController = itemViewController;
	}

	public static LocationView getLocationView(){
        synchronized (LocationView.class){
            if(locationView == null){
                locationView = new LocationView();
            }
        }

        return locationView;
    }


    public void updateLocation(String locationName, List<Integer> arrowAngles) {
         URL locationviewurl = getClass().getResource("images/"+locationName+".jpg");
         Image locationimage = new Image(locationviewurl.toString());
         imageView.setImage(locationimage);
         int buttonbox_width = 60 * arrowAngles.size();
         buttonBox.setPrefWidth(buttonbox_width);
         for(int i=0; i<arrowAngles.size(); i++){
             addArrows(arrowAngles.get(i));
         }
    }


    public void addArrows(Integer arrowAngle){
        Button button = new Button("↑");
        button.setPrefWidth(60);
        button.setRotate(arrowAngle);
        buttonBox.getChildren().add(button);
        button.setOnAction(new EventHandler<ActionEvent>() {
             
            public void handle(ActionEvent event) {
            	// TODO Auto-generated method stub
                if(locationcontroller != null){
                    locationcontroller.moveToDirection(arrowAngle);
                }
            }
        });


    }

    public void chooseItemPosition (ActionEvent event){
        imageView.setEffect(new GaussianBlur());
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouse_event) {
                position_x = mouse_event.getX();
                position_y = mouse_event.getY();
                imageView.setEffect(null);
                bagcontroller.itemPosition(position_x, position_y);
            }
        });
    }
    
    public void openBag(ActionEvent event) {
		// TODO Auto-generated method stub
    	bagViewController.showBag();
	}
    
    public AnchorPane getPage() {
		// TODO Auto-generated method stub
    	return page;
	}
}
