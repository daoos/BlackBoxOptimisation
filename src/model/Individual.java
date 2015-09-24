package model;

public class Individual
{
	public final double[]	x;			// values/variables/genotype
	public double			fitness;	// Score. Higher is better. Maximum is 10
	public double			σ;			// Mutation step size. Needed for self adaptive mutation

	/** Create an individual with initially all values at 0 */
	public Individual()
	{
		x = new double[Space.DIMENSIONS];
		σ = 0;
	}

	public Object getGenotype()
	{
		return (Object) x;
	}
}
