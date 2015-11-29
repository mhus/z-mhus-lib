package de.mhus.lib.vaadin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.Reindeer;

import de.mhus.lib.annotations.pojo.Hidden;
import de.mhus.lib.core.ILog;
import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.FilterRequest;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.vaadin.form.VaadinPojoForm;

public abstract class AbstractListEditor<E> extends VerticalLayout implements MNlsProvider, ILog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Object MY_NEW_MARKER = new Object();

	public static final String LABEL_SAVED_NEW = "new";
	public static final String LABEL_SAVED = "saved";
	public static final String LABEL_DELETED = "deleted";
	public static final String LABEL_CANELED = "canceled";

	protected SimpleTable table;
	private Button bNew;
	private Button bUpdate;
	private Button bDelete;
	protected Object editMode;
	private VaadinPojoForm model;
	protected SearchField filter;
	private boolean showSearchField = true;
	private boolean needSortUpdate = false;
	private Panel detailsPanel;
	private Panel modelPanel;
	private boolean fullSize;
	private VerticalLayout detailsPanelContent;
	private MNls nls;
	private boolean modified = false;
	private boolean initialized = false;
	
	private Map<String, String> labels = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(LABEL_SAVED_NEW,"Saved");
			put(LABEL_SAVED,"Saved");
			put(LABEL_DELETED,"Deleted");
			put(LABEL_CANELED,"Canceled");
			
		}
	};
	
	@Hidden
	private Log log = Log.getLog(this);
	private String sortedColumn;
	private boolean sortedAscending;
	
	@SuppressWarnings("serial")
	public void initUI() {
		if (isInitialized()) return;
		setInitialized(true);
		
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
                
        table.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				doSelectionChanged();
			}
		});

        table.addItemClickListener(new ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				if (editMode == null && event.isDoubleClick())
					doUpdate();
			}
		});
        
        table.addHeaderClickListener(new Table.HeaderClickListener() {
			
			@Override
			public void headerClick(HeaderClickEvent event) {
				
				String name = String.valueOf( event.getPropertyId() );
				if (name.equals(sortedColumn))
					sortedAscending = ! sortedAscending;
				else
					sortedAscending = true;
				sortedColumn = name;
				if (needSortUpdate)
					updateDataSource();
			}
		});
        
        detailsPanel = new Panel(getDetailsName());
        detailsPanel.setWidth("100%");
        detailsPanelContent = new VerticalLayout();
        detailsPanel.setContent(detailsPanelContent);
    	if (fullSize) detailsPanel.setSizeFull();
    	// detailsPanel.setScrollable(false);
        
        try {
        	modelPanel = new Panel();
        	modelPanel.setWidth("100%");
        	if (fullSize) modelPanel.setSizeFull();
        	modelPanel.setStyleName(Reindeer.PANEL_LIGHT);
        	// modelPanel.setScrollable(true);
        	detailsPanelContent.addComponent(modelPanel);
        	
	        model = createForm();
//	        model.doBuild(getActivator());
	        model.doBuild();
	        detailsPanelContent.addComponent(model);
        } catch (Exception e) {
        	e.printStackTrace();
        }   
        
        

        HorizontalLayout buttonBar = new HorizontalLayout();
        
        bNew = new Button(MNls.find(this, "button.create=Create"));
        buttonBar.addComponent(bNew);
        bNew.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				doNew();
			}
		});

        bUpdate = new Button(MNls.find(this, "button.edit=Edit"));
        buttonBar.addComponent(bUpdate);
        bUpdate.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				doUpdate();
			}
		});

        bDelete = new Button(MNls.find(this, "button.delete=Delete"));
        buttonBar.addComponent(bDelete);
        bDelete.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				doDelete();
			}
		});

        
        editMode = null;
        
        createCustomButtons(buttonBar);
        composeElements(filter, table, detailsPanel, buttonBar );
        
        updateEnabled();

	}

	protected MActivator getActivator() {
		return null;
	}

	protected void createCustomButtons(HorizontalLayout buttonBar) {
	}

	protected String getDetailsName() {
		return MNls.find(this, "panel.details.caption=Details");
	}

	protected void doFilter() {
		updateDataSource();
	}

	protected void composeElements(AbstractComponent filter, AbstractComponent table, AbstractComponent formPanel, AbstractComponent buttonBar) {
		
		if (showSearchField) addComponent(filter);
		
//		addComponent(table);
//		setExpandRatio(table, 1.0f);
//		
//		addComponent(formPanel);
//		addComponent(buttonBar);
		
		VerticalSplitPanel split = new VerticalSplitPanel();
				
		split.setFirstComponent(table);
		table.setSizeUndefined();
		table.setSizeFull();
//		split.setSplitPosition(200);
		split.setSecondComponent(formPanel);
		split.setSizeFull();
//		split.setHeight("300px");
		addComponent(split);
		setExpandRatio(split, 1.0f);
		addComponent(buttonBar);
		setSizeFull();
	}

	protected abstract ColumnDefinition[] createColumnDefinitions();
	
	protected String getTableName() {
		return MNls.find(this, "panel.table.caption=Table");
	};
	
	protected abstract E createTarget();
	
	protected VaadinPojoForm createForm() {
		try {
			VaadinPojoForm form = new VaadinPojoForm(createTarget());
			form.setPojo(createTarget());
			return form;
		} catch (Throwable t) {
			log.w(t);
		}
		return null;
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
		
		ConfirmDialog.show(getUI(), MNls.find(this, "confirm.delete=Delete"), MNls.find(this, "confirm.question=Are you sure?"),
				MNls.find(this, "confirm.yes=Yes"), MNls.find(this, "confirm.no=Cancel"), new ConfirmDialog.Listener() {

		            @Override
					public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	try {
			                	doDelete(selectedObj);
								showInformation(labels.get(LABEL_DELETED));
		                	} catch (Throwable e) {
		        				log().i(e);
		        				showError(e);
		                	}
		                	model.setPojo(createTarget());
	
		                	updateDataSource();
		                	modified = true;
		                } else {
		                }
		            }
		        });
		
	}

	protected void doCancel() {
		if (editMode == null) return;
		try {
			if (!MY_NEW_MARKER.equals(editMode)) {
				doCancel(getTarget(editMode));
				showInformation(labels.get(LABEL_CANELED));
			}
		} catch (Throwable t) {
			log().i(t);
			showError(t);
		}
		editMode = null;
    	model.setPojo(createTarget());
		updateEnabled();
	}

	protected abstract void doCancel(E entry) throws Exception;

	protected abstract void doDelete(E entry) throws Exception;
	
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
				E entity = (E) model.getPojo();
				if (MY_NEW_MARKER.equals(editMode)) {
					doSaveNew(entity);
					showInformation(labels.get(LABEL_SAVED_NEW));
				} else {
					doSave(entity);
					showInformation(labels.get(LABEL_SAVED));
				}
				
				updateDataSource();
            	modified = true;
				
			} catch (Throwable e) {
				log().i(e);
				showError(e);
			}
			editMode = null;
		}
		
		updateEnabled();
		
	}

	public void showError(Throwable e) {
		Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
	}

	public void showInformation(String msg) {
		if (MString.isEmpty(msg)) return;
		Notification.show(msg, Notification.Type.HUMANIZED_MESSAGE);
	}
	
	protected void doSaveNew(E entity) throws Exception {
		doSave(entity);
	}

	protected void doUpdate(Object selectedId) {
		editMode = selectedId;
		if (MY_NEW_MARKER.equals(editMode)) return;

		Object target = getEditableTarget(editMode);
		model.setPojo(target);
		
	}

	protected abstract void doSave(E entry) throws Exception;

	protected E getEditableTarget(Object id) {
		return getTarget(id);
	}
	
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
			bNew.setCaption(MNls.find(this, "button.create=Create"));
			bUpdate.setEnabled(selectedId != null && canUpdate(selectedId) );
			bUpdate.setCaption(MNls.find(this, "button.edit=Edit"));
			bDelete.setEnabled(selectedId != null && canDelete(selectedId));
			bDelete.setCaption(MNls.find(this, "button.delete=Delete"));
			if (model != null) model.setEnabled(false);
			table.setEnabled(true);
		} else {
			bNew.setEnabled(false);
			bNew.setCaption(MNls.find(this, "button.create=Create"));
			bUpdate.setEnabled(true);
			bUpdate.setCaption(MNls.find(this, "button.save=Set"));
			bDelete.setEnabled(true);
			bDelete.setCaption(MNls.find(this, "button.cancel=Cancel"));
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
			for ( E entity : createDataList(filter) ) {
				table.addRow(getId(entity), getValues(entity));
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
			for ( E entity : createDataList(filter.createFilterRequest())) {
				Object id = getId(entity);
				Object[] values = getValues(entity);
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

	public boolean isFullSize() {
		return fullSize;
	}

	public void setFullSize(boolean fullSize) {
		this.fullSize = fullSize;
	}

	@Override
	public MNls getNls() {
		return nls;
	}

	public void setNls(MNls nls) {
		this.nls = nls;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	@Override
	public synchronized Log log() {
		if (log == null) {
			log = Log.getLog(this);
		}
		return log;
	}

	public boolean isNeedSortUpdate() {
		return needSortUpdate;
	}

	public void setNeedSortUpdate(boolean needSortUpdate) {
		this.needSortUpdate = needSortUpdate;
	}

	public String getSortedColumn() {
		return sortedColumn;
	}

//	public void setSortedColumn(String sortedColumn) {
//		this.sortedColumn = sortedColumn;
//	}

	public boolean isSortedAscending() {
		return sortedAscending;
	}

//	public void setSortedAscending(boolean sortedAscending) {
//		this.sortedAscending = sortedAscending;
//	}

}
