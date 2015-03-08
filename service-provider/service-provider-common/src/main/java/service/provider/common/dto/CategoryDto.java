package service.provider.common.dto;

import java.util.Set;

public class CategoryDto {
	private String name;

	private Long id;

	private Integer priority;

	private Set<CategoryDto> childCategories;

	private CategoryDto parentCategory;

	private Set<ProviderDto> providers;

	public CategoryDto() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Set<CategoryDto> getChildCategories() {
		return childCategories;
	}

	public void setChildCategories(Set<CategoryDto> childCategories) {
		this.childCategories = childCategories;
	}

	public CategoryDto getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(CategoryDto parentCategory) {
		this.parentCategory = parentCategory;
	}

	public Set<ProviderDto> getProviders() {
		return providers;
	}

	public void setProviders(Set<ProviderDto> providers) {
		this.providers = providers;
	}

	@Override
	public String toString() {
		return "CategoryDto [name=" + name + ", id=" + id + ", priority=" + priority + ", childCategories=" + childCategories + ", parentCategory=" + parentCategory + ", providers=" + providers + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryDto other = (CategoryDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
