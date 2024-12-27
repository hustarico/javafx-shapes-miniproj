import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


//? for cross-class access , dimension set in separate class
class Dimensions{
    public static final int height = 250;
    public static final int width = 250;


    public static final int xOffset = 30;
    public static final int yOffset = 30;
}

public class Shapes extends Application{
    
    
    private int userAnsInt;

    //? const values for bounderies , and input validation
    final int minVal = 1;
    final int maxVal = 4;

    private TemplateStage circleTemplateStage;
    private TemplateStage rectanTemplateStage;
    private TemplateStage arcTemplateStage;
    private TemplateStage polyTemplateStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage mainStage){
        mainStage.setY(mainStage.getY()-Dimensions.height/2);

        
        VBox mainPane = new VBox(10);
        
        Label useIndicatorLabel = new Label(String.format("Please enter the maximum number of allowance of selection (%d-%d) :", minVal,maxVal));
        useIndicatorLabel.setWrapText(true);
        useIndicatorLabel.setTextAlignment(TextAlignment.CENTER);

        TextField userInput = new TextField();
        userInput.setMaxWidth(Dimensions.width/10);
        
        //?this would be a container for the radios or checkboxes added later
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        
        mainPane.getChildren().addAll(useIndicatorLabel,userInput,container);
        mainPane.setAlignment(Pos.CENTER);

        userInput.setOnAction(even ->{
            String userAns = userInput.getText();

            try{

                userAnsInt = Integer.parseInt(userAns);
                if( userAnsInt > maxVal || userAnsInt < minVal){
                    showAlert(String.format("input should be numerical between %d and %d", minVal,maxVal));
                    
                }
                else{
                    container.getChildren().clear();
                    if(userAnsInt == 1){
                        addRadios(container,mainStage);
                    }else{
                        addCheckboxes(container , mainStage);
                    }
                    
                }
            }catch(NumberFormatException e){
                showAlert(String.format("input should be numerical between %d and %d", minVal,maxVal));
            }

            
        });

        mainStage.setTitle("primary stage");
        Scene primaryScene = new Scene(mainPane,Dimensions.width,Dimensions.height);
        mainStage.setScene(primaryScene);

        
        mainStage.show();

        mainStage.centerOnScreen();
        mainStage.setY(mainStage.getY()+Dimensions.height/2);
    }


    private void showAlert(String errMessage){
        Alert al = new Alert(AlertType.ERROR);
        al.setTitle("invalid input");
        al.setContentText(errMessage);
        al.setHeaderText(null);
        al.showAndWait();
    }
    
    private void addRadios(VBox vbx,Stage mainStage){
        RadioButton circleButton = new RadioButton("Circle");
        RadioButton rectangleButton = new RadioButton("Rectangle");
        RadioButton arcButton = new RadioButton("Arc");
        RadioButton polyButton = new RadioButton("Polygon");

        ToggleGroup shapesGroup = new ToggleGroup();

        circleButton.setToggleGroup(shapesGroup);
        rectangleButton.setToggleGroup(shapesGroup);
        arcButton.setToggleGroup(shapesGroup);
        polyButton.setToggleGroup(shapesGroup);

        circleButton.setSelected(true);

        circleButton.setPrefWidth(Dimensions.width*0.4);
        rectangleButton.setPrefWidth(Dimensions.width*0.4);
        arcButton.setPrefWidth(Dimensions.width*0.4);
        polyButton.setPrefWidth(Dimensions.width*0.4);
        
        Button draw = new Button("Draw");
        
        vbx.getChildren().addAll(circleButton,rectangleButton,arcButton,polyButton,draw);


        Random randomGen = new Random();

        draw.setOnAction(buttonPressed ->{
            RadioButton selected = (RadioButton) shapesGroup.getSelectedToggle();

            

            if(selected!=null){
                String selectedString = selected.getText();
                switch (selectedString.toLowerCase()) {

                    case "circle":
                        if(circleTemplateStage == null){
                        circleTemplateStage = new TemplateStage(new Circle(Dimensions.width *0.3));
                        
                        
                        circleTemplateStage.setX(mainStage.getX() - Dimensions.width - Dimensions.xOffset);
                        circleTemplateStage.setY(mainStage.getY());
                        }
                        closeStage(rectanTemplateStage);
                        closeStage(arcTemplateStage);
                        closeStage(polyTemplateStage);
                        circleTemplateStage.show();
                        break;

                    case "rectangle":
                        if(rectanTemplateStage == null){
                            rectanTemplateStage= new TemplateStage(new Rectangle(Dimensions.width*0.5,Dimensions.height*0.5));
                            rectanTemplateStage.setX(mainStage.getX() + Dimensions.width + Dimensions.xOffset);
                            rectanTemplateStage.setY(mainStage.getY());
                        }   
                        closeStage(circleTemplateStage);
                        closeStage(arcTemplateStage);
                        closeStage(polyTemplateStage); 
                        rectanTemplateStage.show();
                        break;

                    case "arc":
                        if(arcTemplateStage == null){
                        Arc arc = new Arc(Dimensions.width / 2, Dimensions.height / 2, 100, 100, randomGen.nextInt(360),60); 
                        arc.setType(ArcType.ROUND);
                        arcTemplateStage = new TemplateStage(arc);
                        arcTemplateStage.setX(mainStage.getX());
                        arcTemplateStage.setY(mainStage.getY()-Dimensions.width-Dimensions.yOffset);
                        }
                        closeStage(rectanTemplateStage);
                        closeStage(circleTemplateStage);
                        closeStage(polyTemplateStage);
                        arcTemplateStage.show();
                        break;

                    case "polygon":
                        
                        if(polyTemplateStage == null){
                        polyTemplateStage = new TemplateStage(drawRandomPolygon(Dimensions.height/3));
                        polyTemplateStage.setX(mainStage.getX());
                        polyTemplateStage.setY(mainStage.getY()+Dimensions.width+Dimensions.yOffset);
                        }
                        closeStage(rectanTemplateStage);
                        closeStage(circleTemplateStage);
                        closeStage(arcTemplateStage);
                        polyTemplateStage.show();
                        break;
                
                    default:
                        break;
                }

            }
        });
    }

    public void closeStage(Stage st){
        if(st!=null)
            if(st.isShowing())
                st.close();
    }


    private void addCheckboxes(VBox vbx , Stage mainStage){
        CheckBox circleCheck = new CheckBox("Circle");
        CheckBox rectangleCheck = new CheckBox("Rectangle");
        CheckBox arcCheck = new CheckBox("Arc");
        CheckBox polyCheck = new CheckBox("Polygon");

        circleCheck.setPrefWidth(Dimensions.width*0.4);
        rectangleCheck.setPrefWidth(Dimensions.width*0.4);
        arcCheck.setPrefWidth(Dimensions.width*0.4);
        polyCheck.setPrefWidth(Dimensions.width*0.4);

        Button draw = new Button("Draw");

        vbx.getChildren().addAll(circleCheck,rectangleCheck,arcCheck,polyCheck,draw);
        Random randomGen = new Random();

        draw.setOnAction(click->{
            if(validChecks(userAnsInt, circleCheck,rectangleCheck,arcCheck,polyCheck)){
                closeStage(circleTemplateStage);
                closeStage(rectanTemplateStage);
                closeStage(arcTemplateStage);
                closeStage(polyTemplateStage);
                if(circleCheck.isSelected()){
                    if(circleTemplateStage == null){
                        circleTemplateStage = new TemplateStage(new Circle(Dimensions.width *0.3));

                        circleTemplateStage.setX(mainStage.getX() - Dimensions.width - Dimensions.xOffset);
                        circleTemplateStage.setY(mainStage.getY());
                    }
                    circleTemplateStage.show();
                }
                if(rectangleCheck.isSelected()){
                    if(rectanTemplateStage == null){
                        rectanTemplateStage= new TemplateStage(new Rectangle(Dimensions.width*0.5,Dimensions.height*0.5));
                        rectanTemplateStage.setX(mainStage.getX() + Dimensions.width + Dimensions.xOffset);
                        rectanTemplateStage.setY(mainStage.getY());
                    }
                    rectanTemplateStage.show();
                }
                if(arcCheck.isSelected()){
                    if(arcTemplateStage == null){
                        Arc arc = new Arc(Dimensions.width / 2, Dimensions.height / 2, 100, 100, randomGen.nextInt(360), 60); 
                        arc.setType(ArcType.ROUND);
                        arcTemplateStage = new TemplateStage(arc);
                        arcTemplateStage.setX(mainStage.getX());
                        arcTemplateStage.setY(mainStage.getY()-Dimensions.width-Dimensions.yOffset);
                    }
                    arcTemplateStage.show();
                }
                if(polyCheck.isSelected()){
                    if(polyTemplateStage == null){
                        polyTemplateStage = new TemplateStage(drawRandomPolygon(Dimensions.height/3));
                        polyTemplateStage.setX(mainStage.getX());
                        polyTemplateStage.setY(mainStage.getY()+Dimensions.width+Dimensions.yOffset);
                    }    
                    polyTemplateStage.show();
                }
            }else{
                showAlert(String.format("max number of shapes exceeded (%d)%n",userAnsInt));
            }
        });

        
    }
    private boolean validChecks(int maxVal,CheckBox ... checks){
        int count = 0;
        for(CheckBox check : checks )
            if(check.isSelected())
                count++;

        return count<=maxVal;
    }

    private Polygon drawRandomPolygon(int displace){

        Random randomGen = new Random();

        return new Polygon(randomGen.nextInt(Dimensions.width),randomGen.nextInt(Dimensions.height-displace),randomGen.nextInt(Dimensions.width),randomGen.nextInt(Dimensions.height-displace),randomGen.nextInt(Dimensions.width),randomGen.nextInt(Dimensions.height-displace),randomGen.nextInt(Dimensions.width),randomGen.nextInt(Dimensions.height-displace),randomGen.nextInt(Dimensions.width),randomGen.nextInt(Dimensions.height-displace),randomGen.nextInt(Dimensions.width),randomGen.nextInt(Dimensions.height-displace));
    }
}

class TemplateStage extends Stage{
    Random random = new Random();
    ColorPicker colorPicker = new ColorPicker();

    Scene scene ;
    VBox vbx ;
    Button color;

    
    public TemplateStage (Shape shape){
        this.setTitle(shape.getClass().getSimpleName() + " stage");
        vbx = new VBox(20);
        scene = new Scene(vbx,Dimensions.width,Dimensions.height);

        color = new Button("feeling lucky");

        vbx.getChildren().addAll(shape,color,colorPicker);
        vbx.setAlignment(Pos.CENTER);


        color.setOnAction(click ->{
            shape.setFill(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        });
        colorPicker.setOnAction(select->{
            shape.setFill(colorPicker.getValue());
        });

        this.setScene(scene);
    }
}