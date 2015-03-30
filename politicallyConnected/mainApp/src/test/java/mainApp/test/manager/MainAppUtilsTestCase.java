package mainApp.test.manager;

import static org.junit.Assert.assertTrue;
import mainApp.utils.MainAppUtils;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class MainAppUtilsTestCase {

	@Test
	public void testRemovingExtraSpacesFromNames() {
		String normalText = "gokhan ozgozen";
		String processedNormalText = MainAppUtils.removeExtraSpaceBetweenNames(normalText);
		assertTrue(processedNormalText.equals(normalText));
		String textWithOneExtraSpace = "gokhan  ozgozen";
		String processedString = MainAppUtils.removeExtraSpaceBetweenNames(textWithOneExtraSpace);
		assertTrue(processedString.equals(normalText));
		String textWithMultipleOneExtraSpace = "suzan  sabanci  dincer";
		processedString = MainAppUtils.removeExtraSpaceBetweenNames(textWithMultipleOneExtraSpace);
		assertTrue(processedString.equals("suzan sabanci dincer"));
		String textWithOneMultipleExtraSpace = "gokhan                          ozgozen";
		processedString = MainAppUtils.removeExtraSpaceBetweenNames(textWithOneMultipleExtraSpace);
		assertTrue(processedString.equals(normalText));
		String textWithMultipleMultipleExtraSpace = "                      abdurrahman        karaibrahimli             cunduruluabbas              sikmeden     durmaz gibi geliyor      di                mi     suzan           sabanci       dincer        ??????   !!!!                        ";
		processedString = MainAppUtils.removeExtraSpaceBetweenNames(textWithMultipleMultipleExtraSpace);
		assertTrue(processedString.equals("abdurrahman karaibrahimli cunduruluabbas sikmeden durmaz gibi geliyor di mi suzan sabanci dincer ?????? !!!!"));
	}

	@Test
	public void testRemovingParanthesisFromMemberNames() {
		String test1 = "gokhanabi";
		String processedString = MainAppUtils.removeParanthesis(test1);
		assertTrue(test1.equals(processedString));
		String test2 = "gokhanabi(nam - i diger ustad ) ozgozen";
		processedString = MainAppUtils.removeParanthesis(test2);
		assertTrue(processedString.equals("gokhanabi ozgozen"));
		String test3 = "Viliyy solomon (baskan ve vekilli, dev tasakli kurucu uye )";
		processedString = MainAppUtils.removeParanthesis(test3);
		assertTrue(processedString.equals("Viliyy solomon "));
	}

}
