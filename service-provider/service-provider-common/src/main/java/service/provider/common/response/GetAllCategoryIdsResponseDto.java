package service.provider.common.response;

import java.util.List;

public class GetAllCategoryIdsResponseDto extends AbstractResponseDto {

	private List<Long> categoryIds;

	public GetAllCategoryIdsResponseDto() {
		;
	}

	public List<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

	@Override
	public String toString() {
		return "GetAllCategoryIdsResponseDto [categoryIds=" + categoryIds + "]";
	}

}
