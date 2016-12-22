package ch.telegraphstudios.TSMenuBar;

import java.io.File;

/**
 * Use this listener to catch a drop event on a droplet component.
 */
public interface TSFileDropListener {

	public void onFileDrop(TSDroplet source, File[] files);
	
}
