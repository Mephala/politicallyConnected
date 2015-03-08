package service.provider.common.response;

import service.provider.common.dto.CategoryDto;

public class GetCategoryResponseDto extends AbstractResponseDto {
	private CategoryDto categoryDto;

	public GetCategoryResponseDto() {
	}

	public CategoryDto getCategoryDto() {
		return categoryDto;
	}

	public void setCategoryDto(CategoryDto categoryDto) {
		this.categoryDto = categoryDto;
	}

}
