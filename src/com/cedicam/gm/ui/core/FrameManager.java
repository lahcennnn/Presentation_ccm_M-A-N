package com.cedicam.gm.ui.core;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Fabien JALABERT
 * L'objectif de ce bean est de stocker l'information concernant la mise en page de l'écran. 
 * Il permet de décrire une disposition composée de plusieurs panneaux horizontaux:
 * 	- un panneau de recherche
 * 	- un panneau de liste de résultat de recherche
 *  - un panneau de détail 
 *
 */
public class FrameManager  {

	public class FramePanel {
		public FramePanel(int _id){
			id=_id;
		}
		
		private int height = 0;
		private int id = 0;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		private boolean expanded= true;
		
		public int getHeight() {			return height;		}
		public void setHeight(int height) {			this.height = height;		}
		public boolean isExpanded() {			return expanded;		}
		public void setExpanded(boolean expended) {			this.expanded = expended;		}
		
	}
	
	public List<FramePanel> panels = new ArrayList<FramePanel>();

	
	public void fitPanels(){
		if(panels.size()==0)return;
		if(panels.size()==1){
			panels.get(0).setHeight(100);
			return;
		}
		
		int oldTotal = 0;
		for(FramePanel panel:panels) oldTotal+=panel.height;
		
		if(oldTotal!=100){
			int newTotal = 0;
			for(FramePanel panel:panels){
				int oldSize = panel.height;
				int newSize = oldSize *100 / oldTotal ;
				panel.setHeight(newSize);
				newTotal +=newSize;
			}
			int finalAdjust = 100-newTotal;
			FramePanel adjustPanel = panels.get(panels.size()-1);
			adjustPanel.setHeight(adjustPanel.getHeight()+finalAdjust);
		}
		
	}
	
	public void addPanel(int id){
		int idx = getIndex(id);
		if(idx>=0) return;
		
		FramePanel panel = new FramePanel(id);
		if(panels.size()==0) panel.setHeight(100);
		else panel.setHeight(100/(panels.size()));
		panels.add(panel);
		fitPanels();
		
	}
	
	public void addPanel(int id, int size){
		int idx = getIndex(id);
		if(idx>=0) return;
		
		FramePanel panel = new FramePanel(id);
		panel.setHeight(size);
		panels.add(panel);
		fitPanels();
		
	}
	
	public void addPanelSplitting(int idNewPanel, int idSplitted){
		int idx = getIndex(idNewPanel);
		if(idx>=0) return;
		
		FramePanel newPanel = new FramePanel(idNewPanel);
		
		FramePanel splitted = getPanel(idSplitted);
		splitted.height = splitted.height/2;
		newPanel.height = splitted.height;
		panels.add(newPanel);
		fitPanels();
		
	}
	
	
	public int getIndex(int id){
		for(int i=0;i<panels.size();i++){
			if(panels.get(i).id==id) return i;
		}
		return -1;
	}
	
	public void removePanel(int id){
		int idx = getIndex(id);
		if(idx>=0)panels.remove(idx);
		fitPanels();
	}
	
	public void removePanelResizing(int id, int idResizePanel){
		int idx = getIndex(id);
		if(idx<0) return;
		
		FramePanel toRemove = getPanel(id);
		FramePanel toResize = getPanel(idResizePanel);
		
		toResize.height=(toResize.height+toRemove.height);
		
		removePanel(id);
	
	}
	
	public FramePanel getPanel(int id){
		for(int i=0;i<panels.size();i++){
			if(panels.get(i).id==id) return panels.get(i);
		}
		return null;
	}
	
	public static float PANEL_SIZE_FACTOR = 1.5f;
	
	public void setBigger(int id){
		FramePanel panel = getPanel(id);
		panel.setHeight((int)(panel.getHeight()*PANEL_SIZE_FACTOR));
		fitPanels();
	}
	
	public void setSmaller(int id){
		FramePanel panel = getPanel(id);
		panel.setHeight((int)(panel.getHeight()/PANEL_SIZE_FACTOR));
		fitPanels();
	}
	
	
	public void expand(int id){
		getPanel(id).setExpanded(true);
	}
	
	public void collapse(int id){
		getPanel(id).setExpanded(false);
	}
	
	public static int PANEL_COLLAPSED_SIZE = 5;
	
	public int getRenderedHeight(int id){
		FramePanel panel = getPanel(id);
		if(panel==null) return 0;
		
		if(panels.size()<=1) return 100;
		FramePanel mypanel = getPanel(id);
		if(!mypanel.expanded) return PANEL_COLLAPSED_SIZE;
		
		int collapsed = 0;
		int totalNotCollapsed = 0;
		
		for(FramePanel tmppanel:panels){
			if(tmppanel.expanded)totalNotCollapsed+=tmppanel.height;
			else collapsed++;
		}
		float result =  mypanel.height * (100-collapsed*PANEL_COLLAPSED_SIZE) / totalNotCollapsed;
		return (int)result;
	}
	
	public boolean isExpanded(int id){
		FramePanel panel = getPanel(id);
		if(panel==null) return false;
		return panel.isExpanded();
		
	}
}
