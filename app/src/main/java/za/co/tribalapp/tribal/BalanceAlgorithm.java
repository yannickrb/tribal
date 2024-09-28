package za.co.tribalapp.tribal;

/**
 * Created by Yannick on 2014/08/21.
 * Balance Algorithm calculation.
 */
public class BalanceAlgorithm
{
    //Variable declarations and initializations - ranges not included yet
    float carbIndex, fatIndex, proteinIndex, dietIndex = 0; //diet
    float cardioIndex, strengthIndex, exerciseIndex = 0; //log_exercise
    float sleepIndex, workIndex, stressIndex = 0; //stress
    int balIndex;
    float carbTargetMin;
    float carbTargetMax;
    float fatTargetMin;
    float fatTargetMax;
    float proteinTargetMin;
    float proteinTargetMax;
    float cardioTargetMin;
    float strengthTargetMin;
    float workTargetMax;
    float sleepTargetMin;

    //no-argument constructor
    public BalanceAlgorithm()
    {
    }

    public BalanceAlgorithm(int carbTargetMin,
                            int carbTargetMax,
                            int fatTargetMin,
                            int fatTargetMax,
                            int proteinTargetMin,
                            int proteinTargetMax,
                            int cardioTargetMin,
                            int strengthTargetMin,
                            int workTargetMax,
                            int sleepTargetMin)
    {
        this.carbTargetMin = (float)carbTargetMin;
        this.carbTargetMax = (float)carbTargetMax;
        this.fatTargetMin = (float)fatTargetMin;
        this.fatTargetMax = (float)fatTargetMax;
        this.proteinTargetMin = (float)proteinTargetMin;
        this.proteinTargetMax = (float)proteinTargetMax;
        this.cardioTargetMin = (float)cardioTargetMin;
        this.strengthTargetMin = (float)strengthTargetMin;
        this.workTargetMax = (float)workTargetMax;
        this.sleepTargetMin = (float)sleepTargetMin;
    }

    //Calculate balance index
    public void calcBalanceIndex()
    {
        balIndex = (int)((dietIndex + exerciseIndex + stressIndex)/3 * 100);
    }

    //Calculate diet index
    public void calcDietIndex(int carbIntake, int proteinIntake, int fatIntake)
    {
        if (carbIntake < carbTargetMin) {
            carbIndex = carbIntake/carbTargetMin;
        }
        else if (carbIntake > carbTargetMax) {
            carbIndex = carbIntake/carbTargetMax;
        }
        else
        {
            carbIndex = 1;
        }

        if (proteinIntake < proteinTargetMin) {
            proteinIndex = proteinIntake/proteinTargetMin;
        }
        else if (proteinIntake > proteinTargetMax)
        {
            proteinIndex = proteinIntake/proteinTargetMax;
        }
        else
        {
            proteinIndex = 1;
        }

        if (fatIntake < fatTargetMin) {
            fatIndex = fatIntake/fatTargetMin;
        }
        else if (fatIntake > fatTargetMax) {
            fatIndex = fatIntake/fatTargetMax;
        }
        else
        {
            fatIndex = 1;
        }
        dietIndex = (carbIndex + proteinIndex + fatIndex) / 3;
    }

    //Calculate log_exercise index
    public void calcExerciseIndex(int cardioActual, int strengthActual)
    {
        if (cardioActual < cardioTargetMin) {
            cardioIndex = cardioActual/cardioTargetMin;
        }
        else {
            cardioIndex = 1;
        }
        if (strengthActual < strengthTargetMin) {
            strengthIndex = strengthActual/strengthTargetMin;
        }
        else {
            strengthIndex = 1;
        }
        exerciseIndex = (cardioIndex + strengthIndex) / 2;
    }

    //Calculate stress index
    public void calcStressIndex(int workActual, int sleepActual)
    {
        if (workActual > workTargetMax) {
            workIndex = workActual/workTargetMax;
        }
        else {
            workIndex = 1;
        }

        if (sleepActual < sleepTargetMin) {
            sleepIndex = sleepActual/sleepTargetMin;
        }
        else {
            sleepIndex = 1;
        }

        stressIndex = (float)(0.6*sleepIndex) + (float)(0.4*workIndex); //60-40 weighting as per research
    }

    //return indices
    public int getDietIndex()
    {
        return (int) (dietIndex * 100);
    }

    public int getCarbIndex() { return (int) (carbIndex * 100); }

    public int getFatIndex() { return (int) (fatIndex * 100); }

    public int getProteinIndex() { return (int) (proteinIndex * 100); }

    public int getExerciseIndex()
    {
        return (int) (exerciseIndex * 100);
    }

    public int getCardioIndex() { return (int) (cardioIndex * 100); }

    public int getStrengthIndex() { return (int) (strengthIndex * 100); }

    public int getStressIndex()
    {
        return (int) (stressIndex * 100);
    }

    public int getWorkIndex() { return (int) (workIndex * 100); }

    public int getSleepIndex() { return (int) (sleepIndex * 100); }

    public int getBalIndex() { return balIndex; }
}
