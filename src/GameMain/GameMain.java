package GameMain;

import java.util.Scanner;

import GAMESTAGE.GameStage;




public class GameMain 
{
    private GameStage stage;
    
    private final int maxMapNum = 4;
    private final String fileName = "Data.ser";

    private static Scanner input = new Scanner(System.in);

//----------------------------------------------------------------

    //Constructor
    public GameMain()
    {
        System.out.print("Enter your name: ");
        String name = input.nextLine();
        System.out.println("Hello \"" + name + "\"! Let's start the game." );
    }


//---------------------------------------------- Run -------------------------------------------------
    public void Run()
    {
        int choice;
        do
        {      
            System.out.println("\n******************************************************************");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> HOME MENU <<<<<<<<<<<<<<<<<<<<<<<<<<<");
            System.out.println("******************************************************************");
            System.out.println("1. New Game");
            System.out.println("2. Continue Game");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = getValidatedInput();
            input.nextLine();                //Consume keyboard buffer

            switch (choice) 
            {
                case 1:
                        this.stage = new GameStage();
                        startStage();
                        break;

                case 2: // reminder: add the sistuation where it cannot find the file
                        this.stage = GameStage.load(fileName);
                        if(this.stage == null)
                        {
                            System.out.println("Not Found Current Game Data!");
                            break;
                        }
                        this.stage.setExitState(false);
                        startStage();
                        break;

                case 3:

                        System.out.println("See you next time!");
                        input.close();
                        break;

                default:
                        System.out.println("ERROR: Invalid Choice! You should enter a number betweent 1 and 3.");
                        break;

            }
        } while (choice != 3);
    }
 

//---------------------------------------- Loop of Stages --------------------------------------------------
    //run a specific stage with current player, map, inventory (Notice when player want to pause program)
    public void startStage()  //~~ Stages Loop
    {   

        if(this.stage.getBossPhase() == 0)       //if now is regular stage
        {
            System.out.println("\n***********************************************************************************************************************");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Welcome to Stage #" + this.stage.getStageNo() + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            System.out.println("***********************************************************************************************************************");
            System.out.println("Press [Enter] to continue: ");
            input.nextLine();
        }
        else                                      //if now is boss stage
        {
            System.out.println("\n***********************************************************************************************************************");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Welcome to Stage #" + this.stage.getStageNo() 
                                                                                    + " (phase #" 
                                                                                    + this.stage.getBossPhase()
                                                                                    + ") <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            System.out.println("***********************************************************************************************************************");
            System.out.println("Press [Enter] to continue: ");
            input.nextLine();
        }

        stage.showGraphic();

        do 
        {
            stage.playerAction();
            if(!stage.isExit())
            {
                stage.monsterAction();
                stage.updateMap();    
                stage.showGraphic();
            }
            stage.save(fileName);
        } while (!stage.win() && !stage.loose() && !stage.isExit());    

         
        if(stage.win())
        {
            if(!(this.stage.getBossPhase() > 0))
            {   
                while(!this.stage.isPlayeratDoor() && !stage.isExit() && !stage.loose())
                {
                    stage.playerAction();
                    stage.updateMap();
                    //1if(!this.stage.isPlayeratDoor())       
                        stage.showGraphic();
                    stage.save(fileName);
                }

                if(this.stage.isPlayeratDoor())
                {
                    System.out.println("Congratulations!! You Passed Stage # "+ this.stage.getStageNo() + "! (Press [Enter] to continue)");
                    input.nextLine();
                    this.stage.resetPlayerWhenNextStage();
                    this.stage.nextmap();
                    this.startStage();
                    stage.save(fileName);
                }
                
            }
            else
            {
                if(this.stage.getCurrentMapNo() != this.maxMapNum)
                {   
                    System.out.println("WARNING: You Have Defeated The Boss!! However, you feel chill down your spine! (Press [Enter] to continue)");
                    input.nextLine();
                    System.out.println("WARNING: The Boss Was Awoken!! Let's prepare to make fight! (Press [Enter] to continue)");
                    input.nextLine();
                    this.stage.nextmap();
                    this.startStage();
                    stage.save(fileName);
                }
                else
                {
                    while(!this.stage.isPlayeratDoor() && !stage.isExit() && !stage.loose())
                    {
                        stage.playerAction();
                        stage.updateMap();
                        stage.showGraphic();
                        stage.save(fileName);
                    }

                    if(this.stage.isPlayeratDoor())
                    {
                        int choice =0;
                        System.out.println("Congratulations!! You Win Entire Game! (Press [Enter] to continue):");
                        input.nextLine();
                        stage.save(fileName);
                        System.out.println("Do you want to carry on the items you achived and go on another adventure? (0:No | 1:Yes)");
                        choice = input.nextInt();           //consume keyboard buffer
                        switch (choice) {
                            case 0:
                                System.out.println("You will now be sent back to Home Menu (Press [Enter] to continue)");
                                input.nextLine();
                                break;
                            case 1:
                                System.out.println("You will now go on another adventure! (Press [Enter] to continue)");
                                input.nextLine();
                                this.stage.resetstage();
                                this.stage.resetPlayerWhenNextStage();
                                this.startStage();
                                break;
                            default:
                                break;
                        }
                    }
                }
            }           
        }      
        if(stage.loose())
        {
            stage.resetstage();
            stage.resetPlayerWhenDied();
            stage.delete(fileName);
            System.out.println(">> You Died!! Let's Start At Beginning! (Press [Enter] to continue)" );
            input.nextLine();
        }     
    }

    private int getValidatedInput() {
        while (true) {
            if (input.hasNextInt()) {
                return input.nextInt();
            } else {
                System.out.print("ERROR: Invalid input! You should enter a number betweent 1 and 3.\nPlease enter again: ");
                input.next();
            }
        }
    }



    public static void main(String[] args) {
        GameMain gmt = new GameMain();
        gmt.Run();
    }
}
