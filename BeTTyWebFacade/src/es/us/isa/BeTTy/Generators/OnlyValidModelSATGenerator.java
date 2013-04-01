package es.us.isa.BeTTy.Generators;

import java.util.Random;

import es.us.isa.FAMA.models.FAMAfeatureModel.transformations.FeatureModelTransform;
import es.us.isa.FAMA.models.variabilityModel.VariabilityModel;
import es.us.isa.Sat4jReasoner.Sat4jReasoner;
import es.us.isa.Sat4jReasoner.questions.Sat4jValidQuestion;
import es.us.isa.generator.Characteristics;
import es.us.isa.generator.IGenerator;
import es.us.isa.generator.FM.AbstractFMGeneratorDecorator;
import es.us.isa.generator.FM.GeneratorCharacteristics;

public class OnlyValidModelSATGenerator extends AbstractFMGeneratorDecorator {

	int maxtries = 20;

	public OnlyValidModelSATGenerator(IGenerator gen) {
		super(gen);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateResetGenerator(Characteristics c) {
		super.updateResetGenerator(c);
	}

	/**
	 * Return a feature model with the characteristics received as input.
	 * 
	 * @param ch
	 *            User's preferences for the generation.
	 * 
	 * @return the feature model generated.
	 */
	@Override
	public VariabilityModel generateFM(Characteristics ch) {

		boolean valid = false;
		VariabilityModel model = null;
		GeneratorCharacteristics gch = ((GeneratorCharacteristics) ch).clone();
		Random random = new Random();

		while (!valid && maxtries < 100
				&& model == null) {
			model = super.generateFM(ch);
			FeatureModelTransform fmt= new FeatureModelTransform();
			Sat4jReasoner r = new Sat4jReasoner();
			fmt.transform(model, r);

			Sat4jValidQuestion vq= new Sat4jValidQuestion();
			r.ask(vq);
			valid=vq.isValid();
			
			long seed = gch.getSeed();
			gch = ((GeneratorCharacteristics) ch).clone();
			gch.setSeed(seed + random.nextInt());
		}
		return model;
	}

}
