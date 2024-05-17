package ITEM;

import java.io.Serializable;
import java.util.*;
import javax.swing.JOptionPane;


public class Inventory implements Serializable
{
    private int cap;
    private List<Item> items;

//------------------------------------------------------------
    //Constructor
    public Inventory(int cap)
    {
        this.cap = cap;
        this.items = new ArrayList<>(cap);
    }

    //Basic Operations
    public void addItem(Item i){
        if(this.items.size() >= cap)
            JOptionPane.showMessageDialog(null, "Inventory is full!!!");
        else this.items.add(i);
    }

    public void removeItem(int index){
        if(this.items.size() < 0)
            JOptionPane.showMessageDialog(null, "Can't remove item!!!");
        this.items.remove(index);
    }

    public Item getItem(int index){
        return this.items.get(index);
    }

    public void clear(){
        this.items.clear();
    }

    public boolean isEmpty(){
        return this.items.isEmpty();
    }

    public int size(){
        return this.items.size();
    }

    public boolean isFull(){
        return this.items.size() >= cap ? true : false;
    }
    
    //Display
    public void displayInventory()
    {
        List<Item> sortedInven = this.items.stream()
                                        .sorted((i1, i2) ->{
                                                return i1.getType() - i2.getType();
                                        })
                                        .toList();

        System.out.println("\n--------------------------------------------> My Inventory <-------------------------------------------");
        System.out.printf("| %-3s | %-25s | %-4s | %-50s | %-5s |\n", "No.",
                                                                        "           Name",
                                                                        "Type",
                                                                        "                    Item Stats",
                                                                        "State");
        System.out.println("-------------------------------------------------------------------------------------------------------");
        for(int i = 0; i < this.size(); i++){
            System.out.printf("| %-3S | %-25s | %-4s | %-50s | %-5s |\n", " " + String.valueOf(i + 1),
                                                                            sortedInven.get(i).getName(),
                                                                            " " + String.valueOf(sortedInven.get(i).getType()),
                                                                            sortedInven.get(i).toString(),
                                                                            "  " + (sortedInven.get(i).getInUse() == true ? "V" : ""));
        }
        System.out.println("-------------------------------------------------------------------------------------------------------");
    }       
    
    
    //Embedded Main 
    public static void main(String[] args) {
        Inventory i = new Inventory(5);
        i.addItem(new Weapon("w1", 0, 0, 0, 0));
        i.addItem(new Weapon("w2", 0, 0, 0, 0));
        i.addItem(new Armor("a1", 0, 0, 0, 0));
        i.addItem(new Weapon("w3", 0, 0, 0, 0));
        i.addItem(new Armor("a2", 0, 0, 0, 0));
        //i.displayInventory();

       
        //i.displayInventory();
    }
}
