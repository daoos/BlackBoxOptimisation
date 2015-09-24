package model;

import java.util.List;
import java.util.Random;

/** EvolutionaryAlgorithm Blueprint. Each Evolutionary Algorithm should have these functions: */
public interface EvolutionaryAlgorithm
{
	/**
	 * Generate the initial population
	 * 
	 * @param random
	 *            Use this object whenever a random number is needed. This ensures a consistent result
	 * @return The population / a list containing all individuals
	 */
	List<Individual> initialisation(Random random);

	/**
	 * Select our parent for the future generation.
	 * 
	 * @param random
	 *            Use this object whenever a random number is needed. This ensures a consistent result
	 * @param matingPool
	 *            An array containing 2 or more partners/parents
	 * @return An array containing the newly born individuals
	 */
	Individual[] recombination(Random random, Individual[] matingPool);

	/**
	 * Genetic mutation might occur when an individual is exposed to chemicals or radiation ;)
	 * 
	 * @param random
	 *            Use this object whenever a random number is needed. This ensures a consistent result
	 * @param individual
	 *            The individual that has to be mutated.
	 */
	void mutation(Random random, Individual individual);

	/**
	 * Select the parents that will create the future generation.<br />
	 * Informally: <i>Who's gonna do who.</i>
	 * 
	 * @param random
	 *            Use this object whenever a random number is needed. This ensures a consistent result
	 * @param population
	 *            All individuals / parent candidates
	 * @return A list containing the mating-pools/parent-couples
	 */
	List<Individual[]> parentSelection(Random random, List<Individual> population);

	/**
	 * Survival of the fittest.
	 * 
	 * @param random
	 *            Use this object whenever a random number is needed. This ensures a consistent result
	 * @param oldGeneration
	 *            All parents / previous population
	 * @param newGeneration
	 *            All children
	 * @return The new population
	 */
	List<Individual> survivorSelection(Random random, List<Individual> oldGeneration, List<Individual> newGeneration);
}
