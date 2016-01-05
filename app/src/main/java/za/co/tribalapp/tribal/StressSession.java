package za.co.tribalapp.tribal;

/**
 * Created by Yannick on 2014/09/12.
 */
public class StressSession
{
    //private variables
    int id;
    String type;
    int duration;
    int hrv;
    long date;

    // no-argument constructor
    public StressSession() {}

    // full constructor
    public StressSession(int id, String type, int duration, int hrv, long date){
        this.id = id;
        this.type = type;
        this.duration = duration;
        this.hrv = hrv;
        this.date = date;
    }

    // partial constructor - for insertions
    public StressSession(String type, int duration, int hrv){
        this.type = type;
        this.duration = duration;
        this.hrv = hrv;
    }
    // getting ID
    public int getID(){
        return this.id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
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
    public int getHRV(){
        return this.hrv;
    }

    // setting session duration
    public void setHRV(int hrv){
        this.hrv = hrv;
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
