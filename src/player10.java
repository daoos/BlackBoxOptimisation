
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;
import implementation.Bootstrap;
import model.EvolutionaryAlgorithm;
import model.Individual;

public class player10 implements ContestSubmission
{
	private Random				random;
	private ContestEvaluation	evaluation;
	private int					evaluationLimit;
	private boolean				isMultimodal, hasStructure, isSeparable;

	private List<Individual>		population;
	private EvolutionaryAlgorithm	evolutionaryAlgorithm;

	public static void main(String[] args)
	{}

	public void setSeed(long seed)
	{
		// Set seed of algorithms random process
		random = new Random(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		this.evaluation = evaluation;

		// Get evaluation properties
		Properties props = evaluation.getProperties();
		evaluationLimit = Integer.parseInt(props.getProperty("Evaluations"));
		isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
		hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
		isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));
	}

	public synchronized void run()
	{
		// INITIALISATION
		evolutionaryAlgorithm = Bootstrap.chooseEvolutionaryAlgorithm(random, isMultimodal, hasStructure, isSeparable, evaluationLimit);
		population = evolutionaryAlgorithm.initialisation(random);

		int evaluationCount = 0;

		for (Individual child : population)
		{
			Double fitness = (Double) evaluation.evaluate(child.getGenotype());
			evaluationCount++;
			child.fitness = fitness;
		}

		while (evaluationCount < evaluationLimit) // TERMINATION CONDITION
		{
			// PARENT SELECTION
			List<Individual[]> coupleList = evolutionaryAlgorithm.parentSelection(random, population);

			// RECOMBANATION
			List<Individual> children = new ArrayList<Individual>();
			for (Individual[] parents : coupleList)
			{
				children.addAll(Arrays.asList(evolutionaryAlgorithm.recombination(random, parents)));
			}

			// MUTATION
			for (Individual child : children)
			{
				evolutionaryAlgorithm.mutation(random, child);
			}

			// SURVIVOR SELECTION
			for (Individual child : children)
			{
				Double fitness = (Double) evaluation.evaluate(child.getGenotype());
				evaluationCount++;

				if (fitness != null)
				{
					child.fitness = fitness;
				}
				else
				{
					break;
				}
			}

			population = evolutionaryAlgorithm.survivorSelection(random, population, children);
		}
	}
}
