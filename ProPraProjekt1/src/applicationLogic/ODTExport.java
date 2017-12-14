package applicationLogic;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.odftoolkit.simple.TextDocument;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ODTExport {
	private TextDocument odf;
	private Map<String, String> map = new HashMap<String, String>();

	public static void export() throws Exception {
		File file1 = new File("odt/template.odt");
		File file2 = new File("odt/result.odt");

		ODTExport template = new ODTExport();
		template.readOdt(file1);
		template.setVariable("TextBox1", "Das ist ein Test");
		template.setVariable("TextBox2", "Ein weiterer Test");
		template.saveOdt(file2);
	}

	public void readOdt(File file) throws Exception {
		odf = (TextDocument) TextDocument.loadDocument(file);
	}

	public void setVariable(String key, String value) {
		map.put(key, value);
	}

	public void saveOdt(File file) throws Exception {
		OfficeTextElement contentRoot = odf.getContentRoot();
		System.out.println(contentRoot);
		iteratorOverEveryVariableSet(contentRoot.getChildNodes());
		odf.save(file);
	}

	private void iteratorOverEveryVariableSet(NodeList childNodes) {
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (item instanceof NodeList)
				iteratorOverEveryVariableSet(item.getChildNodes());

			if (item.getNodeName().equals("form:textarea")) {
				String nodeValue = item.getAttributes().getNamedItem("form:name").getNodeValue();
				System.out.println(nodeValue);
				item.getChildNodes().item(0).setNodeValue(map.get(nodeValue));
				System.out.println(nodeValue);
			}
		}
	}
}
