package implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import model.EvolutionaryAlgorithm;
import model.Individual;
import model.Space;

/** Classic implementation of Evolutionary Strategies */
public class EvolutionaryStrategies implements EvolutionaryAlgorithm
{
	private int		μ;				// Population size
	private int		λ;				// Offspring per generation
	private double	σ;				// Initial mutation step size
	private double	ε0;				// Lower bound of σ
	private double	τ;				// Learning rate: τ ∝ 1/√n. Where n = problem_size/number_of_variables
	private int		matingPoolSize;	// Number of parents per family
	private int		offspring;		// Offspring per mating pool

	public EvolutionaryStrategies(int μ, int λ, double σ, double ε0, double τ, int matingPoolSize, int offspring)
	{
		this.μ = μ;
		this.λ = λ;
		this.σ = σ;
		this.ε0 = ε0;
		this.τ = τ;
		this.matingPoolSize = matingPoolSize;
		this.offspring = offspring;
	}

	public List<Individual> initialisation(Random random)
	{
		// Uniform random

		// create a population
		List<Individual> population = new ArrayList<Individual>();
		for (int i = 0; i < μ; i++)
		{
			// create a person
			Individual randomPerson = new Individual();

			for (int j = 0; j < Space.DIMENSIONS; j++)
			{
				// give person random values
				randomPerson.x[j] = Space.LOWER_BOUND + random.nextDouble() * Space.SIZE;
			}

			randomPerson.σ = σ;

			// add person to population
			population.add(randomPerson);
		}
		return population;
	}

	public Individual[] recombination(Random random, Individual[] matingPool)
	{
		// Whole arithmetic recombination

		Individual[] children = new Individual[offspring];
		for (int i = 0; i < offspring; i++) // foreach child
		{
			Individual child = children[i] = new Individual();

			for (int j = 0; j < matingPool.length; j++)// foreach parent
			{
				for (int k = 0; k < Space.DIMENSIONS; k++) // foreach variable
				{
					// child = papa/2 + mama/2
					child.x[k] += matingPool[j].x[k] / matingPool.length;
				}

				// TODO: how do you pass on σ to the children
				child.σ += matingPool[j].σ / matingPool.length;
			}
		}
		return children;
	}

	public void mutation(Random random, Individual individual)
	{
		// Uncorrelated Mutation with n Step Sizes

		// σ' = σ · e^(τ · N(0,1))
		individual.σ = individual.σ * Math.exp(τ * random.nextGaussian());

		// σ < ε0 ⇒ σ = ε0
		if (individual.σ < ε0)
			individual.σ = ε0;

		for (int i = 0; i < Space.DIMENSIONS; i++)
		{
			// x = x + σ · N(0, 1)
			individual.x[i] += individual.σ * random.nextGaussian();
		}
	}

	public List<Individual[]> parentSelection(Random random, List<Individual> population)
	{
		// Uniform Selection

		int numMatingPools = λ / offspring;

		// create mating pool List
		List<Individual[]> matingPoolList = new ArrayList<Individual[]>();

		for (int i = 0; i < numMatingPools; i++)
		{
			// create a mating pool
			Individual[] matingPool = new Individual[matingPoolSize];

			for (int j = 0; j < matingPoolSize; j++)
			{
				// select random individual
				Individual selectedIndividual = population.get(random.nextInt(μ));

				// add individual to the mating pool
				matingPool[j] = selectedIndividual;
			}

			// add mating pool to the list
			matingPoolList.add(matingPool);
		}

		return matingPoolList;
	}

	public List<Individual> survivorSelection(Random random, List<Individual> oldGeneration, List<Individual> newGeneration)
	{
		// (μ, λ) Selection

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
}
