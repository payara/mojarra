package com.sun.faces.test.javaee8.cdi;

import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_1_4;
import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_2_1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;
import com.sun.faces.test.junit.JsfVersion;

/**
 * @author Azizjon Achilov
 * Created: 25.09.2019
 */
@RunWith(JsfTestRunner.class)
public class Issue4634IT {

  private String webUrl;
  private WebClient webClient;


  @Before
  public void setUp() {
    webUrl = System.getProperty("integration.url");
    webClient = new WebClient();
  }


  @After
  public void tearDown() {
    webClient.closeAllWindows();
  }


  @Test
  @JsfTest(value = JsfVersion.JSF_2_3_0)
  public void testDisplayedSelectBoxes() throws Exception {
    HtmlPage page = webClient.getPage(webUrl + "faces/issue4634.xhtml");
    HtmlSpan checked1 = page.getHtmlElementById("checked1");
    assertNotNull("checked1 is null", checked1);

    HtmlSpan checked2 = page.getHtmlElementById("checked2");
    assertNotNull("checked2 is null", checked2);
  }

}
