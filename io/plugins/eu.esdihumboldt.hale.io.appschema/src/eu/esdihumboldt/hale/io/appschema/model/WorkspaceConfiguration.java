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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * TODO Type description
 * 
 * @author stefano
 */
public class WorkspaceConfiguration {

	private final Map<String, WorkspaceMetadata> workspaceMap;

	/**
	 * 
	 */
	public WorkspaceConfiguration() {
		this.workspaceMap = new TreeMap<>();
	}

	public void clear() {
		workspaceMap.clear();
	}

	public void addWorkspace(WorkspaceMetadata workspace) {
		if (workspace != null) {
			workspaceMap.put(workspace.getNamespace(), workspace);
		}
	}

	public WorkspaceMetadata getWorkspace(String namespaceUri) {
		return workspaceMap.get(namespaceUri);
	}

	public List<WorkspaceMetadata> getWorkspaces() {
		List<WorkspaceMetadata> workspaces = new ArrayList<>(workspaceMap.values());
		return workspaces;
	}

	public boolean hasWorkspace(String namespaceUri) {
		return workspaceMap.get(namespaceUri) != null;
	}
}
