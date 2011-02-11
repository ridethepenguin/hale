/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                 01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2011.
 */

package eu.esdihumboldt.hale.gmlwriter.impl.internal.geometry.writers;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;

import eu.esdihumboldt.hale.gmlwriter.impl.internal.geometry.GeometryWriter;
import eu.esdihumboldt.hale.schemaprovider.model.TypeDefinition;

/**
 * Write {@link MultiPoint}s 
 *
 * @author Simon Templer
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @version $Id$ 
 */
public class MultiPointWriter extends AbstractGeometryWriter<MultiPoint> {
	
	private final PointWriter pointWriter = new PointWriter();

	/**
	 * Default constructor
	 */
	public MultiPointWriter() {
		super(MultiPoint.class);
		
		// compatible types to serve as entry point
		addCompatibleType(new NameImpl("MultiPointType"));
		
		// patterns for matching inside compatible types
		addBasePattern("*/pointMember");
		
		// verification patterns (contained Point)
		addVerificationPattern("*/Point");
	}

	/**
	 * @see GeometryWriter#write(XMLStreamWriter, Geometry, TypeDefinition, Name, String)
	 */
	@Override
	public void write(XMLStreamWriter writer, MultiPoint geometry,
			TypeDefinition elementType, Name elementName, String gmlNs)
			throws XMLStreamException {
		/*
		 * At this point we can assume that the wrapping element matches on of 
		 * the base patterns. The corresponding element name and its type 
		 * definition are given.
		 */
		for (int i = 0; i < geometry.getNumGeometries(); i++) {
			if (i > 0) {
				writer.writeStartElement(elementName.getNamespaceURI(), elementName.getLocalPart());
			}
			
			Descent descent = descend(writer, Pattern.parse("*/Point"), elementType, gmlNs);
			
			Point point = (Point) geometry.getGeometryN(i);
			pointWriter.write(
					writer, 
					point, 
					descent.getPath().getLastType(), 
					descent.getPath().getLastElement().getName(), 
					gmlNs);
			
			descent.close();
			
			if (i < geometry.getNumGeometries() - 1) {
				writer.writeEndElement();
			}
		}
	}

}
