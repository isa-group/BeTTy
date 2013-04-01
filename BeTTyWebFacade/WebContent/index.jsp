<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/form.css"
	title="default">
<script type="text/javascript" src="js/jquery-1.6.4.js"></script>
<script type="text/javascript" src="js/vanadium.js"></script>
<script type="text/javascript" src="js/vanadium-extended.js"></script>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-27557382-1']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
<script>
	function showOptions() {

		$("#div_options").toggle("slow");

		$("#options")
				.text(
						$("#options").text() == 'Show Advanced Options' ? 'Hide Advanced Options'
								: 'Show Advanced Options');

	}
	function checkErrorsMessages() {
		
		if ($("#errors").children().length > 1) {
			$("#errors").show("slow");
		}else{
			$("#errors").hide("slow");

		}
	}
	
</script>


<title>Betty Web Frontend</title>

</head>
<body onload="checkErrorsMessages()">

	<div id="container">
		
		<div id="leftSide">
			<form method="POST" action="BeTTyFacadeZipGenerator" 
				name="form">


				<fieldset>
					<legend>Basic Data</legend>
					<label>Number of models (*)</label>
					<div class="div_input div_texbox">
						<input name="nmodels" type="text"
							class=":required :integerpositive :mult_10000;nmodels-nfeatures"
							id="nmodels" size="20">
					</div>
					<label>Number of features (*)</label>
					<div class="div_input div_texbox">
						<input name="nfeatures" type="text"
							class=":required :integerpositive :greater_than;2 :mult_10000;nmodels-nfeatures"
							id="nfeatures" size="20">
					</div>

					<label>Percentage of CTC (*)</label>
					<div class="div_input div_texbox">
						<input name="pctc" type="text" class=":percentage "
							size="20"><span>%</span>
					</div>
				</fieldset>

				<fieldset>
					<legend>User Information</legend>
					<label>Name (*)</label>
					<div class="div_input div_texbox">
						<input name="name" type="text" class=":required" id="name"
							size="20">
					</div>
					<!--		<label>E-Mail (*)</label>
					<div class="div_texbox">
						<input name="mail" type="text" class=":required :email" id="mail"
							size="20">
					</div> -->
					<label>Organization (*)</label>
					<div class="div_input div_texbox">
						<input name="organization" type="text" class=":required"
							id="organization" size="20">
					</div>
				</fieldset>
				<div id="showoptions" >
				<a id="options" id="options" onclick="showOptions()">Show Advanced Options</a>
				</div>
				<div id="div_options" style="display: none;">
					<fieldset>
						<legend>Feature Tree</legend>
						<label>Percentage of mandatory relationships </label>
						<div class="div_input div_texbox">
							<input type="text" name="pmandatory" id="pmandatory"
								class=":percentage :sum_100;por-palternative-poptional-pmandatory"
								size="20"> <span>% [Default value: random] </span>
						</div>
						<label>Percentage of optional relationships</label>
						<div class="div_input div_texbox">
							<input type="text" name="poptional" id="poptional"
								class=":percentage :sum_100;por-palternative-poptional-pmandatory"
								size="20"> <span>% [Default value: random] </span>
						</div>
						<label>Percentage of alternative relationships</label>
						<div class="div_input div_texbox">
							<input type="text" name="palternative" id="palternative"
								class=":percentage :sum_100;por-palternative-poptional-pmandatory :not_sum_100;por-palternative "
								size="20"> <span>% [Default value: random] </span>
						</div>
						<label>Percentage of or-relationships</label>
						<div class="div_input div_texbox">
							<input type="text" name="por" id="por"
								class=":percentage :sum_100;por-palternative-poptional-pmandatory :not_sum_100;por-palternative "
								size="20"> <span>% [Default value: random] </span>
						</div>
						<label>Maximum branching factor</label>
						<div class="div_input div_texbox">
							<input type="text" name="mbf" id="mbf" size="20"
								class=":integerpositive"> <span> [Default value:
								10] </span>
						</div>
						<label>Maximum number of sub-features in sets</label>
						<div class="div_input div_texbox">
							<input type="text" name="mnc" id="mnc" size="20"
								class=":integerpositive :less_thanmbf;mbf"> <span>
								[Default value: 5] </span>
						</div>
					</fieldset>


					<fieldset>
						<legend>Satisfiability</legend>
						<label>Generate only valid models</label>
						<div class="div_input div_checkbox">
							<input type="checkbox" name="sat" id="sat" size="20"
								class=":check_ctc"><span> [This option applies to non-attributed FMs only] </span>
						</div>
					</fieldset>


					<fieldset>
						<legend>Attributed Feature Models</legend>
						<label>Generate attributes</label>
						<div class="div_input div_checkbox">
							<input type="checkbox" name="attributes" id="attributes"
								size="20" class="">
						</div>
						<!-- 
						<label>Domain</label>
						<div class="div_input div_checkbox">
							<select name="attType" id="attType">
								<option value="double">Real</option>
								<option value="integer" selected="selected">Integer</option>
							</select>
						</div>
						-->
						<label>Number of attributes per feature</label>
						<div class="div_input div_textbox">
							<input type="text" name="numberOfAtts" id="numberOfAtts"
								size="20" class=":integerpositive :less_than_10"><span>[Default value: 2]</span>
						</div>
					</fieldset>


					<fieldset>
						<legend>Formats [This option applies to non-attributed FMs only]</legend>
						<label>SXFM</label>
						<div class="div_input div_checkbox">
							<input id="sxfm" name="splx" type="checkbox" class="">
						</div>
						<label>FaMaXML</label>
						<div class="div_input div_checkbox">
							<input id="famaxml" name="famaxml" type="checkbox" class="">
						</div>
						<label>FaMaTextFormat</label>
						<div class="div_input div_checkbox">
							<input id="famatf" name="famatf" type="checkbox" checked=”checked”  readonly=”readonly”>
							<span> [Default] </span>
						</div>
						<label>DOT</label>
						<div class="div_input div_checkbox ">
							<input id="jpeg" name="jpg" type="checkbox" class="">
						</div>
						<label>X3D</label>
						<div class="div_input div_checkbox">
							<input id="x3d" name="x3d" type="checkbox" class="">
						</div>
					</fieldset>

				</div>
				<div class="button_div">
					<input type="submit" value="Generate!" class="" name="B1">
				</div>
			</form>
		</div>
		<div id="rcontainerparent">
			<div id="rcontainer">
				<div id="information" class="right">
					<div align="center">
						<img alt="BeTTY" height="50" width="100"
							src="http://www.isa.us.es/betty/sites/www.isa.us.es.betty/files/images/BeTTy%20(1)_0.jpg">
					</div>
					<!-- 
					<p>BeTTy is an extensible and highly configurable framework
						supporting BEnchmarking and TestTing on the analYses of feature
						models. It is written in Java and is distributed as a jar file
						facilitating its integration into external projects.</p>
					 -->
					<p>Please, use the following reference to cite this site:
 					</br>
 					</br>
 					S. Segura, J.A. Galindo, D. Benavides and A. Ruiz-Cortés. 
 					BeTTy: Benchmarking and Testing on the Automated Analysis of 
 					Feature Models. Sixth International Workshop on Variability
 					 Modelling of Software-intensive Systems (VaMoS'12). Leipzig, Germnay. 2012.<a href="http://www.lsi.us.es/~segura/files/bibtex/segura12-vamos.bib">[BibTex]</a></p>
				</div>
				<div id="errors" class="right">
					<center>
						<span class="errorheader">Something went wrong with your
							submision</span>
					</center>
					<%
						if (request.getAttribute("message") != null) {
							out.println(request.getAttribute("message"));
						}
					%>
				</div>
			</div>
		</div>
		<div class="clear"></div>
	</div>
</body>
</html>