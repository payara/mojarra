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

package com.sun.faces.application;

import static java.util.logging.Level.FINE;

import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.sun.faces.util.FacesLogger;

/**
 * This action listener implementation processes action events during the
 * <em>Apply Request Values</em> or <em>Invoke Application</em>
 * phase of the request processing lifecycle (depending upon the
 * <code>immediate</code> property of the {@link ActionSource} that
 * queued this event.  It invokes the specified application action method,
 * and uses the logical outcome value to invoke the default navigation handler
 * mechanism to determine which view should be displayed next.</p>
 */
public class ActionListenerImpl implements ActionListener {


    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();


    // --------------------------------------------- Methods From ActionListener
 

    @SuppressWarnings("deprecation")
    @Override
    public void processAction(ActionEvent event) {

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.fine(MessageFormat.format("processAction({0})", event.getComponent().getId()));
        }
        
        UIComponent source = event.getComponent();
        FacesContext context = event.getFacesContext();
        
        MethodBinding binding = ((ActionSource) source).getAction();
        
        invokeNavigationHandling(
            context, source, binding, 
            getNavigationOutcome(context, binding));

        // Trigger a switch to Render Response if needed
        context.renderResponse();
    }
    
    @SuppressWarnings("deprecation")
    private String getNavigationOutcome(FacesContext context, MethodBinding binding) {
        if (binding != null) {
            try {
                Object invokeResult;
                if ((invokeResult = binding.invoke(context, null)) != null) {
                    return invokeResult.toString();
                } 
                // else, default to null, as returned at the end
            } catch (MethodNotFoundException e) {
                if (LOGGER.isLoggable(FINE)) {
                    LOGGER.log(FINE, e.getMessage(), e);
                }
                throw new FacesException(binding.getExpressionString() + ": " + e.getMessage(), e);
            }
            catch (EvaluationException e) {
                if (LOGGER.isLoggable(FINE)) {
                    LOGGER.log(FINE, e.getMessage(), e);
                }
                throw new FacesException(binding.getExpressionString() + ": " + e.getMessage(), e);
            }
        }
        
        return null;
    }
    
    @SuppressWarnings("deprecation")
    private void invokeNavigationHandling(FacesContext context, UIComponent source, MethodBinding binding, String outcome) {
        
        NavigationHandler navHandler = context.getApplication().getNavigationHandler();
        
        String toFlowDocumentId = (String) source.getAttributes().get(TO_FLOW_DOCUMENT_ID_ATTR_NAME);
        if (toFlowDocumentId == null) {
            navHandler.handleNavigation(context,
                    binding != null ?
                    binding.getExpressionString() : null,
                    outcome);
        } else {
            navHandler.handleNavigation(context,
                    binding != null ?
                    binding.getExpressionString() : null,
                    outcome, toFlowDocumentId);
        }
    }

}
