package ph.kana.csvv.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ph.kana.csvv.model.CsvData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class CsvFileUtil {

	private CsvFileUtil() {}

	public static CsvData readCsv(File csvFile) {
		CSVFormat format = CSVFormat.DEFAULT
			.withHeader()
			.withIgnoreEmptyLines();

		try (FileInputStream fileInputStream = new FileInputStream(csvFile)) {
			CSVParser parser = new CSVParser(new InputStreamReader(fileInputStream), format);

			List<String> headers = new ArrayList();
			parser.getHeaderMap()
				.forEach((name, index) -> headers.add(index, name));
			List records = new ArrayList();
			parser.getRecords().stream()
				.map(CSVRecord::toMap)
				.forEachOrdered(records::add);

			CsvData data = new CsvData();
			data.setHeaders(headers);
			data.setValues(records);
			return data;
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		return null;
	}
}
