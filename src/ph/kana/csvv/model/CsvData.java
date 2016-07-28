package ph.kana.csvv.model;

import java.util.LinkedHashMap;
import java.util.List;

public class CsvData {
	private List<String> headers;
	private List<? extends LinkedHashMap> values;

	public void setHeaders(List headers) {
		this.headers = headers;
	}

	public List<String> getHeaders() {
		return headers;
	}

	public void setValues(List<? extends LinkedHashMap> values) {
		this.values = values;
	}

	public List getValues() {
		return values;
	}
}
