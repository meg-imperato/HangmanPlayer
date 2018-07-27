import java.io.*;
import java.util.*;

public class Main {	
	
	//convert string to hangman format --> unknown letters as _
	public static String format(String word) { 
		word= word.replaceAll("\\.", " _ ");
		return word;
	}
	
	//create hashmap of letter frequencies of words w/ correct length and correct placement of known letters
	public static HashMap<Character, Integer> createMap(String answer, String cpuAnswer){
		HashMap<Character, Integer> map= new HashMap<Character, Integer>();
		//import dictionary and hash
		try {
			File file = new File("testDictionary.txt"); //provide dictionary file
			Scanner input = new Scanner(file); 
			while (input.hasNextLine()) {
				String word = input.nextLine();
				if (word.length()==answer.length() && word.matches(cpuAnswer)) { //correct length?
					for (int i = 0; i < word.length(); i++){ //iterate through letters in word
						Character letter=word.charAt(i);
						if (map.get(letter)!=null) { //if char in map, update frequency
							map.put(letter, (map.get(letter)+1));
						} else { //if char not in map, add and make frequency 1
							map.put(letter, 1);
						}
					}
				}	
			}
			input.close();
		} catch (Exception error) {
			error.printStackTrace();
		}
		return map;
	}
	
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in); 
		System.out.println("Let's play hangman! Enter a word.");
		String answer = sc.nextLine();
		
		String cpuAnswer =""; //this is what the cpu knows about the word to start with
		for (int i = 0; i < answer.length(); i++) {
			cpuAnswer+="."; // regex . matches any character
		}
		
		System.out.println(format(cpuAnswer));
		String pastGuesses="";
		
		int round=1;
		
		while (!cpuAnswer.equals(answer)) {
			System.out.println("\n********** Round #"+round+" **********");
			round++;
			
			HashMap<Character, Integer> map = createMap(answer, cpuAnswer);
			
			//determine most frequent character
			char maxFreqChar='e'; //place holder
			int maxFreq=0;
			for (Character k='a'; k<='z'; k++) {
				if (map.get(k)!=null && map.get(k)>maxFreq && !pastGuesses.contains(Character.toString(k))) { //update most freq char
					maxFreqChar=k;
					maxFreq=map.get(k);
				}
			}
			pastGuesses+=maxFreqChar; //track guess
			
			System.out.println("The cpu guesses: "+maxFreqChar);
			if (answer.contains(Character.toString(maxFreqChar))) {
				System.out.println("Good guess!");
				for (int i=0; i<answer.length(); i++) {
					if (answer.charAt(i)==maxFreqChar) //update cpuAnswer w guessed letter
						cpuAnswer= cpuAnswer.substring(0, i) + maxFreqChar + cpuAnswer.substring(i + 1);
				}
				System.out.println(format(cpuAnswer));
			} else {
				System.out.println("Incorrect, guess again!");
			}
		}
	}
}

