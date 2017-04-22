package qt;

import java.io.*;
import java.util.Scanner;

/**
 * UI for the QueryTree class
 * @author Arshana Jain
 * @version December 5, 2016
 */
public class QueryTreeClient {
	/**
	 * Determines whether or not to readIn the previous QueryTree
	 * @param tree QueryTree to potentially readIn
	 * @param input Scanner with which to accept user's response
	 * @return QueryTree that might have been readIn
	 * @throws IOException
	 */
	private static QueryTree toReadIn(QueryTree tree, Scanner input) throws IOException{
		if(validEntryLoop(input, "Do you want to read in the previous tree?").equals("y")){
			tree.readIn(new File("latestTree.txt"));
		}
		return tree;
	}
	
	/**
	 * Loop that determines whether or not user input is valid
	 * @param input Scanner from which to get user input
	 * @param phrase question to ask user
	 * @return first valid user input
	 */
	private static String validEntryLoop(Scanner input, String phrase){
		System.out.print(phrase + " (y/n)? ");
		String affirm = input.nextLine().trim().toLowerCase();
		while(!(affirm.equals("y") || affirm.equals("n"))){
			System.out.println("That was an invalid entry.");
			System.out.print(phrase + " (y/n)? ");
			affirm = input.nextLine().trim().toLowerCase();
		}
		return affirm;
	}
	
	/**
	 * UI: Allows user to interact with QueryTree class
	 * @param args command-line arguments
	 * @throws IOException if File used in invalid
	 */
	public static void main(String[] args) throws IOException{
		Scanner input = new Scanner(System.in);
		QueryTree tree = new QueryTree();
		System.out.println("Welcome to the QueryTree game.");
		
		tree = toReadIn(tree, input);
		System.out.println();
		
		boolean toRepeat = true;
		while(toRepeat){
			System.out.println("Please think of an object for me to guess.");
			while(tree.hasNextQuestion()){
				tree.userResponse(validEntryLoop(input, tree.nextQuestion()).charAt(0));
			}
			
			if(validEntryLoop(input, ("Would your object happen to be " + tree.finalGuess() + "?")).equals("y")){
				System.out.println("Great, I got it right!");
				tree.reset();
			}
			
			else{
				System.out.print("What is the name of your object? ");
				String obj = input.nextLine().trim();
				System.out.print("Please give a yes/no question that distinguishes between your object and mine--> ");
				String question = input.nextLine().trim();
				tree.updateTree(question, obj, validEntryLoop(input, "And what is the answer for your object?").charAt(0));
			}
			
			System.out.println();
			toRepeat = validEntryLoop(input, "Do you want to go again?").equals("y");
		}
		
		tree.writeOut(new File("latestTree.txt"));
		System.out.println("Bye!");
	}
}