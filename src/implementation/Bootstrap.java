package implementation;

import java.util.Random;
import model.EvolutionaryAlgorithm;
import model.SolutionVectors;

/**
 * Choose an algorithm based on the function properties.
 */
public class Bootstrap
{
	/**
	 * Choose an EA and set the parameters.
	 * 
	 * @param random
	 *            Use this object whenever a random number is needed. This ensures a consistent result
	 * @param isMultimodal
	 *            whether the function is multimodal or not
	 * @param isRegular
	 *            whether the function exhibits strong regularity/structure or not
	 * @param isSeparable
	 *            whether the function is separable or not
	 * @param evaluationLimit
	 *            An integer denoting the available number of evaluations for one run
	 * @return The selected EA with set parameters
	 */
	public static EvolutionaryAlgorithm getEvolutionaryAlgorithm(Random random, boolean isMultimodal, boolean isRegular, boolean isSeparable, int evaluationLimit)
	{
		EvolutionaryAlgorithm selectedEA;
		int μ, λ, breedings;
		double σ, ε0, τ, τ1, τ2, α;

		if (isMultimodal)
		{
			if (isRegular)
			{
				μ = 100;
				λ = 400;
			}
			else
			{
				μ = 25;
				λ = 100;
			}
			σ = 1;
			ε0 = 0;
			τ1 = 1.0 / Math.sqrt(2.0 * SolutionVectors.DIMENSIONS); // τ' ∝ 1/√(2n)
			τ2 = 1.0 / Math.sqrt(2.0 * Math.sqrt(SolutionVectors.DIMENSIONS)); // τ ∝ 1/√(2√n)
			breedings = 1;
			α = 0.5;
			selectedEA = new MultimodalSolver(μ, λ, σ, ε0, τ1, τ2, breedings, α);
		}
		else
		{
			μ = 100;
			λ = 400;
			σ = 1;
			ε0 = 0.00005; // 10^-5
			τ = 1.0 / Math.sqrt(SolutionVectors.DIMENSIONS); // τ ∝ 1/√n
			breedings = 1;
			α = 0.5;
			selectedEA = new UnimodalSolver(μ, λ, σ, ε0, τ, breedings, α);
		}

		return selectedEA;
	}
}
