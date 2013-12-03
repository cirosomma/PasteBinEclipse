package eu.miclen.pastebin.popup.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import eu.miclen.pastebin.Activator;
import eu.miclen.pastebin.preferences.PreferenceConstants;

public class Action implements IObjectActionDelegate {

	private Shell shell;
	
	/**
	 * Constructor for Action1.
	 */
	public Action() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	public void run(IAction action){
		IEditorPart part1 = Workbench.getInstance().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		AbstractTextEditor part = (AbstractTextEditor) part1.getAdapter(AbstractTextEditor.class);
		String filename = Workbench.getInstance().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getTitle();
		Connection con = new Connection();
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String username = store.getString(PreferenceConstants.P_USER);
		String password = store.getString(PreferenceConstants.P_PWD);
		if (part != null) {
			IDocument document = part.getDocumentProvider().getDocument(part.getEditorInput());
			if(part instanceof ITextEditor){
				ITextEditor editor = (ITextEditor)part;
				IDocumentProvider provider = editor.getDocumentProvider();
				final TextSelection textSel = (TextSelection)editor.getSelectionProvider().getSelection();
				String content = textSel.getText();
				if(textSel.getText().equals("")) {
					content = document.get();
				}
				String userKey = con.getUserKey(username, password);
				if(userKey.length()==32)
						store.setDefault(PreferenceConstants.P_USERKEY, userKey);
				else {
					if(!store.getString(PreferenceConstants.P_USER).equals("") && !store.getString(PreferenceConstants.P_PWD).equals(""))
						MessageDialog.openInformation(shell, "Error", "Username or password are incorrect");
				}
				String response = con.sendContent(content, filename);
				if(response.substring(0,7).equals("http://"))
					MessageDialog.openInformation(shell, "Successful", "The link of your code: \n"+response+"\nLink has been saved in your ClipBoard (Use cmd : Ctrl + v)");
				else
					MessageDialog.openInformation(shell, "Error", "Bad Request");
				
				StringSelection ss = new StringSelection(response);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
			}
		}
	}
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
