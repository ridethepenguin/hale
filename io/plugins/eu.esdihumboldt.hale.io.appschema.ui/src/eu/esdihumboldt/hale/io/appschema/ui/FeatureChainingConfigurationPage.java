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

package eu.esdihumboldt.hale.io.appschema.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import eu.esdihumboldt.hale.common.align.model.Alignment;
import eu.esdihumboldt.hale.common.align.model.Cell;
import eu.esdihumboldt.hale.common.align.model.Type;
import eu.esdihumboldt.hale.common.align.model.impl.TypeEntityDefinition;
import eu.esdihumboldt.hale.common.schema.SchemaSpaceID;
import eu.esdihumboldt.hale.io.appschema.writer.AbstractAppSchemaConfigurator;
import eu.esdihumboldt.hale.io.appschema.writer.AppSchemaMappingUtils;
import eu.esdihumboldt.hale.ui.HaleUI;
import eu.esdihumboldt.hale.ui.common.definition.viewer.DefinitionComparator;
import eu.esdihumboldt.hale.ui.common.definition.viewer.SchemaPatternFilter;
import eu.esdihumboldt.hale.ui.common.definition.viewer.StyledDefinitionLabelProvider;
import eu.esdihumboldt.hale.ui.io.config.AbstractConfigurationPage;
import eu.esdihumboldt.hale.ui.service.align.AlignmentService;
import eu.esdihumboldt.hale.ui.service.entity.EntityDefinitionService;
import eu.esdihumboldt.hale.ui.service.entity.util.EntityTypeIterableContentProvider;
import eu.esdihumboldt.hale.ui.util.viewer.tree.TreePathFilteredTree;
import eu.esdihumboldt.hale.ui.util.viewer.tree.TreePathProviderAdapter;

/**
 * TODO Type description
 * 
 * @author stefano
 */
public class FeatureChainingConfigurationPage extends
		AbstractConfigurationPage<AbstractAppSchemaConfigurator, AppSchemaAlignmentExportWizard> {

	private TreeViewer targetTypeViewer;
	private TableViewer joinTypesViewer;

	/**
	 * @param pageName
	 */
	public FeatureChainingConfigurationPage() {
		super("feature.chaining.conf");
		setTitle("Configure feature chaining");
	}

	/**
	 * @see eu.esdihumboldt.hale.ui.io.config.AbstractConfigurationPage#enable()
	 */
	@Override
	public void enable() {
		// TODO: do nothing?
	}

	/**
	 * @see eu.esdihumboldt.hale.ui.io.config.AbstractConfigurationPage#disable()
	 */
	@Override
	public void disable() {
		// TODO: do nothing?
	}

	/**
	 * @see eu.esdihumboldt.hale.ui.io.IOWizardPage#updateConfiguration(eu.esdihumboldt.hale.common.core.io.IOProvider)
	 */
	@Override
	public boolean updateConfiguration(AbstractAppSchemaConfigurator provider) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see eu.esdihumboldt.hale.ui.HaleWizardPage#createContent(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createContent(Composite page) {
		AlignmentService alignmentService = HaleUI.getServiceProvider().getService(
				AlignmentService.class);
		Alignment alignment = alignmentService.getAlignment();

		Collection<? extends Cell> typeCells = alignment.getActiveTypeCells();
		for (Cell typeCell : typeCells) {
			if (AppSchemaMappingUtils.isJoin(typeCell)) {
				// JoinParameter joinParameter =
				// AppSchemaMappingUtils.getJoinParameter(typeCell);
				TypeEntityDefinition containerType = AppSchemaMappingUtils
						.getContainerType(typeCell);
				List<TypeEntityDefinition> nestedTypes = AppSchemaMappingUtils
						.getNestedTypes(typeCell);
				Type targetTtype = AppSchemaMappingUtils.getTargetType(typeCell);

				Composite main = new Composite(page, SWT.NONE);
				main.setLayout(new GridLayout(3, false));

				joinTypesViewer = createJoinTypesViewer(main, new TypeEntityDefinition[] {
						containerType, nestedTypes.get(0) });
				targetTypeViewer = createTargetTypeViewer(main,
						Collections.singleton(targetTtype.getDefinition()));
			}
		}
	}

	private TableViewer createJoinTypesViewer(Composite parent, TypeEntityDefinition[] joinTypes) {
		Label intro = new Label(parent, SWT.NONE);
		intro.setText("Select the element in the container type where the joined type will be nested.");
		intro.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false, 3, 1));

		TableViewer table = new TableViewer(parent, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE);
		table.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 2));
		table.setLabelProvider(new StyledDefinitionLabelProvider(table));
		table.setContentProvider(ArrayContentProvider.getInstance());
		table.setInput(joinTypes);

		return table;
	}

	private TreeViewer createTargetTypeViewer(Composite parent,
			Collection<TypeEntityDefinition> input) {

		PatternFilter patternFilter = new SchemaPatternFilter();
		patternFilter.setIncludeLeadingWildcard(true);
		final FilteredTree filteredTree = new TreePathFilteredTree(parent, SWT.SINGLE
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER, patternFilter, true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.minimumHeight = 160;
		gridData.minimumWidth = 140;
		filteredTree.setLayoutData(gridData);
		TreeViewer viewer = filteredTree.getViewer();

		viewer.setComparator(new DefinitionComparator());

		EntityDefinitionService eds = (EntityDefinitionService) PlatformUI.getWorkbench()
				.getService(EntityDefinitionService.class);
		viewer.setContentProvider(new TreePathProviderAdapter(
				new EntityTypeIterableContentProvider(eds, SchemaSpaceID.TARGET)));
		viewer.setLabelProvider(new StyledDefinitionLabelProvider(viewer));

		viewer.setInput(input);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO: verify selected element is correct
			}
		});

		return viewer;
	}

	/**
	 * @see eu.esdihumboldt.hale.ui.HaleWizardPage#onShowPage(boolean)
	 */
	@Override
	protected void onShowPage(boolean firstShow) {
		super.onShowPage(firstShow);
		// TODO: do nothing?
	}

}
