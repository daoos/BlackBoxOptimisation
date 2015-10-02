package implementation;

import java.util.List;
import java.util.Random;
import model.EAUtils;
import model.EvolutionaryAlgorithm;
import model.Individual;

/** Our solution for a multimodal function problem */
public class MultimodalSolver implements EvolutionaryAlgorithm
{
	private int		μ;
	private int		λ;
	private double	σ;
	private double	ε0;
	private double	τ1;
	private double	τ2;
	private int		breedings;
	private double	α;
	private int		matingPoolSize	= 2;	// Number of parents per family. 2 because of ArithmeticRecombination

	/**
	 * @param μ
	 *            Population size
	 * @param λ
	 *            Amount of offspring per generation
	 * @param σ
	 *            Initial mutation step size
	 * @param ε0
	 *            Lower bound of σ
	 * @param τ1
	 *            Learning rate: τ' ∝ 1/√2n. Where n = problem_size/number_of_variables
	 * @param τ2
	 *            Learning rate: τ ∝ 1/√2√n. Where n = problem_size/number_of_variables
	 * @param breedings
	 *            Amount of offspring per mating pool
	 * @param α
	 *            Whole Arithmetic Recombination parameter
	 */
	public MultimodalSolver(int μ, int λ, double σ, double ε0, double τ1, double τ2, int breedings, double α)
	{
		this.μ = μ;
		this.λ = λ;
		this.σ = σ;
		this.ε0 = ε0;
		this.τ1 = τ1;
		this.τ2 = τ2;
		this.breedings = breedings;
		this.α = α;
	}

	public List<Individual> initialisation(Random random)
	{
		// Uniform random
		return EAUtils.initialisationUniformRandom(random, μ, σ);
	}

	public Individual[] recombination(Random random, Individual[] matingPool)
	{
		// Whole arithmetic recombination
		return EAUtils.wholeArithmeticRecombination(matingPool, breedings, α);
	}

	public void mutation(Random random, Individual individual)
	{
		// Uncorrelated Mutation with n Step Sizes
		EAUtils.uncorrelatedMutationWithNStepSizes(random, individual, τ1, τ2, ε0);
	}

	public List<Individual[]> parentSelection(Random random, List<Individual> population)
	{
		// Uniform Selection
		int numMatingPools = λ / breedings;
		return EAUtils.uniformParentSelection(random, population, numMatingPools, matingPoolSize);
	}

	public List<Individual> survivorSelection(Random random, List<Individual> oldGeneration, List<Individual> newGeneration)
	{
		// (μ, λ) Selection
		return EAUtils.μλSelection(oldGeneration, newGeneration, μ, λ);
	}
}
