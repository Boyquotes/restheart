/*
 * Copyright SoftInstigate srl. All Rights Reserved.
 *
 *
 * The copyright to the computer program(s) herein is the property of
 * SoftInstigate srl, Italy. The program(s) may be used and/or copied only
 * with the written permission of SoftInstigate srl or in accordance with the
 * terms and conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied. This copyright notice must not be removed.
 */
package com.softinstigate.restheart.handlers.collection;

import com.mongodb.DBCollection;
import com.softinstigate.restheart.db.CollectionDAO;
import com.softinstigate.restheart.utils.HttpStatus;
import com.softinstigate.restheart.utils.RequestContext;
import com.softinstigate.restheart.utils.ResponseHelper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/**
 *
 * @author uji
 */
public class DeleteCollectionHandler implements HttpHandler
{
    /**
     * Creates a new instance of EntityResource
     */
    public DeleteCollectionHandler()
    {
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception
    {
        RequestContext rc = new RequestContext(exchange);

        DBCollection coll = CollectionDAO.getCollection(rc.getDBName(), rc.getCollectionName());
        
        if (CollectionDAO.isCollectionEmpty(coll))
        {
            CollectionDAO.dropCollection(coll);

            ResponseHelper.endExchange(exchange, HttpStatus.SC_GONE);
        }
        else
        {
            ResponseHelper.endExchange(exchange, HttpStatus.SC_NOT_ACCEPTABLE);
        }
    }
}