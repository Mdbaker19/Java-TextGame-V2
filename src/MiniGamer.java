import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MiniGamer {

    private Input sc = new Input();
    private ArrayList<String> rpcOptions = new ArrayList<>(Arrays.asList("r", "p", "s"));

    public MiniGamer(){}

    public boolean numberGame() throws InterruptedException {
        int choice = sc.getNum(1, 10, "Pick a number between 1 and 10");
        int result = (int) Math.floor(Math.random() * 10) + 1;
        return choice == result;
    }
    public boolean coinGame(){
        String[] options = {"h", "t"};
        String choice = sc.getInput("[h]eads or [t]ails?").toLowerCase();
        String result = options[(int) Math.floor(Math.random() * 2)];
        return choice.equalsIgnoreCase(result);
    }

    public boolean rockPaperScissor() throws InterruptedException {
        String[] options = {"r", "s", "p"};
        String choice = sc.getInput("[r]ock, [p]aper, [s]cissors?", rpcOptions, false);
        String result = options[(int) Math.floor(Math.random() * 3)];
        System.out.printf("%s against %s%n", choice, result);
        if(result.equalsIgnoreCase(choice)){
            System.out.println("Draw, again!");
            return rockPaperScissor();
        }
        return rpsHandler(choice, result);
    }

    private boolean rpsHandler(String choice, String result){
        if(choice.equalsIgnoreCase("r")){
            return !result.equalsIgnoreCase("p");
        } else if(choice.equalsIgnoreCase("p")){
            return !result.equalsIgnoreCase("s");
        }
        return !result.equalsIgnoreCase("r");
    }

}
