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

package org.neo4j.ogm.integration.ingredients;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.ogm.domain.ingredients.Ingredient;
import org.neo4j.ogm.domain.ingredients.Pairing;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.testutil.Neo4jIntegrationTestRule;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

/**
 * @author Luanne Misquitta
 */
public class IngredientsIntegrationTest {

	@ClassRule
	public static Neo4jIntegrationTestRule databaseServerRule = new Neo4jIntegrationTestRule();

	private static Session session;

	@BeforeClass
	public static void init() throws IOException {
		session = new SessionFactory("org.neo4j.ogm.domain.ingredients").openSession(databaseServerRule.url());
	}

	/**
	 * @see DATAGRAPH-639
	 */
	@Test
	public void shouldBeAbleToAddInterrelatedPairings() {

		Ingredient chicken = new Ingredient("Chicken");
		session.save(chicken);

		Ingredient carrot = new Ingredient("Carrot");
		session.save(carrot);

		Ingredient butter = new Ingredient("Butter");
		session.save(butter);

		Pairing pairing = new Pairing();
		pairing.setFirst(chicken);
		pairing.setSecond(carrot);
		pairing.setAffinity("EXCELLENT");
		carrot.addPairing(pairing);
		session.save(chicken);

		Pairing pairing2 = new Pairing();
		pairing2.setFirst(chicken);
		pairing2.setSecond(butter);
		pairing2.setAffinity("EXCELLENT");
		carrot.addPairing(pairing2);
		session.save(chicken);

		Pairing pairing3 = new Pairing();
		pairing3.setFirst(carrot);
		pairing3.setSecond(butter);
		pairing3.setAffinity("EXCELLENT");
		carrot.addPairing(pairing3);
		session.save(carrot); //NullPointerException
	}

    @Test
    public void shouldBeAbleToLoadIngredientsWithoutPairings() {

        Ingredient chicken = new Ingredient("Chicken");
        session.save(chicken);

        Ingredient carrot = new Ingredient("Carrot");
        session.save(carrot);

        Ingredient butter = new Ingredient("Butter");
        session.save(butter);

        Pairing pairing = new Pairing();
        pairing.setFirst(chicken);
        pairing.setSecond(carrot);
        pairing.setAffinity("EXCELLENT");
        carrot.addPairing(pairing);
        session.save(chicken);

        Pairing pairing2 = new Pairing();
        pairing2.setFirst(chicken);
        pairing2.setSecond(butter);
        pairing2.setAffinity("EXCELLENT");
        carrot.addPairing(pairing2);
        session.save(chicken);

        Pairing pairing3 = new Pairing();
        pairing3.setFirst(carrot);
        pairing3.setSecond(butter);
        pairing3.setAffinity("EXCELLENT");
        carrot.addPairing(pairing3);
        session.save(carrot); //NullPointerException

        // it is important to clear the session if you intend the depth of the
        // the objects you want returned to not be the same as the depth currently
        // held in the mapping context.
        session.clear();

        Iterator<Ingredient> it= session.loadAll(Ingredient.class,0).iterator();

        while(it.hasNext()) {
            Ingredient i = it.next();
            assertEquals(i.getName(),0, i.getPairings().size());
        }
    }
}
