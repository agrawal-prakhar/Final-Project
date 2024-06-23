package MathQuiz;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.Scanner;

public class MathWorld {
    public static int arrayLength;
    static int[] randomNumsArray;
    static boolean correct = false; //check if equal
    int answer;
    String displayProblem = "";


    public MathWorld(int initN) { // pass in the number of different arguments you want when calling the math class(like 1+1 is 2 arguments, while 1+1+1 is 3)
        arrayLength = initN;
        randomNumsArray = new int[arrayLength];
        generateRandom();
        //set which type of problem in settings, add switch to determine which problem to do
    }

    public static void generateRandom(){
        for (int i = 0; i< randomNumsArray.length; i++){
            randomNumsArray[i] = (int) (Math.random()*100);
        }
    }

    public void checkAnswer(int answer, String sign){
        //switch for each option +, -, *
        switch (sign){
            case "+":
                correct = (answer == CalculateMath.add(randomNumsArray));
                break;

            case "-":
                correct = (answer == CalculateMath.subtract(randomNumsArray));
                break;

            case "X":
                correct = (answer == CalculateMath.multiply(randomNumsArray));
        }

    }
    public void displayMathProblem(Graphics g, String sign) {
        //switch for if add, if multiply, if subtract, then display + or - or * instead

        displayProblem = "";
        Font font = new Font(Font.MONOSPACED, Font.BOLD, 50);
        g.setColor(Color.WHITE);
        g.setFont(font);
        for (int i = 0; i < arrayLength; i++) {
            if (i == arrayLength-1) {
                sign = " ";
            }
            displayProblem = displayProblem.concat(Integer.toString(randomNumsArray[i])) + " "+sign+" ";
        }

        switch (randomNumsArray.length){
            case 2:
                g.drawString(displayProblem, 350, 200);
                break;
            case 3:
                g.drawString(displayProblem, 300, 200);
                break;
            case 4:
                g.drawString(displayProblem, 250, 200);
                break;
            default:
                break;
        }


    }

    public int [] getRanNumsArray() {
        return randomNumsArray;
    }

    public void setNumArray(int [] problem) {
        randomNumsArray = problem;
    }

}