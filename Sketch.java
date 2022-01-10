import java.awt.*;
import java.util.*;

public class Sketch {
    public TreeMap <Integer,Shape> IDShapeMap = new TreeMap<>();
    private int ID = 0;


    //adding a shape, increasing id
    public void addShape(Shape shape){
        IDShapeMap.put(ID,shape);
        ID++;

    }

    public void removeShape(int i){
        IDShapeMap.remove(i);
    }

    //get id, and distance moved
    public void moveShape(int i, int dx, int dy){
        Shape shape = IDShapeMap.get(i);
        shape.moveBy(dx,dy);
    }

    //get id, and color to be
    public void setShapeColor(int i , int c){
        Shape shape = IDShapeMap.get(i);
        shape.setColor(new Color(c));
    }

    public Map<Integer,Shape> getIDShapeMap(){
        return IDShapeMap;
    }

    //find the id of a shape
    public Integer getID(Shape s){

        for(int ID: IDShapeMap.keySet()){
            if(IDShapeMap.get(ID).equals(s)) return ID;
        }

        System.out.println("ID not found");
        return null;
    }

    public NavigableSet<Integer> getDescendingSet(){
        return IDShapeMap.descendingKeySet();
    }
    public NavigableSet<Integer> getAscendingSet(){
        return IDShapeMap.navigableKeySet();
    }

}

