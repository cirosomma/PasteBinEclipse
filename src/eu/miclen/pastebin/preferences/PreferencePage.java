package eu.miclen.pastebin.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import eu.miclen.pastebin.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class PreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Some useful parameters");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new StringFieldEditor(PreferenceConstants.P_USER,"User :",getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_PWD, "Password:", getFieldEditorParent()) {
			protected void doFillIntoGrid(Composite parent, int numColumns) {
		        super.doFillIntoGrid(parent, numColumns);

		        getTextControl().setEchoChar('*');
		    }
		});
		
		addField(
			new BooleanFieldEditor(
				PreferenceConstants.P_PRIVATE,
				"Content must be Unlisted",
				getFieldEditorParent()));

		addField(new RadioGroupFieldEditor(
				PreferenceConstants.P_EXPIRYDATE,
			"Expiry date of file:",
			1,
			new String[][] { { "Never", "N" }, {
				"10 Minutes", "10M" }, {
				"1 Hour", "1H" }, {
				"1 Day", "1D" }, {
				"1 Week", "1W" }, {
				"2 Week", "2W" }, {
				"1 Month", "1M" }
		}, getFieldEditorParent()));

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}