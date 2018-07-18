package tel.discord.rtab;

import tel.discord.rtab.enums.SpaceType;

class Board
{
	SpaceType[] typeBoard;
	Integer[] cashBoard;
	Integer[] boostBoard;
	Board(int size)
	{
		//Initialise types
		final SpaceType[] TYPE_VALUES = {SpaceType.CASH,SpaceType.BOOSTER};
		final int[] TYPE_WEIGHTS =      {            10,                2};
		typeBoard = new SpaceType[size];
		typeBoard = initBoard(size,typeBoard,TYPE_VALUES,TYPE_WEIGHTS);
		//Initialise cash - uses Integer because generic methods don't like int
		final Integer[] CASH_VALUES = {-25000,-20000,-15000,-10000,-5000, //Negative
				10000,20000,30000,40000,50000,60000,70000,80000,90000,100000, //Small
				125000,150000,175000,200000,250000,300000,400000,500000,750000,1000000}; //Big
		final int[] CASH_WEIGHTS = {2,2,2,2,2, //Negative
				3,3,3,3,3,3,3,3,3,3, //Small
				1,1,1,1,1,1,1,1,1,1}; //Big
		cashBoard = new Integer[size];
		cashBoard = initBoard(size,cashBoard,CASH_VALUES,CASH_WEIGHTS);
		final Integer[] BOOST_VALUES = {-50,-40,-30,-20,-10, //Negative
				10,20,30,40,50,75,100,150,200}; //Positive
		final int[] BOOST_WEIGHTS = {1,2,2,3,3, //Negative
				10,9,7,5,3,2,2,1,1}; //Positive
		boostBoard = new Integer[size];
		boostBoard = initBoard(size,boostBoard,BOOST_VALUES,BOOST_WEIGHTS);
	}
	private <T> T[] initBoard(int size, T[] board, T[] values, int[] weights)
	{
		//Declare possible values and weights
		//Autogenerate cumulative weights
		int[] cumulativeWeights = new int[weights.length];
		int totalWeight = 0;
		for(int i=0; i<weights.length; i++)
		{
			totalWeight += weights[i];
			cumulativeWeights[i] = totalWeight;
		}
		double random;
		for(int i=0; i<size; i++)
		{
			//Get random spot in weight table
			random = Math.random() * totalWeight;
			//Find where we actually landed
			int search=0;
			while(cumulativeWeights[search] < random)
				search++;
			//And set the value to that
			board[i] = values[search];
		}
		return board;
	}
}