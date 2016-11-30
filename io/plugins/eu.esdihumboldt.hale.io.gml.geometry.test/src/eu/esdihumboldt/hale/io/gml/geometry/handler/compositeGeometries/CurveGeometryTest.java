/*
 * Copyright (c) 2012 Data Harmonisation Panel
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
 *     HUMBOLDT EU Integrated Project #030962
 *     Data Harmonisation Panel <http://www.dhpanel.eu>
 */

package eu.esdihumboldt.hale.io.gml.geometry.handler.compositeGeometries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import eu.esdihumboldt.hale.common.instance.model.Instance;
import eu.esdihumboldt.hale.common.instance.model.InstanceCollection;
import eu.esdihumboldt.hale.common.instance.model.ResourceIterator;
import eu.esdihumboldt.hale.common.schema.geometry.GeometryProperty;
import eu.esdihumboldt.hale.io.gml.geometry.handler.internal.AbstractHandlerTest;

/**
 * Test for reading curve geometries
 * 
 * @author Patrick Lieb, Arun Varma
 */
public class CurveGeometryTest extends AbstractHandlerTest {

	private MultiLineString reference;

	private MultiLineString referenceForArc;

	private MultiLineString referenceOnGrid;

	private MultiLineString referenceForArcOnGrid;

	// XXX different segments need different count of coordinates
	// XXX missing Clothoid handler

	/**
	 * @see eu.esdihumboldt.hale.io.gml.geometry.handler.internal.AbstractHandlerTest#init()
	 */
	@Override
	public void init() {
		super.init();

		Coordinate[] coordinates = new Coordinate[] { new Coordinate(0.01, 3.2),
				new Coordinate(3.33, 3.33), new Coordinate(0.01, -3.2) };
		LineString linestring1 = geomFactory.createLineString(coordinates);

		LineString[] lines = new LineString[] { linestring1 };
		reference = geomFactory.createMultiLineString(lines);

		coordinates = new Coordinate[] { new Coordinate(8.0, 8.0), new Coordinate(8.4, 7.6),
				new Coordinate(8.6, 7.6), new Coordinate(9.0, 7.2), new Coordinate(9.2, 7.2),
				new Coordinate(9.4, 7.0), new Coordinate(9.8, 7.0), new Coordinate(10.0, 6.8),
				new Coordinate(10.4, 6.8), new Coordinate(10.6, 6.6), new Coordinate(11.0, 6.6),
				new Coordinate(11.4, 6.6), new Coordinate(11.8, 6.6), new Coordinate(12.0, 6.6),
				new Coordinate(12.0, 6.5), new Coordinate(12.4, 6.4), new Coordinate(12.8, 6.4),
				new Coordinate(13.0, 6.4), new Coordinate(13.2, 6.8), new Coordinate(13.6, 6.8),
				new Coordinate(13.8, 6.8), new Coordinate(13.9, 6.9), new Coordinate(14.3, 6.9),
				new Coordinate(14.5, 7.1), new Coordinate(14.7, 7.1), new Coordinate(15.1, 7.5),
				new Coordinate(15.3, 7.5), new Coordinate(15.7, 7.9), new Coordinate(15.9, 7.9),
				new Coordinate(16.0, 8.0) };
		linestring1 = geomFactory.createLineString(coordinates);

		lines = new LineString[] { linestring1 };
		referenceForArc = geomFactory.createMultiLineString(lines);

		// Grid test

		coordinates = new Coordinate[] { new Coordinate(0, 3.2), new Coordinate(3.3, 3.3),
				new Coordinate(0, -3.2) };
		linestring1 = geomFactory.createLineString(coordinates);

		lines = new LineString[] { linestring1 };
		referenceOnGrid = geomFactory.createMultiLineString(lines);

		coordinates = new Coordinate[] { new Coordinate(8.0, 8.0), new Coordinate(8.4, 7.6),
				new Coordinate(8.6, 7.6), new Coordinate(9.0, 7.2), new Coordinate(9.2, 7.2),
				new Coordinate(9.4, 7.0), new Coordinate(9.8, 7.0), new Coordinate(10.0, 6.8),
				new Coordinate(10.4, 6.8), new Coordinate(10.6, 6.6), new Coordinate(11.0, 6.6),
				new Coordinate(11.4, 6.6), new Coordinate(11.8, 6.6), new Coordinate(12.0, 6.6),
				new Coordinate(12.0, 6.4), new Coordinate(12.4, 6.4), new Coordinate(12.8, 6.4),
				new Coordinate(13.0, 6.4), new Coordinate(13.2, 6.8), new Coordinate(13.6, 6.8),
				new Coordinate(13.8, 6.8), new Coordinate(13.9, 6.9), new Coordinate(14.3, 6.9),
				new Coordinate(14.5, 7.1), new Coordinate(14.7, 7.1), new Coordinate(15.1, 7.5),
				new Coordinate(15.3, 7.5), new Coordinate(15.7, 7.9), new Coordinate(15.9, 7.9),
				new Coordinate(16.0, 8.0) };
		linestring1 = geomFactory.createLineString(coordinates);

		lines = new LineString[] { linestring1 };
		referenceForArcOnGrid = geomFactory.createMultiLineString(lines);
	}

	/**
	 * Test curve geometries read from a GML 3 file
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testCurveGml3() throws Exception {
		InstanceCollection instances = AbstractHandlerTest.loadXMLInstances(
				getClass().getResource("/data/gml/geom-gml3.xsd").toURI(),
				getClass().getResource("/data/curve/sample-curve-gml3.xml").toURI());

		// eleven instances expected
		ResourceIterator<Instance> it = instances.iterator();
		try {
			// 1. segments with LineStringSegment defined through coordinates
			assertTrue("First sample feature missing", it.hasNext());
			Instance instance = it.next();
			checkCurvePropertyInstance(instance);

			// 2. segments with Arc defined through coordinates
			assertTrue("Second sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, true, true); // Arc handler
																// added

			// 3. segments with ArcByBulge defined through coordinates
			assertTrue("Third sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 4. segments with ArcByCenterPoint defined through coordinates
			assertTrue("Fourth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 5. segments with ArcString defined through coordinates
			assertTrue("Fifth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 6. segments with ArcStringByBulge defined through coordinates
			assertTrue("Sixth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 7. segments with Bezier defined through coordinates
			assertTrue("Seventh sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 8. segments with BSpline defined through coordinates
			assertTrue("Eigth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 9. segments with Circle defined through coordinates
			assertTrue("Nineth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 10. segments with CircleByCenterPoint defined through coordinates
			assertTrue("Tenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 11. segments with CubicSpline defined through coordinates
			assertTrue("Eleventh sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);
		} finally {
			it.close();
		}
	}

	/**
	 * Test curve geometries read from a GML 3.1 file
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testCurveGml31() throws Exception {
		InstanceCollection instances = AbstractHandlerTest.loadXMLInstances(
				getClass().getResource("/data/gml/geom-gml31.xsd").toURI(),
				getClass().getResource("/data/curve/sample-curve-gml31.xml").toURI());

		// twelve instances expected
		ResourceIterator<Instance> it = instances.iterator();
		try {
			// 1. segments with LineStringSegment defined through coordinates
			assertTrue("First sample feature missing", it.hasNext());
			Instance instance = it.next();
			checkCurvePropertyInstance(instance);

			// 2. segments with LineStringSegment defined through posList
			assertTrue("Second sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 3. segments with Arc defined through coordinates
			assertTrue("Third sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, true, true); // Arc handler
			// added

			// 4. segments with ArcByBulge defined through coordinates
			assertTrue("Fourth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 5. segments with ArcByCenterPoint defined through coordinates
			assertTrue("Fifth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 6. segments with ArcString defined through coordinates
			assertTrue("Sixth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 7. segments with ArcStringByBulge defined through coordinates
			assertTrue("Seventh sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 8. segments with Bezier defined through coordinates
			assertTrue("Eigth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 9. segments with BSpline defined through coordinates
			assertTrue("Nineth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 10. segments with Circle defined through coordinates
			assertTrue("Tenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 11. segments with CircleByCenterPoint defined through coordinates
			assertTrue("Eleventh sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 12. segments with CubicSpline defined through coordinates
			assertTrue("Twelveth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 13. segments with Geodesic defined through posList
			assertTrue("Thirteenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 14. segments with GeodesicString defined through posList
			assertTrue("Fourteenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);
		} finally {
			it.close();
		}
	}

	/**
	 * Test curve geometries read from a GML 3.2 file
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testCurveGml32() throws Exception {
		InstanceCollection instances = AbstractHandlerTest.loadXMLInstances(
				getClass().getResource("/data/gml/geom-gml32.xsd").toURI(),
				getClass().getResource("/data/curve/sample-curve-gml32.xml").toURI());

		// twelve instances expected
		ResourceIterator<Instance> it = instances.iterator();
		try {
			// 1. segments with LineStringSegment defined through coordinates
			assertTrue("First sample feature missing", it.hasNext());
			Instance instance = it.next();
			checkCurvePropertyInstance(instance);

			// 2. segments with LineStringSegment defined through posList
			assertTrue("Second sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 3. segments with Arc defined through coordinates
			assertTrue("Third sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, true, true); // Arc handler
			// added

			// 4. segments with ArcByBulge defined through coordinates
			assertTrue("Fourth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 5. segments with ArcByCenterPoint defined through coordinates
			assertTrue("Fifth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 6. segments with ArcString defined through coordinates
			assertTrue("Sixth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 7. segments with ArcStringByBulge defined through coordinates
			assertTrue("Seventh sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 8. segments with Bezier defined through coordinates
			assertTrue("Eigth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 9. segments with BSpline defined through coordinates
			assertTrue("Nineth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 10. segments with Circle defined through coordinates
			assertTrue("Tenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 11. segments with CircleByCenterPoint defined through coordinates
			assertTrue("Eleventh sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 12. segments with CubicSpline defined through coordinates
			assertTrue("Twelveth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 13. segments with Geodesic defined through posList
			assertTrue("Thirteenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);

			// 14. segments with GeodesicString defined through posList
			assertTrue("Fourteenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance);
		} finally {
			it.close();
		}
	}

	/**
	 * Test curve geometries read from a GML 3 file. Geometry coordinates will
	 * be moved to the universal grid
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testCurveGml3_Grid() throws Exception {
		InstanceCollection instances = AbstractHandlerTest.loadXMLInstances(
				getClass().getResource("/data/gml/geom-gml3.xsd").toURI(),
				getClass().getResource("/data/curve/sample-curve-gml3.xml").toURI(), false);

		// eleven instances expected
		ResourceIterator<Instance> it = instances.iterator();
		try {
			// 1. segments with LineStringSegment defined through coordinates
			assertTrue("First sample feature missing", it.hasNext());
			Instance instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 2. segments with Arc defined through coordinates
			assertTrue("Second sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, true, false); // Arc handler
			// added

			// 3. segments with ArcByBulge defined through coordinates
			assertTrue("Third sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 4. segments with ArcByCenterPoint defined through coordinates
			assertTrue("Fourth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 5. segments with ArcString defined through coordinates
			assertTrue("Fifth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 6. segments with ArcStringByBulge defined through coordinates
			assertTrue("Sixth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 7. segments with Bezier defined through coordinates
			assertTrue("Seventh sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 8. segments with BSpline defined through coordinates
			assertTrue("Eigth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 9. segments with Circle defined through coordinates
			assertTrue("Nineth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 10. segments with CircleByCenterPoint defined through coordinates
			assertTrue("Tenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 11. segments with CubicSpline defined through coordinates
			assertTrue("Eleventh sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);
		} finally {
			it.close();
		}
	}

	/**
	 * Test curve geometries read from a GML 3.1 file. Geometry coordinates will
	 * be moved to the universal grid
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testCurveGml31_Grid() throws Exception {
		InstanceCollection instances = AbstractHandlerTest.loadXMLInstances(
				getClass().getResource("/data/gml/geom-gml31.xsd").toURI(),
				getClass().getResource("/data/curve/sample-curve-gml31.xml").toURI(), false);

		// twelve instances expected
		ResourceIterator<Instance> it = instances.iterator();
		try {
			// 1. segments with LineStringSegment defined through coordinates
			assertTrue("First sample feature missing", it.hasNext());
			Instance instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 2. segments with LineStringSegment defined through posList
			assertTrue("Second sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 3. segments with Arc defined through coordinates
			assertTrue("Third sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, true, false); // Arc handler
			// added

			// 4. segments with ArcByBulge defined through coordinates
			assertTrue("Fourth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 5. segments with ArcByCenterPoint defined through coordinates
			assertTrue("Fifth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 6. segments with ArcString defined through coordinates
			assertTrue("Sixth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 7. segments with ArcStringByBulge defined through coordinates
			assertTrue("Seventh sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 8. segments with Bezier defined through coordinates
			assertTrue("Eigth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 9. segments with BSpline defined through coordinates
			assertTrue("Nineth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 10. segments with Circle defined through coordinates
			assertTrue("Tenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 11. segments with CircleByCenterPoint defined through coordinates
			assertTrue("Eleventh sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 12. segments with CubicSpline defined through coordinates
			assertTrue("Twelveth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 13. segments with Geodesic defined through posList
			assertTrue("Thirteenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 14. segments with GeodesicString defined through posList
			assertTrue("Fourteenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);
		} finally {
			it.close();
		}
	}

	/**
	 * Test curve geometries read from a GML 3.2 file. Geometry coordinates will
	 * be moved to the universal grid
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testCurveGml32_Grid() throws Exception {
		InstanceCollection instances = AbstractHandlerTest.loadXMLInstances(
				getClass().getResource("/data/gml/geom-gml32.xsd").toURI(),
				getClass().getResource("/data/curve/sample-curve-gml32.xml").toURI(), false);

		// twelve instances expected
		ResourceIterator<Instance> it = instances.iterator();
		try {
			// 1. segments with LineStringSegment defined through coordinates
			assertTrue("First sample feature missing", it.hasNext());
			Instance instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 2. segments with LineStringSegment defined through posList
			assertTrue("Second sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 3. segments with Arc defined through coordinates
			assertTrue("Third sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, true, false); // Arc handler
			// added

			// 4. segments with ArcByBulge defined through coordinates
			assertTrue("Fourth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 5. segments with ArcByCenterPoint defined through coordinates
			assertTrue("Fifth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 6. segments with ArcString defined through coordinates
			assertTrue("Sixth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 7. segments with ArcStringByBulge defined through coordinates
			assertTrue("Seventh sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 8. segments with Bezier defined through coordinates
			assertTrue("Eigth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 9. segments with BSpline defined through coordinates
			assertTrue("Nineth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 10. segments with Circle defined through coordinates
			assertTrue("Tenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 11. segments with CircleByCenterPoint defined through coordinates
			assertTrue("Eleventh sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 12. segments with CubicSpline defined through coordinates
			assertTrue("Twelveth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 13. segments with Geodesic defined through posList
			assertTrue("Thirteenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);

			// 14. segments with GeodesicString defined through posList
			assertTrue("Fourteenth sample feature missing", it.hasNext());
			instance = it.next();
			checkCurvePropertyInstance(instance, false);
		} finally {
			it.close();
		}
	}

	private void checkCurvePropertyInstance(Instance instance) {
		checkCurvePropertyInstance(instance, false, true);
	}

	private void checkCurvePropertyInstance(Instance instance, boolean keepOriginal) {
		checkCurvePropertyInstance(instance, false, keepOriginal);
	}

	private void checkCurvePropertyInstance(Instance instance, boolean isArc,
			boolean keepOriginal) {
		Object[] geomVals = instance.getProperty(new QName(NS_TEST, "geometry"));
		assertNotNull(geomVals);
		assertEquals(1, geomVals.length);

		Object geom = geomVals[0];
		assertTrue(geom instanceof Instance);

		Instance geomInstance = (Instance) geom;
		checkGeomInstance(geomInstance, isArc, keepOriginal);
	}

	private void checkGeomInstance(Instance geomInstance, boolean isArc, boolean keepOriginal) {
		for (GeometryProperty<?> instance : getGeometries(geomInstance)) {
			@SuppressWarnings("unchecked")
			MultiLineString multilinestring = ((GeometryProperty<MultiLineString>) instance)
					.getGeometry();

			assertTrue("Read geometry does not match the reference geometry",
					multilinestring.equalsExact(keepOriginal ? (isArc ? referenceForArc : reference)
							: (isArc ? referenceForArcOnGrid : referenceOnGrid)));
		}
	}

}
