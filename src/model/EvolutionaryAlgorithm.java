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
	 * Combine the parents and create offspring
	 * 
	 * @param random
	 *            Use this object whenever a random number is needed. This ensures a consistent result
	 * @param matingPool
	 *            An array containing 2 or more partners/parents
	 * @return An array containing the newly born individuals
	 */
	Individual[] recombination(Random random, Individual[] matingPool);

	/**
	 * Mutate an individual
	 * 
	 * @param random
	 *            Use this object whenever a random number is needed. This ensures a consistent result
	 * @param individual
	 *            The individual that has to be mutated.
	 */
	void mutation(Random random, Individual individual);

	/**
	 * Select the parents that will create the future generation.
	 * 
	 * @param random
	 *            Use this object whenever a random number is needed. This ensures a consistent result
	 * @param population
	 *            All individuals / parent candidates
	 * @return A list containing the mating-pools/couples
	 */
	List<Individual[]> parentSelection(Random random, List<Individual> population);

	/**
	 * Survival of the fittest. Decide which individuals are allowed to live
	 * 
	 * @param random
	 *            Use this object whenever a random number is needed. This ensures a consistent result
	 * @param oldGeneration
	 *            A list containing the old generation / the previous population
	 * @param newGeneration
	 *            A list containing the new generation / all children
	 * @return Choose and return the new population
	 */
	List<Individual> survivorSelection(Random random, List<Individual> oldGeneration, List<Individual> newGeneration);
}
