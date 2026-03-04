package craft.ncraft.ntroll.model;

public class UnluckyAction {
    private String actionName;
    private int chance;

    public UnluckyAction (String actionName, int chance) {
        this.actionName = actionName;
        this.chance = chance;
    }

    public String getName() {
        return actionName;
    }

    public int getChange() {
        return chance;
    }
}
