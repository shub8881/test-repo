/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2022 Payara Foundation and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://github.com/payara/Payara/blob/master/LICENSE.txt
 * See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * The Payara Foundation designates this particular file as subject to the "Classpath"
 * exception as provided by the Payara Foundation in the GPL Version 2 section of the License
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.payara.examples.requesttracing.traced.beans;

import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.enterprise.context.RequestScoped;
import fish.payara.examples.requesttracing.traced.interfaces.RequestBeanInterface;
import org.eclipse.microprofile.opentracing.Traced;

/**
 *
 * @author Andrew Pielage
 */
@RequestScoped
@Traced
public class RequestBean implements RequestBeanInterface
{
    private final int sleepTime = 5000;
    
    @Override
    public void traceMessage(String message)
    {
        System.out.println("I'm being traced! "
                + "Sleeping to exceed the threshold...");
        try
        {
            Thread.sleep(sleepTime);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(RequestBean.class.getName()).log(Level.INFO, 
                    "Interrupted");
        }
        
        System.out.println("Finished sleeping! Here, have a message: " 
                + message);
        System.out.println("If you've enabled Request Tracing and set its "
                + "threshold to less than " + (sleepTime / 1000) 
                + " seconds, then you should see the trace once the "
                + "EJB has exited!");
    }
}
