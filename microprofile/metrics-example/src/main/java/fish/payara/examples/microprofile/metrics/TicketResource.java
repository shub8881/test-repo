/*
 * DO NOT ALTER OR REMOTE COPYRIGHT NOTICES OR THIS HEADER.
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
package fish.payara.examples.microprofile.metrics;

import org.eclipse.microprofile.metrics.ConcurrentGauge;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mike on 12/01/18.
 */
@Path("tickets")
@ApplicationScoped
public class TicketResource {

    private final String SOLD = "sold";
    List<String> tickets = Arrays.asList(new String[100]);

    @Inject
    @Metric
    ConcurrentGauge ticketCount;

    @PostConstruct
    private void init() {
        // initialise counter with beginning number
        for (int i = 1; i <= 100; i++) {
            ticketCount.inc();
        }
    }

    /*
        We need to set the "name" attribute to make sure the metrics
￼       are differentiated, otherwise there will be a single
￼       Meter for the overloaded methods, rather than one each.
     */

    @GET
    @Path("buy")
    @Metered(name = "Buy any ticket")
    public String buyTicket() {
        int i = 0;
        for (String t : tickets) {
            if (t != SOLD) {
                tickets.set(i, SOLD);
                ticketCount.dec();
                return "Success! You have bought ticket number " + i;
            } else {
                i++;
            }
        }
        return "Error! Sold out";
    }

    @GET
    @Path("buy/{id}")
    @Metered(name = "Buy specific ticket")
    public String buyTicket(@PathParam("id") int id) {
        if (tickets.get(id) != SOLD) {
            tickets.set(id, SOLD);
            ticketCount.dec();
            return "Success! You have bought ticket number " + id;
        } else {
            return "Error! Ticket number " + id + " is not available!";
        }
    }

    @GET
    @Path("return/{id}")
    @Metered
    public String returnTicket(@PathParam("id") int id) {
        if (tickets.get(id) == SOLD) {
            tickets.set(id, "");
            ticketCount.inc();
            return "Success! Ticket number " + id + " has been returned!";
        } else {
            return "Error! Ticket number " + id + " has not been sold!";
        }
    }

}