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
	 * @param μ
	 *            Population size
	 * @param σ
	 *            Initial mutation step size
	 * @return The population / a list containing all individuals
	 */
	public static List<Individual> initialisationUniformRandom(Random random, int μ, double σ)
	{
		List<Individual> population = new ArrayList<Individual>();
		for (int i = 0; i < μ; i++)
		{
			// create a person
			Individual individual = new Individual();

			for (int j = 0; j < SolutionVectors.DIMENSIONS; j++)
			{
				// give person random values
				individual.x[j] = SolutionVectors.LOWER_BOUND + random.nextDouble() * SolutionVectors.SIZE;
				individual.σs[j] = σ;
			}

			individual.σ = σ;

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
				clone.σs[j] = original.σs[j];
			}

			clone.σ = original.σ;
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
	 * @param τ1
	 *            Learning rate: τ' ∝ 1/√(2n). Where n = problem_size/number_of_variables
	 * @param τ2
	 *            Learning rate: τ ∝ 1/√(2√n). Where n = problem_size/number_of_variables
	 * @param ε0
	 *            Lower bound of σ
	 * @return The newly created babies
	 */
	public static void uncorrelatedMutationWithNStepSizes(Random random, Individual individual, double τ1, double τ2, double ε0)
	{
		double N = random.nextGaussian();

		for (int i = 0; i < SolutionVectors.DIMENSIONS; i++)
		{
			// σ' = σ · e^(τ' · N(0,1) + τ · Ni(0,1))
			individual.σs[i] *= Math.exp(τ1 * N + τ2 * random.nextGaussian());

			// σ < ε0 ⇒ σ = ε0
			if (individual.σs[i] < ε0)
				individual.σs[i] = ε0;

			// xi = xi + σi · Ni(0, 1)
			individual.x[i] += individual.σs[i] * random.nextGaussian();
		}
	}

	/**
	 * Self adaptive mutation with one step size <br>
	 * 
	 * @param random
	 *            The random object used for all randomness within this function
	 * @param individual
	 *            The individual that is to be mutated
	 * @param τ
	 *            Learning rate: τ ∝ 1/√n. Where n = problem_size/number_of_variables
	 * @param ε0
	 *            Lower bound of σ
	 * @return The newly created babies
	 */
	public static void uncorrelatedMutationWithOneStepSize(Random random, Individual individual, double τ, double ε0)
	{
		// σ' = σ · e^(τ · N(0,1))
		individual.σ *= Math.exp(τ * random.nextGaussian());

		// σ < ε0 ⇒ σ = ε0
		if (individual.σ < ε0)
			individual.σ = ε0;

		for (int i = 0; i < SolutionVectors.DIMENSIONS; i++)
		{
			// x = x + σ · N(0, 1)
			individual.x[i] += individual.σ * random.nextGaussian();
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
	 * @param α
	 *            Range: [0-1]. Where 0 = take after mother and 1 = take after father. Usually 0.5
	 * @return The newly created babies
	 */
	public static Individual[] wholeArithmeticRecombination(Individual[] matingPool, int breedings, double α)
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
				// z = α·x + (1 − α)·y
				baby.x[j] = α * father.x[j] + (1 - α) * mother.x[j];
				baby.σs[j] = α * father.σs[j] + (1 - α) * mother.σs[j];
			}

			baby.σ = α * father.σ + (1 - α) * mother.σ;
		}
		return babies;
	}

	/**
	 * (μ, λ) Selection, Fitness based replacement.
	 * 
	 * @param oldGeneration
	 *            A list containing the old generation / all parents
	 * @param newGeneration
	 *            A list containing the new generation / all children / offspring
	 * @return The population / a list containing all individuals
	 */
	public static List<Individual> μλSelection(List<Individual> oldGeneration, List<Individual> newGeneration, int μ, int λ)
	{
		// check population size consistency
		if (oldGeneration.size() != μ)
			throw new RuntimeException("Old generation size doesn't match Mu!");
		else if (newGeneration.size() != λ)
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
		population.subList(0, λ - μ).clear();

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
			baby.σs[i] = matingPool[i].σs[i];
		}

		double σSum = 0;
		for (int i = 0; i < SolutionVectors.DIMENSIONS; i++)
		{
			σSum += matingPool[i].σ;
		}

		baby.σ = 1 / (SolutionVectors.DIMENSIONS) * σSum;

		return new Individual[] { baby };
	}
}
