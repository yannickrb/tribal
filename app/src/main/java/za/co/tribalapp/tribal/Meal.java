package za.co.tribalapp.tribal;

/**
 * Created by Yannick on 2014/09/01.
 */
public class Meal
{
    //private variables
    int id;
    String name;
    int carbs;
    int fat;
    int protein;
    long date;

    // no-argument constructor
    public Meal() {}

    // full constructor
    public Meal(int id, String name, int carbs, int fat, int protein, long date){
        this.id = id;
        this.name = name;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
        this.date = date;
    }

    // partial constructor - for insertions
    public Meal(String name, int carbs, int fat, int protein){
        this.name = name;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }
    // getting ID
    public int getID(){
        return this.id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
    }

    // getting name
    public String getName(){
        return this.name;
    }

    // setting name
    public void setName(String name){
        this.name = name;
    }

    // getting carb content
    public int getCarbs(){
        return this.carbs;
    }

    // setting carb content
    public void setCarbs(int carbs){
        this.carbs = carbs;
    }

    // getting fat content
    public int getFat(){
        return this.fat;
    }

    // setting fat content
    public void setFat(int fat){
        this.fat = fat;
    }

    // getting protein content
    public int getProtein(){
        return this.protein;
    }

    // setting protein content
    public void setProtein(int protein){
        this.protein = protein;
    }

    // getting date of entry
    public long getDate(){
        return this.date;
    }

    // setting date of entry
    public void setDate(long date){ this.date = date;
    }
}
