package serviceprovider.relationer.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import service.provider.common.core.RequestApplication;
import service.provider.common.dto.ImageDto;
import serviceprovider.Application;
import serviceprovider.manager.AbstractServiceManager;
import serviceprovider.relationer.dao.DBImageDAO;
import serviceprovider.relationer.model.DBImage;

public class ImageManager extends AbstractServiceManager<DBImage> {

	private static ImageManager instance;
	private DBImageDAO dao;
	private Log logger = LogFactory.getLog(getClass());

	private ImageManager() {
		dao = DBImageDAO.getInstance();
		initializeDAO(dao);
	}

	public static synchronized ImageManager getInstance() {
		if (instance == null)
			instance = new ImageManager();
		return instance;
	}

	private String serializeImage(DBImage image) throws SQLException, UnsupportedEncodingException {
		byte[] bdata = getByteArrayFromDBImage(image);
		String s = new String(Base64.encodeBase64(bdata), "US-ASCII");
		return s;
	}

	public byte[] getByteArrayFromDBImage(DBImage image) {
		byte[] buffer = null;
		try {
			Blob imageBlob = image.getImageBlob();
			buffer = new byte[(int) (imageBlob.length() * 2)];
			int bufsz = (int) imageBlob.length();
			InputStream is = imageBlob.getBinaryStream();
			int len = -1, off = 0;
			while ((len = is.read(buffer, off, bufsz)) != -1) {
				off += len;
			}
		} catch (IOException ioe) {
			logger.error("IO exception during getting byte array from Dbimage", ioe);
		} catch (Exception e) {
			logger.error("Exception occured during getting byte array from DBImage", e);
		}
		return buffer;

	}

	public ImageDto createImageDto(Long id) throws UnsupportedEncodingException, SQLException {
		DBImage dbImage = dao.findModelById(id);
		ImageDto imageDto = null;
		if (dbImage != null) {
			imageDto = new ImageDto();
			imageDto.setId(id);
			imageDto.setEncodedData(serializeImage(dbImage));
		}
		return imageDto;
	}

	public List<DBImage> getAllModelList(RequestApplication requestApp) {
		Application app = Application.SERVICE;
		switch (requestApp) {
		case WEB:
			app = Application.WEB;
			break;
		}
		return dao.getAllModelList(app);
	}

}
