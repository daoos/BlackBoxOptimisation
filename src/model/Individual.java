package model;

public class Individual
{
	public static final int		NUM_VALUES	= 10;						// Each individual has this many values 
	public static final double	MIN_VALUE	= -5;						// Lower bound search space
	public static final double	MAX_VALUE	= 5;						// Higher bound search space
	public static final double	VALUE_SIZE	= MAX_VALUE - MIN_VALUE;	// Search space size

	public final double[]	x;			// values/variables/genotype
	public double			fitness;	// Score. Higher is better. Maximum is 10
	public double			σ;			// Mutation step size. Needed for self adaptive mutation

	/** Create an individual with initially all values at 0 */
	public Individual()
	{
		x = new double[NUM_VALUES];
		σ = 0;
	}

	public Object getGenotype()
	{
		return (Object) x;
	}
}
