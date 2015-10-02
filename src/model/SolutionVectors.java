package model;

/** Defines the search space of the problem solving algorithm */
public class SolutionVectors
{
	public static final int		DIMENSIONS	= 10;							// Number of values/Problem-size 
	public static final double	LOWER_BOUND	= -5;							// Minimum value
	public static final double	UPPER_BOUND	= 5;							// Maximum value
	public static final double	SIZE		= UPPER_BOUND - LOWER_BOUND;	// search-space size
}
