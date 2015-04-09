package serviceprovider.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.provider.common.core.ResponseStatus;
import service.provider.common.dto.ImageDto;
import service.provider.common.exception.AbstractServiceException;
import service.provider.common.request.GetAllImageIdsRequestDto;
import service.provider.common.request.GetImageRequestDto;
import service.provider.common.response.GetAllImageIdsResponseDto;
import service.provider.common.response.GetImageResponseDto;
import serviceprovider.relationer.manager.ImageManager;
import serviceprovider.relationer.model.DBImage;

@Controller
public class ImageController extends AbstractServiceController {

	private final Log logger = LogFactory.getLog(getClass());
	private ImageManager imageManager = ImageManager.getInstance();

	@RequestMapping(value = "/getAllImageIds.do", method = RequestMethod.POST)
	@ResponseBody
	public GetAllImageIdsResponseDto getAllImageIds(HttpServletRequest request, HttpServletResponse response, @RequestBody GetAllImageIdsRequestDto getAllImagesRequest) {
		logger.info("Getting all image ids for request:" + getAllImagesRequest);
		GetAllImageIdsResponseDto responseDto = new GetAllImageIdsResponseDto();
		try {
			validateRequest(getAllImagesRequest);
			List<Long> dbImageIds = new ArrayList<Long>();
			List<DBImage> allImages = imageManager.getAllModelList(getAllImagesRequest.getRequestApp());
			for (DBImage dbImage : allImages) {
				dbImageIds.add(dbImage.getId());
			}
			responseDto.setImageIds(dbImageIds);
			responseDto.setResponseStatus(ResponseStatus.OK);
			logger.info("All Image ids have been set upon request... Request:" + getAllImagesRequest);
		} catch (AbstractServiceException ase) {
			logger.info("request  encountered serviceException. Exception:" + ase);
			setMeaningfulException(responseDto, ase);
		} catch (Exception e) {
			logger.info("request  encountered serviceException. Exception:" + e);
			responseDto.setError("Error occured");
			responseDto.setResponseStatus(ResponseStatus.ERROR);
		}
		return responseDto;
	}

	@RequestMapping(value = "/getImage.do", method = RequestMethod.POST)
	@ResponseBody
	public GetImageResponseDto getImage(HttpServletRequest request, HttpServletResponse response, @RequestBody GetImageRequestDto getImageRequest) {
		logger.info("Received request for serializing an image... Req:" + getImageRequest);
		GetImageResponseDto responseDto = new GetImageResponseDto();
		try {
			validateRequest(getImageRequest);
			Long imageId = getImageRequest.getImageId();
			ImageDto imageDto = imageManager.createImageDto(imageId);
			responseDto.setImageDto(imageDto);
			responseDto.setResponseStatus(ResponseStatus.OK);
			logger.info("Image has been serialized succesfully opon request... Request:" + getImageRequest);
		} catch (AbstractServiceException ase) {
			logger.info("request  encountered serviceException. Exception:" + ase);
			setMeaningfulException(responseDto, ase);
		} catch (Exception e) {
			logger.info("request  encountered serviceException. Exception:" + e);
			responseDto.setError("Error occured");
			responseDto.setResponseStatus(ResponseStatus.ERROR);
		}
		return responseDto;
	}

}
