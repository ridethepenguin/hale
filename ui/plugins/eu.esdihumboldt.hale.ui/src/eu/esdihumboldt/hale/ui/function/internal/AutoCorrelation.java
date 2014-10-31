/*
 * Copyright (c) 2014 Data Harmonisation Panel
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

package eu.esdihumboldt.hale.ui.function.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.ui.PlatformUI;

import eu.esdihumboldt.hale.common.align.model.EntityDefinition;
import eu.esdihumboldt.hale.common.align.model.impl.TypeEntityDefinition;
import eu.esdihumboldt.hale.common.schema.SchemaSpaceID;
import eu.esdihumboldt.hale.common.schema.model.PropertyDefinition;
import eu.esdihumboldt.hale.common.schema.model.TypeDefinition;
import eu.esdihumboldt.hale.ui.selection.SchemaSelection;
import eu.esdihumboldt.hale.ui.service.entity.EntityDefinitionService;
import eu.esdihumboldt.util.Pair;

/**
 * Class to create Retype and Rename cells for multiple sources. It is used to
 * create mappings based on matching between source and target types.
 * 
 * @author Yasmina Kammeyer
 */
public class AutoCorrelation {

	/**
	 * Build and returns pairs of types. A pair represent a match between a
	 * source and a target type.
	 * 
	 * @param sourceAndTarget The SchemaCollection contains the types which will
	 *            be used to create Cells
	 * @param ignoreNamespace Indicates if the namespace is irrelevant for type
	 *            comparison
	 * @return All pairs which should be retyped (only
	 *         {@link TypeEntityDefinition}s)
	 */
	public static Set<Pair<TypeEntityDefinition, TypeEntityDefinition>> retype(
			SchemaSelection sourceAndTarget, boolean ignoreNamespace) {

//		EntityDefinitionService eds = (EntityDefinitionService) PlatformUI.getWorkbench()
//				.getService(EntityDefinitionService.class);

		Set<TypeDefinition> sourceTypes;
		Set<TypeDefinition> targetTypes;

		sourceTypes = collectTypeDefinitions(sourceAndTarget.getSourceItems());
		targetTypes = collectTypeDefinitions(sourceAndTarget.getTargetItems());

//		Set<Pair<TypeDefinition, TypeDefinition>> pairs = HashSet<Pair<TypeDefinition, TypeDefinition>>()// Collections.emptySet();
//		createPairsThroughTypeMatching(sourceTypes, targetTypes, pairs, ignoreNamespace);
		Set<Pair<TypeEntityDefinition, TypeEntityDefinition>> pairs = new HashSet<Pair<TypeEntityDefinition, TypeEntityDefinition>>();
		createPairsThroughTypeComparison(sourceTypes, targetTypes, pairs, ignoreNamespace);

		return pairs;
	}

	/**
	 * Used to collect all TypeDefinitions - preprocessing step
	 * 
	 * @param source Contains the TypeDefinitions to add
	 * @return The Set of Types, which will be source or target of cell
	 */
	public static Set<TypeDefinition> collectTypeDefinitions(Set<EntityDefinition> source) {

		Set<TypeDefinition> result = new HashSet<TypeDefinition>();

		for (EntityDefinition entity : source) {
			if (entity instanceof TypeEntityDefinition) {
				// entity is type definition
				if (entity.getType().getSubTypes().isEmpty()) {
					// entity is concrete type
					result.add((TypeDefinition) entity.getDefinition());
				}
				else {
					collectTypeDefinitions(entity.getType().getSubTypes(), result);
				}
			}
		}

		return result;
	}

	/**
	 * Iterate recursively through all children - preprocessing step
	 * 
	 * @param source The TypeDefinition to add
	 * @param result The result
	 */
	private static void collectTypeDefinitions(Collection<? extends TypeDefinition> source,
			Set<TypeDefinition> result) {

		for (TypeDefinition def : source) {
			// entity is type definition
			if (def.getSubTypes().isEmpty()) {
				// entity is concrete type
				result.add(def);
			}
			else {
				collectTypeDefinitions(def.getSubTypes(), result);
			}
		}
	}

	/**
	 * Used to collect all TypeDefinitions - preprocessing step
	 * 
	 * @param source Contains the TypeDefinitions to add
	 * @param useSuperType False, if only concrete Type properties should be
	 *            collected
	 * @return The Set of Types, which will be source or target of cell
	 */
	public static Set<PropertyDefinition> collectPropertyDefinitions(
			Collection<? extends TypeDefinition> source, boolean useSuperType) {

		Set<PropertyDefinition> result = new HashSet<PropertyDefinition>();

		for (TypeDefinition def : source) {
			if (useSuperType) {
				// entity is concrete type
				collectPropertyDefinitions(def.getSubTypes(), result);
			}
			else {
				collectPropertyDefinitions(def.getSubTypes(), result);
			}
		}

		return result;
	}

	/**
	 * Iterate recursively through all children - preprocessing step
	 * 
	 * @param source The TypeDefinition to add
	 * @param result The result
	 */
	private static void collectPropertyDefinitions(Collection<? extends TypeDefinition> source,
			Set<PropertyDefinition> result) {

		for (TypeDefinition def : source) {
			// entity is type definition
			if (def.getSubTypes().isEmpty()) {
				// entity is concrete type

			}
			else {
				collectPropertyDefinitions(def.getSubTypes(), result);
			}
		}
	}

	/**
	 * @param sourceTypes The source types
	 * @param targetTypes The target types
	 * @param pairs The {@link Set} to collect the pairs
	 * @param ignoreNamespace The name space is irrelevant for types to be
	 *            compared
	 */
	private static void createPairsThroughTypeComparison(Collection<TypeDefinition> sourceTypes,
			Collection<TypeDefinition> targetTypes,
			Set<Pair<TypeEntityDefinition, TypeEntityDefinition>> pairs, boolean ignoreNamespace) {

		if (sourceTypes == null || sourceTypes.size() <= 0 || targetTypes == null
				|| targetTypes.size() <= 0 || pairs == null) {
			return;
		}

		EntityDefinitionService eds = (EntityDefinitionService) PlatformUI.getWorkbench()
				.getService(EntityDefinitionService.class);

		TypeEntityDefinition target;
		TypeEntityDefinition source;

		for (TypeDefinition targetTypeDef : targetTypes) {
			// note: there will be one TypeEntityDefinition, the dafault one
			// without a filter
			// XXX only use type without context/filter ???
			target = eds.getTypeEntities(targetTypeDef, SchemaSpaceID.TARGET).iterator().next();

			for (TypeDefinition sourceTypeDef : sourceTypes) {
				source = eds.getTypeEntities(sourceTypeDef, SchemaSpaceID.SOURCE).iterator().next();
				// best match - same qname
				if (targetTypeDef.getName().equals(sourceTypeDef.getName())) {
					pairs.add(new Pair<TypeEntityDefinition, TypeEntityDefinition>(source, target));
				}
				else if (ignoreNamespace
						&& targetTypeDef.getName().getLocalPart()
								.equals(sourceTypeDef.getName().getLocalPart())) {
					// weaker match - same local part (e.g. name)
					pairs.add(new Pair<TypeEntityDefinition, TypeEntityDefinition>(source, target));
				}
			}
		}
	}

	/**
	 * Build and returns pairs of properties. A pair represent a match between a
	 * source and a target property.
	 * 
	 * @param sourceAndTarget The SchemaCollection contains the types which will
	 *            be used to create Cells
	 * @param ignoreNamespace Indicates if the namespace is irrelevant for type
	 *            comparison
	 * @param ignoreInherited Inherited Attributes will be ignored
	 * @return All pairs which should be retyped (only
	 *         {@link PropertyDefinition}s)
	 */
	public Set<Pair<PropertyDefinition, PropertyDefinition>> rename(
			SchemaSelection sourceAndTarget, boolean ignoreNamespace, boolean ignoreInherited) {
		// TODO
		return null;
	}

	/**
	 * @param sourceAndTarget The SchemaCollection contains the types which will
	 *            be used to create Cells
	 * @param ignoreNamespace Indicates if the namespace is irrelevant for type
	 *            comparison
	 * @param ignoreInherited Inherited Attributes will be ignored
	 * @return ???
	 */
	public Set<Pair<PropertyDefinition, PropertyDefinition>> retypeAndRename(
			SchemaSelection sourceAndTarget, boolean ignoreNamespace, boolean ignoreInherited) {
		// TODO
		return null;
	}

}
