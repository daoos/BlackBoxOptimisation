package model;

/** Every individual looks like this */
public class Individual
{
	public double	fitness;	// Score. Higher is better. Maximum is 10
	public double[]	x;			// values/variables/genotype
	public double	σ;			// Mutation step size. Needed for self adaptive mutation with ONE step size
	public double[]	σs;			// Mutation step sizes. Needed for self adaptive mutation with N step sizes

	/** Create an individual with initially all values at 0 */
	public Individual()
	{
		x = new double[SolutionVectors.DIMENSIONS];
		σ = 0;
		σs = new double[SolutionVectors.DIMENSIONS];
	}

	public Object getGenotype()
	{
		return (Object) x;
	}
}
