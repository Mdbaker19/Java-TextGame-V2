public class HowMany {

    private Input sc = new Input();

    private int amount;
    private String itemName;

    public HowMany(int cost, String name, Player player) throws InterruptedException {
        this.amount = sc.getNum("How many would you like?", player.getWallet(), cost);
        this.itemName = name;
        if(this.amount > 1){
            this.itemName = this.amount + " " + name + "s";
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
