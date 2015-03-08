package serviceprovider.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import serviceprovider.util.FileUtils;

@Controller
public class FileDownloadController {

	Log logger = LogFactory.getLog(getClass());

	@RequestMapping(value = "/{file_name}/getFile.do", method = RequestMethod.GET)
	public void getFile(@PathVariable("file_name") String fileName, HttpServletResponse response) {
		try {
			// get your file as InputStream
			InputStream is = FileUtils.getFileAsInputStream(fileName);
			// copy it to response's OutputStream
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			logger.info("Error writing file to output stream. Filename was " + fileName + " , Exception:" + ex);
			throw new RuntimeException("IOError writing file to output stream");
		}

	}

	@RequestMapping(value = { "/{file_name}/getFileAsDownload.do", "/{ignoreString}/{file_name}/getFileAsDownload.do" }, method = RequestMethod.GET)
	public void getFileAsDownload(@PathVariable("file_name") String fileName, HttpServletResponse response, HttpServletRequest request) throws IOException {
		// get absolute path of the application

		InputStream inputStream = FileUtils.getFileAsInputStream(fileName);

		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", fileName);
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[2048];

		int bytesRead;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outStream.close();

	}
}
