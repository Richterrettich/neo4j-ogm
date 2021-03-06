/*
 * Copyright (c)  [2011-2015] "Neo Technology" / "Graph Aware Ltd."
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with
 * separate copyright notices and license terms. Your use of the source
 * code for these subcomponents is subject to the terms and
 * conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package org.neo4j.ogm.session.request;

import org.neo4j.ogm.session.response.Neo4jResponse;

/**
 * @author Vince Bickers
 */
public interface Neo4jRequest<T> {

    Neo4jResponse<T> execute(String url, String jsonStatements);

}
