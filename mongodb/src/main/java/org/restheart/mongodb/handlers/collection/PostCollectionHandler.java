/*-
 * ========================LICENSE_START=================================
 * restheart-mongodb
 * %%
 * Copyright (C) 2014 - 2020 SoftInstigate
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * =========================LICENSE_END==================================
 */
package org.restheart.mongodb.handlers.collection;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.restheart.exchange.BsonRequest;
import org.restheart.exchange.BsonResponse;
import org.restheart.exchange.ExchangeKeys.DOC_ID_TYPE;
import org.restheart.exchange.OperationResult;
import org.restheart.exchange.RequestContext;
import org.restheart.handlers.PipelinedHandler;
import org.restheart.mongodb.db.DocumentDAO;
import org.restheart.mongodb.utils.ResponseHelper;
import org.restheart.mongodb.utils.URLUtils;
import org.restheart.representation.RepresentationUtils;
import org.restheart.utils.HttpStatus;

/**
 *
 * @author Andrea Di Cesare {@literal <andrea@softinstigate.com>}
 */
public class PostCollectionHandler extends PipelinedHandler {
    private final DocumentDAO documentDAO;

    /**
     * Creates a new instance of PostCollectionHandler
     */
    public PostCollectionHandler() {
        this(null, new DocumentDAO());
    }

    /**
     *
     * @param documentDAO
     */
    public PostCollectionHandler(DocumentDAO documentDAO) {
        this(null, new DocumentDAO());
    }

    /**
     *
     * @param next
     */
    public PostCollectionHandler(PipelinedHandler next) {
        this(next, new DocumentDAO());
    }

    /**
     *
     * @param next
     * @param documentDAO
     */
    public PostCollectionHandler(
            PipelinedHandler next,
            DocumentDAO documentDAO) {
        super(next);
        this.documentDAO = documentDAO;
    }

    /**
     *
     * @param exchange
     * @throws Exception
     */
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        var request = BsonRequest.wrap(exchange);
        var response = BsonResponse.wrap(exchange);
        
        if (request.isInError()) {
            next(exchange);
            return;
        }

        BsonValue _content = request.getContent();

        if (_content == null) {
            _content = new BsonDocument();
        }

        // cannot POST an array
        if (!_content.isDocument()) {
            response.setIError(
                    HttpStatus.SC_NOT_ACCEPTABLE,
                    "data must be a json object");
            next(exchange);
            return;
        }

        BsonDocument content = _content.asDocument();

        if (content.containsKey("_id")
                && content.get("_id").isString()
                && RequestContext.isReservedResourceDocument(
                        request.getType(),
                        content.get("_id").asString().getValue())) {
            response.setIError(
                    HttpStatus.SC_FORBIDDEN,
                    "reserved resource");
            next(exchange);
            return;
        }

        // if _id is not in content, it will be autogenerated as an ObjectId
        // check if the doc_type is different
        if (!content.containsKey("_id")) {
            if (!(request.getDocIdType() == DOC_ID_TYPE.OID)
                    && !(request.getDocIdType() == DOC_ID_TYPE.STRING_OID)) {
                response.setIError(
                        HttpStatus.SC_NOT_ACCEPTABLE,
                        "_id in content body is mandatory "
                        + "for documents with id type "
                        + request.getDocIdType().name());
                next(exchange);
                return;
            }
        }

        OperationResult result = this.documentDAO
                .upsertDocumentPost(
                        request.getClientSession(),
                        request.getDBName(),
                        request.getCollectionName(),
                        request.getFiltersDocument(),
                        request.getShardKey(),
                        content,
                        request.getETag(),
                        request.isETagCheckRequired());

        response.setDbOperationResult(result);

        // inject the etag
        if (result.getEtag() != null) {
            ResponseHelper.injectEtagHeader(exchange, result.getEtag());
        }

        if (result.getHttpCode() == HttpStatus.SC_CONFLICT) {
            response.setIError(
                    HttpStatus.SC_CONFLICT,
                    "The document's ETag must be provided using the '"
                    + Headers.IF_MATCH
                    + "' header.");

            next(exchange);
            return;
        }
        
        // handle the case of duplicate key error
        if (result.getHttpCode() == HttpStatus.SC_EXPECTATION_FAILED) {
            response.setIError(
                    HttpStatus.SC_EXPECTATION_FAILED,
                    ResponseHelper.getMessageFromErrorCode(11000));
            next(exchange);
            return;
        }

        response.setStatusCode(result.getHttpCode());

        // insert the Location handler for new documents
        // note, next handlers might change the status code
        if (result.getHttpCode() == HttpStatus.SC_CREATED) {
            exchange.getResponseHeaders()
                    .add(HttpString.tryFromString("Location"),
                            RepresentationUtils.getReferenceLink(
                                    URLUtils.getRemappedRequestURL(exchange),
                                    result.getNewData().get("_id")));
        }

        next(exchange);
    }
}
