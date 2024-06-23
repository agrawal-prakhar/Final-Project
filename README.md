# Smart-Alarm-Clock

**Instructions**

How to Compile and Run

Go inside the src folder and compile all the files javac */*.java Use java Main to run the program

How To Use

The main alarm

This program allows you to set a maximum of 6 alarms by typing the time you want to set it for (in military time or 24-hour clock). After typing the time, you can choose a game from the drop down menu which you want to play to make the alarm stop after it starts ringing (For the flappy game, you can choose a level of difficulty with a greater number meaning a greater level of difficulty). You can press ‘Set Alarm’ to initiate it, and to set more alarms, you can just type a new time, and repeat the same process.

If you type a time, for which the alarm is already set, the program will tell you that an alarm is already set for that time and give you an error.
Once you are able to set an alarm, you will see multiple features including:

1. One to turn off/on your alarm
2. One that shows what time and how much time will that particular alarm ring in
3. One that shows which game will start once the alarm rings off
4. One to delete the alarm
5. One to name the alarm
   
Once you press the ‘save’ button, the alarms that are currently on the screen are saved into an external file, and they are reloaded into the main program if you run it again later.

Once the current time matches one of the alarm times, the alarm will go off, and the program will ask you to play the necessary game to turn it off. It will then redirect you to one of the following:

**Flappy Game**

The ‘Flappy Game’ is a keyboard-interactive game with one simple goal: collect enough hammers to shut the alarm off. To determine how many hammers a user needs to collect, they specify a specific difficulty level from the drop down menu on the alarm page.
In the game, the clock has a downward acceleration and zero horizontal velocity (in relation to the frame as a whole). The the following keys can be used to keep control the clock:

1. ‘w’ key is used to to make the alarm jump, or stop it from moving down
 2. ‘d’ key is used to give it a velocity towards the right (in relation to the frame as a whole)
3. ‘a’ key is used to give it a velocity towards the left (in relation to the frame as a whole)
4. ‘s’ key is used to stop it moving the in the horizontal direction/make it’s horizontal velocity zero (in relation to the frame as a whole)
   
Using these keys, the user has to ensure that they do not crash into the bars and instead always try to pass between the gap between the bars. The length of the upper and lower bar is randomly generated, however the gap between them is kept constant (ensuring that one bar doesn’t become too big and the other not too small).

Randomly, there are hammers that travel between the bars, which the user tries collect by touching the alarm to the hammers to break the clock (trying to reach the goal that they chose in the difficulty when setting up the alarm), and when enough hammers are collected, the alarm stop and the program goes back to the main menu.

If the user hits the bars, one of the users collected hammers is deducted and the clock goes back to its default settings of acceleration, velocity, and position. The ‘high score’ on the top left tells you the maximum number of hammers collected during the game. Note that it’ll give you the all time high score as it is saved in an external file, and not just for that particular time that you are playing the game.

**Math Problems**

The ‘Math Problems’ part is pretty simple to use and self-explanatory. You need to solve 3 problems to make the alarm stop.
The first problem will always be a multiplication between 2 numbers, both between 1 and 20 randomly generated. This can be a complex calculation sometimes, and that is deliberately done so that the user either has to think a lot to solve it, use a pen and paper, or open a calculator and do it, all of which should take effort, and would make the user more awake.

The second problem will be either a subtraction or an addition (randomly chosen operation) between 3 randomly generated numbers between 1 and 100, while the third and the final test would be again either a subtraction or an addition but between 4 randomly generated numbers between 1 and 100.
Some things to note are:

1. The user can just type the answer using their keyboard in the boxes provided. There are validation checks so they would only be able to input the numbers and the ‘-’ sign for negative numbers
2. The user can press the enter key to check their answer and move to the next question
3. If the user answers all three questions correctly, then they would be prompted to press
the ‘esc’ key, after pressing which the alarm will stop and the program will terminate!
4. If the user answers any question incorrectly, they will be given the option to either solve the problem again, or skip to a different question.
5. If they skip to a different question, it doesn’t mean that they have to answer 3 new questions all over again, they just get a different problem set for that particular question round (out of 3 rounds), and have to get one question right in all the 3 rounds to make the alarm stop!
   
**Wordle**

How this game works is that the computer chooses a five-letter word (which can be any five-letter word in the english alphabet) by random and your task is to guess it in 8 tries.
When making a guess, to input a letter, you can just type it using your keyboard or you can choose to use your mouse to type the word using the virtual keyboard! You can also use the delete button to remove a letter and the enter button when you are sure with your guess. Notice you cannot type any other character from your keyboard except lower and upper case alphabets. Whenever you try to guess a word, the program will only accept it if it is what it considers a valid 5-letter words (the words in its pre-defined dictionary, which are pretty much all the five letter words that exist), otherwise it will just reject it by displaying an error message and deleting the letters for you.

If the guess is considered valid, the program will color the letters according to this scheme:
1. If the letter is in the word AND at the right place, then it will be colored green.
2. If the letter is in the word BUT not at the right place, then it will be colored yellow.
3. If the letter is not in word, it will be colored gray.


This color scheme will also be reflected on the virtual keyboard so that the user can use that as a reference to which letters they have already guessed and which they have not.
In addition, with words where there are repeating alphabets, this is how the program addresses that:

Say the pre-defined word chosen by the computer is ‘WOODS’:
1. Green will take precedence over yellow and gray (and yellow over gray), so if your guess is ‘DRONE’ then the ‘O’ will be colored green and not yellow.
2. If you guess ‘SOCKS’ then the first ‘S’ will be colored gray while the ‘S’ in the 5th position will be colored green.
3. If you guess ‘BLOOD’ then the ‘O’ in the 3rd position will be colored green, while the O in the 4th position will be colored yellow.
   
The coloring in the virtual keyboard also follows similar rules where green takes precedence over yellow, and yellow over gray. So, if your guess contains a repeated letter, one of which is colored yellow and the other colored green in the guess, the key in the virtual keyboard will be colored green and so on.

If you are not able to solve the Wordle correctly in 8 guesses, the program will tell you what the word actually was, and then it will ask you to press space and play another round, (with a newly generated word and repeating the same process as above) and the alarm will continue to ring, till you guess it correctly. This is done so that the user just doesn’t randomly type some words, gets them wrong and goes to sleep; the goal is to make them think so that they really wake up.

(To test the following part, you can check the terminal where the right word will get printed out, type it out and get the word correct in the first or whichever number try) If you do get the wordle right, the alarm will stop. But the game is not over and there is a fun twist! The lesser the guesses you took to guess the right word, the more coins you will get. If you guessed it correctly in the 1st try, you will get 35 coins, for the 2nd try, you will get 30 coins and so on.

You can now press space and play the game again (without the alarm ringing of course), but this time if you have more than 10 coins you have the option to use hints. Every hint takes up 10 of your coins (and they transfer to next game if you continue playing wordle) and it gives you one right letter in its right position as a hint. Some things to note about the hints are:

1. It will give you the closest letter from the left as your hint, given that it is not already correctly guessed by you. So if you haven’t guessed any word, one hint will give you the first correct letter, but if you have guessed the first letter, than it will give you the second letter as your hint and so on.
2. If all the letters have already been correctly guessed (in different guesses), then it will not give you a hint and not deduct your coins as it recognizes that you already have the word, and you just need to put the letters together!
   
You can play the game for as long as you want, or you can press ‘h’ to go to home/the main alarm screen, whenever you’re done, and we hope you have fun with it!
You will not be able to go back to the home screen or stop the alarm, unless you guess at least one word correctly, unless you exceed 5 minutes, at which it automatically puts you back to the main screen (so that alarm doesn’t ring infinitely)
