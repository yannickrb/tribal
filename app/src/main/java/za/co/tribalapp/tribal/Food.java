package za.co.tribalapp.tribal;

/**
 * Created by Yannick on 2014/08/22.
 */
public class Food {

    //private variables
    int id;
    String name;
    float carbs;
    float fat;
    float protein;
    float c_measure; //common measure, i.e. typical grams per portion
    String unit; //e.g serving, cup

    // Empty constructor
    public Food() {}

    // constructor
    public Food(int id, String name, float carbs, float fat, float protein, float c_measure, String unit){
        this.id = id;
        this.name = name;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
        this.c_measure = c_measure;
        this.unit = unit;
    }

    // constructor
    public Food(String name, float carbs, float fat, float protein, float c_measure, String unit){
        this.name = name;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
        this.c_measure = c_measure;
        this.unit = unit;
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
    public float getCarbs(){
        return this.carbs;
    }

    // setting carb content
    public void setCarbs(int carbs){
        this.carbs = carbs;
    }

    // getting fat content
    public float getFat(){
        return this.fat;
    }

    // setting fat content
    public void setFat(int fat){
        this.fat = fat;
    }

    // getting protein content
    public float getProtein(){
        return this.protein;
    }

    // setting protein content
    public void setProtein(int protein){
        this.protein = protein;
    }

    // getting common measure
    public float getCMeasure(){
        return this.c_measure;
    }

    // setting common measure
    public void setCMeasure(int protein){
        this.c_measure = c_measure;
    }

    // getting unit
    public String getUnit(){
        return this.unit;
    }

    // setting unit
    public void setUnit(String unit){
        this.unit = unit;
    }
}
