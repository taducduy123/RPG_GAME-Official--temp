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
        
        System.out.printf("|%10s | %20s | %10s | %10s|\n", "No.",
                                                                        "Name",
                                                                        "Type",
                                                                        "State");

        for(int i = 0; i < this.size(); i++){
            System.out.printf("|%10d | %20s | %10d | %10s|\n", i + 1,
                                                                    this.items.get(i).getName(),
                                                                    this.items.get(i).getType(),
                                                                    this.items.get(i).getInUse() == true ? "V" : "");
        }
       
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
