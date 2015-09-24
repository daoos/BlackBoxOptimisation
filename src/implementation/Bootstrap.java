package implementation;

import java.util.Random;
import model.EvolutionaryAlgorithm;
import model.Space;

/** Choose an algorithm and population-size based on the given arguments */
public class Bootstrap
{
	public static EvolutionaryAlgorithm chooseEvolutionaryAlgorithm(Random random, boolean isMultimodal, boolean hasStructure, boolean isSeparable, int evaluationLimit)
	{
		EvolutionaryAlgorithm selectedEA;

		// TODO: select the best suiting evolutionary-algorithm based on the given arguments 
		selectedEA = new EvolutionaryStrategies(100, 400, 1, 0.00001, 1 / Math.sqrt(Space.DIMENSIONS), 2, 1);

		// return 
		return selectedEA;
	}
}
