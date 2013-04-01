package es.us.isa.BeTTy.WebFacade;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.us.isa.BeTTy.CharacteristicsGeneratorGenerator;
import es.us.isa.BeTTy.Generators.OnlyValidModelSATGenerator;
import es.us.isa.FAMA.models.domain.Range;
import es.us.isa.benchmarking.Benchmark;
import es.us.isa.benchmarking.Experiment;
import es.us.isa.benchmarking.FAMABenchmark;
import es.us.isa.benchmarking.RandomExperiment;
import es.us.isa.generator.IGenerator;
import es.us.isa.generator.FM.FMGenerator;
import es.us.isa.generator.FM.attributed.AttributedCharacteristic;
import es.us.isa.generator.FM.attributed.AttributedFMGenerator;
import es.us.isa.generator.FM.attributed.distribution.DoubleUniformDistributionFunction;
import es.us.isa.generator.FM.attributed.distribution.IntegerUniformDistributionFunction;
import es.us.isa.utils.FMWriter;

/**
 * Servlet implementation class BeTTyFacadeZipGenerator
 */
public class BeTTyFacadeZipGenerator extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	private static String filepath = FILE_SEPARATOR + "home" + FILE_SEPARATOR	+ "malawito" + FILE_SEPARATOR + "form" + FILE_SEPARATOR;
	//private static String filepath="C:\\Users\\malawito\\Documents\\";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		filepath.trim();
		String propertiesFilePath = getServletContext().getRealPath(
				"WEB-INF/errors.properties");
		Properties prop = new Properties();
		prop.load(new FileInputStream(propertiesFilePath));

		CharacteristicsGeneratorGenerator chGen = new CharacteristicsGeneratorGenerator(
				request.getParameter("name"), request.getParameter("mail"),
				request.getParameter("organization"),
				request.getParameter("nmodels"),
				request.getParameter("nfeatures"),
				request.getParameter("pmandatory"),
				request.getParameter("poptional"),
				request.getParameter("palternative"),
				request.getParameter("por"), request.getParameter("pctc"),
				request.getParameter("mbf"), request.getParameter("mnc"),
				request.getParameter("attributes"), "integer",
				request.getParameter("numberOfAtts"),
				request.getParameter("sat"), request.getParameter("splx"),
				request.getParameter("famaxml"),
				request.getParameter("famatf"), request.getParameter("jpg"),
				request.getParameter("x3d"), prop);
		if (chGen.hasErrors()) {
			printInformation(chGen);
			request.setAttribute("message", chGen.getHtmlErrors());

			RequestDispatcher dispatcher = request
					.getRequestDispatcher("index.jsp");
			dispatcher.forward(request, response);
		} else {
			File directory = createTempDir();
			FMGenerator generator = new FMGenerator();
			OnlyValidModelSATGenerator genV = new OnlyValidModelSATGenerator(
					generator);
			IGenerator atGenerator = new AttributedFMGenerator(generator);
			Benchmark b;

			if (chGen.extended) {
				b = new FAMABenchmark(atGenerator);
				

			} else {
				if (chGen.sat) {

					b = new FAMABenchmark(genV);
				} else {
					b = new FAMABenchmark(generator);
				}
			}
			try {
				ArrayList<RandomExperiment> experiments = b
						.createSetRandomExperiment("FeatureModel",chGen.nmodels,
								chGen.getCharacteristics());
				for (Experiment e : experiments) {
					// STEP 3: Save the model and the products
					FMWriter writer = new FMWriter();

					if (chGen.famaxml) {
						writer.saveFM(e.getVariabilityModel(),
								directory.getPath() + "/" + e.getName()
										+ ".xml");
					}
					if (chGen.jpg) {
						writer.saveFM(e.getVariabilityModel(),
								directory.getPath() + "/" + e.getName()
										+ ".dot");
					}
					if (chGen.splx) {
						writer.saveFM(e.getVariabilityModel(),
								directory.getPath() + "/" + e.getName()
										+ ".splx");
					}
					if (chGen.x3d) {
						writer.saveFM(e.getVariabilityModel(),
								directory.getPath() + "/" + e.getName()
										+ ".x3d");
					}
					if (chGen.famatf) {
						writer.saveFM(e.getVariabilityModel(),
								directory.getPath() + "/" + e.getName()
										+ ".afm");
					}
				}
				experiments = null;

			} catch (Exception e2) {

				request.setAttribute("message", prop.getProperty("headclass")
						+ e2.getMessage() + prop.getProperty("tailclass"));

				RequestDispatcher dispatcher = request
						.getRequestDispatcher("index.jsp");
				dispatcher.forward(request, response);
			}

			printInformation(chGen);
			//If all went ok, clear all error messages
			request.removeAttribute("message");
			
			// Compress and return the generated files
			compressAndReturn(response, directory);


			// force cleaning
			b = null;
			System.gc();
		}
	}

	private void compressAndReturn(HttpServletResponse response, File directory)
			throws IOException {

		String[] files = directory.list();

		//
		// Checks to see if the directory contains some files.
		//
		if (files != null && files.length > 0) {

			//
			// Call the zipFiles method for creating a zip stream.
			//
			byte[] zip = zipFiles(directory, files);

			//
			// Sends the response back to the user / browser. The
			// content for zip file type is "application/zip". We
			// also set the content disposition as attachment for
			// the browser to show a dialog that will let user
			// choose what action will he do to the sent content.
			//
			ServletOutputStream sos = response.getOutputStream();
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition",
					"attachment; filename=\"DATA.ZIP\"");

			sos.write(zip);
			sos.flush();
			sos.close();
			sos = null;
		}

		// Delete the directory as it will not be any longer used
		recursiveDelete(directory);
	}

	/**
	 * Compress the given directory with all its files.
	 */
	private byte[] zipFiles(File directory, String[] files) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		byte bytes[] = new byte[2048];

		for (String fileName : files) {
			FileInputStream fis = new FileInputStream(directory.getPath()
					+ BeTTyFacadeZipGenerator.FILE_SEPARATOR + fileName);
			BufferedInputStream bis = new BufferedInputStream(fis);

			zos.putNextEntry(new ZipEntry(fileName));

			int bytesRead;
			while ((bytesRead = bis.read(bytes)) != -1) {
				zos.write(bytes, 0, bytesRead);
			}
			zos.closeEntry();
			bis.close();
			fis.close();
		}
		zos.flush();
		baos.flush();
		zos.close();
		baos.close();
		return baos.toByteArray();
	}

	/**
	 * Create a new temporary directory. Use something like
	 * {@link #recursiveDelete(File)} to clean this directory up since it isn't
	 * deleted automatically
	 * 
	 * @return the new directory
	 * @throws IOException
	 *             if there is an error creating the temporary directory
	 */
	public File createTempDir() throws IOException {
		final File sysTempDir = new File(System.getProperty("java.io.tmpdir"));
		File newTempDir;
		final int maxAttempts = 20;
		int attemptCount = 0;
		do {
			attemptCount++;
			if (attemptCount > maxAttempts) {
				throw new IOException(
						"The highly improbable has occurred! Failed to "
								+ "create a unique temporary directory after "
								+ maxAttempts + " attempts.");
			}
			String dirName = UUID.randomUUID().toString();
			newTempDir = new File(sysTempDir, dirName);
		} while (newTempDir.exists());

		if (newTempDir.mkdirs()) {
			return newTempDir;
		} else {
			throw new IOException("Failed to create temp dir named "
					+ newTempDir.getAbsolutePath());
		}
	}

	/**
	 * Recursively delete file or directory
	 * 
	 * @param fileOrDir
	 *            the file or dir to delete
	 * @return true iff all files are successfully deleted
	 */
	public boolean recursiveDelete(File fileOrDir) {
		if (fileOrDir.isDirectory()) {
			// recursively delete contents
			for (File innerFile : fileOrDir.listFiles()) {
				if (!this.recursiveDelete(innerFile)) {
					return false;
				}
			}
		}

		return fileOrDir.delete();
	}

	private void printInformation(CharacteristicsGeneratorGenerator g) {

		BufferedWriter outcsv = null;
		try {
			Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat("MMyyyy");
		    String date = sdf.format(cal.getTime());
			
			
			// Should be an static route, because we need permisions on the path
			File file = new File(filepath+date+".csv");
			if (!file.exists()) {
				outcsv = new BufferedWriter(new FileWriter(file, false));
				outcsv.write(g.getHeader());

			} else {
				outcsv = new BufferedWriter(new FileWriter(file, true));
			}
			outcsv.write("\r\n");
			outcsv.write(g.getCSV());
			outcsv.flush();
			outcsv.close();
			outcsv = null;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
