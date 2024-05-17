package GAMESTAGE;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import CHARACTER.*;
import ITEM.*;
import MAP.*;


public class GameStage implements Serializable
{
    private Player player;
    private Inventory invent;
    private Map map;

    private int currentMapNo;
    private int stageNo;
    
    private boolean isExit; 

    private final int maxNumPhasesUsingEachStage = 2;
    private final int inventorySize = 10;
    private static Scanner input = new Scanner(System.in);

//-------------------------------------------------------------------------------------------------------------------

    //Constructor
    public GameStage()
    {
        this.currentMapNo = 1;
        this.stageNo = 1;
        this.isExit = false;

        this.player = new Player();
        this.map = new Map("src\\InputFile\\map"+ currentMapNo + ".txt");
        this.invent = new Inventory(inventorySize);
    }

//---------------------------------------------- Map-Stage Section ----------------------------------------------------
    
    public void nextmap()
    {
        (this.currentMapNo)++;
        if(this.map.getBossPhase() != 1)
            (this.stageNo)++;
        this.map = new Map("src\\InputFile\\map"+ currentMapNo + ".txt");
    }

    public void resetstage()
    {
        this.stageNo = 1;
        this.currentMapNo = 1;
        this.map = new Map("src\\InputFile\\map"+ currentMapNo + ".txt");
    }

    public int getStageNo()
    {
        return this.stageNo;
    }

    public int getCurrentMapNo()
    {
        return this.currentMapNo;
    }

    public int getMaxNumPhasesUsingEachStage()
    {return this.maxNumPhasesUsingEachStage;}

    public void updateMap()
    {
        this.map.doWork(this.player, this.invent, this);
    }

    
//---------------------------------------- win-loose-exit-door condition-----------------------------------------

    public boolean win()                //(win = true ==> loose == false)  //(win = false ==> loose == true || false)
    {
        if(this.map.numberOfMonsters() == 0){
            return true;
        }
        else
            return false;
    }

    public boolean isExit()
    {
        return this.isExit;
    }

    public void setExitState(boolean stat)
    {
        this.isExit = stat;
    }

    public boolean loose()
    {
        if (this.player.getHP() == 0)
            return true;
        else
            return false;
    }

    public boolean isPlayeratDoor()
    {
        return this.map.containDoorAt(this.player.getX(), this.player.getY());
    }

    public int getBossPhase()
    {
        return this.map.getBossPhase();
    }


//--------------------------------------------------- Player Section ---------------------------------------------------

    public void playerAction() {
        System.out.println("\n*************************** What Do? ******************************");
        System.out.println("1. Move Up");
        System.out.println("2. Move Down");
        System.out.println("3. Move Left");
        System.out.println("4. Move Right");
        System.out.println("5. No Move");
        System.out.println("6. Attack");
        System.out.println("7. Inventory");
        System.out.println("8. Exit");
        int choice = getValidatedInput("Enter your choice: ");
        input.nextLine();                       //consume keyboard buffer

        switch (choice) {
            case 1:
                this.player.moveUp(this.map);
                break;
            case 2:
                this.player.moveDown(this.map);
                break;
            case 3:
                this.player.moveLeft(this.map);
                break;
            case 4:
                this.player.moveRight(this.map);
                break;
            case 5:
                break;
            case 6:
                playerAttack();
                break;
            case 7:
                playerInvent();
                break;
            case 8:
                this.isExit = true;
                break;
            default:
                System.out.println("\n>> ERROR: Invalid Choice! You should enter a number between 1 and 8!\n");
                playerAction();
                break;
        }
    }


    public void playerAttack(){ // Reminder: need to improve this function (too complex and should have print out in menu if avalible) || solution: None
        //Find all monsters in range of player
        List<Monster> targets = new ArrayList<Monster>();
        for(int i = 0; i < this.map.numberOfMonsters(); i++){
            if(player.collideMonster(this.map.getMonsterAtIndex(i)))
                targets.add(this.map.getMonsterAtIndex(i));
        }

        //Print all monsters in range so that player can pick one to attack
        if(targets.size() == 0)
        {
            System.out.println("\n>> No monster in range to attack! (Press [Enter] to continue)");
            input.nextLine();
            System.out.println("-------------------------------------------------------------------------------------------------------\n");
        }
        else
        {
            
            //Sort by current list of targets by HP (remember do not use Stream here)
            targets.sort((m1, m2) ->{return m1.getHP() - m2.getHP();});
                                            
            //Now, Let's display
            System.out.println("\n-------------------> Monsters in Your Range <----------------------");
            System.out.printf("| %-3s | %-25s | %-16s | %-10s |\n", "No.",
                                                                        "          Name",
                                                                        "       Type",
                                                                        "    HP");
            System.out.println("-------------------------------------------------------------------");
            String typeMonst = "";
            for(int i = 0; i < targets.size(); i++){

                //Classify monsters
                if(targets.get(i) instanceof RegularMonster)
                    typeMonst = "Regular Monster";
                else if(targets.get(i) instanceof TargetMonster)
                    typeMonst = "Target Monster";
                else if(targets.get(i) instanceof Boss)
                    typeMonst = "Boss";

                System.out.printf("| %-3s | %-25s | %-16s | %-10s |\n", " " + String.valueOf(i + 1), 
                                                                            targets.get(i).getName(),
                                                                            typeMonst,
                                                                            targets.get(i).getHP() + "/" + targets.get(i).getMaxHp());
            }
            System.out.println("-------------------------------------------------------------------");

            int choice;
            System.out.print("Enter a number (0: Exit || 1 - " + targets.size() + ") to attack monster: ");
            choice = getValidatedInput();
            input.nextLine();                   //consume keyboard buffer
            System.out.println("\n-------------------------------------------------------------------------------------------------------\n");
            if(choice > 0 && choice <= targets.size()){
                targets.get(choice - 1).takeDamage(this.player.getAttack());
            }
            else if(choice < 0 || choice > targets.size())
                System.out.println("\n>> ERROR: Invalid Choice! Please enter again.\n");
            else{           
                //System.out.println("\n-------------------------------------------------------------------------------------------------------\n");
                showGraphic();
                playerAction();
            }
        }
    }

      
    public void playerInvent() {
        if(this.invent.isEmpty()) {
            System.out.println("\n>> Empty inventory \n");
            System.out.println("Press [Enter] to continue");
            input.nextLine();
            System.out.println("---------------------------------------------------------------------------------------------\n");
        } else {
            this.invent.displayInventory();
            this.player.showState();
            boolean status = true;
            boolean status1 = true;
            do {
                int choice = getValidatedInput("Enter a number to show item (Exit: 0 | Range: 1 - " + this.invent.size() + "): ");
                input.nextLine();                        //consume keyboard buffer
                if(choice == 0) {
                    this.showGraphic();
                    status = false;
                } else if(0 < choice && choice <= this.invent.size()){
                    status1 = handleInventoryItem(choice);
                } else {
                    System.out.println("\n>> ERROR: Invalid Choice! Please enter again.\n");
                }
            } while(status == true && status1 == true);

            if(status1 == false)
                showGraphic();
            playerAction();
        }
    }
 
    
    private boolean handleInventoryItem(int choice) {
        boolean status1 = true;
        boolean shouldContinue = true;

        System.out.println("\n---------------------------------------------------------------------------------------------\n");
        if(this.invent.getItem(choice - 1) instanceof Weapon)
            System.out.println((this.invent.getItem(choice - 1)).toString());
        else if(this.invent.getItem(choice - 1) instanceof Armor)
            System.out.println((this.invent.getItem(choice - 1)).toString());
        else if(this.invent.getItem(choice - 1) instanceof Potion)
            System.out.println(this.invent.getItem(choice - 1).toString());
        do {
            int choice1 = getValidatedInput("1. Equip item\n" + 
                                            "2. Unequip item\n" + 
                                            "3. Remove item\n" + 
                                            "4. Back\n" + 
                                            "Enter your choice: ");
            input.nextLine();                           //consume keyboard buffer
            if(choice1 == 1){
                shouldContinue = useItem(choice);
                status1 = false;
            }
            else if(choice1 == 2){
                unequipItem(choice);
                status1 = false;
            }
            else if(choice1 == 3){
                shouldContinue = removeItem(choice);
                status1 = false;
            } else if(choice1 == 4){
                playerInvent();
                status1 = false;
            } else {
                System.out.println("\n>> ERROR: Invalid Choice! Please enter again.\n");
            }
        } while (status1 == true);

        return shouldContinue;
    }
 
    
    private boolean useItem(int choice) {

        boolean shouldContinue = true;

        if(this.invent.getItem(choice - 1) instanceof Weapon)
            this.player.equipWeapon(this.invent.getItem(choice - 1));
        else if (this.invent.getItem(choice - 1) instanceof Armor)
            this.player.equipArmor(this.invent.getItem(choice - 1));
        else if (this.invent.getItem(choice -1) instanceof Potion){
            this.player.equipPotion(this.invent.getItem(choice -1));
            this.invent.removeItem(choice - 1);
        }
        System.out.println("\n>> Equip successfully");
        System.out.println("\n---------------------------------------------------------------------------------------------");
     

        if(this.invent.isEmpty()){
            System.out.println("\n>> Empty Inventory\n");
            System.out.println("Press [Enter] to continue");
            input.nextLine();
            System.out.println("---------------------------------------------------------------------------------------------\n");
            showGraphic();
            playerAction();
            shouldContinue = false;
        }
        else{
            this.invent.displayInventory();
            this.player.showState();
        }
        return shouldContinue;
    }
  
    
    private void unequipItem(int choice){
        if(invent.getItem(choice - 1) instanceof Weapon){
            if(player.getWeapon() == null)
                System.out.println("\n>> Unequip fail. Player did not use any weapon!!!");
            else if(!player.getWeapon().equal((Weapon)invent.getItem(choice - 1)))
                System.out.println("\n>> Unequip fail. Wrong item to unequip!!!");
            else{
                player.unequipWeapon();
                System.out.println("\n>> Unequip successfully");
            }
        }
        else if(invent.getItem(choice - 1) instanceof Armor){
            if(player.getArmor() == null)
                System.out.println("\n>> Unequip fail. Player did not use any armor!!!");
            else if(!player.getArmor().equal((Armor)invent.getItem(choice - 1)))
                System.out.println("\n>> Unequip fail. Wrong item to unequip!!!");
            else{ 
                player.unequipArmor();
                System.out.println("\n>> Unequip successfully");
            }
        }
        else if(invent.getItem(choice - 1) instanceof Potion)
            System.out.println("\n>> Unequip fail! You cannot unequip Potion!!!");
        System.out.println("\n---------------------------------------------------------------------------------------------");
        this.invent.displayInventory();
        this.player.showState();
    }
  
    
    private boolean removeItem(int choice) {

        boolean shouldContinue = true;

        if(this.invent.getItem(choice - 1) instanceof Weapon && this.invent.getItem(choice - 1).getInUse() == true)
            this.player.unequipWeapon();
        else if(this.invent.getItem(choice - 1) instanceof Armor && this.invent.getItem(choice - 1).getInUse() == true)
            this.player.unequipArmor();
        this.invent.removeItem(choice - 1);
        System.out.println("\n>> Remove successfully");
        System.out.println("\n---------------------------------------------------------------------------------------------");

        if(this.invent.isEmpty()){
            System.out.println("\n>> Empty Inventory\n");
            System.out.println("Press [Enter] to continue");
            input.nextLine();
            System.out.println("---------------------------------------------------------------------------------------------\n");
            showGraphic();
            playerAction();
            shouldContinue = false;
        }
        else{
            this.invent.displayInventory();
            this.player.showState();
        }
        return shouldContinue;
    }
 
    
    private int getValidatedInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (input.hasNextInt()) {
                return input.nextInt();
            } else {
                System.out.println("ERROR: Invalid Input! Please enter \"A NUMBER\" in SCOPE of Menu.\nPlease enter again:");
                input.next();
            }
        }
    }


    private int getValidatedInput() {
        while (true) {
            if (input.hasNextInt()) {
                return input.nextInt();
            } else {
                System.out.println("ERROR: Invalid Input! Please enter \"A NUMBER\" in SCOPE of Menu.\nPlease enter again:");
                input.next();
            }
        }
    }


    public void messageToShow()
    {      
        if(player.detectMonsters(map) > 0)
        {
            JOptionPane.showMessageDialog(null, "WARNING: " 
                                                            + player.detectMonsters(map) 
                                                            + " monster(s) in front of you!!!");
        }          
    }


//---------------------------------------------------- Reset Player -------------------------------------------
    public void resetPlayerWhenDied(){
        this.player = new Player();
        this.invent = new Inventory(inventorySize);
    }

    
    public void resetPlayerWhenNextStage(){
        this.player.setXY(0, 0, map);
        this.player.heal(this.player.getMaxHp());
    }


//---------------------------------------------------- Monster Section -----------------------------------------
    public void monsterAction()
    {
        for(int i = 0; i < this.map.numberOfMonsters(); i++)
        {
            this.map.getMonsterAtIndex(i).doWork(this.player, this.map);
        }
    }


//----------------------------------------------------- Graphic Section ----------------------------------------
    public void showGraphic()
    {
        this.map.drawMap(this.player);
        this.player.showState();
        this.messageToShow();
    }


//-------------------------------------------- Save Game for the next time playing -----------------------------------------

    public void save(String filename) 
    {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameStage load(String filename) 
    {
        GameStage stage = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            stage = (GameStage) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("");
        }
        return stage;
    }

    public boolean delete(String filename) 
    {
        File file = new File(filename);
        return file.delete();
    }






//---------------------------------------------------- Embedded Main ---------------------------------
    public static void main(String[] args) 
    {
    

    }
}


