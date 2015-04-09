package service.provider.common.dto;

import java.math.BigDecimal;

public class ProviderDto {

	private Long id;

	private String title;

	private BigDecimal lattitute;

	private BigDecimal longitute;

	private String staticPhone;

	private String gsm;

	private String tckn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getLattitute() {
		return lattitute;
	}

	public void setLattitute(BigDecimal lattitute) {
		this.lattitute = lattitute;
	}

	public BigDecimal getLongitute() {
		return longitute;
	}

	public void setLongitute(BigDecimal longitute) {
		this.longitute = longitute;
	}

	public String getStaticPhone() {
		return staticPhone;
	}

	public void setStaticPhone(String staticPhone) {
		this.staticPhone = staticPhone;
	}

	public String getGsm() {
		return gsm;
	}

	public void setGsm(String gsm) {
		this.gsm = gsm;
	}

	public String getTckn() {
		return tckn;
	}

	public void setTckn(String tckn) {
		this.tckn = tckn;
	}

	@Override
	public String toString() {
		return "ProviderDto [id=" + id + ", title=" + title + ", lattitute=" + lattitute + ", longitute=" + longitute + ", staticPhone=" + staticPhone + ", gsm=" + gsm + ", tckn=" + tckn + "]";
	}

}
