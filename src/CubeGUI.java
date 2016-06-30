import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.w3c.dom.css.Rect;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Lord Daniel on 6/7/2016.
 */
public class CubeGUI  extends Application implements Observer{
    private CubeModel model;
    private int dim = 40;
    private ArrayList<String> faces;
    private ArrayList<Rectangle> cells = new ArrayList<>();
    private Text right;
    private Text top = new Text("WELCOME TO DANIEL'S RUBIK'S CUBE SOLVER!");

    public Rectangle createCell(String face, int i, int j){
        Rectangle R = new Rectangle(this.dim, this.dim);
        R.setFill(Color.LIGHTGRAY);
        R.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                model.clear=false;
                if (R.getFill().equals(Color.LIGHTGRAY) || R.getFill().equals(Color.LAWNGREEN)) {
                    R.setFill(Color.WHITE);
                    char[][] newFace = model.cube.get(face);
                    newFace[i][j] = 'W';
                    model.cube.put(face, newFace);
                } else if (R.getFill().equals(Color.WHITE)) {
                    R.setFill(Color.YELLOW);
                    char[][] newFace = model.cube.get(face);
                    newFace[i][j] = 'Y';
                    model.cube.put(face, newFace);
                } else if (R.getFill().equals(Color.YELLOW)) {
                    R.setFill(Color.ORANGERED);
                    char[][] newFace = model.cube.get(face);
                    newFace[i][j] = 'O';
                    model.cube.put(face, newFace);
                } else if (R.getFill().equals(Color.ORANGERED)) {
                    R.setFill(Color.DARKRED);
                    char[][] newFace = model.cube.get(face);
                    newFace[i][j] = 'R';
                    model.cube.put(face, newFace);
                } else if (R.getFill().equals(Color.DARKRED)) {
                    R.setFill(Color.BLUE);
                    char[][] newFace = model.cube.get(face);
                    newFace[i][j] = 'B';
                    model.cube.put(face, newFace);
                } else if (R.getFill().equals(Color.BLUE)) {
                    R.setFill(Color.LAWNGREEN);
                    char[][] newFace = model.cube.get(face);
                    newFace[i][j] = 'G';
                    model.cube.put(face, newFace);
                }
            }
        });
        this.cells.add(R);
        return R;
    }

    public GridPane createFace(String face){
        GridPane grid = new GridPane();
        int gap = 7;
        grid.setVgap( gap ); // gap between grid cells
        grid.setHgap( gap );
        grid.setPadding( new Insets( 15 ) );
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                grid.add(createCell(face, i, j),j,i);
            }
        }
        return grid;
    }

    public GridPane createCube(){
        GridPane cube = new GridPane();
        cube.setPadding( new Insets( 30 ) );

        GridPane top = createFace("top");
        cube.add(top,1,0);

        GridPane front = createFace("front");
        cube.add(front,1,1);

        GridPane left = createFace("left");
        cube.add(left,0,1);

        GridPane right = createFace("right");
        cube.add(right,2,1);

        GridPane bottom = createFace("bottom");
        cube.add(bottom,1,2);

        GridPane back = createFace("back");
        cube.add(back,1,3);

        return cube;
    }

    @Override
    public void init() throws Exception{
        this.model = new CubeModel();
        model.addObserver( this );
    }

    @Override
    public void start(Stage stage) throws Exception{
        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20, 20, 20, 20));
        border.setCenter(createCube());

        VBox  left = new VBox();
        left.setSpacing(20);
        left.setPadding( new Insets(20,20,20,20));
        Button solve = new Button("Solve");
        solve.setOnAction(event -> model.solve());
        Button clear = new Button("Clear");
        clear.setOnAction(event -> {
            model.clear();
            //make all cells grey
            for (Rectangle r:cells){
                r.setFill(Color.LIGHTGRAY);
            }
        });
        left.getChildren().addAll(solve, clear);
        border.setLeft(left);

        right = new Text("         ");
        right.setFont(new Font(16));
        border.setRight(right);

        border.setTop(top);
        top.setFill(Color.BLUEVIOLET);
        top.setFont( new Font(20) );

        Scene scene = new Scene( border );
        scene.setFill(Color.BLACK);
        stage.setTitle( "Cube Solver" );
        stage.setScene( scene );
        stage.show();
    }

    @Override
    public void update(Observable o, Object arg){
        top.setText(model.message);
        if (model.missingSelections) {
            top.setText("SOME SELECTIONS MISSING!!!");
            top.setFill(Color.RED);
        }
        if (model.clear) {
            top.setFill(Color.BLUEVIOLET);
            top.setText("WELCOME TO DANIEL'S RUBIK'S CUBE SOLVER!");
            right.setText(" ");
        }
        if (model.clear == false && model.solved == false && model.missingSelections==false) {
            top.setFill(Color.BLUEVIOLET);
            top.setText("Solving...");
        }
        if (model.solved) {
            top.setFill(Color.BLUEVIOLET);
            String st = "STEPS TO SOLUTION\n\n";
            ArrayList moves = model.getMoves();
            for (Object s : moves) {
                st += (String) s + "\n";
            }
            right.setText(st);
            System.out.println("GUI solved");
            top.setText("SOLVED!");
        } else {
            right.setText("");
        }


    }

}
