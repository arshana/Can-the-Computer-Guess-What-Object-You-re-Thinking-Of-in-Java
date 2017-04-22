package qt;

import java.io.*;
import java.util.Scanner;

/**
 * Creates and maintains a QueryTree. 
 * Follows the idea of the "Twenty Questions" game.
 * @author Arshana Jain
 * @version December 5, 2016
 */
public class QueryTree {
	//overall root of QueryTree
	private QueryTreeNode overallRoot;
	//current location in QueryTree (as per the UI client class)
	private QueryTreeNode current;
	
	/**
	 * Creates a QueryTree with a single, default item
	 */
	public QueryTree(){
		overallRoot = new QueryTreeNode("computer", null, null);
		//starting location
		current = overallRoot;
	}
	
	/**
	 * Checks if there is another question in QueryTree
	 * @return whether or not there is another question in the QueryTree
	 */
	public boolean hasNextQuestion(){
		return !current.isLeaf();
	}
	
	/**
	 * Returns next question in QueryTree
	 * @return next question in QueryTree
	 */
	public String nextQuestion(){
		if(current.isLeaf()){
			throw new IllegalStateException();
		}
		return current.data;
	}
	
	/**
	 * Updates current location in QueryTree according user's response
	 * @param input user's response
	 */
	public void userResponse(char input){
		//cannot update location once final guess is reached
		if(current.isLeaf()){
			throw new IllegalArgumentException();
		}
		if(Character.toLowerCase(input) == 'y'){
			current = current.yes;
		}
		else if(Character.toLowerCase(input) == 'n'){
			current = current.no;
		}
		else{
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Returns the final guess based on all the answers so far
	 * @return final guess
	 */
	public String finalGuess(){
		//can't return a question as a final guess
		if(!current.isLeaf()){
			throw new IllegalStateException();
		}
		return current.data;
	}
	
	/**
	 * Updates QueryTree with user's input if final guess is not correct
	 * @param q question associated with provided object
	 * @param item user's new object
	 * @param input answer for provided object when associated question is asked
	 */
	public void updateTree(String q, String item, char input){
		if(!current.isLeaf()){
			throw new IllegalStateException();
		}
		String currVal = current.data;
		current.data = q;
		if(Character.toLowerCase(input) == 'y'){
			current.no = new QueryTreeNode(currVal, null, null);
			current.yes = new QueryTreeNode(item, null, null);
		}
		else if(Character.toLowerCase(input) == 'n'){
			current.yes = new QueryTreeNode(currVal, null, null);
			current.no = new QueryTreeNode(item, null, null);		}
		else{
			throw new IllegalArgumentException();
		}
		reset();
	}
	
	/**
	 * Resets current location to that of overall root
	 */
	public void reset(){
		current = overallRoot;
	}
	
	/**
	 * Writes QueryTree out to a provided File
	 * @param f provided File 
	 * @throws IOException if File doesn't exist or can't be written to
	 */
	public void writeOut(File f) throws IOException{
		if(!f.exists() || !f.canWrite()){
			throw new IOException("File either doesn't exist or can't be written to.");
		}
		if(overallRoot != null){
			writeOut(new PrintStream(f), overallRoot);
		}
	}
	
	/**
	 * Recursive method that writes out the QueryTree to a File using provided PrintStream
	 * @param output PrintStream to use to write QueryTree to File
	 * @param current location in QueryTree to write to File
	 */
	private void writeOut(PrintStream output, QueryTreeNode current){
		if(current.isLeaf()){
			output.println("A:");
			output.println(current.data);
		}
		else{
			output.println("Q:");
			output.println(current.data);
			writeOut(output, current.no);
			writeOut(output, current.yes);
		}
	}
	
	/**
	 * Reads a File and sets up QueryTree accordingly
	 * @param f File containing instructions for QueryTree set up
	 * @throws IOException if File doesn't exist or can't be read
	 */
	public void readIn(File f) throws IOException{
		if(!f.exists() || !f.canRead()){
			throw new IOException("File either doesn't exist or can't be read.");
		}
		Scanner file = new Scanner(f);
		if(!file.hasNext()){
			throw new IllegalArgumentException("Empty File.");
		}
		overallRoot = readIn(file);
		current = overallRoot;
	}
	
	/**
	 * Recursive method that uses a provided Scanner to read in a File and set up QueryTree accordingly
	 * @param file Scanner with link to File to read
	 * @return each new QueryTreeNode to attach to QueryTree
	 */
	private QueryTreeNode readIn(Scanner file){
		if(!file.hasNextLine()){
			return null;
		}
		String type = file.nextLine().trim();
		boolean isQ = type.equals("Q:");
		if(!isQ && !type.equals("A:")){
			throw new IllegalArgumentException("Invalid File.");
		}
		if(!file.hasNextLine()){
			throw new IllegalArgumentException("Incomplete File.");
		}
		String phrase = file.nextLine();
		if(!isQ){
			return new QueryTreeNode(phrase, null, null);
		}
		return new QueryTreeNode(phrase, readIn(file), readIn(file));
	}
	
	/**
	 * Node of a QueryTree
	 * @author Arshana Jain
	 * @version December 5, 2016
	 */
	private class QueryTreeNode{
		//a question or a final guess
		public String data;
		//objects that answer 'no' to question
		public QueryTreeNode no;
		//objects that answer 'yes' to question
		public QueryTreeNode yes;
		
		/**
		 * Creates a QueryTreeNode
		 * @param data question or final guess
		 * @param no an object that is not what the question asks
		 * @param yes an object that is what the question asks
		 */
		public QueryTreeNode(String data, QueryTreeNode no, QueryTreeNode yes){
			this.data = data;
			this.no = no;
			this.yes = yes;
		}
		
		/**
		 * Checks if a QueryTreeNode is a final guess
		 * @return whether or not a QueryTreeNode is a final guess
		 */
		public boolean isLeaf(){
			return yes == null && no == null;
		}
	}
}