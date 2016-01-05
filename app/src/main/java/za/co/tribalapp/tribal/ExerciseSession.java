package za.co.tribalapp.tribal;

/**
 * Created by Yannick on 2014/09/12.
 */
public class ExerciseSession
{
    //private variables
    int id;
    String name;
    String type;
    int duration;
    int hrr;
    long date;

    // no-argument constructor
    public ExerciseSession() {}

    // full constructor
    public ExerciseSession(int id, String name, String type, int duration, int hrr, long date){
        this.id = id;
        this.name = name;
        this.type = type;
        this.duration = duration;
        this.hrr = hrr;
        this.date = date;
    }

    // partial constructor - for insertions
    public ExerciseSession(String name, String type, int duration, int hrr){
        this.name = name;
        this.type = type;
        this.duration = duration;
        this.hrr = hrr;
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

    // getting session type
    public String getType(){
        return this.type;
    }

    // setting session type
    public void setType(String type){
        this.type = type;
    }

    // getting session duration
    public int getDuration(){
        return this.duration;
    }

    // setting session duration
    public void setDuration(int duration){
        this.duration = duration;
    }

    // getting session duration
    public int getHRR(){
        return this.hrr;
    }

    // setting session duration
    public void setHRR(int hrr){
        this.hrr = hrr;
    }

    // getting date of entry
    public long getDate(){
        return this.date;
    }

    // setting date of entry
    public void setDate(long date){
        this.date = date;
    }
}
