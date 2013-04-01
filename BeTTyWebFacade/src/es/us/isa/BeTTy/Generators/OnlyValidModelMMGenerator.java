package es.us.isa.BeTTy.Generators;

import java.util.Random;

import es.us.isa.FAMA.models.variabilityModel.VariabilityModel;
import es.us.isa.generator.Characteristics;
import es.us.isa.generator.IGenerator;
import es.us.isa.generator.FM.GeneratorCharacteristics;
import es.us.isa.generator.FM.MetamorphicFMGenerator;

public class OnlyValidModelMMGenerator extends MetamorphicFMGenerator {

	int maxtries = 20;

	public OnlyValidModelMMGenerator(IGenerator gen) {
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

		VariabilityModel model = null;
		GeneratorCharacteristics gch = ((GeneratorCharacteristics) ch).clone();
		Random random = new Random();

		while (super.getNumberOfProducts() < 1 && maxtries < 100
				&& model == null) {
			model = super.generateFM(ch);
			long seed = gch.getSeed();
			gch = ((GeneratorCharacteristics) ch).clone();
			gch.setSeed(seed + random.nextInt());

		}
		return model;
	}

}
