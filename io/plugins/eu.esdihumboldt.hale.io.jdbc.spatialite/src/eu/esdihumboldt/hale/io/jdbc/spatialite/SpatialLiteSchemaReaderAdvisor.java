/*
 * Copyright (c) 2015 Data Harmonisation Panel
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Data Harmonisation Panel <http://www.dhpanel.eu>
 */

package eu.esdihumboldt.hale.io.jdbc.spatialite;

import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import eu.esdihumboldt.hale.io.jdbc.JDBCSchemaReader;
import eu.esdihumboldt.hale.io.jdbc.extension.JDBCSchemaReaderAdvisor;

/**
 * Adapts {@link JDBCSchemaReader} behavior for SpatialLite.
 * 
 * @author Simon Templer
 */
public class SpatialLiteSchemaReaderAdvisor implements JDBCSchemaReaderAdvisor {

	@Override
	public void configureSchemaCrawler(SchemaCrawlerOptions options) {
		// TODO adapt configuration
//		options.setTableInclusionRule(tableInclusionRule);
	}

}
