import java.util.List;
import java.util.Scanner;

public class Input {

    private Scanner sc;

    public Input(){
        this.sc = new Scanner(System.in);
    }

    public String getInput(){
        return this.sc.next();
    }
    public String getInput(String prompt){
        System.out.println("\033[0;34m"+prompt);
        return this.sc.next();
    }

    public String getInput(String prompt, List<String> options, boolean showOptions) throws InterruptedException {
        System.out.println(prompt);
        Thread.sleep(600);
        int count = 1;
        if(showOptions) {
            for (String c : options) {
                System.out.printf("Option %d : %s%n", count, c);
                count++;
            }
        }
        String choice = this.sc.next().toLowerCase();
        options.replaceAll(String::toLowerCase);
        options.replaceAll(s -> s.split(" ")[0]);
        if(options.contains(choice)){
            return choice;
        }
        return getInput("Not valid", options, showOptions);
    }

    public int getNum(String message, int currentWallet, int itemCost) throws InterruptedException {
        System.out.println(message);
        Thread.sleep(600);
        String input = this.sc.next();
        try {
            int amountRequested = Integer.parseInt(input);
            int cost = itemCost * amountRequested;
            if(cost > currentWallet){
                return getNum("Sorry, " + cost + " is over your current savings of " +currentWallet+" you can not buy " + amountRequested + " how many would you like?", currentWallet, itemCost);
            }
            return amountRequested;
        } catch (NumberFormatException e){
            return getNum("Sorry, not a valid input", currentWallet, itemCost);
        }
    }

}
