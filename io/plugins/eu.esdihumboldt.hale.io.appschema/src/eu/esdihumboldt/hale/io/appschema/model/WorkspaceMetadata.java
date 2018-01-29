/*
 * Copyright (c) 2018 wetransform GmbH
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
 *     wetransform GmbH <http://www.wetransform.to>
 */

package eu.esdihumboldt.hale.io.appschema.model;

import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Strings;

/**
 * TODO Type description
 * 
 * @author stefano
 */
public class WorkspaceMetadata implements Comparable<WorkspaceMetadata> {

	private final String defaultName;
	private String name;
	private final String namespace;
	private boolean isolated;
	private Set<String> featureTypes;

	public WorkspaceMetadata(String defaultName, String namespace) {
		if (Strings.isNullOrEmpty(defaultName)) {
			throw new IllegalArgumentException("Workspace default name must be provided");
		}
		if (Strings.isNullOrEmpty(namespace)) {
			throw new IllegalArgumentException("Workspace namespace must be provided");
		}
		this.defaultName = defaultName;
		this.name = this.defaultName;
		this.namespace = namespace;
		this.isolated = false;
	}

	/**
	 * @return the defaultName
	 */
	public String getDefaultName() {
		return defaultName;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (Strings.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("Workspace name cannot be empty");
		}
		this.name = name;
	}

	public boolean hasDefaultName() {
		return name.equals(defaultName);
	}

	public boolean isIsolated() {
		return isolated;
	}

	public void setIsolated(boolean isolated) {
		this.isolated = isolated;
	}

	public Set<String> getFeatureTypes() {
		if (featureTypes == null) {
			featureTypes = new TreeSet<>();
		}
		return featureTypes;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(WorkspaceMetadata o) {
		if (o == null) {
			return -1;
		}
		return getName().compareToIgnoreCase(o.getName());
	}

}
