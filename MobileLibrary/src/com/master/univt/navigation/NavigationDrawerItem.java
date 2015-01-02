package com.master.univt.navigation;

/**
 * The navigation item from the list on the left sliding layout of the
 * application.
 * 
 * @author Gabriela Zaharieva
 */
public class NavigationDrawerItem {

	private String title;
	private int icon;
	private String count = "0";
	/** boolean to set visiblity of the counter */
	private boolean isCounterVisible = false;

	public NavigationDrawerItem() {
	}

	/**
	 * Initialization of the navigation bar item with data for title and icon.
	 * 
	 * @param title
	 *            the title of the item
	 * @param icon
	 *            the icon item id
	 */
	public NavigationDrawerItem(final String title, final int icon) {
		this.title = title;
		this.icon = icon;
	}

	public NavigationDrawerItem(final String title, final int icon,
			final boolean isCounterVisible, final String count) {
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @return the icon
	 */
	public int getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(final int icon) {
		this.icon = icon;
	}

	/**
	 * @return the count
	 */
	public String getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(final String count) {
		this.count = count;
	}

	/**
	 * @return the isCounterVisible
	 */
	public boolean isCounterVisible() {
		return isCounterVisible;
	}

	/**
	 * @param isCounterVisible
	 *            the isCounterVisible to set
	 */
	public void setCounterVisible(final boolean isCounterVisible) {
		this.isCounterVisible = isCounterVisible;
	}

}