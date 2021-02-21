import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Converter {

    public Converter(){}

    public Player makePlayer(List<String> playerInfo) {
        List<String> state = new ArrayList<>();
        String name = playerInfo.get(0);
        String type = playerInfo.get(1);
        int victories = Integer.parseInt(playerInfo.get(2));
        int level = Integer.parseInt(playerInfo.get(3));
        int wallet = Integer.parseInt(playerInfo.get(5));
        HashMap<String, Integer> stats = new HashMap<>();
        for(int i = 6; i < 11; i++){
            String stat = playerInfo.get(i).split(" ")[0];
            int value = Integer.parseInt(playerInfo.get(i).split(" ")[1]);
            stats.put(stat, value);
        }
        int maxHealth = Integer.parseInt(playerInfo.get(11));
        int mp = Integer.parseInt(playerInfo.get(12).split("/")[0]);
        int maxMp = Integer.parseInt(playerInfo.get(12).split("/")[1]);
        int exp = Integer.parseInt(playerInfo.get(13));
        int expReq = Integer.parseInt(playerInfo.get(14));
        state.add(playerInfo.get(17).substring(1, playerInfo.get(17).length() - 1));

        HashMap<String, Integer> inventory = new HashMap<>();
        if(playerInfo.size() > 18) {
            for (int i = 18; i < playerInfo.size() - 1; i++) {
            inventory.put(playerInfo.get(i).split(" ")[0], Integer.parseInt(playerInfo.get(i).split(" ")[1]));
            }
        }

        return new Player(name, type, state, stats, inventory, wallet, victories, level, exp, expReq, maxHealth, mp, maxMp);
    }

    public List<String> formatInfo(List<String> baseInfo){
        if(baseInfo.size() < 1){
            return null;
        }
        List<String> out = new ArrayList<>();
        List<String> item = new ArrayList<>();
        for (String s : baseInfo) {
            if (s.equalsIgnoreCase("%")) {
                break;
            } else {
                out.add(s);
            }
        }
        String name = baseInfo.get(0).split(" ")[0];
        String type = baseInfo.get(0).split(" ")[3];
        String battles = baseInfo.get(0).split(" ")[5];
        String level = baseInfo.get(0).split(" ")[9];

        item.add(name);
        item.add(type);
        item.add(battles);
        item.add(level);

        String savings = baseInfo.get(1).split(" ")[2];
        String holding = baseInfo.get(2).split(" ")[2];
        String stat1 = baseInfo.get(3).split(" ")[3] +" "+ baseInfo.get(3).split(" ")[4];
        String stat2 = baseInfo.get(4).split(" ")[0] +" "+ baseInfo.get(4).split(" ")[1];
        String stat3 = baseInfo.get(5).split(" ")[0] +" "+ baseInfo.get(5).split(" ")[1];
        String stat4 = baseInfo.get(6).split(" ")[0] +" "+ baseInfo.get(6).split(" ")[1];
        String stat5 = baseInfo.get(7).split(" ")[0] +" "+ baseInfo.get(7).split(" ")[1];
        String maxHealth = baseInfo.get(8).split(" ")[3];
        String maxMp = baseInfo.get(9).split(" ")[2];

        item.add(savings);
        item.add(holding);
        item.add(stat1);
        item.add(stat2);
        item.add(stat3);
        item.add(stat4);
        item.add(stat5);
        item.add(maxHealth);
        item.add(maxMp);

        String exp = baseInfo.get(10).split(" ")[2];
        String expReq = baseInfo.get(11).split(" ")[3];
        String rate = baseInfo.get(12).split(" ")[2];
        String gamblerLvl = baseInfo.get(13).split(" ")[3];
        String state = baseInfo.get(14);

        item.add(exp);
        item.add(expReq);
        item.add(rate);
        item.add(gamblerLvl);
        item.add(state);

        int i = 16;
        while(i < baseInfo.size() && !baseInfo.get(i).equals("%")){
            item.add(baseInfo.get(i));
            i++;
        }

        return item;
    }

    public Gambler getGambler(List<String> baseInfo){
        Gambler gambler = new Gambler();
        gambler.setStorage(Integer.parseInt(baseInfo.get(1).split(" ")[2]));
        gambler.setRate(Double.parseDouble(baseInfo.get(12).split(" ")[2]));
        gambler.setLevel(Integer.parseInt(baseInfo.get(13).split(" ")[3]));
        gambler.setCheckNumber(1);
        return gambler;
    }

}
