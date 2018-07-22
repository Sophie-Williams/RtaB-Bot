package tel.discord.rtab.minigames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class Supercash implements MiniGame {
	static final boolean BONUS = true;
	final static int BOARD_SIZE = 24;
	final static int MAX_VALUE = 10000000;
	final static int[] VALUES = {0,500000,1000000,2000000,3000000,4000000,5000000,
		6000000,7000000,8000000,9000000,MAX_VALUE}; //Bad things happen if this isn't sorted
	final static int NEEDED_TO_WIN = (BOARD_SIZE/VALUES.length);
	static int[] numberPicked = new int[VALUES.length];
	ArrayList<Integer> board = new ArrayList<Integer>(BOARD_SIZE);
	int lastSpace;
	int lastPicked;
	boolean[] pickedSpaces = new boolean[BOARD_SIZE];
	boolean firstPlay = true;
	
	@Override
	public void sendNextInput(String pick)
	{
		if(!isNumber(pick))
		{
			lastPicked = -2;
			return;
		}
		if(!checkValidNumber(pick))
		{
			lastPicked = -1;
			return;
		}
		else
		{
			lastSpace = Integer.parseInt(pick)-1;
			pickedSpaces[lastSpace] = true;
			lastPicked = board.get(lastSpace);
			numberPicked[Arrays.binarySearch(VALUES,lastPicked)] ++;
		}
	}

	boolean isNumber(String message)
	{
		try
		{
			//If this doesn't throw an exception we're good
			Integer.parseInt(message);
			return true;
		}
		catch(NumberFormatException e1)
		{
			return false;
		}
	}
	boolean checkValidNumber(String message)
	{
		int location = Integer.parseInt(message)-1;
		return (location >= 0 && location < BOARD_SIZE && !pickedSpaces[location]);
	}

	@Override
	public LinkedList<String> getNextOutput()
	{
		LinkedList<String> output = new LinkedList<>();
		if(firstPlay)
		{
			//Initialise board
			board.clear();
			for(int i=0; i<VALUES.length; i++)
				for(int j=0; j<NEEDED_TO_WIN; j++)
					board.add(VALUES[i]);
			//Switch one of the lowest values for an extra copy of the highest value
			board.set(0,MAX_VALUE);
			Collections.shuffle(board);
			numberPicked = new int[VALUES.length];
			pickedSpaces = new boolean[BOARD_SIZE];
			//Display instructions
			output.add("Congratulations on earning the chance to play this bonus game!");
			output.add("In Supercash, you can win up to ten million dollars!");
			output.add("Hidden on the board are three \"$10,000,000\" spaces, simply pick them all to win.");
			output.add("There are also other, lesser values, make a pair of those to win that amount instead.");
			output.add("Oh, and there's also a single bomb hidden somewhere on the board. If you pick that, you win nothing.");
			output.add("Best of luck! Make your first pick when you are ready.");
			firstPlay = false;
		}
		else if(lastPicked == -2)
		{
			//Random unrelated non-number doesn't need feedback
			return output;
		}
		else if(lastPicked == -1)
		{
			output.add("Invalid pick.");
		}
		else
		{
			output.add(String.format("Space %d selected...",lastSpace+1));
			output.add("...");
			if(lastPicked == 0)
			output.add("**BOOM**");
			else
			output.add(String.format("$%,d!",lastPicked));
		}
		output.add(generateBoard());
		return output;
	}
	
	String generateBoard()
	{
		StringBuilder display = new StringBuilder();
		display.append("```\n");
		display.append("    SUPERCASH    \n");
		for(int i=0; i<BOARD_SIZE; i++)
		{
			if(pickedSpaces[i])
			{
				display.append("  ");
			}
			else
			{
				display.append(String.format("%02d",(i+1)));
			}
			if((i%(VALUES.length/2)) == ((VALUES.length/2)-1))
				display.append("\n");
			else
				display.append(" ");
		}
		display.append("\n");
		//Next display how many of each we have
		for(int i=1; i<VALUES.length; i++)
		{
			display.append(String.format("%1$dx $%2$,d\n",numberPicked[i],VALUES[i]));
		}
		display.append("```");
		return display.toString();
	}

	@Override
	public boolean isGameOver()
	{
		for(int i=0; i<VALUES.length; i++)
		{
			//Lowest amount is easier to win
			if(i == 0)
			{
				if(numberPicked[i] >= (NEEDED_TO_WIN-1))
					return true;
			}
			//Highest amount is harder to win
			else if(i == (VALUES.length-1))
			{
				if(numberPicked[i] >= (NEEDED_TO_WIN+1))
					return true;
			}
			//Other amounts are normal rarity
			else
			{
				if(numberPicked[i] >= NEEDED_TO_WIN)
					return true;
			}
		}
		return false;
	}

	@Override
	public int getMoneyWon()
	{
		firstPlay = true;
		return lastPicked;
	}

	@Override
	public boolean isBonusGame() {
		return BONUS;
	}
}
