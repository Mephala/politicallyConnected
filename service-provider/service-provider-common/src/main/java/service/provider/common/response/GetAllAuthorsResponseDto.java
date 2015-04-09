package service.provider.common.response;

import java.util.Set;

import service.provider.common.dto.AuthorDto;

public class GetAllAuthorsResponseDto extends AbstractResponseDto {

	private Set<AuthorDto> authors;

	public GetAllAuthorsResponseDto() {

	}

	public Set<AuthorDto> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<AuthorDto> authors) {
		this.authors = authors;
	}

	@Override
	public String toString() {
		return "GetAllAuthorsResponseDto [authors=" + authors + "]";
	}

}
