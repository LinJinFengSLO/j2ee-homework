package utilities;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;

public abstract class XmlUtilities {

	public static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	      CharacterData cd = (CharacterData) child;
	      return cd.getData();
	    }
	    return "";
	  }
}
