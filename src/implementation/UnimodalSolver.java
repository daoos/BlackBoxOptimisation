package implementation;

import java.util.List;
import java.util.Random;
import model.EAUtils;
import model.EvolutionaryAlgorithm;
import model.Individual;

/**
 * Our solution for an unimodal function problem
 * 
 * Initialization:		Uniform random
 * Recombination:		Intermediate / whole arithmetic
 * Mutation:			Self adaptive mutation with one step size
 * Parent selection:	Uniform random
 * Survivor selection:	(μ, λ) Selection
 * 
 * Parameters are set in 'Bootstrap.java'
 */
public class UnimodalSolver implements EvolutionaryAlgorithm
{
	private int		μ;
	private int		λ;
	private double	σ;
	private double	ε0;
	private double	τ;
	private double	α;
	private int		breedings;
	private int		matingPoolSize	= 2;	// Number of parents per family. 2 because of WholeArithmeticRecombination

	/**
	 * @param μ
	 *            Population size
	 * @param λ
	 *            Amount of offspring per generation
	 * @param σ
	 *            Initial mutation step size
	 * @param ε0
	 *            Lower bound of σ
	 * @param τ
	 *            Learning rate. Usually: τ ∝ 1/√n. Where n = problem_size/number_of_variables
	 * @param breedings
	 *            Amount of offspring per mating pool
	 * @param α
	 *            Whole Arithmetic Recombination parameter
	 */
	public UnimodalSolver(int μ, int λ, double σ, double ε0, double τ, int breedings, double α)
	{
		this.μ = μ;
		this.λ = λ;
		this.σ = σ;
		this.ε0 = ε0;
		this.τ = τ;
		this.breedings = breedings;
		this.α = α;
	}

	public List<Individual> initialisation(Random random)
	{
		return EAUtils.initialisationUniformRandom(random, μ, σ);
	}

	public Individual[] recombination(Random random, Individual[] matingPool)
	{
		return EAUtils.wholeArithmeticRecombination(matingPool, breedings, α);
	}

	public void mutation(Random random, Individual individual)
	{
		EAUtils.uncorrelatedMutationWithOneStepSize(random, individual, τ, ε0);
	}

	public List<Individual[]> parentSelection(Random random, List<Individual> population)
	{
		int numMatingPools = λ / breedings;
		return EAUtils.uniformParentSelection(random, population, numMatingPools, matingPoolSize);
	}

	public List<Individual> survivorSelection(Random random, List<Individual> oldGeneration, List<Individual> newGeneration)
	{
		return EAUtils.μλSelection(oldGeneration, newGeneration, μ, λ);
	}
}
