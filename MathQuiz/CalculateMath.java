package MathQuiz;

public class CalculateMath {


    public CalculateMath() {
    }

    public static int add(int [] numArray){
        int total = 0;
        for (int i = 0; i< numArray.length; i++){
            total+= numArray[i];
        }
        return total;
    }
    public static int subtract(int [] numArray){
        int difference = numArray[0];
        for (int i = 1; i< numArray.length; i++){
            difference-= numArray[i];
        }
        return difference;

    }
    public static int multiply(int [] numArray){
        int product = 1;
        for (int i = 0; i< numArray.length; i++){
            product*= numArray[i];
        }
        return product;
    }
}
