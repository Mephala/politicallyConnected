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
import org.springframework.web.bind.annotation.ResponseBody;

import serviceprovider.util.SPSFileUtils;

@Controller
public class FileDownloadController extends AbstractServiceController {

	private final Log logger = LogFactory.getLog(getClass());

	@RequestMapping(value = { "/{file_name}/downloadFile.do", "/{ignoredString}/{file_name}/downloadFile.do" }, method = RequestMethod.GET)
	public synchronized void getFileAsDownload(@PathVariable("file_name") String fileName, HttpServletResponse response, HttpServletRequest request) throws IOException {
		// get absolute path of the application
		logger.info("New download request has been received with filename:" + fileName);
		InputStream inputStream = SPSFileUtils.getFileAsInputStream(fileName);
		if (inputStream != null) {
			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", fileName);
			response.setHeader(headerKey, headerValue);
			// get output stream of the response
			OutputStream outStream = response.getOutputStream();

			try {
				// get your file as InputStream
				// copy it to response's OutputStream
				org.apache.commons.io.IOUtils.copy(inputStream, outStream);
				response.flushBuffer();
				logger.info("Requested file has been uploaded successfully. Filename +" + fileName);
			} catch (IOException ex) {
				logger.error("Error writing file to output stream. Filename was " + fileName, ex);
			}
		} else {
			logger.info("Requested file is not found. FileName:" + fileName);
		}
	}

	@RequestMapping(value = "/{file_name}/getFileSize.do")
	@ResponseBody
	public synchronized Long getFileSize(@PathVariable("file_name") String fileName, HttpServletResponse response, HttpServletRequest request) throws IOException {
		logger.info("Returning filesize for:" + fileName);
		InputStream inputStream = SPSFileUtils.getFileAsInputStream(fileName);
		if (inputStream != null) {
			byte[] buffer = new byte[1024];
			int readBytes = 0;
			long totalBytes = 0;
			while (readBytes != -1) {
				readBytes = inputStream.read(buffer);
				if (readBytes != -1)
					totalBytes += readBytes;
			}
			logger.info("Returning filesize for:" + fileName + " as " + totalBytes + " bytes.");
			return Long.valueOf(totalBytes);
		} else {
			logger.info("File not found for filename:" + fileName);
			return Long.valueOf(-1);
		}
	}
}