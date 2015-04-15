package de.mhus.lib.vaadin;

import java.util.LinkedList;
import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.mhus.lib.vaadin.form2.VaadinPojoForm;

public abstract class AbstractListEditor<E> extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Object MY_NEW_MARKER = new Object();
	private SimpleTable table;
	private Button bNew;
	private Button bUpdate;
	private Button bDelete;
	protected Object editMode;
	private VaadinPojoForm model;
	private SearchField filter;
	private boolean showSearchField = true;
	private Panel detailsPanel;
	private boolean showInformation = true;
	private VerticalLayout informationPane;
	private Panel modelPanel;
	private boolean fullSize;
	
	@SuppressWarnings("serial")
	public void initUI() {
		
    	if (fullSize) setSizeFull();
		setSpacing(true);
		setMargin(true);
		
		filter = new SearchField();
		filter.setListener(new SearchField.Listener() {
			
			@Override
			public void doFilter(SearchField searchField) {
				AbstractListEditor.this.doFilter();
			}
		});
		table = new SimpleTable(getTableName());
		table.setSelectable(true);
        table.setMultiSelect(false);
        table.setImmediate(true);
        table.setSizeFull();
        table.createDataSource(createColumnDefinitions());
        fillDataSource(new FilterRequest(""));
                
        table.addListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				doSelectionChanged();
			}
		});
        
        table.addListener(new ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				if (editMode == null && event.isDoubleClick())
					doUpdate();
			}
		});
        
        
        detailsPanel = new Panel(getDetailsName());
        detailsPanel.setWidth("100%");
    	if (fullSize) detailsPanel.setSizeFull();
    	detailsPanel.setScrollable(false);
        
        if (showInformation) {
        	informationPane = new VerticalLayout();
        	detailsPanel.addComponent(informationPane);
        	informationPane.setWidth("100%");
        }
        try {
        	modelPanel = new Panel();
        	modelPanel.setWidth("100%");
        	if (fullSize) modelPanel.setSizeFull();
        	modelPanel.setStyleName(Reindeer.PANEL_LIGHT);
        	modelPanel.setScrollable(true);
        	detailsPanel.addComponent(modelPanel);
        	
	        model = createForm();
	        model.setInformationContainer(informationPane);
	        model.doBuild(modelPanel);
        } catch (Exception e) {
        	e.printStackTrace();
        }   
        
        

        HorizontalLayout buttonBar = new HorizontalLayout();
        
        bNew = new Button("Neu");
        buttonBar.addComponent(bNew);
        bNew.addListener(new ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				doNew();
			}
		});

        bUpdate = new Button("Bearbeiten");
        buttonBar.addComponent(bUpdate);
        bUpdate.addListener(new ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				doUpdate();
			}
		});

        bDelete = new Button("L�schen");
        buttonBar.addComponent(bDelete);
        bDelete.addListener(new ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				doDelete();
			}
		});

        
        editMode = null;
        
        createCustomButtons(buttonBar);
        composeElements(filter, table, detailsPanel, buttonBar );
        
        updateEnabled();

	}

	protected void createCustomButtons(HorizontalLayout buttonBar) {
	}

	protected String getDetailsName() {
		return "Details";
	}

	protected void doFilter() {
		updateDataSource();
	}

	protected void composeElements(AbstractComponent filter, AbstractComponent table, AbstractComponent formPanel, AbstractComponent buttonBar) {
		
		if (showSearchField) addComponent(filter);
		addComponent(table);
		setExpandRatio(table, 1.0f);
		
		addComponent(formPanel);
		addComponent(buttonBar);
		
//		VerticalSplitPanel split = new VerticalSplitPanel();
//		
//		VerticalLayout bottom = new VerticalLayout();
//		bottom.addComponent(formPanel);
//		bottom.addComponent(buttonBar);
//		
//		split.addComponent(table);
//		split.setSplitPosition(200);
//		split.addComponent(bottom);
//
//		addComponent(split);
//		setExpandRatio(split, 1.0f);

	}

	protected abstract ColumnDefinition[] createColumnDefinitions();
	
	protected abstract String getTableName();
	
	protected abstract E createTarget();
	
	protected VaadinPojoForm createForm() {
		VaadinPojoForm form = new VaadinPojoForm();
		form.setPojo(createTarget());
		return form;
	}
	
	protected void doSelectionChanged() {
		if (editMode != null) return;
		Object selectedId = table.getValue();
		Object target = null;
		if (selectedId == null)
			target = createTarget();
		else
			target = getTarget(selectedId);
		model.setPojo(target);
		updateEnabled();
	}

	protected void doDelete() {
		if (editMode != null) {
			// Cancel
			doCancel();
			return;
		}
		
		Object selectedId = table.getValue();
		if (selectedId == null || !canDelete(selectedId)) return;
		final E selectedObj = getTarget(selectedId);
		if (selectedObj == null) return;
		
		ConfirmDialog.show(getWindow(), "L�schen", "Sind Sie sicher?", // TODO  Labels !!!
		        "Ja", "Nein", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	doDelete(selectedObj);

		                	model.setPojo(createTarget());
	
		                	updateDataSource();
		                } else {
		                }
		            }
		        });
		
	}

	protected void doCancel() {
		if (editMode == null) return;
		if (!MY_NEW_MARKER.equals(editMode))
			doCancel(getTarget(editMode));
		editMode = null;
    	model.setPojo(createTarget());
		updateEnabled();
	}

	protected abstract void doCancel(E entry);

	protected abstract void doDelete(E entry);
	
	protected void doUpdate() {
		if (editMode == null) {
			Object selectedId = table.getValue();
			if (selectedId == null || !canUpdate(selectedId)) return;
			try {
				doUpdate(selectedId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// fill 
		} else {
			// save
			try { 
				if (!canUpdate(editMode)) return;
				@SuppressWarnings("unchecked")
				E unit = (E) model.getPojo();
				doSave(unit);
				
				updateDataSource();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			editMode = null;
		}
		
		updateEnabled();
		
	}

	protected void doUpdate(Object selectedId) {
		editMode = selectedId;
		if (MY_NEW_MARKER.equals(editMode)) return;

		Object target = getTarget(editMode);
		model.setPojo(target);
		
	}

	protected abstract void doSave(E entry);

	protected abstract E getTarget(Object id);

	protected void doNew() {
		if (editMode != null || !canNew()) return;
		editMode = MY_NEW_MARKER;
    	model.setPojo(createTarget());
		
		updateEnabled();
	}

	protected void updateEnabled() {
		Object selectedId = table.getValue();
		
		if (!isEditMode()) {
			bNew.setEnabled(canNew());
			bNew.setCaption("Neu");
			bUpdate.setEnabled(selectedId != null && canUpdate(selectedId) );
			bUpdate.setCaption("Bearbeiten");
			bDelete.setEnabled(selectedId != null && canDelete(selectedId));
			bDelete.setCaption("L�schen");
			model.setEnabled(false);
			table.setEnabled(true);
		} else {
			bNew.setEnabled(false);
			bNew.setCaption("Neu");
			bUpdate.setEnabled(true);
			bUpdate.setCaption("Speichern");
			bDelete.setEnabled(true);
			bDelete.setCaption("Abbruch");
			model.setEnabled(true);
			table.setEnabled(false);
		}
		doUpdateEnabled(selectedId);
	}
	
	/**
	 * Overwrite this to update your own buttons
	 */
	protected void doUpdateEnabled(Object selectedId) {
	}

	public boolean isEditMode() {
		return editMode != null;
	}

	public boolean canDelete(Object selectedId) {
		return true;
	}

	public boolean canUpdate(Object selectedId) {
		return true;
	}

	public boolean canNew() {
		return true;
	}

	protected void fillDataSource(FilterRequest filter) {
		try {
			table.removeAllItems();
			for ( E unit : createDataList(filter) ) {
				table.addRow(getId(unit), getValues(unit));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract Object[] getValues(E entry);

	protected abstract Object getId(E entry);

	protected abstract List<E> createDataList(FilterRequest filter);

	public void updateDataSource() {
		try {
			LinkedList<Object> newIds = new LinkedList<Object>();
			for ( E unit : createDataList(filter.createFilterRequest())) {
				Object id = getId(unit);
				Object[] values = getValues(unit);
				if (!table.updateRow(id, values))
					table.addRow(id, values);
				newIds.add(id);
			}
			
			for ( Object id : new LinkedList<Object>( table.getItemIds() ) ) {
				if (!newIds.contains(id))
					table.removeItem(id);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isShowSearchField() {
		return showSearchField;
	}

	public void setShowSearchField(boolean showSearchField) {
		this.showSearchField = showSearchField;
	}

	public void doUpdateCaptions() {
		table.setCaption(getTableName());
		detailsPanel.setCaption(getDetailsName());
	}
	
	public E getSingleSelected() {
		if (isEditMode())
			return getTarget(editMode);
		Object selectedId = table.getValue();
		if (selectedId == null) return null;
		return getTarget(selectedId);
	}

	public boolean isShowInformation() {
		return showInformation;
	}

	public void setShowInformation(boolean showInformation) {
		this.showInformation = showInformation;
	}

	public boolean isFullSize() {
		return fullSize;
	}

	public void setFullSize(boolean fullSize) {
		this.fullSize = fullSize;
	}
	
}
