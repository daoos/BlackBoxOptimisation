package implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import model.EvolutionaryAlgorithm;
import model.Individual;

/** ES */
public class EvolutionairyStrategies implements EvolutionaryAlgorithm
{
	private int		μ;				// Population size
	private int		λ;				// Total number of offspring per generation
	private double	σ;				// Initial mutation step size
	private double	ε0;				// Lower bound of σ
	private double	τ;				// Usually: τ ∝ 1/√n. Where n = problem_size/number_of_variables
	private int		matingPoolSize;	// Number of parents per family
	private int		numChildren;	// Number of children per family

	public EvolutionairyStrategies(int μ, int λ, double σ, double ε0, double τ, int matingPoolSize, int numChildren)
	{
		this.μ = μ;
		this.λ = λ;
		this.σ = σ;
		this.ε0 = ε0;
		this.τ = τ;
		this.matingPoolSize = matingPoolSize;
		this.numChildren = numChildren;
	}

	public List<Individual> initialisation(Random random)
	{
		// the first population is seeded by randomly generated individuals.

		// create a population
		List<Individual> population = new ArrayList<Individual>();
		for (int i = 0; i < μ; i++)
		{
			// create a person
			Individual individual = new Individual();

			for (int j = 0; j < Individual.NUM_VALUES; j++)
			{
				// give person random values
				individual.x[j] = Individual.MIN_VALUE + random.nextDouble() * Individual.VALUE_SIZE;
			}

			individual.σ = σ;

			// add person to population
			population.add(individual);
		}
		return population;
	}

	public List<Individual[]> parentSelection(Random random, List<Individual> population)
	{
		// Uniform Selection

		int numMatingPools = λ / numChildren;

		// create mating pool List
		List<Individual[]> matingPoolList = new ArrayList<Individual[]>();

		for (int i = 0; i < numMatingPools; i++)
		{
			// create a mating pool
			Individual[] matingPool = new Individual[matingPoolSize];

			for (int j = 0; j < matingPoolSize; j++)
			{
				// select random individual & add individual to the mating pool
				matingPool[j] = population.get(random.nextInt(μ));
			}

			// add mating pool to the list
			matingPoolList.add(matingPool);
		}

		return matingPoolList;
	}

	public Individual[] recombination(Random random, Individual[] matingPool)
	{
		// Intermediate Crossover

		Individual[] children = new Individual[numChildren];
		for (int i = 0; i < numChildren; i++) // foreach child
		{
			Individual child = children[i] = new Individual();

			// child = papa/2 + mama/2
			for (int j = 0; j < matingPool.length; j++)// foreach parent
			{
				Individual parent = matingPool[j];

				for (int k = 0; k < Individual.NUM_VALUES; k++) // foreach variable
				{
					child.x[k] += parent.x[k] / matingPool.length;
				}

				// TODO: how do you pass on σ to the children
				child.σ += parent.σ / matingPool.length;
			}
		}
		return children;
	}

	public void mutation(Random random, Individual individual)
	{
		// Uncorrelated Mutation with One Step Size

		// σ' = σ · e^(τ · N(0,1))
		individual.σ = individual.σ * Math.exp(τ * random.nextGaussian());

		// σ < ε0 ⇒ σ = ε0
		if (individual.σ < ε0)
			individual.σ = ε0;

		for (int i = 0; i < Individual.NUM_VALUES; i++)
		{
			// xi = xi + σ · Ni(0, 1)
			individual.x[i] = individual.x[i] + individual.σ * random.nextGaussian();
		}
	}

	public List<Individual> survivorSelection(Random random, List<Individual> oldGeneration, List<Individual> newGeneration)
	{
		// Fitness-Based Replacement ((μ, λ) Selection)

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
		population.subList(0, population.size() - oldGeneration.size()).clear();

		// our new population =)
		return population;
	}
}
