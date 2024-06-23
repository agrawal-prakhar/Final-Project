package Wordle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

class World{

    int height;
    int width;
    int x = 0;

    HashSet<String> set = new HashSet<>();

    public World(int initWidth, int initHeight){
        width = initWidth;
        height = initHeight;

    }

    //returns whether the word guessed exists in the hash set or not
    public boolean doesWordExist(String userWord){
        return set.contains(userWord.toLowerCase());
    }

    //colors the guessed letters appropriately according the rules in ReadMe
    public String[] colorWord(String word, String userWord){

        String[] colorArray = new String[5];

        char[] wordArray = word.toCharArray();
        char[] userWordArray = userWord.toCharArray();

        for (int i = 0; i<5; i++){
            colorArray[i] = "grey";

            if (wordArray[i] == userWordArray[i]){
                colorArray[i] = "green";
                wordArray[i] = '#';
            }

        }

        for (int i = 0; i<5; i++){
            for (int j = 0; j<5; j++){
                if (colorArray[i].equals("green")){
                    break;
                }
                if (userWordArray[i] == wordArray[j]){
                    colorArray[i] = "yellow";
                    wordArray[j] = '#';
                    break;
                }
            }

        }

        return colorArray;
    }



    public String readFile(){
        try {
            //reads all the words from the file and puts it into the set
            File myObj = new File("Wordle/SampleWords/fiveLetterWords.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {

                String data = myReader.nextLine();
                set.add(data);
                x++;
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //picks out a random word from all the words in the file and returns it
        Random random = new Random();
        String[] wordArray = set.toArray(new String[set.size()]);
        String randomElement = wordArray[random.nextInt(wordArray.length)];
        System.out.println(randomElement);

        return randomElement;

    }
}

//class used for keys of virtual keyboard, having fields of their position on the screen etc
class VirtualKeyboardKey{
    int startX;
    int width = 55;
    int startY;
    int height = 55;
    char letter;


    public VirtualKeyboardKey(){

    }
}