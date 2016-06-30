import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Observable;

/**
 * Created by Lord Daniel on 6/7/2016.
 */
public class CubeModel extends Observable {

    public String message;
    public HashMap<String, char[][]> cube = new HashMap<>();
    private CubeConfig cubeConfig;
    private CubeConfig solution;
    private ArrayList<String> moves;
    private Backtracker backtracker;
    public boolean clear;
    public boolean solved;
    public boolean missingSelections = false;

    public CubeModel(){

        //char[][] top = new char[3][3];
        this.cube.put("top",new char[3][3]);
        this.cube.put("front",new char[3][3]);
        this.cube.put("left",new char[3][3]);
        this.cube.put("right",new char[3][3]);
        this.cube.put("bottom",new char[3][3]);
        this.cube.put("back",new char[3][3]);

        this.backtracker = new Backtracker();
        this.message = "WELCOME TO RUBIK'S SOLVER!";
        this.clear=true;
        this.solved=false;
    }

    public void solve(){
        System.out.println("solve running");
        for (String s:this.cube.keySet()){
            char[][] face = cube.get(s);
            for (int i=0;i<3;i++){
                for (int j=0;j<3;j++){
                    //System.out.println(face[i][j]);
                    if (face[i][j] == 0){
                        System.out.println("missing selections");
                        this.missingSelections=true;
                        break;
                    }
                }
                if (missingSelections){
                    break;
                }
            }
            if (missingSelections){
                break;
            }
        }
        if (missingSelections){
            message = "SOME SELECTIONS MISSING!";
        } else {
            message="SOLVING...";
            setChanged();
            notifyObservers();
            try {
                this.cubeConfig = new CubeConfig(this.cube);
                System.out.println(this.cubeConfig);
                this.solution = (CubeConfig) backtracker.solve(cubeConfig).get();
                this.moves = solution.getMoves();
                message = "Solved!";
                System.out.println("model solved");
                this.solved = true;
            } catch (NoSuchElementException e) {
                message = "No Solution.";
            }
            setChanged();
            notifyObservers();
        }
        setChanged();
        notifyObservers();
    }

    public void clear(){
        this.cube.put("top",new char[3][3]);
        this.cube.put("front",new char[3][3]);
        this.cube.put("left",new char[3][3]);
        this.cube.put("right",new char[3][3]);
        this.cube.put("bottom",new char[3][3]);
        this.cube.put("back",new char[3][3]);

        this.message = "WELCOME TO RUBIK'S SOLVER!";
        this.missingSelections=false;
        this.clear=true;
        this.solved=false;
        setChanged();
        notifyObservers();
    }

    public  ArrayList getMoves(){
        return moves;
    }
}
