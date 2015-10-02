package implementation;

import java.util.Random;
import model.EvolutionaryAlgorithm;
import model.SolutionVectors;

/** Choose an algorithm and population-size based on the function properties */
public class Bootstrap
{
	public static EvolutionaryAlgorithm chooseEvolutionaryAlgorithm(Random random, boolean isMultimodal, boolean hasStructure, boolean isSeparable, int evaluationLimit)
	{
		EvolutionaryAlgorithm selectedEA;
		int μ, λ, breedings;
		double σ, ε0, τ1, τ2, α, transpose;

		if (isMultimodal)
		{
			μ = 100;
			λ = 400;
			σ = 1;
			ε0 = 0.00001;
			τ1 = 1.0 / Math.sqrt(2.0 * SolutionVectors.DIMENSIONS);
			τ2 = 1.0 / Math.sqrt(2.0 * Math.sqrt(SolutionVectors.DIMENSIONS));
			breedings = 1;
			α = 0.5;
			selectedEA = new MultimodalSolver(μ, λ, σ, ε0, τ1, τ2, breedings, α);
		}
		else
		{
			μ = 100;
			λ = 400;
			σ = 1;
			ε0 = 0.00001;
			τ1 = 1.0 / Math.sqrt(2.0 * SolutionVectors.DIMENSIONS);
			τ2 = 1.0 / Math.sqrt(2.0 * Math.sqrt(SolutionVectors.DIMENSIONS));
			breedings = 1;
			transpose = 0.1;
			selectedEA = new UnimodalSolver(μ, λ, σ, ε0, τ1, τ2, breedings, transpose);
		}

		return selectedEA;
	}
}
