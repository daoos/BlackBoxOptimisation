package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/** EA Utilities. Several basic 'components' for an Evolutionary Algorithm */
public class EAUtils
{
	/**
	 * Fitness Proportional Selection (FPS)
	 * 
	 * @param random
	 *            The random object used for all randomness within this function
	 * @param population
	 *            A List containing the whole population
	 * @param numMatingPools
	 *            The number of parent couples
	 * @param matingPoolSize
	 *            The number of parents per couple
	 * @param transpose
	 *            Changes the selection probabilities
	 * @return The newly created babies
	 */
	public static List<Individual[]> fitnessProportionalSelection(Random random, List<Individual> population, int numMatingPools, int matingPoolSize, double transpose)
	{
		double minimum = 0;
		double sum = 0;
		double[] fitnisses = new double[population.size()];

		// save fitness and find minimum 
		for (int i = 0; i < population.size(); i++)
		{
			fitnisses[i] = population.get(i).fitness;

			if (minimum > fitnisses[i])
			{
				minimum = fitnisses[i];
			}
		}

		// make sure the fitness is NOT negative and calc sum
		for (int i = 0; i < population.size(); i++)
		{
			fitnisses[i] += (-minimum + transpose);
			sum += fitnisses[i];
		}

		// create a mating pool List
		List<Individual[]> matingPoolList = new ArrayList<Individual[]>();

		for (int i = 0; i < numMatingPools; i++)
		{
			// create one mating pool
			Individual[] matingPool = new Individual[matingPoolSize];

			for (int j = 0; j < matingPoolSize; j++)
			{
				// select a random individual based on his fitness
				double value = random.nextDouble() * sum;
				for (int k = 0; k < population.size(); k++)
				{
					value -= fitnisses[k];

					if (value <= 0)
					{
						// add the selected-individual to the mating pool
						matingPool[j] = population.get(k);
						break;
					}
				}
			}

			// add the mating pool to the list
			matingPoolList.add(matingPool);
		}

		return matingPoolList;
	}

	/**
	 * Generates and returns a new population with uniform-randomly initialized individuals
	 * 
	 * @param random
	 *            The random object used for all randomness within this function
	 * @param ??
	 *            Population size
	 * @param ??
	 *            Initial mutation step size
	 * @return The population / a list containing all individuals
	 */
	public static List<Individual> initialisationUniformRandom(Random random, int ??, double ??)
	{
		List<Individual> population = new ArrayList<Individual>();
		for (int i = 0; i < ??; i++)
		{
			// create a person
			Individual individual = new Individual();

			for (int j = 0; j < SolutionVectors.DIMENSIONS; j++)
			{
				// give person random values
				individual.x[j] = SolutionVectors.LOWER_BOUND + random.nextDouble() * SolutionVectors.SIZE;
				individual.??s[j] = ??;
			}

			individual.?? = ??;

			// add person to population
			population.add(individual);
		}

		return population;
	}

	/**
	 * No recombination. Clone the parent. Used in Evolutionary Programming (EP).
	 * 
	 * @param matingPool
	 *            The mating pool with only one parent
	 * @param breedings
	 *            Number of desired clones
	 * @return An array containing clones of the parent
	 */
	public static Individual[] noRecombination(Individual[] matingPool, int breedings)
	{
		if (matingPool.length != 1)
			throw new RuntimeException("Invalid number of parents!");

		Individual original = matingPool[0];
		Individual[] clones = new Individual[breedings];

		for (int i = 0; i < breedings; i++)
		{
			Individual clone = clones[i] = new Individual();

			for (int j = 0; j < SolutionVectors.DIMENSIONS; j++)
			{
				clone.x[j] = original.x[j];
				clone.??s[j] = original.??s[j];
			}

			clone.?? = original.??;
		}
		return clones;
	}

	/**
	 * Self adaptive mutation with n step sizes
	 * 
	 * @param random
	 *            The random object used for all randomness within this function
	 * @param individual
	 *            The individual that is to be mutated
	 * @param ??1
	 *            Learning rate: ??' ??? 1/???(2n). Where n = problem_size/number_of_variables
	 * @param ??2
	 *            Learning rate: ?? ??? 1/???(2???n). Where n = problem_size/number_of_variables
	 * @param ??0
	 *            Lower bound of ??
	 * @return The newly created babies
	 */
	public static void uncorrelatedMutationWithNStepSizes(Random random, Individual individual, double ??1, double ??2, double ??0)
	{
		double N = random.nextGaussian();

		for (int i = 0; i < SolutionVectors.DIMENSIONS; i++)
		{
			// ??' = ?? ?? e^(??' ?? N(0,1) + ?? ?? Ni(0,1))
			individual.??s[i] *= Math.exp(??1 * N + ??2 * random.nextGaussian());

			// ?? < ??0 ??? ?? = ??0
			if (individual.??s[i] < ??0)
				individual.??s[i] = ??0;

			// xi = xi + ??i ?? Ni(0, 1)
			individual.x[i] += individual.??s[i] * random.nextGaussian();
		}
	}

	/**
	 * Self adaptive mutation with one step size <br>
	 * 
	 * @param random
	 *            The random object used for all randomness within this function
	 * @param individual
	 *            The individual that is to be mutated
	 * @param ??
	 *            Learning rate: ?? ??? 1/???n. Where n = problem_size/number_of_variables
	 * @param ??0
	 *            Lower bound of ??
	 * @return The newly created babies
	 */
	public static void uncorrelatedMutationWithOneStepSize(Random random, Individual individual, double ??, double ??0)
	{
		// ??' = ?? ?? e^(?? ?? N(0,1))
		individual.?? *= Math.exp(?? * random.nextGaussian());

		// ?? < ??0 ??? ?? = ??0
		if (individual.?? < ??0)
			individual.?? = ??0;

		for (int i = 0; i < SolutionVectors.DIMENSIONS; i++)
		{
			// x = x + ?? ?? N(0, 1)
			individual.x[i] += individual.?? * random.nextGaussian();
		}
	}

	/**
	 * Uniform parent selection
	 * 
	 * @param random
	 *            The random object used for all randomness within this function
	 * @param population
	 *            A List containing the whole population
	 * @param numMatingPools
	 *            The number of parent couples
	 * @param matingPoolSize
	 *            The number of parents per couple
	 * @return The newly created babies
	 */
	public static List<Individual[]> uniformParentSelection(Random random, List<Individual> population, int numMatingPools, int matingPoolSize)
	{
		// create mating pool List
		List<Individual[]> matingPoolList = new ArrayList<Individual[]>();

		for (int i = 0; i < numMatingPools; i++)
		{
			// create a mating pool
			Individual[] matingPool = new Individual[matingPoolSize];

			for (int j = 0; j < matingPoolSize; j++)
			{
				// select random individual and add to the mating pool
				matingPool[j] = population.get(random.nextInt(population.size()));
			}

			// add mating pool to the list
			matingPoolList.add(matingPool);
		}

		return matingPoolList;
	}

	/**
	 * Whole Arithmetic Recombination
	 * 
	 * Takes 2 parents and creates a bunch of babies
	 * 
	 * @param matingPool
	 *            An array containing 2 parents
	 * @param breedings
	 *            The number of babies that are to be created
	 * @param ??
	 *            Range: [0-1]. Where 0 = take after mother and 1 = take after father. Usually 0.5
	 * @return The newly created babies
	 */
	public static Individual[] wholeArithmeticRecombination(Individual[] matingPool, int breedings, double ??)
	{
		if (matingPool.length != 2)
			throw new RuntimeException("Invalid number of parents!");

		Individual father = matingPool[0];
		Individual mother = matingPool[1];
		Individual[] babies = new Individual[breedings];

		for (int i = 0; i < breedings; i++)
		{
			Individual baby = babies[i] = new Individual();

			for (int j = 0; j < SolutionVectors.DIMENSIONS; j++)
			{
				// z = ????x + (1 ??? ??)??y
				baby.x[j] = ?? * father.x[j] + (1 - ??) * mother.x[j];
				baby.??s[j] = ?? * father.??s[j] + (1 - ??) * mother.??s[j];
			}

			baby.?? = ?? * father.?? + (1 - ??) * mother.??;
		}
		return babies;
	}

	/**
	 * (??, ??) Selection, Fitness based replacement.
	 * 
	 * @param oldGeneration
	 *            A list containing the old generation / all parents
	 * @param newGeneration
	 *            A list containing the new generation / all children / offspring
	 * @return The population / a list containing all individuals
	 */
	public static List<Individual> ????Selection(List<Individual> oldGeneration, List<Individual> newGeneration, int ??, int ??)
	{
		// check population size consistency
		if (oldGeneration.size() != ??)
			throw new RuntimeException("Old generation size doesn't match Mu!");
		else if (newGeneration.size() != ??)
			throw new RuntimeException("New generation size doesn't match Lambda!");

		// population = new generation. So basically all parents die
		List<Individual> population = newGeneration;

		// sort the population-list so that the 'weakest' will be at the beginning of the list
		Collections.sort(population, new Comparator<Individual>()
		{
			@Override
			public int compare(Individual i1, Individual i2)
			{
				double c = i1.fitness - i2.fitness;
				return c < 0 ? -1 : c > 0 ? 1 : 0;
			}
		});

		// Clear the beginning of the list: the weakest individuals are removed from the population
		population.subList(0, ?? - ??).clear();

		// our new population =)
		return population;
	}

	/**
	 * Global Arithmetic Recombination
	 * Takes n parents and create one baby
	 * 
	 * @param matingPool
	 *            An array containing 2 parents
	 * @return The newly created baby
	 */
	public static Individual[] globalArithmeticRecombination(Individual[] matingPool)
	{
		if (matingPool.length != SolutionVectors.DIMENSIONS)
			throw new RuntimeException("Invalid number of parents!");

		Individual baby = new Individual();

		for (int i = 0; i < SolutionVectors.DIMENSIONS; i++)
		{
			baby.x[i] = matingPool[i].x[i];
			baby.??s[i] = matingPool[i].??s[i];
		}

		double ??Sum = 0;
		for (int i = 0; i < SolutionVectors.DIMENSIONS; i++)
		{
			??Sum += matingPool[i].??;
		}

		baby.?? = 1 / (SolutionVectors.DIMENSIONS) * ??Sum;

		return new Individual[] { baby };
	}
}
