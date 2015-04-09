package service.provider.common.request;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveCategoryRequestDto extends AbstractRequestDto {

	public SaveCategoryRequestDto() {
		;
	}

	public SaveCategoryRequestDto(String saveUserUri) {
		this.setRequestUri(saveUserUri);
	}

	private String categoryName;

	private Long categoryId;

	private Integer priority;

	private Long parentCategoryId;

	private List<Long> childCategoryIds;

	public boolean isValid() {
		if (categoryName != null && categoryName.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public List<Long> getChildCategoryIds() {
		return childCategoryIds;
	}

	public void setChildCategoryIds(List<Long> childCategoryIds) {
		this.childCategoryIds = childCategoryIds;
	}

	@Override
	public String toString() {
		return "SaveCategoryRequestDto [categoryName=" + categoryName + ", priority=" + priority + ", parentCategoryId=" + parentCategoryId + ", childCategoryIds=" + childCategoryIds + "]";
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

}
