/*
 * $Id: OptionListRenderer.java,v 1.8 2002/02/05 00:56:16 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// OptionListRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.beans.PropertyDescriptor;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.UIComponent;
import javax.faces.UISelectOne;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>OptionListRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: OptionListRenderer.java,v 1.8 2002/02/05 00:56:16 rogerk Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class OptionListRenderer extends Object implements Renderer {
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public OptionListRenderer() {
        super();
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init() {
        // super.init();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //

    public boolean supportsType(UIComponent c) {
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof UISelectOne ) {
            supports = true;
        }
        return supports;
    }

    public boolean supportsType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_UISELECTONE)) {
            supports = true;
        }
        return supports;
    }

    public Iterator getSupportedAttributeNames(String componentType) throws FacesException {
        return null;
    }

    public Iterator getSupportedAttributes(String componentType) throws FacesException {
	return null;
    }

    public PropertyDescriptor getAttributeDescriptor(String attributeName)
	throws FacesException {
	return null;
    }


    public void renderStart(RenderContext rc, UIComponent c) 
        throws IOException, FacesException {

        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);
 
        UISelectOne uiSelectOne = null;
        if ( supportsType(c)) {
            uiSelectOne = (UISelectOne) c;
        } else {
            throw new FacesException("Invalid component type. " +
                      "Expected UISelectOne");
        }

        String optionListId = uiSelectOne.getId();
        Assert.assert_it(null != optionListId);

        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );

        StringBuffer output = new StringBuffer();
        output.append("<SELECT NAME=\"");
        output.append(optionListId);
        output.append("\">");
        outputMethod.writeText(output.toString());
        outputMethod.flush();
    }

    public void renderChildren(RenderContext rc, UIComponent c) 
        throws IOException {
        return;
    }

    public void renderComplete(RenderContext rc, UIComponent c) 
            throws IOException,FacesException {
        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);
	
        UISelectOne uiSelectOne = null;
        if ( supportsType(c)) {
            uiSelectOne = (UISelectOne) c;
        } else {
            throw new FacesException("Invalid component type. " +
				     "Expected UISelectOne");
        }
        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );

        StringBuffer output = new StringBuffer();

        String selectedValue = (String) uiSelectOne.getSelectedValue(rc);

        // Iterate over the components items collection and
        // build the rendering string.
        //
        Iterator itemsIter = uiSelectOne.getItems(rc);
        while (itemsIter.hasNext()) {
            UISelectOne.Item item = (UISelectOne.Item)itemsIter.next();
            String itemLabel = item.getLabel(); 
            output.append("<OPTION VALUE=\"");
            output.append(item.getValue());    
            output.append("\"");

            if ((selectedValue != null) &&
                selectedValue.equals(itemLabel)) {
                output.append(" SELECTED");
            }
            output.append(">");
            output.append(itemLabel);
            output.append("</OPTION>");
        }
	
        output.append("</SELECT>");
        outputMethod.writeText(output.toString());
        outputMethod.flush();

    }

    public boolean getCanRenderChildren(RenderContext rc, UIComponent c) {
        return false;
    }


} // end of class OptionListRenderer
