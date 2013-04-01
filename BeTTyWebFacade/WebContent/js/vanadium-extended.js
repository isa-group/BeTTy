String.prototype.tokenize = tokenize;

// -------------------- vanadium-jquery.js -----------------------------
function tokenize() {
	var input = "";
	var separator = " ";
	var trim = "";
	var ignoreEmptyTokens = true;

	try {
		String(this.toLowerCase());
	} catch (e) {
		window
				.alert("Tokenizer Usage: string myTokens[] = myString.tokenize(string separator, string trim, boolean ignoreEmptyTokens);");
		return;
	}

	if (typeof (this) != "undefined") {
		input = String(this);
	}

	if (typeof (tokenize.arguments[0]) != "undefined") {
		separator = String(tokenize.arguments[0]);
	}

	if (typeof (tokenize.arguments[1]) != "undefined") {
		trim = String(tokenize.arguments[1]);
	}

	if (typeof (tokenize.arguments[2]) != "undefined") {
		if (!tokenize.arguments[2])
			ignoreEmptyTokens = false;
	}

	var array = input.split(separator);

	if (trim)
		for ( var i = 0; i < array.length; i++) {
			while (array[i].slice(0, trim.length) == trim)
				array[i] = array[i].slice(trim.length);
			while (array[i].slice(array[i].length - trim.length) == trim)
				array[i] = array[i].slice(0, array[i].length - trim.length);
		}

	var token = new Array();
	if (ignoreEmptyTokens) {
		for ( var i = 0; i < array.length; i++)
			if (array[i] != "")
				token.push(array[i]);
	} else {
		token = array;
	}

	return token;
}

function getElementMessage(name) {
	if (name == "nmodels") {
		return "Number of models";
	} else if (name == "nfeatures") {
		return "Number of features";
	} else if (name == "pctc") {
		return "Percentage of CTC";
	} else if (name == "name") {
		return "Name";
	} else if (name == "mail") {
		return "Mail";
	} else if (name == "organization") {
		return "Organization";
	} else if (name == "pmandatory") {
		return "Percentage of mandatory";
	} else if (name == "poptional") {
		return "Percentage of optional";
	} else if (name == "por") {
		return "Percentage of or";
	} else if (name == "palternative") {
		return "Percentage of alternative";
	} else if (name == "mbf") {
		return "Maximun branching factor";
	} else if (name == "mnc") {
		return "Maximum number of sub-features in sets";
	} else if (name == "sat") {
		return "Satisfiability";
	} else if (name == "attributes") {
		return "Attributes";
	} else if (name == "sat") {
		return "Satisfiability";
	} else if (name == "attType") {
		return "Domain";
	} else if (name == "numberOfAtts") {
		return "Number of attributes per feature";
	} else if (name == "splx") {
		return "SPLX";
	} else if (name == "famaxml") {
		return "FaMa XML";
	} else if (name == "famatf") {
		return "FaMa TExt Format";
	} else if (name == "jpg") {
		return "JPG";
	} else if (name == "x3d") {
		return "x3D";
	}
}

ElementValidation.prototype.create_advice = function(validation_result) {

	var span = document.createElement("span");
	this.created_advices.push(span);
	jQuery(span).addClass(Vanadium.config.advice_class);
	var paramInfo = getElementMessage(this.element.name);
	jQuery(span).html(
			"<p>" + paramInfo + " : " + validation_result.message + "</p>");
	// jQuery(this.element).after(span);
	jQuery("#errors").append(span);
	jQuery("#errors").show();

	return span;
};

ElementValidation.prototype.reset = function() {
	this.invalid = undefined; // mark this validation element as undefined
	// this.element_containers().each(function(_element, container) {
	// container.decorate();
	// });
	var element_advice = document.getElementById(this.advice_id);
	if (element_advice) {
		if (jQuery(element_advice).hasClass(Vanadium.empty_advice_marker_class)) {
			jQuery(element_advice).empty();
		}
		jQuery(element_advice).hide();
	}
	Vanadium.each(this.validations, function() {
		var advice = document.getElementById(this.adviceId);
		if (advice) {
			if (jQuery(advice).hasClass(Vanadium.empty_advice_marker_class)) {
				jQuery(advice).empty();
			}
			jQuery(advice).hide();
		}
		;
	});

	var created_advice = this.created_advices.pop();
	while (!(created_advice === undefined)) {
		jQuery(created_advice).remove();
		created_advice = this.created_advices.pop();
	}
	;
	Vanadium.removeValidationClass(this.element);

	if ($("#errors").children().length < 2) {
		$("#errors").hide();
	}
};

VanadiumCustomValidationTypes = [
		['percentage',	function(v) {
			if (Vanadium.validators_types['empty'].test(v)){
				throw new Error("Forced error");
			}
				
			var f = parseFloat(v);
			return (!isNaN(f) && f.toString() == v
					&& Math.round(f) == f && f >= 0 && f <= 100)
		},	'Please enter a valid percentage, between 0 and 100, in this field.' ],
		[ 'sum_100', function(v, elm) {
			var sum = 0;
			var options = elm.tokenize("-", "", true);
			var bool =true;
			for ( var index = 0; index < options.length && bool; index++) {
				var option = jQuery('input[name="' + options[index] + '"]');
				var f = parseFloat(option[0].value);
				if (!isNaN(f)) {
					sum += f;
				}else{
					bool=bool&&false;
				}
			}
			if(!bool){
				return true;
			}
			if(sum == 100){
			for ( var index = 0; index < options.length; index++) {
				var option = jQuery('input[name="' + options[index] + '"]');
				var element = Vanadium.elements_validators_by_id[options[index]];
                element.reset();
                option.removeClass(Vanadium.config.invalid_class);
                option.addClass(Vanadium.config.valid_class);
			}

			}
			return (sum == 100 || sum==0)
		}, 'Percentage of relation types should sum 100.' ],
		[ 'not_sum_100', function(v, elm) {
			var sum = 0;
			var options = elm.tokenize("-", "", true);
			for ( var index = 0; index < options.length; index++) {
				var option = jQuery('input[name="' + options[index] + '"]');
				var f = parseFloat(option[0].value);
				if (isNaN(f)) {
					return true;
				}
				sum += f;
			}
			if(sum != 100){
				for ( var index = 0; index < options.length; index++) {
					var option = jQuery('input[name="' + options[index] + '"]');
					var element = Vanadium.elements_validators_by_id[options[index]];
	                element.reset();
	                option.removeClass(Vanadium.config.invalid_class);
	                option.addClass(Vanadium.config.valid_class);
				}

				}
			return (sum != 100)
		}, 'Percentage of or and  alternative sum 100.' ],
		[ 'less_thanmbf', function(v, elm) {
			var v = parseFloat(v);
			var options = elm.tokenize("-", "", true);
			for ( var index = 0; index < options.length; index++) {
				var option = jQuery('input[name="' + options[index] + '"]');
				var f = parseFloat(option[0].value);
				if (isNaN(f)) {
					return true;
				}else if(v>f){
					return false;
				}
				
			}
			return (true)
		}, 'This parameter should be less or equal than branching factor.' ],
		[ 'less_than_10', function(v) {
			var v = parseFloat(v);
			if(v>10){
				return false;
			}else{
				return true;	
			}
			
		}, 'This parameter should be less or equal than 10.' ],
		[ 'less_than_50', function(v) {
			var v = parseFloat(v);

			if(v>50){
				return false;
			}else{
				return true;

			}

		}, 'This parameter should be less or equal than 50.' ],
		[ 'greater_than', function(v, elm) {
			var v = parseFloat(v);
			var f = parseFloat(elm);

			if (isNaN(f)) {
				return true;
			}else if(v<f){
				return false;
			}

			return (true)
		}, 'This parameter should be greater or equal than 2.' ],
	    ['integerpositive', function(v) {
	        if (Vanadium.validators_types['empty'].test(v)) return true;
	        var f = parseFloat(v);
	        return (!isNaN(f) && f.toString() == v && Math.round(f) == f && f>0);
	      }, 'Please enter a valid positive integer in this field.'],
		[ 'mult_10000', function(v, elm) {
			var mul = 1;
			var options = elm.tokenize("-", "", true);

			for ( var index = 0; index < options.length; index++) {
				var option = jQuery('input[name="' + options[index] + '"]');
				var f = parseFloat(option[0].value);
				if (isNaN(f)) {
					return true;
				}
				mul = mul * f;
			}
			if(mul < 10000){
				for ( var index = 0; index < options.length; index++) {
					var option = jQuery('input[name="' + options[index] + '"]');
					var element = Vanadium.elements_validators_by_id[options[index]];
	                element.reset();
	                option.removeClass(Vanadium.config.invalid_class);
	                option.addClass(Vanadium.config.valid_class);
				}

				}
			// Vanadium.validators_types['empty'].test(v)v
			return (mul < 10000)
		}, 'The total number of features should be less than 10000.' ],
		[ 'check_ctc', function(v) {
			var pctc = jQuery('input[name="pctc"]');
            var status = jQuery('input[name="sat"]').is(':checked');

			var f = parseFloat(pctc[0].value);
			if(isNaN(f)){
				return true;
			}
			return ((f<50 && status)||!status);
		}, 'If satisfability checked, please reduce the number of ctc less than 50.' ],
];
