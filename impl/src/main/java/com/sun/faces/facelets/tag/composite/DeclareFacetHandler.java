/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.facelets.tag.composite;

import com.sun.faces.facelets.tag.TagHandlerImpl;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.*;
import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class DeclareFacetHandler extends TagHandlerImpl {

    private static final String[] ATTRIBUTES = {
          "required",
          "displayName",
          "expert",
          "hidden",
          "preferred",
          "shortDescription",
          "default"
    };

    private static final PropertyHandlerManager ATTRIBUTE_MANAGER =
          PropertyHandlerManager.getInstance(ATTRIBUTES);

    private TagAttribute name = null;



    public DeclareFacetHandler(TagConfig config) {
        super(config);
        this.name = this.getRequiredAttribute("name");

        
    }
    
    @SuppressWarnings({"unchecked"})
    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        // only process if it's been created
        if (null == parent || 
            (null == (parent = parent.getParent())) ||
            !(ComponentHandler.isNew(parent))) {
            return;
        }
        
        Map<String, Object> componentAttrs = parent.getAttributes();

        CompositeComponentBeanInfo componentBeanInfo = (CompositeComponentBeanInfo)
                componentAttrs.get(UIComponent.BEANINFO_KEY);

        // Get the value of required the name propertyDescriptor
        ValueExpression ve = name.getValueExpression(ctx, String.class);
        String strValue = (String) ve.getValue(ctx);
        BeanDescriptor componentBeanDescriptor = componentBeanInfo.getBeanDescriptor();
        
        Map<String, PropertyDescriptor> facetDescriptors = (Map<String, PropertyDescriptor>) 
                   componentBeanDescriptor.getValue(UIComponent.FACETS_KEY);
        
        if (facetDescriptors == null) {
            facetDescriptors = new HashMap<>();
            componentBeanDescriptor.setValue(UIComponent.FACETS_KEY, 
                    facetDescriptors);
        }

        PropertyDescriptor propertyDescriptor;
        try {
            propertyDescriptor = new PropertyDescriptor(strValue, null, null);
        } catch (IntrospectionException ex) {
            throw new  TagException(tag, "Unable to create property descriptor for facet" + strValue, ex);
        }
        facetDescriptors.put(strValue, propertyDescriptor);
        
        for (TagAttribute tagAttribute : this.tag.getAttributes().getAll()) {
            String attributeName = tagAttribute.getLocalName();
            PropertyHandler handler = ATTRIBUTE_MANAGER.getHandler(ctx, attributeName);
            if (handler != null) {
                handler.apply(ctx, attributeName, propertyDescriptor, tagAttribute);
            }

        }
        
        this.nextHandler.apply(ctx, parent);
        
    }

}