package es.us.isa.BeTTy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletContext;

import es.us.isa.FAMA.models.domain.Range;
import es.us.isa.generator.Characteristics;
import es.us.isa.generator.FM.GeneratorCharacteristics;
import es.us.isa.generator.FM.attributed.AttributedCharacteristic;

public class CharacteristicsGeneratorGenerator {

	// DATA
	public String mail = "not used";
	public String name, organization;

	// FORMATS

	public boolean sat = false;
	public boolean splx = false;
	public boolean famaxml = false;
	public boolean famatf = false;
	public boolean jpg = false;
	public boolean x3d = false;

	private Properties prop;

	// OPTHERS
	private GeneratorCharacteristics chars;
	private Collection<String> errors = new ArrayList<String>();

	public boolean extended;
	public int numberOfAttributes =2;
	public String attType;
	public int nmodels;

	public CharacteristicsGeneratorGenerator(String name, String mail,
			String organization, String nmodels, String nfeatures,
			String pmandatory, String poptional, String palternative,
			String por, String pctc, String mbf, String mnc, String attributes,
			String attType, String numberOfAtts, String sat, String splx,
			String famaxml, String famatf, String jpg, String x3d,
			Properties prop) {

		this.prop = prop;
		this.name = name;
		this.mail = mail;
		this.organization = organization;

		// Select the type of model to generate
		if (attributes == null || attributes.equals("")) {
			chars = new GeneratorCharacteristics();
			extended = false;
		} else {
			chars = new AttributedCharacteristic();
			//Add default parameters
			((AttributedCharacteristic)chars).setNumberOfAttibutesPerFeature(2);
			((AttributedCharacteristic)chars).addRange(new Range(0, 100));
			String argumentsDistributionFunction[] = { "0", "100" };
			((AttributedCharacteristic)chars).setDistributionFunctionArguments(argumentsDistributionFunction);
			((AttributedCharacteristic)chars).setHeadAttributeName("Atribute");
			((AttributedCharacteristic)chars).setNumberOfExtendedCTC(0);

			extended = true;
		}

		// Start with the validation

		// If required is missing, send feedback to the user
		if (nmodels == null || nmodels.equals("")) {
			this.errors.add("modelsrequired");
			// If everything is correct, assign data to the characteristic
		} else {
			// The number of models should not be in characteristic
			try {
				this.nmodels = Integer.parseInt(nmodels);
				if (this.nmodels < 1) {
					this.errors.add("modelsinteger");
				}
			} catch (Exception e) {
				this.errors.add("modelsinteger");

			}
		}

		// If required is missing, send feedback to the user
		if (nfeatures == null || nfeatures.equals("")) {
			this.errors.add("featuresrequired");

			// If everything is correct, assign data to the characteristic
		} else {
			try {
				int i = Integer.parseInt(nfeatures);
				this.chars.setNumberOfFeatures(i);
				if (i < 2) {
					this.errors.add("featuresinteger");
				}
			} catch (Exception e) {
				// Finally its not an Integer, add an error

				this.errors.add("featuresinteger");
			}
		}

		if(this.chars.getNumberOfFeatures()*this.nmodels>10000){
			this.errors.add("featureslessthan");

		}
		// If required is missing, send feedback to the user
		if (pctc == null || pctc.equals("")) {
			this.errors.add("ctcrequired");
			// If everything is correct, assign data to the characteristic
		} else {
			try {
				int i = Integer.parseInt(pctc);
				checkPercentage(i);
				this.chars.setPercentageCTC(i);
			} catch (Exception e) {
				// Finally its not an Integer, add an error
				this.errors.add("ctcpercentage");
			}

		}

		// Check that if one percentage is marked the sum should be 100 and if
		// sum 100, add to generator chars
		if (pmandatory != null && !pmandatory.equals("") || poptional != null
				&& !poptional.equals("") || por != null && !por.equals("")
				|| palternative != null && !palternative.equals("")) {

			int ma, op, or, al;
			ma = 0;
			op = 0;
			or = 0;
			al = 0;
			boolean cuatro = true;
			if (pmandatory == null || pmandatory.equals("")) {
				ma = 0;
				cuatro = cuatro && false;
			} else {
				try {
					ma = Integer.parseInt(pmandatory);
					checkPercentage(ma);
				} catch (Exception e) {
					this.errors.add("mandatorypercentage");
				}
			}

			if (poptional == null || poptional.equals("")) {
				op = 0;
				cuatro = cuatro && false;
			} else {
				try {
					op = Integer.parseInt(poptional);
					checkPercentage(op);
				} catch (Exception e) {
					this.errors.add("optionalpercentage");
				}
			}

			if (por == null || por.equals("")) {
				or = 0;
				cuatro = cuatro && false;

			} else {
				try {
					or = Integer.parseInt(por);
					checkPercentage(or);
				} catch (Exception e) {
					this.errors.add("orpercentage");

				}
			}

			if (palternative == null || palternative.equals("")) {
				al = 0;
				cuatro = cuatro && false;

			} else {
				try {
					al = Integer.parseInt(palternative);
					checkPercentage(al);
				} catch (Exception e) {
					this.errors.add("alternativepercentage");

				}
			}

			int sum = ma + op + or + al;

			if (sum != 100 && sum != 0 && cuatro) {
				this.errors.add("sumpercentage");
			} else if (sum != 0) {
				if (al != 0) {
					this.chars.setProbabilityAlternative(al);
				}
				if (ma != 0) {
					this.chars.setProbabilityMandatory(ma);
				}
				if (op != 0) {
					this.chars.setProbabilityOptional(op);
				}
				if (or != 0) {
					this.chars.setProbabilityOr(or);
				}
			}
		}

		// Check optional variables without any relation
		// if not null then check if its an integer
		if (mbf != null && !mbf.equals("")) {
			try {
				this.chars.setMaxBranchingFactor(Integer.parseInt(mbf));
				if (this.chars.getMaxBranchingFactor() < 1) {
					this.errors.add("branchingfactorinteger");
				}
			} catch (Exception e) {
				this.errors.add("branchingfactorinteger");

			}
		}

		// if not null then check if its an integer
		if (mnc != null && !mnc.equals("")) {
			try {
				this.chars.setMaxSetChildren(Integer.parseInt(mnc));
				if (this.chars.getMaxSetChildren() < 1) {
					this.errors.add("numberofchildinteger");
				}
			} catch (Exception e) {
				this.errors.add("numberofchildinteger");
			}
		}

		if (this.chars.getMaxSetChildren() > this.chars.getMaxBranchingFactor()) {
			this.errors.add("numberofchildrenlt");
		}
		// Check the formats and sat

		if (sat != null && sat.equals("on")) {
			this.sat = true;
			this.chars.setMaxProducts(Integer.MAX_VALUE);
		}
		if (this.sat && this.chars.getPercentageCTC() > 50) {
			this.errors.add("satctc");
		}
		if (splx != null && splx.equals("on")) {
			this.splx = true;
		}
		if (famaxml != null && famaxml.equals("on")) {
			this.famaxml = true;
		}
		if (famatf != null && famatf.equals("on")) {
			this.famatf = true;
		}
		if (jpg != null && jpg.equals("on")) {
			this.jpg = true;
		}
		if (x3d != null && x3d.equals("on")) {
			this.x3d = true;
		}

		// At least one format should be on
		if (!this.splx && !this.famaxml && !this.famatf && !this.jpg
				&& !this.x3d) {
			this.famatf = true;
		}

		// Attributes
		// If we use attributes only return the result in famatf
		if (extended) {
			this.splx = false;
			this.famatf = true;
			this.famaxml = false;
			this.jpg = false;
			this.x3d = false;
		}

		// if extended be sure that the att is integer o double
		if (extended && !attType.equals("integer") && !attType.equals("double")) {
			this.errors.add("attributestype");
		} else {
			this.attType = attType;
		}

		// if extended specify the number of atts
		if ((extended && numberOfAtts == null)
				|| (extended && numberOfAtts.equals(""))) {
			//this.errors.add("attributesrequired");
			//now we use a default value
		} else if (extended) {
			try {
				this.numberOfAttributes = (Integer.parseInt(numberOfAtts));
				((AttributedCharacteristic)chars).setNumberOfAttibutesPerFeature(numberOfAttributes);
				
				if (this.numberOfAttributes > 10) {
					this.errors.add("maxatts");
				}
			} catch (Exception e) {
				this.errors.add("attributesinteger");

			}
		}

	}

	public Characteristics getCharacteristics() {
		return this.chars;

	}

	public void checkPercentage(int i) {
		if (i > 100 || i < 0) {
			throw new NumberFormatException();
		}
	}

	public String getHeader() {
		String str = "Name;Organization;Mail;Extended;NModels;NFeatures;PercentageOfCTC;PMandatory;POptional;PAlternative;POr;Satisafbility;NumberOfAtts;TypeOfAtts;Formats;Errors";
		return str;
	}

	public String getCSV() {
		String str = this.name + ";" + this.organization + ";" + this.mail
				+ ";" + this.extended + ";" + this.nmodels + ";"
				+ this.chars.getNumberOfFeatures() + ";"
				+ this.chars.getPercentageCTC() + ";"
				+ this.chars.getProbabilityMandatory() + ";"
				+ this.chars.getProbabilityOptional() + ";"
				+ this.chars.getProbabilityAlternative() + ";"
				+ this.chars.getProbabilityOr() + ";" + numberOfAttributes
				+ ";" + this.attType + ";" + this.sat + ";";

		if (splx) {
			str += "splx,";
		}
		;
		if (famatf) {
			str += "famatf,";
		}
		;
		if (famaxml) {
			str += "famaxml,";
		}
		;
		if (jpg) {
			str += "jpg,";
		}
		;
		if (x3d) {
			str += "x3d,";
		}
		;
		str = str.substring(0, str.length() - 1);
		str += ";";

		for (String data : errors) {
			str += prop.getProperty(data) + ",";
		}
		str = str.substring(0, str.length() - 1);

		return str;
	}

	public boolean hasErrors() {
		return this.errors.size() > 0;
	}

	public String getHtmlErrors() {
		String ret = "";
		for (String data : errors) {
			ret += prop.getProperty("headclass") + prop.getProperty(data)
					+ prop.getProperty("tailclass");
		}
		return ret;

	}

}
