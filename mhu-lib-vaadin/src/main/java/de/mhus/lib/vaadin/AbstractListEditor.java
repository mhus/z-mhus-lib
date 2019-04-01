/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.vaadin;

import java.util.LinkedList;
import java.util.List;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.v7.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.HeaderClickEvent;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.annotations.pojo.Hidden;
import de.mhus.lib.core.ILog;
import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.FilterRequest;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsBundle;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.form.MutableMForm;
import de.mhus.lib.vaadin.form.VaadinPojoForm;

@SuppressWarnings("deprecation")
public abstract class AbstractListEditor<E> extends VerticalLayout implements MNlsProvider, ILog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Object MY_NEW_MARKER = new Object();

    public static final String LABEL_SAVED_NEW = "entity.new=new";
    public static final String LABEL_SAVED = "entity.saved=saved";
    public static final String LABEL_DELETED = "entity.deleted=deleted";
    public static final String LABEL_CANELED = "entity.canceled=canceled";

    protected SimpleTable table;
    private Button bNew;
    private Button bUpdate;
    private Button bDelete;
    protected Object editMode;
    protected VaadinPojoForm<E> model;
    protected SearchField filter;
    private boolean showSearchField = true;
    private boolean needSortUpdate = false;
    private Panel detailsPanel;
    private boolean fullSize;
    private VerticalLayout detailsPanelContent;
    private MNlsBundle nlsBundle;
    private boolean modified = false;
    private boolean initialized = false;

    @Hidden
    private Log log = Log.getLog(this);
    private String sortedColumn;
    private boolean sortedAscending;
    
    @SuppressWarnings("serial")
    public void initUI() {
        if (isInitialized()) return;
        setInitialized(true);
        
        if (fullSize) setSizeFull();
        setSpacing(false);
        setMargin(false);
        
        filter = new SearchField(this);
        filter.setMargin(false);
        filter.setSpacing(false);
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
                doItemClick(event);
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
        detailsPanelContent.setMargin(false);
        detailsPanelContent.setSpacing(false);
        if (fullSize) detailsPanel.setSizeFull();
        // detailsPanel.setScrollable(false);
        
        try {
            
            model = createForm();
            if (model.getForm().getNlsBundle() == null && model.getForm() instanceof MutableMForm)
                    ((MutableMForm)model.getForm()).setNlsBundle(MNlsBundle.lookup(this));
//          model.doBuild(getActivator());
            model.doBuild();
            detailsPanelContent.addComponent(model);
            model.setMargin(false);
            model.setSpacing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }   
        
        

        HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setMargin(false);
        buttonBar.setSpacing(false);
        
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

    protected void doItemClick(ItemClickEvent event) {
        if (editMode == null && event.isDoubleClick())
            doUpdate();
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
        
//      addComponent(table);
//      setExpandRatio(table, 1.0f);
//      
//      addComponent(formPanel);
//      addComponent(buttonBar);
        
        VerticalSplitPanel split = new VerticalSplitPanel();
                
        split.setFirstComponent(table);
        table.setSizeUndefined();
        table.setSizeFull();
//      split.setSplitPosition(200);
        split.setSecondComponent(formPanel);
        split.setSizeFull();
//      split.setHeight("300px");
        addComponent(split);
        setExpandRatio(split, 1.0f);
        addComponent(buttonBar);
        setMargin(false);
        setSpacing(false);
        setSizeFull();
    }

    protected abstract ColumnDefinition[] createColumnDefinitions();
    
    protected String getTableName() {
        return MNls.find(this, "panel.table.caption=Table");
    };
    
    protected abstract E createTarget();
    
    protected VaadinPojoForm<E> createForm() {
        try {
            VaadinPojoForm<E> form = new VaadinPojoForm<>(createTarget());
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
        E target = null;
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
                                showInformation(MNls.find(AbstractListEditor.this, LABEL_DELETED));
                            } catch (Throwable e) {
                                log().d(e);
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
                showInformation(MNls.find(AbstractListEditor.this,LABEL_CANELED));
            }
        } catch (Throwable t) {
            log().d(t);
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
                log().d(e);
            }
            
            // fill 
        } else {
            // save
            try {
                if (!canUpdate(editMode)) return;
                E entity = (E) model.getPojo();
                if (MY_NEW_MARKER.equals(editMode)) {
                    String error = doValidateNew(entity);
                    if (error != null) {
                        showError(error);
                        return;
                    }
                    doSaveNew(entity);
                    showInformation(MNls.find(AbstractListEditor.this,LABEL_SAVED_NEW));
                } else {
                    String error = doValidate(entity);
                    if (error != null) {
                        showError(error);
                        return;
                    }
                    doSave(entity);
                    showInformation(MNls.find(AbstractListEditor.this,LABEL_SAVED));
                }
                
                updateDataSource();
                    modified = true;
                    editMode = null;
                
            } catch (Throwable e) {
                log().d(e);
                showError(e);
            }
        }
        
        updateEnabled();
        
    }

    /**
     * Overwrite this function if you want to validate a new entity before saving. Return null if the
     * values are correct or return the internationalized error message.
     * 
     * @param entity
     * @return
     */
    protected String doValidateNew(E entity) {
        return null;
    }

    /**
     * Overwrite this function if you want to validate an updated entity before saving. Return null if the
     * values are correct or return the internationalized error message.
     * 
     * @param entity
     * @return
     */
    protected String doValidate(E entity) {
        return null;
    }
    
    public void showError(Throwable e) {
        showError(e.getMessage());
    }

    public void showError(String e) {
        Notification.show(e, Notification.Type.ERROR_MESSAGE);
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

        E target = getEditableTarget(editMode);
        model.setPojo(target);
    }

    protected abstract void doSave(E entry) throws Exception;

    protected abstract E getEditableTarget(Object id);
    
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
        if (nlsBundle == null)
            nlsBundle = MNlsBundle.lookup(this);
        return nlsBundle.getNls(UI.getCurrent().getLocale());
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

//  public void setSortedColumn(String sortedColumn) {
//      this.sortedColumn = sortedColumn;
//  }

    public boolean isSortedAscending() {
        return sortedAscending;
    }

    public MNlsBundle getNlsBundle() {
        return nlsBundle;
    }

    public void setNlsBundle(MNlsBundle nlsBundle) {
        this.nlsBundle = nlsBundle;
    }

//  public void setSortedAscending(boolean sortedAscending) {
//      this.sortedAscending = sortedAscending;
//  }

}