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


import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class MVaadinPortlet extends MVaadinApplication {


	private static final long serialVersionUID = 1L;
	protected VerticalLayout layout;
	protected Window window;
	private HorizontalLayout control;
	private Button bHeightAdd;
	private Button bHeightSub;
	private Button bWidthAdd;
	private Button bWidthSub;
	private Button bFull;
	private boolean isFull = true;
	private int width;
	private int height;
	private boolean hasButtons = true;

	@SuppressWarnings({ "serial" })
	@Override
	public void doContent(VerticalLayout innerLayout) {
		
//		innerLayout.setHeightUnits(Sizeable.UNITS_PIXELS);
//		innerLayout.setWidthUnits(Sizeable.UNITS_PIXELS);
		
		layout = new VerticalLayout(); // overwrite layout !
		layout.setSizeFull();
		//layout.setWidth("100%");
		//layout.setHeight("100px");
		innerLayout.addComponent(layout);
		innerLayout.setExpandRatio(layout, 1);
		innerLayout.setMargin(false);
		
		control = new HorizontalLayout();
		innerLayout.addComponent(control);
		innerLayout.setExpandRatio(control, 0);
		innerLayout.setComponentAlignment(control, Alignment.TOP_RIGHT);
		control.setWidth("100%");
		
		if (hasButtons) {
			
			createCustomButtons(control);
			
			bHeightAdd = new Button(" \\/ ");
			bHeightAdd.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					doAddHeight();
				}
			});
	
			control.addComponent(bHeightAdd);
			
			bHeightSub = new Button(" /\\ ");
			bHeightSub.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					doSubHeight();
				}
			});
	
			control.addComponent(bHeightSub);
			
			bWidthAdd = new Button(" > ");
			bWidthAdd.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					doAddWidth();
				}
			});
	
			control.addComponent(bWidthAdd);
			
			bWidthSub = new Button(" < ");
			bWidthSub.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					doSubWidth();
				}
			});
	
			control.addComponent(bWidthSub);
	
			bFull = new Button(" * ");
			bFull.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					doFull();
				}
			});
	
			control.addComponent(bFull);
			control.addComponent(new Label("  "));
		}
    	window.setResizable(true);
    	
		int[] size = getRememberedSize();
		
		isFull = false;
		setHeight(size[1]);
		setWidth(size[0]);
		setFullSize(size[2] == 1);

    }
	
	protected void createCustomButtons(HorizontalLayout buttonBar) {
	}

	protected void doFull() {
		isFull = ! isFull;
		setFullSize(isFull);
		
	}

	public void setFullSize(boolean full) {
		VerticalLayout innerLayout = (VerticalLayout)getContent();
		// innerLayout.setSizeUndefined();
		if (full) {
			// innerLayout.setSizeFull();
			innerLayout.setWidth("100%");
		} else {
			isFull = false;
//			innerLayout.setHeightUnits(Sizeable.UNITS_PIXELS);
//			innerLayout.setWidthUnits(Sizeable.UNITS_PIXELS);
			setWidth(width);
			setHeight(height);
		}
			
		isFull = full;
		if (hasButtons) {
			bFull.setCaption(full ? " * " : " o ");
			//bHeightAdd.setEnabled(!full);
			//bHeightSub.setEnabled(!full);
			bWidthAdd.setEnabled(!full);
			bWidthSub.setEnabled(!full);
		}
	}

	protected void doSubHeight() {
		setHeight(height - 50);
	}

	protected void doAddHeight() {
		setHeight(height + 50);
	}

	protected void doSubWidth() {
		setWidth(width - 50);
	}

	protected void doAddWidth() {
		setWidth(width + 50);
	}

	protected void doRememberSize(int[] opts) {
//		VerticalLayout innerLayout = (VerticalLayout) getMainWindow().getContent();
	}

	protected int[] getRememberedSize() {
		return new int[] {1000,600,1};
	}
	@Override
	public void close() {
		super.close();
	}
	
	
	protected VerticalLayout getLayoutContent() {
		return layout;
	}
	
	public void setHeight(int height) {
		if (height < 0) return;
		VerticalLayout innerLayout = (VerticalLayout) getContent();
		/*if (!isFull)*/ innerLayout.setHeight(height,Unit.PIXELS);
		this.height = height;
		doRememberSize(new int[] {width,height,isFull?1:0});
	}
	
	public void setWidth(int width) {
		if (width < 0) return;
		VerticalLayout innerLayout = (VerticalLayout)getContent();
		if (!isFull) innerLayout.setWidth(width,Unit.PIXELS);
		this.width = width;
		doRememberSize(new int[] {width,height,isFull?1:0});
	}

	public boolean isHasButtons() {
		return hasButtons;
	}

	protected void setHasButtons(boolean hasButtons) {
		this.hasButtons = hasButtons;
	}

}
