package actions;

import entities.*;
import gui.GamePanel;

public class ProposingAction implements Action{
    private int energyCost;
    private int TIME_COST_IN_HOUR = 1;
    private NPC targetNpc;
    GamePanel gp;

    public ProposingAction(NPC targetNpc, GamePanel gp){
        this.targetNpc = targetNpc;
        this.gp = gp;
        if (targetNpc.getHeartPoints() == 150){
            energyCost = 10;
        }
        else{
            energyCost = 20;
        }
    }

    public NPC getTargetNpcToPropose(){
        return targetNpc;
    }

    @Override
    public boolean execute(Player player){
        if (player.getEnergy() < energyCost){
            System.out.println("You don't have enough energy to do this action.");
            return false;
        }

        if (!player.getPlayerInventory().hasItem("Wedding Ring")){
            System.out.println("You don't have the required item.");
            return false;
        }

        if (!targetNpc.getRelationshipStatus().equals("single")){
            System.out.println("You can't propose to " + targetNpc.getNpcName() + ".");
            return false;
        }

        if (energyCost == 20){
            player.setEnergy(player.getEnergy() - energyCost);
            Farm farm = FarmManager.getFarmByName(player.getFarm());
            farm.getTime().skipTimeHour(TIME_COST_IN_HOUR);
            System.out.println("You propose to " + targetNpc.getNpcName() + ", and got rejected.");
            return true;
        }

        player.setEnergy(player.getEnergy() - energyCost);
        Farm farm = FarmManager.getFarmByName(player.getFarm());
        farm.getTime().skipTimeHour(TIME_COST_IN_HOUR);

        targetNpc.setRelationshipStatus("Fiance");
        targetNpc.setProposedDay(farm.getTime().getDay());

        System.out.println("You propose to " + targetNpc.getNpcName() + ", and got accepted.");
        player.setPartner(targetNpc);
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            gp.chatPanel.hideDialogue();
        }).start();
        return true;
    }
}
