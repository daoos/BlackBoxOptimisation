package model;

/** Defines the search space */
public class Space
{
	public static final int		DIMENSIONS	= 10;							// Problem-size/Each individual has this many values 
	public static final double	LOWER_BOUND	= -5;							// Minimum value
	public static final double	UPPER_BOUND	= 5;							// Maximum value
	public static final double	SIZE		= UPPER_BOUND - LOWER_BOUND;	// search-space/genotype-space size
}
